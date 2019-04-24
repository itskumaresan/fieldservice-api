package com.gaksvytech.fieldservice.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gaksvytech.fieldservice.entity.Events;
import com.gaksvytech.fieldservice.entity.Schedules;
import com.gaksvytech.fieldservice.entity.Users;
import com.gaksvytech.fieldservice.enums.ActiveFlagEnum;
import com.gaksvytech.fieldservice.enums.EventStatusEnum;
import com.gaksvytech.fieldservice.enums.UserWorkStatusEnum;
import com.gaksvytech.fieldservice.model.EventModel;
import com.gaksvytech.fieldservice.model.EventModelUI;
import com.gaksvytech.fieldservice.model.UserModelUI;
import com.gaksvytech.fieldservice.repository.EventRepository;
import com.gaksvytech.fieldservice.repository.ScheduleRepository;
import com.gaksvytech.fieldservice.repository.UserRepository;
import com.gaksvytech.fieldservice.repository.ZoneRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@Api(value = "Field Service Events API")
@RequestMapping("api/v1/events/")
public class EventContoller {

	@Autowired
	public EventRepository eventRepository;

	@Autowired
	public ZoneRepository zoneRepository;

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	public ScheduleRepository scheduleRepository;

	@ApiOperation(value = "View a list of Event(s)", response = EventModelUI.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list") })
	@GetMapping("")
	public ResponseEntity<List<EventModelUI>> read() {

		return ResponseEntity.ok(eventRepository.findAll().stream().map(workForce -> convertToModelUI(workForce)).collect(Collectors.toList()));
	}

	@ApiOperation(value = "View a Event By Id", response = EventModelUI.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved the Event By Given Id"), @ApiResponse(code = 404, message = "Unable to retrieve the Event By Id. The Id does not exists") })
	@GetMapping("{id}")
	public ResponseEntity<EventModelUI> read(@PathVariable("id") Long id) {
		Optional<Events> workForceOptional = eventRepository.findById(id);
		if (!workForceOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(convertToModelUI(workForceOptional.get()));
		}
	}

	@ApiOperation(value = "View all Events for given Zone Id", response = UserModelUI.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved the Event By Given Zone Id"),
			@ApiResponse(code = 404, message = "Unable to retrieve the Event By Zone Id. The Id does not exists") })
	@GetMapping("zones/{zoneId}")
	public ResponseEntity<List<EventModelUI>> readByZoneId(@PathVariable("zoneId") int zoneId) {
		return ResponseEntity.ok(eventRepository.findByZoneId(zoneId).stream().map(workForce -> convertToModelUI(workForce)).collect(Collectors.toList()));
	}

	@ApiOperation(value = "Create an Event", response = EventModelUI.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Successfully created the Event") })
	@PostMapping("")
	public ResponseEntity<EventModelUI> create(@RequestBody EventModel workforceModel) throws URISyntaxException {
		Events user = convertToEntity(workforceModel);
		user.setActive(ActiveFlagEnum.Y);
		user.setStatus(EventStatusEnum.UNASSIGNED);
		Events saved = eventRepository.save(user);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saved.getId()).toUri();
		return ResponseEntity.created(uri).body(convertToModelUI(saved));
	}

	@ApiOperation(value = "Update a Event", response = EventModelUI.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated the Event"), @ApiResponse(code = 404, message = "Unable to retrieve the Event By Id. The Id does not exists. Update unsuccessfull") })
	@PutMapping("{id}")
	public ResponseEntity<EventModelUI> update(@RequestBody EventModel workforceModel, @PathVariable Long id) {
		Optional<Events> workForceOptional = eventRepository.findById(id);
		if (!workForceOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		} else {
			Events event = convertToEntity(workforceModel);
			event.setId(id);
			/*if(event.getStatus().equals(EventStatusEnum.COMPLETED)) {
				updateUsersSchedule(event);
			}*/
			Events saved = eventRepository.save(event);
			if (saved == null) {
				return ResponseEntity.notFound().build();
			} else {
				return ResponseEntity.ok(convertToModelUI(saved));
			}
		}
	}

	@ApiOperation(value = "Update a Event with the provided status", response = EventModelUI.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated the Event with provided status"),
			@ApiResponse(code = 404, message = "Unable to retrieve the Event By Id. The Id does not exists. Update unsuccessfull") })
	@PutMapping("{id}/{toStatus}")
	public ResponseEntity<EventModelUI> updateStatus(@PathVariable Long id, @PathVariable EventStatusEnum toStatus) {
		Optional<Events> workForceOptional = eventRepository.findById(id);
		if (!workForceOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		} else {
			Events event = workForceOptional.get();
			event.setStatus(toStatus);
			if(event.getStatus().equals(EventStatusEnum.COMPLETED)) {
				event.setActive(ActiveFlagEnum.N);
				updateUsersSchedule(event);
			}
			Events saved = eventRepository.save(event);
			if (saved == null) {
				return ResponseEntity.notFound().build();
			} else {
				return ResponseEntity.ok(convertToModelUI(saved));
			}
		}
	}
	
	private void updateUsersSchedule(Events event) {		
		for(Schedules schd: scheduleRepository.findByEventId(event.getId())) {
			schd.setStatus(UserWorkStatusEnum.COMPLETED);
			scheduleRepository.save(schd);			
		}
	}

	private EventModelUI convertToModelUI(Events event) {
		EventModelUI eventModelUI = modelMapper.map(event, EventModelUI.class);
		eventModelUI.setZone(zoneRepository.findById(eventModelUI.getZoneId()));
		return eventModelUI;
	}

	private Events convertToEntity(EventModel workforceModel) {
		Events event = modelMapper.map(workforceModel, Events.class);
		return event;
	}
}
