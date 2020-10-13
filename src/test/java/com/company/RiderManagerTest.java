package com.company;

import com.company.exception.RiderAlreadyPresentException;
import com.company.exception.RiderNotFoundException;
import com.company.manager.RiderManager;
import com.company.model.Rider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RiderManagerTest {

	RiderManager riderManager;

	@BeforeEach
	void setup() {
		riderManager = new RiderManager();
	}

	@Test
	void test_createRiderAndGetRider() {
		// Given.
		riderManager.createRider(new Rider(1, "Amar"));
		riderManager.createRider(new Rider(2, "Shubham"));

		// When.
		Rider rider1 = riderManager.getRider(1);
		Rider rider2 = riderManager.getRider(2);

		// Then.
		Assertions.assertEquals("Amar", rider1.getName());
		Assertions.assertEquals("Shubham", rider2.getName());

		// Then.
		Assertions.assertThrows(RiderNotFoundException.class, () -> {
			// When.
			riderManager.getRider(4);
		});
	}

	@Test
	void test_createRiderWithDuplicateIdMethod() {
		// Given.
		Rider rider2 = new Rider(2, "Prateek");
		riderManager.createRider(new Rider(1, "Amar"));
		riderManager.createRider(new Rider(2, "Shubham"));

		// Then.
		Assertions.assertThrows(RiderAlreadyPresentException.class, () -> {
			// When.
			riderManager.createRider(rider2);
		});
	}

}
