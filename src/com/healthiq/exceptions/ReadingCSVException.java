package com.healthiq.exceptions;

/**
 * An Exception, thrown by Simulator when a reading CSV files.
 * 
 * @author Bakhy
 *
 */
@SuppressWarnings("serial")
public class ReadingCSVException extends RuntimeException{

	private String message;
	private String filePath;
	
	public ReadingCSVException(String message, String filePath) {
		this.message = message;
		this.filePath = filePath;
	}

	public String getMessage() {
		return message + " " + filePath;
	}
	
	public String getFilePath() {
		return filePath;
	}
}
