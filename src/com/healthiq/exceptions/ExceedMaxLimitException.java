package com.healthiq.exceptions;

import java.util.Date;

/**
 * An Exception, thrown by Simulator when a blood sugar exceeds MAX_LIMIT.
 * 
 * @author Bakhy
 *
 */
@SuppressWarnings("serial")
public class ExceedMaxLimitException extends RuntimeException{
	
	private String message;
	private Float bloodSugar;
	Date time;
	
	public ExceedMaxLimitException(String message, Float bloodSugar, Date time) {
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
