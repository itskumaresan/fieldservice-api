package com.gaksvytech.fieldservice.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gaksvytech.fieldservice.emuns.UserWorkStatusEnum;
import com.gaksvytech.fieldservice.entity.Schedules;
import com.gaksvytech.fieldservice.model.ScheduleModelUI;
import com.gaksvytech.fieldservice.repository.ScheduleRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@Api(value = "Field Service Event Schedule API")
@RequestMapping("api/v1/schedule/")
public class ScheduleContoller {

	@Autowired
	public ScheduleRepository eventScheduleRepository;

	@Autowired
	private ModelMapper modelMapper;

	@ApiOperation(value = "View a list of Schedule(s)", response = ScheduleModelUI.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list") })
	@GetMapping("")
	public ResponseEntity<List<ScheduleModelUI>> read() {
		return ResponseEntity.ok(eventScheduleRepository.findAll().stream().map(workForce -> convertToModelUI(workForce)).collect(Collectors.toList()));
	}

	@ApiOperation(value = "View a Schedule By Date", response = ScheduleModelUI.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved the Schedule for Given Date"), @ApiResponse(code = 404, message = "Unable to retrieve the Schedule for the given date") })
	@GetMapping("{date}")
	public ResponseEntity<List<ScheduleModelUI>> read(@PathVariable("id") String scheduleDate) throws ParseException {
		Date scheduledDate = new SimpleDateFormat("yyyy-MM-dd").parse(scheduleDate);
		return ResponseEntity.ok(eventScheduleRepository.findAll().stream().map(workForce -> convertToModelUI(workForce)).filter(schedule -> on(scheduledDate, schedule.getScheduleDate())).collect(Collectors.toList()));

	}

	@ApiOperation(value = "Update a Schedule with the provided status", response = ScheduleModelUI.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated the Schedule with provided status"),
			@ApiResponse(code = 404, message = "Unable to retrieve the Schedule By Id. The Id does not exists. Update unsuccessfull") })
	@PutMapping("{id}/{toStatus}")
	public ResponseEntity<ScheduleModelUI> updateStatus(@PathVariable Long id, @PathVariable UserWorkStatusEnum toStatus) {
		Optional<Schedules> workForceOptional = eventScheduleRepository.findById(id);
		if (!workForceOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		} else {
			Schedules schedules = workForceOptional.get();
			schedules.setStatus(toStatus);
			Schedules saved = eventScheduleRepository.save(schedules);
			if (saved == null) {
				return ResponseEntity.notFound().build();
			} else {
				return ResponseEntity.ok(convertToModelUI(saved));
			}
		}
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

	private ScheduleModelUI convertToModelUI(Schedules schedules) {
		ScheduleModelUI workforceModelUI = modelMapper.map(schedules, ScheduleModelUI.class);
		return workforceModelUI;
	}

}
