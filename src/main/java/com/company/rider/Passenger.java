package com.company.rider;

import com.company.exception.DuplicateRideIDException;
import com.company.exception.InvalidRideParamException;
import com.company.exception.RideNotFoundException;
import com.company.exception.RideStatusException;
import com.company.ride.Ride;
import com.company.ride.RideStatus;

import java.util.HashMap;
import java.util.Map;

public class Passenger implements Rider {

	private String name;
	private Map<Integer, Ride> rides;

	public Passenger(String name) {
		this.name = name;
		this.rides = new HashMap<>();
	}

	public void createRide(int id, int origin, int destination, int seats) {

		if (origin >= destination)
			throw new InvalidRideParamException("Origin cannot be greater than destination, please try again.");

		if (this.rides.containsKey(id))
			throw new DuplicateRideIDException("Ride with Id = " + id + " already present, try with unique Id.");

		this.rides.put(id, new Ride.RideBuilder(id)
				.setOrigin(origin)
				.setDestination(destination)
				.setSeats(seats)
				.setRideStatus(RideStatus.CREATED)
				.build());
	}

	public void updateRide(int id, int origin, int destination, int seats) {

		Ride ride = this.rides.getOrDefault(id, null);

		if (ride != null && (ride.getRideStatus() == RideStatus.WITHDRAWN || ride.getRideStatus() == RideStatus.COMPLETED))
			throw new RideStatusException("Ride has already been " + ride.getRideStatus().toString().toLowerCase() + ", can't update now.");


		this.rides.put(id, new Ride.RideBuilder(id)
				.setOrigin(origin)
				.setDestination(destination)
				.setSeats(seats)
				.setRideStatus(RideStatus.CREATED)
				.build());
	}

	public void withdraw(int id) {

		if (!this.rides.containsKey(id))
			throw new RideNotFoundException("Incorrect Id passed, cannot withdraw ride.");

		if (this.rides.get(id).getRideStatus() != RideStatus.CREATED)
			throw new RideStatusException("Ride wasn't in progress, can't withdraw ride");

		this.rides.get(id).setRideStatus(RideStatus.WITHDRAWN);
	}

	public double closeRide(int id) {

		if (this.rides.get(id).getRideStatus() != RideStatus.CREATED) {
			throw new RideStatusException("Ride wasn't in progress, can't be completed.");
		}

		this.rides.get(id).setRideStatus(RideStatus.COMPLETED);

		return this.rides.get(id).calculateFare(isPreferred());
	}

	private boolean isPreferred() {
		return this.rides
				.values()
				.stream()
				.filter(r -> r.getRideStatus().equals(RideStatus.COMPLETED))
				.count() >= 10;
	}
}
