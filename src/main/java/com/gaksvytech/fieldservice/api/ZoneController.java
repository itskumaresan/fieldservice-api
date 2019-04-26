package com.gaksvytech.fieldservice.api;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.gaksvytech.fieldservice.model.ZoneModel;
import com.gaksvytech.fieldservice.model.ZoneModelUI;
import com.gaksvytech.fieldservice.repository.ZoneRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@Api(value = "Field Service Events API")
@RequestMapping("api/v1/zones/")
public class ZoneController {

	@Autowired
	public ZoneRepository zoneRepository;

	@Autowired
	private ModelMapper modelMapper;

	@ApiOperation(value = "View a list of Zone(s)", response = ZoneModelUI.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list") })
	@GetMapping("")
	public ResponseEntity<List<ZoneModelUI>> read() {
		return ResponseEntity.ok(zoneRepository.findAll().stream().map(zone -> convertToModelUI(zone)).collect(Collectors.toList()));
	}
	
	private ZoneModelUI convertToModelUI(ZoneModel zone) {
		ZoneModelUI zoneModelUI = modelMapper.map(zone, ZoneModelUI.class);
		return zoneModelUI;
	}
}
