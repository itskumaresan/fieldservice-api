package com.gaksvytech.fieldservice.api;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
public class EventScheduleContoller {

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
	public ResponseEntity<ScheduleModelUI> read(@PathVariable("id") String scheduleDate) throws ParseException {
		Optional<Schedules> workForceOptional = eventScheduleRepository.findByScheduleDate(new SimpleDateFormat("yyyy-MM-dd").parse(scheduleDate));
		if (!workForceOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(convertToModelUI(workForceOptional.get()));
	}

	private ScheduleModelUI convertToModelUI(Schedules schedules) {
		ScheduleModelUI workforceModelUI = modelMapper.map(schedules, ScheduleModelUI.class);
		return workforceModelUI;
	}

}
