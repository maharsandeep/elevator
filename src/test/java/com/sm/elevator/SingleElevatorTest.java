/**
 * 
 */
package com.sm.elevator;

import static com.sm.elevator.util.TestUtil.*;
import junit.framework.TestCase;

import org.junit.Test;

import com.sm.elevator.entity.Passenger;
import com.sm.elevator.enums.PassengerStatus;

/**
 * This test case tests all possible cases with single elevator. Intend here is
 * to test the functionality in depth 
 * 
 * @author mahars
 * 
 */
public class SingleElevatorTest extends TestCase {

	private long floorWaitTime = 20;
	private long floorTravelTime = 10;

	@Test
	public void testSingleElevatorWithSinglePassengerReachesDestinationFloor() throws Exception {
		int numElevators = 1;
		int numFloors = 5;
		int elevatorCapacity = 2;
		// Create building
		Building building = createBuliding(numElevators, numFloors, elevatorCapacity, floorWaitTime, floorTravelTime);
		// Create passengers
		Passenger andy = new Passenger("Andy", building, 0, 5);
		// Start passengers
		andy.request();
		// Start elevators
		building.startElevators();

		// Trip#1 - UP - Andy
		while (andy.getStatus() != PassengerStatus.DONE) {
			// wait until request is not completed
		}

		assertEquals("Request queue should be empty", 0, building.getPassengerRequestQueue().size());
		assertEquals("Only one elevator should be present", 1, building.getElevators().length);
		assertEquals("Elevator should be at destination floor", 5, building.getElevators()[0].getCurrentFloor());
		assertEquals("Elevator should traveled up one time only", 1, building.getElevators()[0].getUpTravelCount());
		assertEquals("Elevator should not traveled down", 0, building.getElevators()[0].getDownTravelCount());

		// Stop elevators
		building.stopElevators();
	}

	@Test
	public void testSingleElevatorWithMaxPassengersTravelsToDestinationFloorOnlyOnce() throws Exception {
		int numElevators = 1;
		int numFloors = 5;
		int elevatorCapacity = 3;// max passengers
		// Create building
		Building building = createBuliding(numElevators, numFloors, elevatorCapacity, floorWaitTime, floorTravelTime);
		// Create passengers
		Passenger andy = new Passenger("Andy", building, 0, 5);
		Passenger barbie = new Passenger("Barbie", building, 1, 3);
		Passenger charlie = new Passenger("Charlie", building, 3, 4);
		// Start passengers
		andy.request();
		barbie.request();
		charlie.request();
		// Start elevators
		building.startElevators();

		// Trip#1 - UP - Andy, Barbie, Charlie
		while (!(andy.getStatus() == PassengerStatus.DONE && barbie.getStatus() == PassengerStatus.DONE && charlie
				.getStatus() == PassengerStatus.DONE)) {
			// wait until request is not completed
		}

		assertEquals("Request queue should be empty", 0, building.getPassengerRequestQueue().size());
		assertEquals("Only one elevator should be present", 1, building.getElevators().length);
		assertEquals("Elevator should be at destination floor", 5, building.getElevators()[0].getCurrentFloor());
		assertEquals("Elevator should traveled up one time only", 1, building.getElevators()[0].getUpTravelCount());
		assertEquals("Elevator should not traveled down", 0, building.getElevators()[0].getDownTravelCount());

		// Stop elevators
		building.stopElevators();
	}

	@Test
	public void testSingleElevatorWithMoreThanMaxPassengersTravelsToDestinationFloorOnlyOnce() throws Exception {
		int numElevators = 1;
		int numFloors = 5;
		int elevatorCapacity = 2;// max passengers
		// Create building
		Building building = createBuliding(numElevators, numFloors, elevatorCapacity, floorWaitTime, floorTravelTime);
		// Create passengers -
		Passenger andy = new Passenger("Andy", building, 0, 5);
		Passenger barbie = new Passenger("Barbie", building, 1, 3);
		Passenger charlie = new Passenger("Charlie", building, 3, 4);
		// Start passengers
		andy.request();
		barbie.request();
		charlie.request();
		// Start elevators
		building.startElevators();

		// Trip#1 - UP - Andy, Barbie, Charlie
		// Barbie get down at 3rd floor so no overlap with Charlie
		while (!(andy.getStatus() == PassengerStatus.DONE && barbie.getStatus() == PassengerStatus.DONE && charlie
				.getStatus() == PassengerStatus.DONE)) {
			// wait until request is not completed
		}

		assertEquals("Request queue should be empty", 0, building.getPassengerRequestQueue().size());
		assertEquals("Only one elevator should be present", 1, building.getElevators().length);
		assertEquals("Elevator should be at destination floor", 5, building.getElevators()[0].getCurrentFloor());
		assertEquals("Elevator should traveled up one time only", 1, building.getElevators()[0].getUpTravelCount());
		assertEquals("Elevator should not traveled down", 0, building.getElevators()[0].getDownTravelCount());

		// Stop elevators
		building.stopElevators();
	}

	@Test
	public void testSingleElevatorWithMoreThanMaxPassengersTravelsToDestinationFloorTwoTimes() throws Exception {
		int numElevators = 1;
		int numFloors = 5;
		int elevatorCapacity = 2;// max passengers
		// Create building
		Building building = createBuliding(numElevators, numFloors, elevatorCapacity, floorWaitTime, floorTravelTime);
		// Create passengers
		Passenger andy = new Passenger("Andy", building, 0, 5);
		Passenger barbie = new Passenger("Barbie", building, 1, 3);
		Passenger charlie = new Passenger("Charlie", building, 2, 4);
		// Start passengers
		andy.request();
		barbie.request();
		charlie.request();
		// Start elevators
		building.startElevators();

		// Trip#1 - UP - Andy, Barbie
		// Trip#2 - DOWN -
		// Trip#3 - UP - Charlie
		while (!(andy.getStatus() == PassengerStatus.DONE && barbie.getStatus() == PassengerStatus.DONE && charlie
				.getStatus() == PassengerStatus.DONE)) {
			// wait until request is not completed
		}

		assertEquals("Request queue should be empty", 0, building.getPassengerRequestQueue().size());
		assertEquals("Only one elevator should be present", 1, building.getElevators().length);
		assertEquals("Elevator should be at destination floor", 4, building.getElevators()[0].getCurrentFloor());
		assertEquals("Elevator should traveled up two times", 2, building.getElevators()[0].getUpTravelCount());
		assertEquals("Elevator should traveled down one time", 1, building.getElevators()[0].getDownTravelCount());

		// Stop elevators
		building.stopElevators();
	}

	@Test
	public void testSingleElevatorWithThreePassengersTravelsUpAndDownOneTimeEach() throws Exception {
		int numElevators = 1;
		int numFloors = 5;
		int elevatorCapacity = 2;// max passengers
		// Create building
		Building building = createBuliding(numElevators, numFloors, elevatorCapacity, floorWaitTime, floorTravelTime);
		// Create passengers
		Passenger andy = new Passenger("Andy", building, 0, 5);
		Passenger barbie = new Passenger("Barbie", building, 1, 3);
		Passenger charlie = new Passenger("Charlie", building, 4, 2);
		// Start passengers
		andy.request();
		barbie.request();
		charlie.request();
		// Start elevators
		building.startElevators();

		// Trip#1 - UP - Andy, Barbie
		// Trip#2 - DOWN - Charlie
		while (!(andy.getStatus() == PassengerStatus.DONE && barbie.getStatus() == PassengerStatus.DONE && charlie
				.getStatus() == PassengerStatus.DONE)) {
			// wait until request is not completed
		}

		assertEquals("Request queue should be empty", 0, building.getPassengerRequestQueue().size());
		assertEquals("Only one elevator should be present", 1, building.getElevators().length);
		assertEquals("Elevator should be at destination floor", 2, building.getElevators()[0].getCurrentFloor());
		assertEquals("Elevator should traveled up one time", 1, building.getElevators()[0].getUpTravelCount());
		assertEquals("Elevator should traveled down one time", 1, building.getElevators()[0].getDownTravelCount());

		// Stop elevators
		building.stopElevators();
	}

	@Test
	public void testSingleElevatorWithSixPassengersTravelsUpTwoTimesAndDownOneTime() throws Exception {
		int numElevators = 1;
		int numFloors = 5;
		int elevatorCapacity = 3;// max passengers
		// Create building
		Building building = createBuliding(numElevators, numFloors, elevatorCapacity, floorWaitTime, floorTravelTime);
		// Create passengers
		Passenger andy = new Passenger("Andy", building, 0, 5);
		Passenger barbie = new Passenger("Barbie", building, 1, 3);
		Passenger charlie = new Passenger("Charlie", building, 4, 2);
		Passenger dave = new Passenger("Dave", building, 3, 1);
		Passenger elie = new Passenger("Elie", building, 0, 3);
		Passenger fab = new Passenger("Fab", building, 2, 4);
		// Start passengers
		andy.request();
		barbie.request();
		charlie.request();
		dave.request();
		elie.request();
		fab.request();
		// Start elevators
		building.startElevators();

		// Trip#1 - UP - Andy, Barbie, Elie
		// Trip#2 - DOWN - Charlie, Dave
		// Trip#3 - UP - Fab
		while (!(andy.getStatus() == PassengerStatus.DONE && barbie.getStatus() == PassengerStatus.DONE
				&& charlie.getStatus() == PassengerStatus.DONE && dave.getStatus() == PassengerStatus.DONE
				&& elie.getStatus() == PassengerStatus.DONE && fab.getStatus() == PassengerStatus.DONE)) {
			// wait until request is not completed
		}

		assertEquals("Request queue should be empty", 0, building.getPassengerRequestQueue().size());
		assertEquals("Only one elevator should be present", 1, building.getElevators().length);
		assertEquals("Elevator should be at destination floor", 4, building.getElevators()[0].getCurrentFloor());
		assertEquals("Elevator should traveled up two times", 2, building.getElevators()[0].getUpTravelCount());
		assertEquals("Elevator should traveled down one time", 1, building.getElevators()[0].getDownTravelCount());

		// Stop elevators
		building.stopElevators();
	}

	@Test
	public void testSingleElevatorWithSixPassengersTravelsUpThreeTimesAndDownTwoTimes() throws Exception {
		int numElevators = 1;
		int numFloors = 5;
		int elevatorCapacity = 2;// max passengers
		// Create building
		Building building = createBuliding(numElevators, numFloors, elevatorCapacity, floorWaitTime, floorTravelTime);
		// Create passengers
		Passenger andy = new Passenger("Andy", building, 0, 5);
		Passenger barbie = new Passenger("Barbie", building, 1, 3);
		Passenger charlie = new Passenger("Charlie", building, 4, 2);
		Passenger dave = new Passenger("Dave", building, 3, 1);
		Passenger elie = new Passenger("Elie", building, 0, 3);
		Passenger fab = new Passenger("Fab", building, 2, 4);
		// Start passengers
		andy.request();
		barbie.request();
		charlie.request();
		dave.request();
		elie.request();
		fab.request();
		// Start elevators
		building.startElevators();

		// Trip#1 - UP - Andy, Elie
		// Trip#2 - DOWN -
		// Trip#3 - UP - Barbie
		// Trip#2 - DOWN - Charlie, Dave
		// Trip#3 - UP - Fab
		while (!(andy.getStatus() == PassengerStatus.DONE && barbie.getStatus() == PassengerStatus.DONE
				&& charlie.getStatus() == PassengerStatus.DONE && dave.getStatus() == PassengerStatus.DONE
				&& elie.getStatus() == PassengerStatus.DONE && fab.getStatus() == PassengerStatus.DONE)) {
			// wait until request is not completed
		}

		assertEquals("Request queue should be empty", 0, building.getPassengerRequestQueue().size());
		assertEquals("Only one elevator should be present", 1, building.getElevators().length);
		assertEquals("Elevator should be at destination floor", 4, building.getElevators()[0].getCurrentFloor());
		assertEquals("Elevator should traveled up three times", 3, building.getElevators()[0].getUpTravelCount());
		assertEquals("Elevator should traveled down two times", 2, building.getElevators()[0].getDownTravelCount());

		// Stop elevators
		building.stopElevators();
	}

}
