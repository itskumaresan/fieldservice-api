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

import com.gaksvytech.fieldservice.emuns.ActiveFlagEnum;
import com.gaksvytech.fieldservice.emuns.EventPriorityEnum;
import com.gaksvytech.fieldservice.emuns.EventSeverityEnum;
import com.gaksvytech.fieldservice.emuns.EventStatusEnum;

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
public class Events {

	@Id
	@GeneratedValue
	private long id;

	private String name;

	@Enumerated(EnumType.STRING)
	private EventStatusEnum status;

	@Enumerated(EnumType.STRING)
	private EventPriorityEnum priority;

	@Enumerated(EnumType.STRING)
	private EventSeverityEnum severity;

	@Enumerated(EnumType.STRING)
	private ActiveFlagEnum active;

	private String latitude;
	private String longitude;

	@Temporal(TemporalType.DATE)
	private Date startDate;

	@Temporal(TemporalType.DATE)
	private Date endDate;

	@CreationTimestamp
	@Column(updatable = false)
	private LocalDateTime created;

	@UpdateTimestamp
	private LocalDateTime updated;

}
