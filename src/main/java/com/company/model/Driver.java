package com.company.model;

public class Driver {

	private final String name;
	private int id;
	private Trip currentTrip;
	private boolean isAcceptingRider;

	public Driver(int id, String name) {
		this.id = id;
		this.name = name;
		this.isAcceptingRider = true;
	}

	/**
	 * Helper method to check whether driver can accept new riders or not.
	 *
	 * @return
	 */
	public boolean isAvailable() {
		return this.isAcceptingRider && this.currentTrip == null;
	}

	public int getId() {
		return id;
	}

	public Trip getCurrentTrip() {
		return currentTrip;
	}

	public void setCurrentTrip(Trip currentTrip) {
		this.currentTrip = currentTrip;
	}

	public void setAcceptingRider(boolean acceptingRider) {
		isAcceptingRider = acceptingRider;
	}
}
