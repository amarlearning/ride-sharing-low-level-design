package com.company;

import com.company.exception.DriverAlreadyPresentException;
import com.company.exception.DriverNotFoundException;
import com.company.manager.DriverManager;
import com.company.model.Driver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DriverManagerTest {

	DriverManager driverManager;

	@BeforeEach
	void setup() {
		driverManager = new DriverManager();
	}

	@Test
	void test_createDriverAndGetDrivers() {
		// Given.
		Driver dummyDriver = new Driver(2, "Shubham");
		driverManager.createDriver(new Driver(1, "Amar"));
		driverManager.createDriver(new Driver(2, "Prateek"));
		driverManager.createDriver(new Driver(3, "Rajat"));

		// Then.
		Assertions.assertThrows(DriverAlreadyPresentException.class, () -> {
			// When.
			driverManager.createDriver(dummyDriver);
		});

		// Then.
		Assertions.assertEquals(3, driverManager.getDrivers().size());
	}

	@Test
	void test_updateDriverAvailability() {

		// Given.
		driverManager.createDriver(new Driver(1, "Amar"));
		driverManager.createDriver(new Driver(2, "Prateek"));
		driverManager.createDriver(new Driver(3, "Rajat"));

		Assertions.assertEquals(3, driverManager.getDrivers().size());

		// When.
		driverManager.updateDriverAvailability(3, false);

		// Then.
		Assertions.assertEquals(2, driverManager.getDrivers().size());

		// Then.
		Assertions.assertThrows(DriverNotFoundException.class, () -> {
			// When.
			driverManager.updateDriverAvailability(10, false);
		});
	}
}
