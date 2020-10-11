package com.company.ride;

/**
 * Ride class is used to store all ride details.
 */
public class Ride {

	private static final int AMT_PER_KM = 20;

	private int id;
	private int seats;
	private int origin;
	private int destination;
	private RideStatus rideStatus;

	public Ride(RideBuilder rideBuilder) {
		this.id = rideBuilder.id;
		this.origin = rideBuilder.origin;
		this.destination = rideBuilder.destination;
		this.seats = rideBuilder.seats;
		this.rideStatus = rideBuilder.rideStatus;
	}

	public RideStatus getRideStatus() {
		return rideStatus;
	}

	public void setRideStatus(RideStatus rideStatus) {
		this.rideStatus = rideStatus;
	}

	public double calculateFare(boolean isPreferred) {

		double fare = AMT_PER_KM * seats * (double) (destination - origin);

		if (this.seats <= 1) {
			fare = fare * (isPreferred ? 0.75 : 1);
		} else {
			fare = fare * (isPreferred ? 0.50 : 0.75);
		}

		return fare;
	}

	public static class RideBuilder {

		private final int id;
		private int seats;
		private int origin;
		private int destination;
		private RideStatus rideStatus;

		public RideBuilder(int id) {
			this.id = id;
		}

		public RideBuilder setOrigin(int origin) {
			this.origin = origin;
			return this;
		}

		public RideBuilder setDestination(int destination) {
			this.destination = destination;
			return this;
		}

		public RideBuilder setSeats(int seats) {
			this.seats = seats;
			return this;
		}

		public RideBuilder setRideStatus(RideStatus rideStatus) {
			this.rideStatus = rideStatus;
			return this;
		}

		public Ride build() {
			return new Ride(this);
		}
	}

}
