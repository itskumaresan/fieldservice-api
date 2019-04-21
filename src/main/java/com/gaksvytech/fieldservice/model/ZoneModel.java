package com.gaksvytech.fieldservice.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ZoneModel {

	private int zoneId;
	private String name; // East, West, North South

	private double lattitude;
	private double longitude;

	private List<Integer> nearByZones; // 2, 3, 4

}
