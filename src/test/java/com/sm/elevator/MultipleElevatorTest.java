/**
 * 
 */
package com.sm.elevator;

import static com.sm.elevator.util.TestUtil.createBuliding;
import junit.framework.TestCase;

import org.junit.Test;

import com.sm.elevator.entity.Passenger;
import com.sm.elevator.enums.PassengerStatus;

/**
 * @author mahars
 * 
 */
public class MultipleElevatorTest extends TestCase {

	private long floorWaitTime = 20;
	private long floorTravelTime = 10;

	@Test
	public void testMultipleElevatorsWithMultiplePassengersServicedByCorrectElevators() throws Exception {
		int numElevators = 5;
		int numFloors = 15;
		int elevatorCapacity = 10;// max passengers
		// Create building
		Building building = createBuliding(numElevators, numFloors, elevatorCapacity, floorWaitTime, floorTravelTime);
		// Create passengers
		Passenger andy = new Passenger("Andy", building, 0, 12);
		Passenger barbie = new Passenger("Barbie", building, 0, 13);
		Passenger charlie = new Passenger("Charlie", building, 0, 10);
		Passenger dave = new Passenger("Dave", building, 0, 9);
		Passenger elie = new Passenger("Elie", building, 11, 3);
		Passenger fab = new Passenger("Fab", building, 11, 9);
		Passenger git = new Passenger("Git", building, 11, 0);
		// Start passengers
		andy.request();
		barbie.request();
		charlie.request();
		dave.request();
		elie.request();
		fab.request();
		git.request();
		// Start elevators
		building.startElevators();

		while (!(andy.getStatus() == PassengerStatus.DONE && barbie.getStatus() == PassengerStatus.DONE
				&& charlie.getStatus() == PassengerStatus.DONE && dave.getStatus() == PassengerStatus.DONE
				&& elie.getStatus() == PassengerStatus.DONE && fab.getStatus() == PassengerStatus.DONE && git
					.getStatus() == PassengerStatus.DONE)) {
			// wait until request is not completed
		}

		assertEquals("Request queue should be empty", 0, building.getPassengerRequestQueue().size());
		assertEquals("Five elevators should be present", numElevators, building.getElevators().length);

		// Passengers going up should be in same elevator
		Elevator elevator1 = andy.getRequests().get(0).getElevator();
		String msg1 = "Passengers going up should be in same elevator";
		assertTrue(msg1, elevator1.equals(andy.getRequests().get(0).getElevator()));
		assertTrue(msg1, elevator1.equals(barbie.getRequests().get(0).getElevator()));
		assertTrue(msg1, elevator1.equals(charlie.getRequests().get(0).getElevator()));
		assertTrue(msg1, elevator1.equals(dave.getRequests().get(0).getElevator()));

		assertEquals("Elevator should be at destination floor", 13, elevator1.getCurrentFloor());
		assertEquals("Elevator should traveled up one time", 1, elevator1.getUpTravelCount());

		// Passengers going down should be in same elevator
		Elevator elevator2 = elie.getRequests().get(0).getElevator();
		String msg2 = "Passengers going down should be in same elevator";
		assertTrue(msg2, elevator2.equals(elie.getRequests().get(0).getElevator()));
		assertTrue(msg2, elevator2.equals(fab.getRequests().get(0).getElevator()));
		assertTrue(msg2, elevator2.equals(git.getRequests().get(0).getElevator()));

		assertEquals("Elevator should be at destination floor", 0, elevator2.getCurrentFloor());
		assertEquals("Elevator should traveled down one time", 1, elevator2.getDownTravelCount());

		// Stop elevators
		building.stopElevators();

	}
}
