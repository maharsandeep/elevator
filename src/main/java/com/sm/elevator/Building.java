/**
 * 
 */
package com.sm.elevator;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.sm.elevator.entity.PassengerRequest;

/**
 * The Building class. It has floors, the elevators, a queue of passenger
 * requests and a elevator controller which coordinates between elevators and
 * serves passenger requests in FIFO order.
 * 
 * @author mahars
 * 
 */
public class Building {
	public final int TOTAL_ELEVATORS;// number of elevators in the building
	public final int TOTAL_FLOORS; // total floors in the building

	private Elevator[] elevators; // Array of elevators within building
	private Queue<PassengerRequest> passengerRequestQueue; // holds requests
															// from passengers
	private ElevatorController elevatorController;// processes passenger
													// requests

	public Building(int elevators, int floors, int capacity, long floorWaitTime, long floorTravelTime) {
		TOTAL_ELEVATORS = elevators;
		TOTAL_FLOORS = floors;
		this.elevators = new Elevator[TOTAL_ELEVATORS];

		this.passengerRequestQueue = new ConcurrentLinkedQueue<PassengerRequest>();

		initElevators(capacity, floorWaitTime, floorTravelTime);

		elevatorController = new ElevatorController(this.elevators, passengerRequestQueue);
	}

	/**
	 * Initialize elevators of the building
	 * 
	 * @param capacity
	 * @param floorWaitTime
	 * @param floorTravelTime
	 */
	private void initElevators(int capacity, long floorWaitTime, long floorTravelTime) {
		for (int i = 0; i < TOTAL_ELEVATORS; i++) {
			elevators[i] = new Elevator("Lift#" + (i + 1), capacity, TOTAL_FLOORS, floorWaitTime, floorTravelTime);
		}
	}

	/**
	 * Start operations of all elevators
	 */
	public void startElevators() {
		for (Elevator elevator : elevators) {
			elevator.start();
		}
		elevatorController.start();
	}

	/**
	 * Stop operations of all elevators
	 */
	public void stopElevators() {
		while (!passengerRequestQueue.isEmpty()) {
			// wait until passenger requests are pending
		}
		for (Elevator elevator : elevators) {
			elevator.stopElevator();
		}
		elevatorController.setStopService(true);
	}

	/**
	 * Queue user request and serves in first come first serve basis
	 * 
	 * @param request
	 */
	public void requestForElevator(PassengerRequest request) {
		if (request.getDestinationFloor() >= 0 && request.getDestinationFloor() <= TOTAL_FLOORS) {
			passengerRequestQueue.add(request);
		}
	}

	public Elevator[] getElevators() {
		return elevators;
	}

	public Queue<PassengerRequest> getPassengerRequestQueue() {
		return passengerRequestQueue;
	}
}
