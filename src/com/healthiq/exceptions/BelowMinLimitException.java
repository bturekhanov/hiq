package com.healthiq.exceptions;

import java.util.Date;

/**
 * An Exception, thrown by Simulator when a blood sugar is less than MIN_LIMIT.
 * 
 * @author Bakhy
 *
 */
@SuppressWarnings("serial")
public class BelowMinLimitException extends RuntimeException{
	
	private String message;
	private Float bloodSugar;
	Date time;
	
	public BelowMinLimitException(String message, Float bloodSugar, Date time) {
		this.message = message;
		this.bloodSugar = bloodSugar;
		this.time = time;
	}
	
	public String getMessage() {
		return message + " " + bloodSugar + " " + time;
	}
	
	public Float getBloodSugar() {
		return bloodSugar;
	}
}
