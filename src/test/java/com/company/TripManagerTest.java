package com.company;

import com.company.exception.DriverNotFoundException;
import com.company.exception.InvalidRideParamException;
import com.company.exception.TripNotFoundException;
import com.company.manager.DriverManager;
import com.company.manager.RiderManager;
import com.company.manager.TripManager;
import com.company.model.Driver;
import com.company.model.Rider;
import com.company.strategy.DefaultPricingStrategy;
import com.company.strategy.OptimalDriverStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

class TripManagerTest {

	TripManager tripManager;
	Driver driver1, driver2;
	Rider rider1, rider2, rider3;

	@BeforeEach
	void setup() {

		driver1 = new Driver(1, "Driver1");
		driver2 = new Driver(2, "Driver2");

		DriverManager driverManager = new DriverManager();
		driverManager.createDriver(driver1);
		driverManager.createDriver(driver2);

		rider1 = new Rider(1, "Rider1");
		rider2 = new Rider(2, "Rider2");
		rider3 = new Rider(3, "Rider3");

		RiderManager riderManager = new RiderManager();
		riderManager.createRider(rider1);
		riderManager.createRider(rider2);
		riderManager.createRider(rider3);

		tripManager = new TripManager(riderManager,
				driverManager,
				new DefaultPricingStrategy(),
				new OptimalDriverStrategy()
		);
	}

	@Test
	void test_createRideMethod() {

		// Given.
		// Driver1 booked by rider1, one driver left now.
		tripManager.createTrip(rider1, 50, 60, 1);

		// Driver2 booked by rider2, zero driver left now.
		tripManager.createTrip(rider2, 60, 70, 2);

		// Then.
		Assertions.assertThrows(DriverNotFoundException.class, () -> {
			// When.
			tripManager.createTrip(rider3, 80, 100, 2);
		});

		Assertions.assertThrows(InvalidRideParamException.class, () -> {
			tripManager.createTrip(rider3, 50, 40, 1);
		});

	}

	@Test
	void test_endRideAndAddAcceptNewRiderRequest() {

		// Given.
		tripManager.createTrip(rider1, 20, 60, 2);
		tripManager.createTrip(rider2, 40, 70, 2);

		// End the trip of rider2 and book the ride of rider3.
		Driver driverForRider2 = tripManager.getDriverForCurrentTripRider(rider2);

		// When.
		Assertions.assertEquals(900, tripManager.endTrip(driverForRider2));

		// Then.
		tripManager.createTrip(rider3, 80, 100, 2);
	}

	@Test
	void test_getFareForDriverWhenNotRiding() {
		// Then.
		Assertions.assertThrows(TripNotFoundException.class, () -> {
			// When.
			tripManager.endTrip(driver1);
		});
	}

	@Test
	void test_preferredRiderFareCalculation() {

		// When.
		IntStream.range(1, 12).forEach(i -> {
			tripManager.createTrip(rider1, i * 10, (i + 1) * 10, 1);
			Driver driverForRider1 = tripManager.getDriverForCurrentTripRider(rider1);
			tripManager.endTrip(driverForRider1);
		});

		// Then.
		Assertions.assertTrue(tripManager.tripHistory(rider1).size() == 11);

		tripManager.createTrip(rider1, 10, 20, 2);
		Driver driverForRider1 = tripManager.getDriverForCurrentTripRider(rider1);

		Assertions.assertEquals(200, tripManager.endTrip(driverForRider1));
	}

}
