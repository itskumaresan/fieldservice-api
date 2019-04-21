package com.gaksvytech.fieldservice.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gaksvytech.fieldservice.model.ZoneModel;

@Repository
public class ZoneRepository {

	static List<ZoneModel> zones = new ArrayList<>();
	static {
		zones.add(new ZoneModel(1, "East", 123456, 56789, Arrays.asList(2, 3, 4, 5)));
		zones.add(new ZoneModel(2, "West", 123456, 56789, Arrays.asList(2, 3, 4, 5)));
		zones.add(new ZoneModel(3, "Central", 123456, 56789, Arrays.asList(2, 3, 4, 5)));
		zones.add(new ZoneModel(4, "North", 123456, 56789, Arrays.asList(2, 3, 4, 5)));
		zones.add(new ZoneModel(5, "South", 123456, 56789, Arrays.asList(2, 3, 4, 5)));
	}

	public List<ZoneModel> findAll() {
		return zones;
	}

	public ZoneModel findById(int id) {
		return zones.stream().filter(zone -> zone.getId() == id).findFirst().get();
	}

}
