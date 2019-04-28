package com.gaksvytech.fieldservice.utils;

import java.util.Comparator;

import com.gaksvytech.fieldservice.model.EventModelUI;

public class EventModelUIComparator implements Comparator<EventModelUI> {

	@Override
	public int compare(EventModelUI o1, EventModelUI o2) {
		return o2.getSeverity().value() - o1.getSeverity().value();
	}
}
