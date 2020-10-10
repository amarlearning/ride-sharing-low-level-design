package com.company.rider;

import com.company.exception.InvalidRideParamException;
import com.company.exception.RideNotFoundException;
import com.company.exception.RideStatusException;
import com.company.ride.Ride;
import com.company.ride.RideStatus;

import java.util.ArrayList;
import java.util.List;

public class Passenger implements Rider {

	private Ride ride;
	private String name;
	private List<Ride> completedRides;

	public Passenger(String name) {
		this.name = name;
		this.completedRides = new ArrayList<>();
	}

	public void createRide(int id, int origin, int destination, int seats) {

		if (origin >= destination)
			throw new InvalidRideParamException("Origin cannot be greater than destination, please try again.");

		this.ride = new Ride.RideBuilder(id)
				.setOrigin(origin)
				.setDestination(destination)
				.setSeats(seats)
				.setRideStatus(RideStatus.CREATED)
				.build();
	}

	public void updateRide(int id, int origin, int destination, int seats) {

		if (this.ride.getRideStatus() == RideStatus.WITHDRAWN || this.ride.getRideStatus() == RideStatus.COMPLETED)
			throw new RideStatusException("Ride has already been " + this.ride.getRideStatus().toString().toLowerCase() + ", can't update now.");

		createRide(id, origin, destination, seats);
	}

	public void withdraw(int id) {

		if (this.ride.getId() != id)
			throw new RideNotFoundException("Incorrect Id passed, cannot withdraw ride.");


		if (this.ride.getRideStatus() != RideStatus.CREATED)
			throw new RideStatusException("Ride wasn't in progress, can't withdraw ride");

		this.ride.setRideStatus(RideStatus.WITHDRAWN);
	}

	public double closeRide() {

		if (this.ride.getRideStatus() != RideStatus.CREATED) {
			throw new RideStatusException("Ride wasn't in progress, can't be completed.");
		}

		this.ride.setRideStatus(RideStatus.COMPLETED);
		this.completedRides.add(ride);

		return ride.calculateFare(isPreferred());
	}

	private boolean isPreferred() {
		return this.completedRides.size() >= 10;
	}
}
