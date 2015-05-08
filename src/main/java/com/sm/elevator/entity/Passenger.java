/**
 * 
 */
package com.sm.elevator.entity;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sm.elevator.Building;
import com.sm.elevator.enums.Direction;
import com.sm.elevator.enums.PassengerStatus;

/**
 * Passenger class (Assumption - Passenger name is unique that is why name is
 * used in toString and hashcode methods)
 * 
 * @author mahars
 * 
 */
public class Passenger {
	private Log log = LogFactory.getLog(this.getClass());

	private final String name; // passenger name
	private Building building;
	private int currentFloor;
	private int destinationFloor;
	private PassengerStatus status;
	private List<PassengerRequest> requests;

	public Passenger(String name, Building building, int currentFloor, int destinationFloor) {
		super();
		this.name = name;
		this.building = building;
		this.currentFloor = currentFloor;
		this.destinationFloor = destinationFloor;
		this.status = PassengerStatus.WAITING;
		
		this.requests = new ArrayList<PassengerRequest>();
	}

	public void request() {
		log.info("Passenger " + name + " is at floor " + currentFloor + " and is going to floor " + destinationFloor);

		PassengerRequest request = new PassengerRequest(this);
		building.requestForElevator(request);
		
		requests.add(request);
	}

	public void setStatus(PassengerStatus status) {
		this.status = status;
	}

	public PassengerStatus getStatus() {
		return status;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	public int getDestinationFloor() {
		return destinationFloor;
	}

	public Direction getDirection() {
		return (destinationFloor > currentFloor) ? Direction.UP : Direction.DOWN;
	}

	public List<PassengerRequest> getRequests() {
		return requests;
	}

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Passenger other = (Passenger) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
