package com.healthiq.util;

/**
 * Two type of input data.
 * 
 * @author Bakhy
 *
 */
public enum Type {

	FOOD(120), EXERCISE(60);
	
	private int period; // in minutes

	private Type(int period) {
		this.period = period;
	}

	public int getPeriod() {
		return period;
	}
}
