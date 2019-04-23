package com.gaksvytech.fieldservice.emuns;

public enum EventStatusEnum {
	
	UNASSIGNED, // When the event is created [triggered by Event screen]
	SCHEDULING, // When the event is getting scheduled (scheduled users < noOfUsersRequired) [triggered by scheduler]
	ASSIGNED,   // When the event is getting scheduled (scheduled users = noOfUsersRequired) [triggered by scheduler]
	COMPLETED;  // When the event is completed [triggered by the schedule screen]
	
}