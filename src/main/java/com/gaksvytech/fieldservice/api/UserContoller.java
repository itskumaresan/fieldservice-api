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

import com.gaksvytech.fieldservice.emuns.UserStatusEnum;
import com.gaksvytech.fieldservice.emuns.UserWorkStatusEnum;
import com.gaksvytech.fieldservice.entity.Users;
import com.gaksvytech.fieldservice.model.UserModel;
import com.gaksvytech.fieldservice.model.UserModelUI;
import com.gaksvytech.fieldservice.repository.UserRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Controller
@Api(value = "Field Service Work Force API")
@RequestMapping("api/v1/workforce/")
public class UserContoller {

	@Autowired
	public UserRepository workForceRepository;

	@Autowired
	private ModelMapper modelMapper;

	@ApiOperation(value = "View a list of Work Force(s)", response = UserModelUI.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved list") })
	@GetMapping("")
	public ResponseEntity<List<UserModelUI>> read() {

		return ResponseEntity.ok(workForceRepository.findAll().stream().map(workForce -> convertToModelUI(workForce))
				.collect(Collectors.toList()));
	}

	@ApiOperation(value = "View a Work Force By Id", response = UserModelUI.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully retrieved the Work Force By Given Id"),
			@ApiResponse(code = 404, message = "Unable to retrieve the Work Force By Id. The Id does not exists") })
	@GetMapping("{id}")
	public ResponseEntity<UserModelUI> read(@PathVariable("id") Long id) {
		Optional<Users> workForceOptional = workForceRepository.findById(id);
		if (!workForceOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(convertToModelUI(workForceOptional.get()));
		}
	}

	@ApiOperation(value = "Create a Work Force", response = UserModelUI.class)
	@ApiResponses(value = { @ApiResponse(code = 201, message = "Successfully created the Work Force") })
	@PostMapping("")
	public ResponseEntity<UserModelUI> create(@RequestBody UserModel workforceModel)
			throws URISyntaxException {
		Users user = convertToEntity(workforceModel);
		user.setActive(UserStatusEnum.Y);
		user.setWorkStatus(UserWorkStatusEnum.UNASSIGNED);
		Users saved = workForceRepository.save(user);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(saved.getId()).toUri();
		return ResponseEntity.created(uri).body(convertToModelUI(saved));
	}

	@ApiOperation(value = "Update a Work Force", response = UserModelUI.class)
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Successfully updated the Work Force"),
			@ApiResponse(code = 404, message = "Unable to retrieve the Work Force By Id. The Id does not exists. Update unsuccessfull") })
	@PutMapping("{id}")
	public ResponseEntity<UserModelUI> update(@RequestBody UserModel workforceModel, @PathVariable Long id) {
		Optional<Users> workForceOptional = workForceRepository.findById(id);
		if (!workForceOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		} else {
			Users user = convertToEntity(workforceModel);
			user.setId(id);
			Users saved = workForceRepository.save(user);
			if (saved == null) {
				return ResponseEntity.notFound().build();
			} else {
				return ResponseEntity.ok(convertToModelUI(saved));
			}
		}
	}

	@ApiOperation(value = "Update a Work Force with the provided status", response = UserModelUI.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successfully updated the Work Force with provided status"),
			@ApiResponse(code = 404, message = "Unable to retrieve the Work Force By Id. The Id does not exists. Update unsuccessfull") })
	@PutMapping("{id}/{toStatus}")
	public ResponseEntity<UserModelUI> updateStatus(@PathVariable Long id,
			@PathVariable UserWorkStatusEnum toStatus) {
		Optional<Users> workForceOptional = workForceRepository.findById(id);
		if (!workForceOptional.isPresent()) {
			return ResponseEntity.notFound().build();
		} else {
			Users user = workForceOptional.get();
			user.setWorkStatus(toStatus);
			Users saved = workForceRepository.save(user);
			if (saved == null) {
				return ResponseEntity.notFound().build();
			} else {
				return ResponseEntity.ok(convertToModelUI(saved));
			}
		}
	}

	private UserModelUI convertToModelUI(Users workForce) {
		UserModelUI workforceModelUI = modelMapper.map(workForce, UserModelUI.class);
		return workforceModelUI;
	}

	private Users convertToEntity(UserModel workforceModel) {
		Users workForce = modelMapper.map(workforceModel, Users.class);
		return workForce;
	}
}
