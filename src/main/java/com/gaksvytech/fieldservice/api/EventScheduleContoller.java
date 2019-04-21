package com.gaksvytech.fieldservice.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gaksvytech.fieldservice.scheduler.EventSchedulerEngine;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@Api(value = "Field Service Batch Process Schedule API")
@RequestMapping("api/v1/batch/")
public class EventScheduleContoller {

	@Autowired
	public EventSchedulerEngine eventSchedulerEngine;

	@ApiOperation(value = "To run the batch", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Batch ran successfully") })
	@PostMapping("")
	public ResponseEntity<String> read() throws Exception {
		eventSchedulerEngine.process();
		return ResponseEntity.ok("Success");
	}

}
