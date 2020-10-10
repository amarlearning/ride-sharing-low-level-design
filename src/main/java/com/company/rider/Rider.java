package com.company.rider;

/**
 * Rider Interface that needed to be implemented by every rider.
 */
public interface Rider {

	/**
	 * Method is used to create ride for passenger.
	 *
	 * @param id
	 * @param origin
	 * @param destination
	 * @param seats
	 */
	void createRide(int id, int origin, int destination, int seats);

	/**
	 * Method is used to update ride
	 *
	 * @param id
	 * @param origin
	 * @param destination
	 * @param seats
	 */
	void updateRide(int id, int origin, int destination, int seats);

	/**
	 * Method is used to withdraw a ride if it is not yet started.
	 *
	 * @param id
	 */
	void withdraw(int id);


	/**
	 * Method is used to mark the ride as complete and return the fare.
	 *
	 * @return fare.
	 */
	double closeRide();

}
