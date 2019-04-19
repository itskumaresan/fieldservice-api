package com.gaksvytech.fieldservice.model;

import java.time.LocalDateTime;

import com.gaksvytech.fieldservice.emuns.UserRoleEnum;
import com.gaksvytech.fieldservice.emuns.UserStatusEnum;
import com.gaksvytech.fieldservice.emuns.UserWorkStatusEnum;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class WorkforceModelUI {
	private long id;
	private String username;
	private String password;
	private String email;
	private UserRoleEnum role;
	private UserWorkStatusEnum workStatus;
	private UserStatusEnum active;
	private String latitude;
	private String longitude;
	private LocalDateTime created;
	private LocalDateTime updated;
}
