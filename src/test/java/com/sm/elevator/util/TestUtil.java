/**
 * 
 */
package com.sm.elevator.util;

import com.sm.elevator.Building;

/**
 * Test utility class
 * 
 * @author mahars
 * 
 */
public class TestUtil {

	public static Building createBuliding(int numElevators, int numFloors, int elevatorCapacity, long floorWaitTime,
			long floorTravelTime) {
		Building building = new Building(numElevators, numFloors, elevatorCapacity, floorWaitTime, floorTravelTime);
		return building;
	}

}
