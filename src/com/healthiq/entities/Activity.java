package com.healthiq.entities;

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
