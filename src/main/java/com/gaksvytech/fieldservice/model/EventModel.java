package com.gaksvytech.fieldservice.model;

import java.util.Date;

import com.gaksvytech.fieldservice.enums.ActiveFlagEnum;
import com.gaksvytech.fieldservice.enums.EventSeverityEnum;
import com.gaksvytech.fieldservice.enums.EventStatusEnum;

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
public class EventModel {
	private String name;
	private int zoneId;
	private EventStatusEnum status;
	private EventSeverityEnum severity;
	private ActiveFlagEnum active;
	private Date startDate;
	private Date endDate;
	private int numberOfWorkersRequired;
}
