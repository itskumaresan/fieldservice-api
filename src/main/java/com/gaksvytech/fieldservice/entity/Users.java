package com.gaksvytech.fieldservice.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.gaksvytech.fieldservice.emuns.UserRoleEnum;
import com.gaksvytech.fieldservice.emuns.UserStatusEnum;
import com.gaksvytech.fieldservice.emuns.UserWorkStatusEnum;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Users {

	@Id
	@GeneratedValue
	private long id;
	private String username;
	private String password;
	private String email;
	@Enumerated(EnumType.STRING)
	private UserRoleEnum role;
	@Enumerated(EnumType.STRING)
	private UserWorkStatusEnum workStatus;
	@Enumerated(EnumType.STRING)
	private UserStatusEnum active;
	private String latitude;
	private String longitude;
	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime created;
	@UpdateTimestamp
	private LocalDateTime updated;

}
