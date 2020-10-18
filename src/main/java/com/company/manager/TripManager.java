package com.company.manager;

import com.company.exception.DriverNotFoundException;
import com.company.exception.InvalidRideParamException;
import com.company.exception.TripNotFoundException;
import com.company.exception.TripStatusException;
import com.company.model.Driver;
import com.company.model.Rider;
import com.company.model.Trip;
import com.company.model.TripStatus;
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

	public String createTrip(final Rider rider, final int origin, final int destination, final int seats) {

		// Throw exception if origin is greater than destination
		if (origin >= destination)
			throw new InvalidRideParamException("Origin should always be greater than exception, try with valid request.");

		// Find a driver for this ride if not found throw exception.
		List<Driver> drivers = driverManager.getDrivers();
		Optional<Driver> matchedDriver = driverMatchingStrategy.findDriver(rider, drivers, origin, destination);

		if (!matchedDriver.isPresent())
			throw new DriverNotFoundException("Driver not found, Please try after some time");


		// Create a trip for rider if all's good.
		Driver driver = matchedDriver.get();

		double fare = isRiderPreferred(rider) ? pricingStrategy.calculateFareForPreferred(origin, destination, seats) :
				pricingStrategy.calculateFare(origin, destination, seats);

		Trip trip = new Trip(rider, driver, origin, destination, seats, fare);

		if (!trips.containsKey(rider.getId())) {
			trips.put(rider.getId(), new ArrayList<>());
		}

		trips.get(rider.getId()).add(trip);
		driver.setCurrentTrip(trip);

		return trip.getId();
	}

	public void updateTrip(final String tripId, final int origin, final int destination, final int seats) {

		// Throw exception if origin is greater than destination
		if (origin >= destination)
			throw new InvalidRideParamException("Origin should always be greater than exception, try with valid request.");

		Optional<Trip> optionalTrip = this.getTrip(tripId);

		if (!optionalTrip.isPresent())
			throw new TripNotFoundException("No Trip found for the given Id = " + tripId + ", please try with valid Trip Id.");

		Trip trip = optionalTrip.get();

		if (trip.getStatus().equals(TripStatus.COMPLETED) || trip.getStatus().equals(TripStatus.WITHDRAWN))
			throw new TripStatusException("Trip has already been completed or withdrawn try with valid Trip Id.");

		double fare = isRiderPreferred(trip.getRider()) ? pricingStrategy.calculateFareForPreferred(origin, destination, seats) :
				pricingStrategy.calculateFare(origin, destination, seats);

		trip.updateTrip(origin, destination, seats, fare);
	}

	public void withdrawTrip(final String tripId) {

		Optional<Trip> optionalTrip = this.getTrip(tripId);

		if (!optionalTrip.isPresent())
			throw new TripNotFoundException("No Trip found for the given Id = " + tripId + ", please try with valid Trip Id.");

		Trip trip = optionalTrip.get();

		if (trip.getStatus().equals(TripStatus.COMPLETED))
			throw new TripStatusException("Trip has already been completed, can't withdraw now.");

		Driver driver = trip.getDriver();
		driver.setCurrentTrip(null);
		trip.withdrawTrip();
	}

	public double endTrip(final Driver driver) {
		if (driver.getCurrentTrip() == null)
			throw new TripNotFoundException("Currently rider is not riding, please try again.");

		double fare = driver.getCurrentTrip().getFare();
		driver.getCurrentTrip().endTrip();
		driver.setCurrentTrip(null);

		return fare;
	}

	/**
	 * Method to get all trips done by a particular rider.
	 *
	 * @param rider
	 * @return
	 */
	public List<Trip> tripHistory(final Rider rider) {
		return trips.getOrDefault(rider.getId(), new ArrayList<>());
	}

	/**
	 * Helper method to get the respective driver for the given rider.
	 *
	 * @param tripId
	 * @return
	 */
	public Optional<Driver> getDriverForTrip(final String tripId) {
		Optional<Trip> trip = this.trips.values()
				.stream()
				.flatMap(list -> list.stream())
				.filter(t -> t.getId().equals(tripId))
				.findFirst();

		return Optional.of(trip.get().getDriver());
	}

	/**
	 * Helper Method to check if the given rider is preferred or not.
	 *
	 * @param rider
	 * @return
	 */
	private boolean isRiderPreferred(final Rider rider) {
		return tripHistory(rider).size() >= 10;
	}

	/**
	 * Helper method to get trip for the given Trip Id.
	 *
	 * @param tripId
	 * @return
	 */
	private Optional<Trip> getTrip(final String tripId) {
		return trips.values()
				.stream()
				.flatMap(list -> list.stream())
				.filter(t -> t.getId().equals(tripId))
				.findFirst();
	}
}
