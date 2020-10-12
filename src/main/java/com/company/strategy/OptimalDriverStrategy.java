package com.company.strategy;

import com.company.model.Driver;
import com.company.model.Rider;

import java.util.List;
import java.util.Optional;

public class OptimalDriverStrategy implements DriverMatchingStrategy {

	@Override
	public Optional<Driver> findDriver(Rider rider, List<Driver> nearByDrivers, int origin, int destination) {
		return nearByDrivers.stream().findAny();
	}
}
