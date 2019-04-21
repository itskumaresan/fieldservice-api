package com.gaksvytech.fieldservice.schedule.engine;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gaksvytech.fieldservice.emuns.EventStatusEnum;
import com.gaksvytech.fieldservice.emuns.UserWorkStatusEnum;
import com.gaksvytech.fieldservice.model.EventModel;
import com.gaksvytech.fieldservice.model.UserModel;
import com.gaksvytech.fieldservice.model.ZoneModel;
import com.gaksvytech.fieldservice.repository.EventRepository;
import com.gaksvytech.fieldservice.repository.ScheduleRepository;
import com.gaksvytech.fieldservice.repository.UserRepository;
import com.gaksvytech.fieldservice.repository.ZoneRepository;

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

		// Get Unassigned Users by Priority
		List<UserModel> unassignedUsers = getUsersOrderBySeverity();

		// Get Unassigned Events by Priority
		List<EventModel> unassignedEvents = getEventsOrderBySeverity();

		// Get List of Zones
		Map<Integer, ZoneModel> zoneMap = getZoneMap();

		// Both should be NON-EMPTY
		if (!unassignedEvents.isEmpty() && !unassignedUsers.isEmpty()) {
			unassignedEvents.stream().forEach(event -> {
				scheduleEvent(event, zoneMap);
			});
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
		return (int) userRepository.findByStatusAndZoneId(UserWorkStatusEnum.UNASSIGNED, zoneId).stream().distinct().count();
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
			distanceInKm = EventSchedulerUtil.distanceBetweenZones(eventZone.getLattitude(), eventZone.getLongitude(), zone.getValue().getLattitude(), zone.getValue().getLongitude());
			if (distanceInKm < min) {
				min = distanceInKm;
				nearestZone = zone.getValue();
			}
		}

		return nearestZone;
	}

	private List<UserModel> getUsersOrderBySeverity() {
		// TODO: Sorting based on Sev
		return userRepository.findByStatus(UserWorkStatusEnum.UNASSIGNED).stream().distinct().map(event -> modelMapper.map(event, UserModel.class)).collect(Collectors.toList());
	}

	private List<EventModel> getEventsOrderBySeverity() {
		// TODO: Sorting based on Sev
		return eventRepository.findByStatus(EventStatusEnum.UNASSIGNED).stream().distinct().map(event -> modelMapper.map(event, EventModel.class)).collect(Collectors.toList());
	}

	private Map<Integer, ZoneModel> getZoneMap() {
		return zoneRepository.findAll().stream().collect(Collectors.toMap(ZoneModel::getId, Function.identity()));
	}

}
