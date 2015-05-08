/**
 * 
 */
package com.sm.elevator.entity;

import com.sm.elevator.Elevator;
import com.sm.elevator.enums.Direction;

/**
 * Holds request of a passenger for an elevator within a building
 * 
 * @author mahars
 * 
 */
public class PassengerRequest {

	private Passenger passenger; // passenger
	private int currentFloor;
	private int destinationFloor;
	private Direction direction; // Going up or down
	private Elevator elevator;// which elevator has serviced the request

	public PassengerRequest(Passenger passenger) {
		super();
		this.passenger = passenger;
		this.currentFloor = passenger.getCurrentFloor();
		this.destinationFloor = passenger.getDestinationFloor();
		this.direction = passenger.getDirection();
	}

	public Passenger getPassenger() {
		return passenger;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	public int getDestinationFloor() {
		return destinationFloor;
	}

	public Direction getDirection() {
		return direction;
	}

	public Elevator getElevator() {
		return elevator;
	}

	public void setElevator(Elevator elevator) {
		this.elevator = elevator;
	}

}
