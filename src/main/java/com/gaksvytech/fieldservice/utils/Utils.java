package com.gaksvytech.fieldservice.utils;

public class Utils {

	public static double distanceBetweenZones(double lat1, double lon1, double lat2, double lon2) {
		final int RADIUS = 6371; // Radius of the earth

		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distanceInKms = RADIUS * c; // Distance in km
		return distanceInKms;
	}

}
