/**
 * 
 */
package com.sm.elevator;

import static com.sm.elevator.util.TestUtil.createBuliding;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Test;

import com.sm.elevator.entity.Passenger;
import com.sm.elevator.enums.PassengerStatus;

/**
 * This test case is to test that application should not keep any user waiting
 * for indefinite time.
 * 
 * @author mahars
 * 
 */
public class ElevatorExtremeCaseTest extends TestCase {

	private long floorWaitTime = 5;
	private long floorTravelTime = 1;
	private int maxPassengers = 3000;
	private int maxWaitSecsForTestCaseRun = 60 * 1;// 1 min

	@Test
	public void testIfPassengersDoesNotWaitForIndefiniteTime() throws Exception {
		int numElevators = 10;
		int numFloors = 15;
		int elevatorCapacity = 10;// max passengers
		// Create building
		Building building = createBuliding(numElevators, numFloors, elevatorCapacity, floorWaitTime, floorTravelTime);
		// Create passengers
		Set<Passenger> passengers = new HashSet<Passenger>();
		for (int i = 1; i <= maxPassengers; i++) {
			// Set random current floor and destination floor
			Passenger p = new Passenger("P#" + i, building, i % 15, (i + 100 + i % 15) % 15);
			p.request();
			passengers.add(p);
		}
		// Start elevators
		building.startElevators();

		long startTime = Calendar.getInstance().getTimeInMillis();

		// wait until request is not completed
		while (!(isAllPassengersReachedDestination(passengers))) {
			// Check if test case is timed out
			if ((Calendar.getInstance().getTimeInMillis() - startTime) / 1000 > maxWaitSecsForTestCaseRun) {
				if (building.getPassengerRequestQueue().size() > 0) {
					fail("Test case timed out in " + maxWaitSecsForTestCaseRun + " secs and "
							+ building.getPassengerRequestQueue().size() + " requests pending");
				}
			}
		}

		assertEquals("Request queue should be empty", 0, building.getPassengerRequestQueue().size());

		// Stop elevators
		building.stopElevators();
	}

	private boolean isAllPassengersReachedDestination(Set<Passenger> passengers) {
		for (Passenger p : passengers) {
			if (p.getStatus() != PassengerStatus.DONE) {
				return false;
			}
		}
		return true;
	}
}
