package com.gaksvytech.fieldservice.model;

import java.time.LocalDateTime;
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
public class EventModelUI {
	private long id;
	private int zoneId;
	private String name;
	private EventStatusEnum status;
	private EventSeverityEnum severity;
	private ActiveFlagEnum active;
	private Date startDate;
	private Date endDate;
	private LocalDateTime created;
	private LocalDateTime updated;
	private ZoneModel zone;
	private int numberOfWorkersRequired;
}
