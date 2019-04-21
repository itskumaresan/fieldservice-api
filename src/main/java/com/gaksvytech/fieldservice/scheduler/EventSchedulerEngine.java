package com.gaksvytech.fieldservice.scheduler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gaksvytech.fieldservice.emuns.EventStatusEnum;
import com.gaksvytech.fieldservice.emuns.UserWorkStatusEnum;
import com.gaksvytech.fieldservice.entity.Schedules;
import com.gaksvytech.fieldservice.model.EventModelUI;
import com.gaksvytech.fieldservice.model.ScheduleModelUI;
import com.gaksvytech.fieldservice.model.UserModelUI;
import com.gaksvytech.fieldservice.model.ZoneModel;
import com.gaksvytech.fieldservice.repository.EventRepository;
import com.gaksvytech.fieldservice.repository.ScheduleRepository;
import com.gaksvytech.fieldservice.repository.UserRepository;
import com.gaksvytech.fieldservice.repository.ZoneRepository;
import com.gaksvytech.fieldservice.utils.EventModelUIComparator;

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

			// Get Unassigned Events Order by Priority for a given date
			List<EventModelUI> unassignedEvents = getEventsOrderBySeverity(scheduleDate);
			System.out.println("unassignedEvents for scheduleDate [" + scheduleDate + "] is " + unassignedEvents);

			// Get List of Zones
			Map<Integer, ZoneModel> zoneMap = getZoneMap();

			// Both should be NON-EMPTY
			if (!unassignedEvents.isEmpty()) {
				unassignedEvents.stream().forEach(event -> {
					System.out.println("Scheduling for " + scheduleDate);
					scheduleEvent(event, zoneMap, scheduleDate);
				});
			}

			// Add One day and iterate again
			cal.add(Calendar.DAY_OF_MONTH, 1);
		}

	}

	public void scheduleEvent(EventModelUI event, Map<Integer, ZoneModel> zoneMap, Date scheduleDate) {
		scheduleUsers(zoneMap.get(event.getZoneId()), event.getNumberOfWorkersRequired(), event, zoneMap, scheduleDate);
	}

	private void scheduleUsers(ZoneModel zone, int numberOfWorkersRequired, EventModelUI event, Map<Integer, ZoneModel> zoneMap, Date scheduleDate) {
		// Get List Of Available Users
		List<UserModelUI> avilableUsers = getNoOfAvailableUsersInZone(zone.getId(), scheduleDate);
		if (avilableUsers.isEmpty())
			return;

		if (avilableUsers.size() >= numberOfWorkersRequired) {
			allocateUsersToEvent(event, zone, numberOfWorkersRequired, avilableUsers, scheduleDate);
		} else {
			avilableUsers = getNoOfAvailableUsersInZone(zone.getId(), scheduleDate);
			int noOfAvailableUsersInZone = avilableUsers.size();
			allocateUsersToEvent(event, zone, noOfAvailableUsersInZone, avilableUsers, scheduleDate);
			numberOfWorkersRequired = numberOfWorkersRequired - noOfAvailableUsersInZone;
			List<Integer> nearZones = zone.getNearByZones();
			for (Integer z : nearZones) {
				if (numberOfWorkersRequired <= 0) {
					break;
				}
				avilableUsers = getNoOfAvailableUsersInZone(zone.getId(), scheduleDate);
				if (avilableUsers.isEmpty())
					return;

				noOfAvailableUsersInZone = avilableUsers.size();
				allocateUsersToEvent(event, zoneMap.get(z), noOfAvailableUsersInZone, avilableUsers, scheduleDate);
				numberOfWorkersRequired = numberOfWorkersRequired - noOfAvailableUsersInZone;
			}
		}
	}

	private List<UserModelUI> getNoOfAvailableUsersInZone(int zoneId, Date scheduleDate) {
		// Get Scheduled Users for Given Date
		Map<Long, ScheduleModelUI> scheduledUsersMap = scheduleRepository.findAll().stream().map(workForce -> convertToModelUI(workForce)).filter(schedule -> on(scheduleDate, schedule.getScheduleDate()))
				.collect(Collectors.toMap(ScheduleModelUI::getId, Function.identity()));

		// Get All User for Given Date and filter with above scheduled users
		return userRepository.findAll().stream().map(event -> modelMapper.map(event, UserModelUI.class)).filter(user -> betweenInclusive(scheduleDate, user.getStartDate(), user.getEndDate()))
				.filter(user -> !scheduledUsersMap.containsKey(user.getId())).collect(Collectors.toList());
	}

	private void allocateUsersToEvent(EventModelUI event, ZoneModel zone, int noOfUsers, List<UserModelUI> avilableUsers, Date scheduleDate) {

		for (Iterator<UserModelUI> iterator = avilableUsers.iterator(); iterator.hasNext();) {
			UserModelUI userModelUI = (UserModelUI) iterator.next();
			System.out.println("User[" + userModelUI.getId() + "] allocating to event[" + event.getId() + "] for Scheduled Date [" + scheduleDate + "]");
			scheduleRepository.save(new Schedules(0l, event.getId(), userModelUI.getId(), scheduleDate, UserWorkStatusEnum.ASSIGNED, null, null));
			if (--noOfUsers == 0) {
				break;
			}
		}

	}

	private ScheduleModelUI convertToModelUI(Schedules schedules) {
		ScheduleModelUI workforceModelUI = modelMapper.map(schedules, ScheduleModelUI.class);
		return workforceModelUI;
	}

	private List<EventModelUI> getEventsOrderBySeverity(Date scheduleDate) {
		return eventRepository.findByStatus(EventStatusEnum.UNASSIGNED).stream().map(event -> modelMapper.map(event, EventModelUI.class))
				.filter(event -> betweenInclusive(scheduleDate, event.getStartDate(), event.getEndDate())).sorted(new EventModelUIComparator()).collect(Collectors.toList());
	}

	private boolean betweenInclusive(Date scheduleDate, Date dateStart, Date dateEnd) {

		boolean returnStatus = false;
		if (scheduleDate != null && dateStart != null && dateEnd != null) {
			if ((scheduleDate.equals(dateStart) || scheduleDate.after(dateStart)) && (scheduleDate.equals(dateEnd) || scheduleDate.before(dateEnd))) {
				returnStatus = true;
			}
		}

		System.out.println("scheduleDate[" + scheduleDate + "] dateStart[" + dateStart + "] dateEnd[" + dateEnd + "] is [[" + returnStatus + "]]");

		return returnStatus;
	}

	private boolean on(Date fromDate, Date toDate) {

		boolean returnStatus = false;
		if (fromDate != null && toDate != null) {
			if (fromDate.equals(toDate)) {
				returnStatus = true;
			}
		}

		System.out.println("fromDate[" + fromDate + "] toDate[" + toDate + "] is [[" + returnStatus + "]]");

		return returnStatus;
	}

	private Map<Integer, ZoneModel> getZoneMap() {
		return zoneRepository.findAll().stream().collect(Collectors.toMap(ZoneModel::getId, Function.identity()));
	}

}
