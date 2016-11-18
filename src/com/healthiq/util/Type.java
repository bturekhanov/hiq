package com.healthiq.util;

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
