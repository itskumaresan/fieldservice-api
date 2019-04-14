package com.gaksvytech.fieldservice.api;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

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

import com.gaksvytech.fieldservice.entity.WorkForce;
import com.gaksvytech.fieldservice.repository.WorkForceRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@Api(value = "Field Service Work Force API")
@RequestMapping("/api/v1/workforce")
public class WorkForceContoller {

	@Autowired
	public WorkForceRepository workForceRepository;

	@ApiOperation(value = "View a list of Work Force(s)", response = List.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list") })
	@GetMapping("/")
	public ResponseEntity<List<WorkForce>> read() {
		return ResponseEntity.ok(workForceRepository.findAll());
	}

	@ApiOperation(value = "View a Work Force By Id", response = WorkForce.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved the Work Force By Given Id"),
			@ApiResponse(code = 404, message = "Unable to retrieve the Work Force By Id. The Id does not exists") })
	@GetMapping("/{id}")
	public ResponseEntity<WorkForce> read(@PathVariable("id") Long id) {
		Optional<WorkForce> workForceOptional = workForceRepository.findById(id);
		if (!workForceOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(workForceOptional.get());
		}
	}

	@ApiOperation(value = "Create a Work Force", response = WorkForce.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Successfully created the Work Force") })
	@PostMapping("/")
	public ResponseEntity<WorkForce> create(@RequestBody WorkForce workForce) throws URISyntaxException {
		WorkForce saved = workForceRepository.save(workForce);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saved.getId()).toUri();
		return ResponseEntity.created(uri).body(saved);
	}

	@ApiOperation(value = "Update a Work Force", response = WorkForce.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated the Work Force"),
			@ApiResponse(code = 404, message = "Unable to retrieve the Work Force By Id. The Id does not exists. Update unsuccessfull") })
	@PutMapping("/{id}")
	public ResponseEntity<WorkForce> update(@RequestBody WorkForce workForce, @PathVariable Long id) {
		Optional<WorkForce> workForceOptional = workForceRepository.findById(id);
		if (!workForceOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		} else {
			WorkForce saved = workForceRepository.save(workForce);
			if (saved == null) {
				return ResponseEntity.notFound().build();
			} else {
				return ResponseEntity.ok(saved);
			}
		}

	}
}
