package com.gaksvytech.fieldservice.model;

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
public class ZoneModelUI {
	private int id;
	private String name;
	private double lattitude;
	private double longitude;
}
