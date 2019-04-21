package com.gaksvytech.fieldservice.model;

import java.util.Date;

import com.gaksvytech.fieldservice.emuns.ActiveFlagEnum;
import com.gaksvytech.fieldservice.emuns.UserRoleEnum;

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
	private ActiveFlagEnum active;
	private Date startDate;
	private Date endDate;
}
