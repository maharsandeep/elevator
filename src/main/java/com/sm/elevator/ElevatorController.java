/**
 * 
 */
package com.sm.elevator;

import java.util.Queue;

import com.sm.elevator.entity.PassengerRequest;
import com.sm.elevator.enums.Direction;

/**
 * The ElevatorController class to control operations of elevators within a
 * building. It serves passenger requests on first come first serve basis so
 * that no passenger is waiting indefinitely. Each building has one
 * ElevatorController.
 * 
 * @author mahars
 * 
 */
public class ElevatorController extends Thread {
	private volatile boolean stopService;

	private Elevator[] elevators;
	private Queue<PassengerRequest> passengerRequestQueue;

	public ElevatorController(Elevator[] elevators, Queue<PassengerRequest> passengerRequestQueue) {
		super();
		this.elevators = elevators;
		this.passengerRequestQueue = passengerRequestQueue;
		stopService = false;
	}

	/**
	 * Serves passenger requests on first come first serve basis so that no
	 * passenger is waiting indefinitely
	 */
	public void run() {
		while (!stopService) {
			if (!passengerRequestQueue.isEmpty()) {
				PassengerRequest request = passengerRequestQueue.peek();
				if (request != null) {
					// wait for an elevator
					Elevator elevator = getElevatorForPassenger(request.getCurrentFloor(), request.getDirection());
					if (elevator != null) {
						// Found one elevator - Get passenger in now
						boardPassengerToElevator(elevator, request);
						// Check if there are other requests that lie in the
						// path of its intended destination
						for (PassengerRequest req : passengerRequestQueue) {
							if (req.getDirection() == request.getDirection()
									&& request.getCurrentFloor() == req.getCurrentFloor()) {
								boardPassengerToElevator(elevator, req);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Boards a passenger to Elevator. Boarding might be unsuccessful if
	 * elevator already reached maximum passenger capacity
	 * 
	 * @param elevator
	 * @param request
	 */
	private void boardPassengerToElevator(Elevator elevator, PassengerRequest request) {
		boolean elevatorTaken = elevator.takeElevator(request.getPassenger(), request.getCurrentFloor(),
				request.getDestinationFloor());
		if (elevatorTaken) {
			// Set which elevator is serving user request
			request.setElevator(elevator);
			// Remove request from the queue
			passengerRequestQueue.remove(request);
		}
	}

	/**
	 * Returns the first elevator to reach the current floor and going in right
	 * direction. Wait if necessary for one to arrive
	 * 
	 * @return
	 */
	public synchronized Elevator getElevatorForPassenger(int currFloor, Direction direction) {
		for (Elevator elevator : elevators) {
			// Check if elevator is free - get this to serve request
			if (elevator.isFree()) {
				elevator.callToFloor(currFloor);
				return elevator;
			}
			// Get the nearest match elevator based on direction and the floor
			else if (!elevator.isFull() && elevator.getDirection() == direction) {
				if ((direction == Direction.UP && elevator.getCurrentFloor() <= currFloor)
						|| (direction == Direction.DOWN && elevator.getCurrentFloor() >= currFloor)) {
					return elevator;
				}
			}
		}
		// No free elevator - try again
		return null;
	}

	/**
	 * Stop service by setting this flag
	 * 
	 * @param stopService
	 */
	public void setStopService(boolean stopService) {
		this.stopService = stopService;
	}
}
