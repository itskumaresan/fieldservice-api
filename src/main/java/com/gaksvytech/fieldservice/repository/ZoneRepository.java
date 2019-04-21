package com.gaksvytech.fieldservice.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.gaksvytech.fieldservice.model.ZoneModel;

public class ZoneRepository {

	static List<ZoneModel> zones = new ArrayList<>();
	static {
		zones.add(new ZoneModel(1, "East", 123456, 56789, Arrays.asList(2, 3, 4, 5)));
		zones.add(new ZoneModel(2, "West", 123456, 56789, Arrays.asList(2, 3, 4, 5)));
		zones.add(new ZoneModel(3, "Central", 123456, 56789, Arrays.asList(2, 3, 4, 5)));
		zones.add(new ZoneModel(4, "North", 123456, 56789, Arrays.asList(2, 3, 4, 5)));
		zones.add(new ZoneModel(5, "South", 123456, 56789, Arrays.asList(2, 3, 4, 5)));
	}

	public List<ZoneModel> getZones() {

		return zones;

	}

	public List<ZoneModel> getZoneById(int id) {

		return zones.stream().filter(zone -> zone.getZoneId() == id).collect(Collectors.toList());

	}

}
