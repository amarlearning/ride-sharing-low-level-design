package com.company.strategy;

public class DefaultPricingStrategy implements PricingStrategy {

	@Override
	public double calculateFare(int origin, int destination, int seats) {
		return AMT_PER_KM * (destination - origin) * seats * (seats > 1 ? 0.75 : 1);
	}

	@Override
	public double calculateFareForPreferred(int origin, int destination, int seats) {
		return AMT_PER_KM * (destination - origin) * seats * (seats > 1 ? 0.5 : 0.75);
	}
}
