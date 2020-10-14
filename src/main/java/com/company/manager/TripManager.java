package com.company.manager;

import com.company.exception.DriverNotFoundException;
import com.company.exception.InvalidRideParamException;
import com.company.exception.TripNotFoundException;
import com.company.model.Driver;
import com.company.model.Rider;
import com.company.model.Trip;
import com.company.strategy.DriverMatchingStrategy;
import com.company.strategy.PricingStrategy;

import java.util.*;

/**
 * TripManager class is used to manage all riders and drivers.
 */
public class TripManager {

	private RiderManager riderManager;
	private DriverManager driverManager;
	private PricingStrategy pricingStrategy;
	private DriverMatchingStrategy driverMatchingStrategy;

	//	Mapping of Rider with it's associated trips.
	private Map<Integer, List<Trip>> trips = new HashMap<>();

	public TripManager(RiderManager riderManager, DriverManager driverManager, PricingStrategy pricingStrategy, DriverMatchingStrategy driverMatchingStrategy) {
		this.riderManager = riderManager;
		this.driverManager = driverManager;
		this.pricingStrategy = pricingStrategy;
		this.driverMatchingStrategy = driverMatchingStrategy;
	}

	public void createTrip(Rider rider, int origin, int destination, int seats) {

		if (origin >= destination)
			throw new InvalidRideParamException("Origin should always be greater than exception, try with valid request.");

		List<Driver> drivers = driverManager.getDrivers();

		Optional<Driver> matchedDriver = driverMatchingStrategy.findDriver(rider, drivers, origin, destination);

		if (!matchedDriver.isPresent())
			throw new DriverNotFoundException("Driver not found, Please try after some time");

		double fare = isRiderPreferred(rider) ? pricingStrategy.calculateFareForPreferred(origin, destination, seats) :
				pricingStrategy.calculateFare(origin, destination, seats);

		Trip trip = new Trip(rider, matchedDriver.get(), origin, destination, seats, fare);

		if (!trips.containsKey(rider.getId())) {
			trips.put(rider.getId(), new ArrayList<>());
		}

		trips.get(rider.getId()).add(trip);
		matchedDriver.get().setCurrentTrip(trip);
	}

	public List<Trip> tripHistory(final Rider rider) {
		return trips.getOrDefault(rider.getId(), new ArrayList<>());
	}

	public double endTrip(final Driver driver) {
		double fare = 0.0;
		if (driver.getCurrentTrip() == null)
			throw new TripNotFoundException("Currently rider is not riding, please try again.");

		driver.getCurrentTrip().endTrip();
		fare = driver.getCurrentTrip().getFare();
		driver.setCurrentTrip(null);

		return fare;
	}

	public Driver getDriverForCurrentTripRider(final Rider rider) {
		Optional<Trip> trip = this.tripHistory(rider)
				.stream()
				.reduce((f, s) -> s);

		return trip.isPresent() ? trip.get().getDriver() : null;
	}

	private boolean isRiderPreferred(final Rider rider) {
		return tripHistory(rider).size() >= 10;
	}
}
