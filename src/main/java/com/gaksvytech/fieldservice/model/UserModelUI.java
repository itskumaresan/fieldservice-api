package com.gaksvytech.fieldservice.model;

import java.time.LocalDateTime;

import com.gaksvytech.fieldservice.emuns.UserRoleEnum;
import com.gaksvytech.fieldservice.emuns.ActiveFlagEnum;
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
public class UserModelUI {
	private long id;
	private int zoneId;
	private String name;
	private String password;
	private String email;
	private UserRoleEnum role;
	private UserWorkStatusEnum status;
	private ActiveFlagEnum active;
	private LocalDateTime created;
	private LocalDateTime updated;
	private ZoneModel zone;
}
