package com.gaksvytech.fieldservice.enums;

public enum EventSeverityEnum {

	CRITICAL(4), MAJOR(3), MODERATE(2), LOW(1);

	private int value;

	EventSeverityEnum(int value) {
		this.value = value;
	}

	public int value() {
		return value;
	}

}
