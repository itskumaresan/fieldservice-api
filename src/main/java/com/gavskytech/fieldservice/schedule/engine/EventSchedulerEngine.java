package com.gavskytech.fieldservice.schedule.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gaksvytech.fieldservice.model.EventModel;
import com.gaksvytech.fieldservice.model.Zone;

public class EventSchedulerEngine {

	static Map<Integer, Integer> userZoneMap = new HashMap<>();
	static List<Zone> zoneList = new ArrayList<>();

	static {
		userZoneMap.put(1, 10);
		userZoneMap.put(2, 20);
		userZoneMap.put(3, 20);
		userZoneMap.put(4, 10);
		userZoneMap.put(5, 5);

		zoneList.add(new Zone(1, "4,3", 123456, 23456));
		zoneList.add(new Zone(2, "5,1", 123456, 23456));
		zoneList.add(new Zone(3, "1,4", 123456, 23456));
		zoneList.add(new Zone(4, "5,1", 123456, 23456));
		zoneList.add(new Zone(5, "3,4", 123456, 23456));
	}
	
	public void scheduleEvent(EventModel event) {
		int noOfUsers = 10;
		Zone nearByZone = distanceZoneToEvent(event);
		scheduleUsers(nearByZone, noOfUsers, event);
	}
	
	private void scheduleUsers(Zone zone, int noOfUsers, EventModel event) {
		if(getNoOfAvailableUsersInZone(zone.getZoneId()) >= noOfUsers) {
			allocateUsersToEvent(event, zone, noOfUsers);
		} else {
			int noOfAvailableUsersInZone = getNoOfAvailableUsersInZone(zone.getZoneId());
			allocateUsersToEvent(event, zone, noOfAvailableUsersInZone);
			noOfUsers = noOfUsers - noOfAvailableUsersInZone;
			List<String> nearZones = Arrays.asList(zone.getNearByZones().split(","));
			for(String z: nearZones) {
				Zone nextNearestZone = getZoneById(Integer.parseInt(z));
				if(noOfUsers <= 0) {
					break;
				}
				allocateUsersToEvent(event, nextNearestZone, noOfUsers);
				noOfUsers = noOfUsers - nextNearestZone.getZoneId();
			}
		}
	}
	
	private int getNoOfAvailableUsersInZone(int zoneId) {
		// TODO Auto-generated method stub
		// DAO call to get number of available users in the zone
		return userZoneMap.get(zoneId);
	}

	private Zone getZoneById(int zoneId) {
		// TODO Query DAO to get the zone object
		Zone zone = null;
		for(Zone z: zoneList) {
			if(z.getZoneId() == zoneId) {
				zone = z;
				break;
			}
		}
		return zone;
	}

	private void allocateUsersToEvent(EventModel event, Zone zone, int noOfUsers) {
		//TODO - Persist in EventSchedule Table DAO Call
		//getUsersByZone(); - Available users
		//persistEventScheduleWithUserList
		/*
		 * Mark user as scheduled for this event
		 * 
		 */
	}
	
	private Zone distanceZoneToEvent(EventModel event) {
		double distanceInKm = 0;
		double min = Double.MAX_VALUE;
		Zone nearestZone = null;
		for(Zone zone: zoneList) {
			distanceInKm = EventSchedulerUtil.distanceBetweenZones(event.getLatitude(), event.getLongitude(), zone.getLattitude(), zone.getLongitude());
			if(distanceInKm < min) {
				min = distanceInKm;
				nearestZone = zone;
			}
		}
		return nearestZone;
	}
	
	public static void main(String[] args) {
		EventModel event = new EventModel();
		event.setLatitude(62535463);
		event.setLatitude(85978555);
		new EventSchedulerEngine().scheduleEvent(event);
	}
}
