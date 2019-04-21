package com.gaksvytech.fieldservice.model;

import com.gaksvytech.fieldservice.emuns.ActiveFlagEnum;
import com.gaksvytech.fieldservice.emuns.UserRoleEnum;
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
public class UserModel {
	private String name;
	private int zoneId;
	private String password;
	private String email;
	private UserRoleEnum role;
	private UserWorkStatusEnum status;
	private ActiveFlagEnum active;
}
