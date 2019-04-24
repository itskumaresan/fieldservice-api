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

import com.gaksvytech.fieldservice.enums.UserWorkStatusEnum;

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
public class Schedules {

	@Id
	@GeneratedValue
	private long id;

	private long eventId;

	private long userId;

	@Temporal(TemporalType.DATE)
	private Date scheduleDate;

	@Enumerated(EnumType.STRING)
	private UserWorkStatusEnum status;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime created;

	@UpdateTimestamp
	private LocalDateTime updated;

}
