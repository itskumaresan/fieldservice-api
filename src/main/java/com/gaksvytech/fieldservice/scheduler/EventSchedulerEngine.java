package com.gaksvytech.fieldservice.scheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gaksvytech.fieldservice.emuns.EventStatusEnum;
import com.gaksvytech.fieldservice.model.EventModel;
import com.gaksvytech.fieldservice.model.UserModel;
import com.gaksvytech.fieldservice.model.ZoneModel;
import com.gaksvytech.fieldservice.repository.EventRepository;
import com.gaksvytech.fieldservice.repository.ScheduleRepository;
import com.gaksvytech.fieldservice.repository.UserRepository;
import com.gaksvytech.fieldservice.repository.ZoneRepository;
import com.gaksvytech.fieldservice.utils.EventModelComparator;
import com.gaksvytech.fieldservice.utils.Utils;

@Service
public class EventSchedulerEngine {

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public EventRepository eventRepository;

	@Autowired
	public ScheduleRepository scheduleRepository;

	@Autowired
	public ZoneRepository zoneRepository;

	@Autowired
	private ModelMapper modelMapper;

	public void process() throws Exception {

		// Prepare the schedule staring today and till next one week
		Calendar cal = Calendar.getInstance();
		int toDate = cal.get(Calendar.DAY_OF_MONTH) + 7;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		while (toDate > cal.get(Calendar.DAY_OF_MONTH)) {
			Date scheduleDate = cal.getTime();
			System.out.println("Schedule for " + sdf.format(scheduleDate));

			// Get Unassigned Users for a given date
			List<UserModel> unassignedUsers = getUsers(scheduleDate);
			System.out.println("unassignedUsers for scheduleDate [" + scheduleDate + "] is " + unassignedUsers);

			// Get Unassigned Events Order by Priority for a given date
			List<EventModel> unassignedEvents = getEventsOrderBySeverity(scheduleDate);
			System.out.println("unassignedEvents for scheduleDate [" + scheduleDate + "] is " + unassignedEvents);

			// Get List of Zones
			Map<Integer, ZoneModel> zoneMap = getZoneMap();

			// Both should be NON-EMPTY
			if (!unassignedEvents.isEmpty() && !unassignedUsers.isEmpty()) {
				unassignedEvents.stream().forEach(event -> {
					System.out.println("Scheduling for " + scheduleDate);
					scheduleEvent(event, zoneMap);
				});
			}

			// Add One day and iterate again
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}

	}

	public void scheduleEvent(EventModel event, Map<Integer, ZoneModel> zoneMap) {
		ZoneModel nearByZone = distanceZoneToEvent(event, zoneMap);
		scheduleUsers(nearByZone, event.getNumberOfWorkersRequired(), event, zoneMap);
	}

	private void scheduleUsers(ZoneModel zone, int noOfUsers, EventModel event, Map<Integer, ZoneModel> zoneMap) {
		if (getNoOfAvailableUsersInZone(zone.getId()) >= noOfUsers) {
			allocateUsersToEvent(event, zone, noOfUsers);
		} else {
			int noOfAvailableUsersInZone = getNoOfAvailableUsersInZone(zone.getId());
			allocateUsersToEvent(event, zone, noOfAvailableUsersInZone);
			noOfUsers = noOfUsers - noOfAvailableUsersInZone;
			List<Integer> nearZones = zone.getNearByZones();
			for (Integer z : nearZones) {
				if (noOfUsers <= 0) {
					break;
				}
				noOfAvailableUsersInZone = getNoOfAvailableUsersInZone(z);
				allocateUsersToEvent(event, zoneMap.get(z), noOfAvailableUsersInZone);
				noOfUsers = noOfUsers - noOfAvailableUsersInZone;
			}
		}
	}

	private int getNoOfAvailableUsersInZone(int zoneId) {
		return (int) userRepository.findByZoneId(zoneId).stream().distinct().count();
	}

	private void allocateUsersToEvent(EventModel event, ZoneModel zone, int noOfUsers) {
		// TODO - Persist in EventSchedule Table DAO Call
		// getUsersByZone(); - Available users
		// persistEventScheduleWithUserList
		/*
		 * Mark user as scheduled for this event
		 * 
		 */
	}

	private ZoneModel distanceZoneToEvent(EventModel event, Map<Integer, ZoneModel> availableZones) {
		double distanceInKm = 0;
		double min = Double.MAX_VALUE;
		ZoneModel nearestZone = null;

		for (Map.Entry<Integer, ZoneModel> zone : availableZones.entrySet()) {
			ZoneModel eventZone = zoneRepository.findById(event.getZoneId());
			distanceInKm = Utils.distanceBetweenZones(eventZone.getLattitude(), eventZone.getLongitude(), zone.getValue().getLattitude(), zone.getValue().getLongitude());
			if (distanceInKm < min) {
				min = distanceInKm;
				nearestZone = zone.getValue();
			}
		}

		return nearestZone;
	}

	private List<UserModel> getUsers(Date scheduleDate) {
		
		return userRepository
				.findAll()
				.stream()
				.map(event -> modelMapper.map(event, UserModel.class))
				.filter(event -> betweenInclusive(scheduleDate, event.getStartDate(), event.getEndDate()))
				.collect(Collectors.toList());
	}
	
	private List<EventModel> getEventsOrderBySeverity(Date scheduleDate) {
		return eventRepository
				.findByStatus(EventStatusEnum.UNASSIGNED)
				.stream()
				.map(event -> modelMapper.map(event, EventModel.class))
				.filter(event -> betweenInclusive(scheduleDate, event.getStartDate(), event.getEndDate()))
				.sorted(new EventModelComparator())
				.collect(Collectors.toList());
	}

	private boolean betweenInclusive(Date date, Date dateStart, Date dateEnd) {
		
		if (date != null && dateStart != null && dateEnd != null) {
			if ((date.equals(dateStart) || date.after(dateStart)) && (date.equals(dateEnd) || date.before(dateEnd))) {
				return true;
			}
			return false;
		}
		return false;

	}
	
	private Map<Integer, ZoneModel> getZoneMap() {
		return zoneRepository.findAll().stream().collect(Collectors.toMap(ZoneModel::getId, Function.identity()));
	}

}
