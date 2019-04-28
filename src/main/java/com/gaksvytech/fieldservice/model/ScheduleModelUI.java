package com.gaksvytech.fieldservice.model;

import java.time.LocalDateTime;
import java.util.Date;

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
public class ScheduleModelUI {

	private long id;
	private long eventId;
	private long userId;
	private Date scheduleDate;
	private String userName;
	private String eventName;
	private EventStatusEnum status;

	private LocalDateTime created;
	private LocalDateTime updated;
}
