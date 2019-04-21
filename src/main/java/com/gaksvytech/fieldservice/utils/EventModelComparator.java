package com.gaksvytech.fieldservice.utils;

import java.util.Comparator;

import com.gaksvytech.fieldservice.model.EventModel;

public class EventModelComparator implements Comparator<EventModel> {

	@Override
	public int compare(EventModel o1, EventModel o2) {
		return o1.getSeverity().value() - o2.getSeverity().value();
	}
}
