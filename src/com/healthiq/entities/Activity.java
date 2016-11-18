package com.healthiq.entities;

import com.healthiq.util.Type;

/**
 * 
 * Activity holds sugarValuePerMinute, quantity and type.
 * 
 * Example:
 * if an activity is "Squats" then [-1, 60, Exercise]
 * if an activity is "Sweet corn on the cob" then [0.5, 120, Food]
 * 
 * @author Bakhy
 *
 */
public class Activity {
	
	Float sugerPerMinute;
	int quantity;
	Type type;
	
	public Activity(Float sugerPerMinute, Type type) {
		this.sugerPerMinute = sugerPerMinute;
		this.quantity = type.getPeriod();
		this.type = type;
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
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Activity [sugerPerMinute=");
		builder.append(sugerPerMinute);
		builder.append(", quantity=");
		builder.append(quantity);
		builder.append(", type=");
		builder.append(type);
		builder.append("]");
		return builder.toString();
	}
}
