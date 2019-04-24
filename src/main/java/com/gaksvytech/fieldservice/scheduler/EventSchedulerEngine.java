package com.gaksvytech.fieldservice.scheduler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gaksvytech.fieldservice.entity.Events;
import com.gaksvytech.fieldservice.entity.Schedules;
import com.gaksvytech.fieldservice.enums.EventStatusEnum;
import com.gaksvytech.fieldservice.enums.UserWorkStatusEnum;
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

	// Initialized by initialize method - starts
	private List<UserModelUI> totalUnassignedUsersInZoneForScheduleDate = new ArrayList<UserModelUI>();
	private List<ScheduleModelUI> allocatedUsersForScheduleDate = new ArrayList<ScheduleModelUI>();
	private int totalUsersRequiredByEvent = 0;
	private int totalUsersToBeAssignedForScheduleDate = 0;
	// Initialized by initialize method - ends

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

	// This needs to be called whenever there is change in ZoneId or scheduleDate
	private void initialize(EventModelUI event, int zoneId, Date scheduleDate) {

		// Reset all the variables
		totalUnassignedUsersInZoneForScheduleDate = null;
		allocatedUsersForScheduleDate = null;
		totalUsersRequiredByEvent = 0;
		totalUsersToBeAssignedForScheduleDate = 0;

		totalUsersRequiredByEvent = event.getNumberOfWorkersRequired();

		// Get Scheduled Users for ZoneId and Give Date
		allocatedUsersForScheduleDate = scheduleRepository
				.findAll()
				.stream()
				.map(workForce -> convertToModelUI(workForce))
				.filter(schedule -> on(scheduleDate, schedule.getScheduleDate()))
				.collect(Collectors.toList());
		
		totalUsersToBeAssignedForScheduleDate = totalUsersRequiredByEvent - allocatedUsersForScheduleDate.size();

		Map<Long, ScheduleModelUI> scheduledUsersMap = allocatedUsersForScheduleDate
				.stream()
				.collect(Collectors.toMap(ScheduleModelUI::getId, Function.identity()));

		// Get All User for Given Date and Zone Id and filter with above scheduled users
		totalUnassignedUsersInZoneForScheduleDate = userRepository.findByZoneId(zoneId)
				.stream()
				.map(user -> modelMapper.map(user, UserModelUI.class))
				.filter(user -> betweenInclusive(scheduleDate, user.getStartDate(), user.getEndDate()))
				.filter(user -> !scheduledUsersMap.containsKey(user.getId())).collect(Collectors.toList());

	}

	public void scheduleEvent(EventModelUI event, Map<Integer, ZoneModel> zoneMap, Date scheduleDate) {
		scheduleUsers(zoneMap.get(event.getZoneId()), event, zoneMap, scheduleDate);
	}

	private void scheduleUsers(ZoneModel zone, EventModelUI event, Map<Integer, ZoneModel> zoneMap, Date scheduleDate) {

		initialize(event, zone.getId(), scheduleDate);
		if (totalUnassignedUsersInZoneForScheduleDate.isEmpty()) {
			// Go for NEXT near by zone
			scheduleUsersToNearByZones(zone, event, scheduleDate, zoneMap);
		} else {
			if (totalUnassignedUsersInZoneForScheduleDate.size() >= totalUsersToBeAssignedForScheduleDate) {
				allocateUsersToEvent(event, zone, totalUsersToBeAssignedForScheduleDate, scheduleDate);
			} else {
				allocateUsersToEvent(event, zone, Math.min(totalUnassignedUsersInZoneForScheduleDate.size(), totalUsersToBeAssignedForScheduleDate), scheduleDate);
				// Go for NEXT near by zone
				scheduleUsersToNearByZones(zone, event, scheduleDate, zoneMap);
			}
		}
		// Update the Event Status
		// When the event is getting scheduled (scheduled users < noOfUsersRequired)
		// When the event is getting scheduled (scheduled users = noOfUsersRequired)
		initialize(event, zone.getId(), scheduleDate);
		updateEventStatus(event.getId());
	}

	private void updateEventStatus(long eventId) {
		Optional<Events> workForceOptional = eventRepository.findById(eventId);
		if (workForceOptional.isPresent()) {
			Events user = workForceOptional.get();
			user.setStatus(totalUsersToBeAssignedForScheduleDate <= 0 ? EventStatusEnum.ASSIGNED : EventStatusEnum.SCHEDULING);
			eventRepository.save(user);
		}
	}

	private void scheduleUsersToNearByZones(ZoneModel zone, EventModelUI event, Date scheduleDate, Map<Integer, ZoneModel> zoneMap) {
		List<Integer> nearZones = zone.getNearByZones();
		for (Integer z : nearZones) {

			initialize(event, z, scheduleDate);

			if (totalUsersToBeAssignedForScheduleDate <= 0) {
				break;
			}

			if (totalUnassignedUsersInZoneForScheduleDate.isEmpty()) {
				// Continue to Next Zone
				continue;
			}
			// Allocate the users
			allocateUsersToEvent(event, zoneMap.get(z), Math.min(totalUnassignedUsersInZoneForScheduleDate.size(), totalUsersToBeAssignedForScheduleDate), scheduleDate);
		}
	}

	private void allocateUsersToEvent(EventModelUI event, ZoneModel zone, int totalUsersToBeAssignedForScheduleDate, Date scheduleDate) {

		for (Iterator<UserModelUI> iterator = totalUnassignedUsersInZoneForScheduleDate.iterator(); iterator.hasNext();) {
			UserModelUI userModelUI = (UserModelUI) iterator.next();
			// Allocate the User to the Event for the Schedule Date
			System.out.println("User[" + userModelUI.getId() + "] allocating to event[" + event.getId() + "] for Scheduled Date [" + scheduleDate + "]");
			scheduleRepository.save(new Schedules(0l, event.getId(), userModelUI.getId(), scheduleDate, UserWorkStatusEnum.ASSIGNED, null, null));
			if (--totalUsersToBeAssignedForScheduleDate == 0) {
				break;
			}
		}

	}

	private ScheduleModelUI convertToModelUI(Schedules schedules) {
		ScheduleModelUI workforceModelUI = modelMapper.map(schedules, ScheduleModelUI.class);
		return workforceModelUI;
	}

	private List<EventModelUI> getEventsOrderBySeverity(Date scheduleDate) {

		// Get Only Events with Status UNASSIGNED and SCHEDULING
		// When the event is created [triggered by Event screen]
		// When the event is getting scheduled (scheduled users < noOfUsersRequired)
		// Sort by Severity(EventSeverityEnum)

		return eventRepository.findByStatus(EventStatusEnum.UNASSIGNED).stream().map(event -> modelMapper.map(event, EventModelUI.class)).filter(event -> on(scheduleDate, event.getStartDate()))
				.filter(event -> event.getStatus() == EventStatusEnum.UNASSIGNED || event.getStatus() == EventStatusEnum.SCHEDULING).sorted(new EventModelUIComparator()).collect(Collectors.toList());
	}

	private boolean betweenInclusive(Date scheduleDate, Date dateStart, Date dateEnd) {

		boolean returnStatus = false;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			scheduleDate = sdf.parse(sdf.format(scheduleDate));
			dateStart = sdf.parse(sdf.format(dateStart));
			dateEnd = sdf.parse(sdf.format(dateEnd));
		} catch (Exception e) {
			scheduleDate = null;
			dateStart = null;
			dateEnd = null;
		}

		if (scheduleDate != null && dateStart != null && dateEnd != null) {
			if ((scheduleDate.equals(dateStart) || scheduleDate.after(dateStart)) && (scheduleDate.equals(dateEnd) || scheduleDate.before(dateEnd))) {
				returnStatus = true;
			}
		}

		System.out.println("scheduleDate[" + scheduleDate + "] dateStart[" + dateStart + "] dateEnd[" + dateEnd + "] is [[" + returnStatus + "]]");

		return returnStatus;
	}

	private boolean on(Date fromDate, Date toDate) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fromDate = sdf.parse(sdf.format(fromDate));
			toDate = sdf.parse(sdf.format(toDate));
		} catch (Exception e) {
			fromDate = null;
			toDate = null;
		}
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
