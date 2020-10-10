package com.company;

import com.company.driver.Driver;
import com.company.exception.InvalidRideParamException;
import com.company.exception.RideNotFoundException;
import com.company.exception.RideStatusException;
import com.company.rider.Passenger;
import com.company.rider.Rider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

class RiderTest {

	Driver d1, d2;
	Rider p1, p2, p3;

	@BeforeEach
	public void setup() {
		d1 = new Driver("Salman");
		d2 = new Driver("Ajay");

		p1 = new Passenger("Amar");
		p2 = new Passenger("Shubham");
		p3 = new Passenger("Prateek");
	}

	@Test
	void test_exceptionScenariosInCreateRideRide() {

		// Then.
		Assertions.assertThrows(InvalidRideParamException.class, () -> {
			// When.
			p1.createRide(1, 70, 60, 1);
		});
	}

	@Test
	void test_exceptionScenariosInUpdateRide() {

		// Given.
		p1.createRide(1, 10, 30, 2);
		p1.closeRide();

		// Then.
		Assertions.assertThrows(RideStatusException.class, () -> {
			// When.
			p1.updateRide(1, 10, 40, 1);
		});

		// Given.
		p2.createRide(1, 50, 100, 2);
		p2.withdraw(1);

		// Then.
		Assertions.assertThrows(RideStatusException.class, () -> {
			// When.
			p2.updateRide(1, 50, 70, 2);
		});
	}

	@Test
	void test_exceptionScenariosInWithdrawRide() {

		// Given.
		p1.createRide(1, 50, 60, 1);

		// Then.
		Assertions.assertThrows(RideNotFoundException.class, () -> {
			// When.
			p1.withdraw(2);
		});

		// Given.
		p1.createRide(1, 50, 60, 1);

		// When.
		p1.closeRide();

		// Then.
		Assertions.assertThrows(RideStatusException.class, () -> {
			p1.withdraw(1);
		});
	}

	@Test
	void test_exceptionScenariosInCloseRide() {

		// Given.
		p1.createRide(1, 50, 60, 1);

		// When.
		p1.withdraw(1);

		// Then.
		Assertions.assertThrows(RideStatusException.class, () -> {
			p1.closeRide();
		});
	}

	@Test
	void test_normalFlowToRide() {
		// When.
		p1.createRide(1, 20, 30, 2);

		// Then.
		Assertions.assertEquals(300, p1.closeRide());

		p2.createRide(2, 5, 15, 1);
		p2.updateRide(2, 10, 40, 2);

		Assertions.assertEquals(900, p2.closeRide());
	}

	@Test
	void test_priorityRiderCalculateFare() {

		// Given.
		IntStream.range(1, 12).forEach(i -> {
			p1.createRide(i, i, i * 10, 1);
			p1.closeRide();
		});

		// When. Now after 10 rides, p1 is a priority rider.
		p1.createRide(11, 200, 300, 1);

		// Then.
		Assertions.assertEquals(1500, p1.closeRide());

		// When. Now after 10 rides, p1 is a priority rider.
		p1.createRide(12, 50, 60, 2);

		// Then.
		Assertions.assertEquals(200, p1.closeRide());
	}
}
