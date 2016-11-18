package com.healthiq.entities;

/**
 * 
 * Activity is a pair of sugarValuePerMinute and quantity.
 * 
 * Example:
 * if an activity is "Squats" then [-1, 60]
 * if an activity is "Sweet corn on the cob" then [0.5, 120]
 * 
 * @author Bakhy
 *
 */
public class Activity {
	
	Float sugerPerMinute;
	int quantity;
	
	public Activity(Float sugerPerMinute, int quantity) {
		this.sugerPerMinute = sugerPerMinute;
		this.quantity = quantity;
	}
	public Float getSugerPerMinute() {
		return sugerPerMinute;
	}
	public void setSugerPerMinute(Float sugerPerMinute) {
		this.sugerPerMinute = sugerPerMinute;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
