/**
 * 
 */
package com.sm.elevator;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sm.elevator.entity.Passenger;
import com.sm.elevator.enums.Direction;
import com.sm.elevator.enums.PassengerStatus;

/**
 * The Elevator class with features like capacity of elevator, number of floors
 * the elevator services, open close doors logic, onboard passengers, unload
 * passengers etc. It serves passenger requests and moves them UP or DOWN to
 * their destination floors. It waits at every floor 'floorWaitTime'
 * mili-seconds and takes 'floorTravelTime' mili-seconds to travel time between
 * two floors. It also maintains the count of times the elevator moved UP or
 * DOWN.
 * 
 * @author mahars
 * 
 */
public class Elevator extends Thread {

	private Log log = LogFactory.getLog(this.getClass());

	private final String name;// name of the elevator
	private final int capacity;// max peoples a elevator can carry at a time
	private final int floors;// max floors in the building

	private Set<Passenger> boardedPassengers; // Passengers inside the elevator

	private int currentFloor; // where is elevator now
	private long floorWaitTime;// max time elevator waits at each floor
	private long floorTravelTime; // how long it takes between floors
	private volatile boolean doorsOpened;// whether elevator running

	private volatile boolean isElevatorOperational;// whether elevator running
	private Direction direction; // Current direction - Going up or down
	private Direction lastDirection; // Last direction before wait

	private int upTravelCount;// count times elevator traveled up direction
	private int downTravelCount;// count times elevator traveled down direction

	public Elevator(String name, int capacity, int floors, long floorWaitTime, long floorTravelTime) {
		super();
		this.name = name;
		this.capacity = capacity;
		this.floors = floors;
		this.floorWaitTime = floorWaitTime;
		this.floorTravelTime = floorTravelTime;

		this.currentFloor = 0;
		this.direction = Direction.WAITING;// default
		this.isElevatorOperational = true;
		this.doorsOpened = false;

		this.boardedPassengers = new CopyOnWriteArraySet<Passenger>();
	}

	/**
	 * Check if elevator reached maximum capacity
	 * 
	 * @return
	 */
	public boolean isFull() {
		return boardedPassengers.size() >= capacity;
	}

	/**
	 * Return true if elevator is in waiting state
	 * 
	 * @return
	 */
	public synchronized boolean isFree() {
		return getDirection() == Direction.WAITING;
	}

	/**
	 * When elevator is free then passenger calls elevator to his current floor
	 * 
	 * @param floor
	 */
	public synchronized void callToFloor(int floor) {
		if (getDirection() == Direction.WAITING) {
			// Set direction
			direction = (floor > currentFloor) ? Direction.UP : Direction.DOWN;
			// Update floor
			currentFloor = floor;
			// Update travel count if elevator direction is changed
			if (lastDirection != direction) {
				if (lastDirection == Direction.DOWN) {
					// Elevator was previously going DOWN and now going UP so
					// increment travel up count
					upTravelCount++;
				} else if (lastDirection == Direction.UP) {
					// Elevator was previously going UP and now going DOWN so
					// increment travel down count
					downTravelCount++;
				}
			}
		}
	}

	/**
	 * Starts operation of elevator until it is operational. It picks up
	 * passengers and drop them to their destination floors.
	 */
	public void run() {
		log.info("Starting elevator " + name);

		while (isElevatorOperational) {
			// Process request only when elevator is not WAITING and it's doors
			// are closed
			if (getDirection() != Direction.WAITING && !doorsOpened) {

				// Notify passengers so that they can exit or board
				unloadPassengers();

				// Wait for passengers to board or exit
				try {
					sleep(floorWaitTime);
				} catch (InterruptedException e) {
					log.error("Exception during floor wait", e);
				}

				// Check and set direction of elevator - if elevator has reached
				// top
				// floor or the ground floor
				setElevatorDirection();

				if (boardedPassengers.size() == 0) {
					// No passenger to move - wait on same floor for some input
					lastDirection = direction;
					direction = Direction.WAITING;
					log.info("Elevator " + name + " is WAITING for passengers at floor " + currentFloor);
				} else {
					log.info("Elevator " + name + " leaving floor " + currentFloor + " with "
							+ boardedPassengers.size() + " passengers and is going " + direction);

					// Move elevator one floor at a time based on it's direction
					moveElevator();

					// Wait for elevator to arrive at next floor
					try {
						sleep(floorTravelTime);
					} catch (InterruptedException e) {
						log.error("Exception during floor travel", e);
					}

				}
			}
		}
	}

	/**
	 * Move elevator up or down based on direction
	 */
	private void moveElevator() {
		if (direction == Direction.UP) {
			currentFloor++;
		} else if (direction == Direction.DOWN) {
			currentFloor--;
		}
	}

	/**
	 * Check and set direction of elevator based on - if elevator has reached
	 * top floor or it has reached ground floor
	 */
	private void setElevatorDirection() {
		if (currentFloor >= floors) {
			direction = Direction.DOWN;
			downTravelCount++;
		} else if (currentFloor <= 0) {
			direction = Direction.UP;
			upTravelCount++;
		}
	}

	/**
	 * Passengers takes elevator via this method. If elevator is at user
	 * requested floor and there is space for new passenger then passenger
	 * boards the elevator and get down at destination floor. Else current floor
	 * is returned so that user can request again.
	 * 
	 * @param passenger
	 * @param currFloor
	 * @param destFloor
	 * @return
	 */
	public synchronized boolean takeElevator(Passenger passenger, int currFloor, int destFloor) {
		// Passenger was not able to board elevator so return false
		boolean result = false;
		openDoors();

		// Check if passenger can board the elevator
		if (currFloor == currentFloor && !isFull()) {
			boardedPassengers.add(passenger);
			passenger.setStatus(PassengerStatus.INSIDE_ELEVATOR);

			log.info("Passenger " + passenger + " taken elevator " + name + " at floor " + currentFloor
					+ " and will exit at floor " + destFloor);

			result = true;// Passenger boarded elevator successfully
		}
		closeDoors();
		return result;
	}

	/**
	 * Passenger boarded successfully. Start the elevator
	 */
	private void closeDoors() {
		doorsOpened = false;
		log.info("Elevator " + name + " doors closed at floor " + currentFloor);
	}

	/**
	 * Stop the elevator as passengers are boarding the elevator
	 */
	private void openDoors() {
		doorsOpened = true;
		log.info("Elevator " + name + " doors opened at floor " + currentFloor);
	}

	/**
	 * Unload passengers who've reached their destination
	 */
	public synchronized void unloadPassengers() {
		openDoors();
		for (Passenger passenger : boardedPassengers) {
			if (passenger.getDestinationFloor() == currentFloor) {
				// Remove passenger from boarded list
				passenger.setStatus(PassengerStatus.DONE);
				log.info("Passenger " + passenger + " reached destination floor " + currentFloor + " using elevator "
						+ this);
				boardedPassengers.remove(passenger);
			}
		}
		closeDoors();
	}

	@Override
	public String toString() {
		return name;
	}

	public void stopElevator() {
		this.isElevatorOperational = false;
	}

	public int getCurrentFloor() {
		return currentFloor;
	}

	public int getUpTravelCount() {
		return upTravelCount;
	}

	public int getDownTravelCount() {
		return downTravelCount;
	}

	public Direction getDirection() {
		return direction;
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
		Elevator other = (Elevator) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
