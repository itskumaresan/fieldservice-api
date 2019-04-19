package com.gaksvytech.fieldservice.model;

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
public class Zone {

	private int zoneId;
	private String nearByZones; // Z2, Z3
	private double lattitude;
	private double longitude;
	
}
