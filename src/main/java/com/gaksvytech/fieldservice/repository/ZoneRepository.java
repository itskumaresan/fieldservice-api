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
		zones.add(new ZoneModel(1, "East", 22.259323561177727, 88.3910771207826, Arrays.asList(3, 4, 5, 2))); // Kolkatta
		zones.add(new ZoneModel(2, "West", 22.625425760875363, 70.83812277432776, Arrays.asList(3, 4, 5, 1))); // Gujarat
		zones.add(new ZoneModel(3, "Central", 21.369929558732032, 79.10644239971191, Arrays.asList(1, 2, 4, 5))); // Nagpur
		zones.add(new ZoneModel(4, "North", 28.27270327820999, 77.35521342274046, Arrays.asList(2, 3, 1, 5))); // New Delhi
		zones.add(new ZoneModel(5, "South", 13.263321945380216, 80.3063624398012, Arrays.asList(2, 3, 1, 4))); // Chennai
	}

	public List<ZoneModel> findAll() {
		return zones;
	}

	public ZoneModel findById(int id) {
		return zones.stream().filter(zone -> zone.getId() == id).findFirst().get();
	}

}
