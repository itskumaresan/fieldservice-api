package com.gaksvytech.fieldservice.entity;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.gaksvytech.fieldservice.enums.ActiveFlagEnum;
import com.gaksvytech.fieldservice.enums.UserRoleEnum;

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

	private int zoneId;

	private String name;

	private String password;

	private String email;

	@Temporal(TemporalType.DATE)
	private Date startDate;

	@Temporal(TemporalType.DATE)
	private Date endDate;

	@Enumerated(EnumType.STRING)
	private UserRoleEnum role;

	@Enumerated(EnumType.STRING)
	private ActiveFlagEnum active;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime created;

	@UpdateTimestamp
	private LocalDateTime updated;

}
