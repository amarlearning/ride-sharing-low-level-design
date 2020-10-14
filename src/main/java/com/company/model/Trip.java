package com.company.model;

import java.util.UUID;

public class Trip {

	private String id;
	private Rider rider;
	private Driver driver;
	private int origin;
	private int destination;
	private int seats;
	private double fare;

	private TripStatus status;

	public Trip(final Rider rider, final Driver driver, final int origin, final int destination, final int seats, final double fare) {
		this.id = UUID.randomUUID().toString();
		this.rider = rider;
		this.driver = driver;
		this.origin = origin;
		this.destination = destination;
		this.seats = seats;
		this.fare = fare;

		this.status = TripStatus.IN_PROGRESS;
	}

	public void updateTrip(final int origin, final int destination, final int seats, final double fare) {
		this.origin = origin;
		this.destination = destination;
		this.seats = seats;
		this.fare = fare;
	}

	public void endTrip() {
		this.status = TripStatus.COMPLETED;
	}

	public void withdrawTrip() {
		this.status = TripStatus.WITHDRAWN;
	}

	public String getId() {
		return id;
	}

	public Rider getRider() {
		return rider;
	}

	public Driver getDriver() {
		return driver;
	}

	public double getFare() {
		return fare;
	}

	public TripStatus getStatus() {
		return status;
	}
}
