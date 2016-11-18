package com.healthiq.entities;

import java.util.Date;

/**
 * This is an input data.
 * 
 * Examples:
 * 	[07:00, "EXERCISE", 1]
 *  [07:31, "FOOD", 1]
 *  
 * @author Bakhy
 *
 */
public class Entry {

	public Date time;
	public String type;
	public Integer id;
	
	public Entry(Date time, String type, Integer id) {
		this.time = time;
		this.type = type;
		this.id = id;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	/**
	 * @return minutes diff between BeginningOfDay and Entry time
	 */
	public Integer getTime(Date beginingOfDay) {
		if( beginingOfDay == null || time == null ) return 0;
	    return (int)((time.getTime()/60000) - (beginingOfDay.getTime()/60000));
	}
	
	public Date getTimestamp() {
		return time;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Entry [time=");
		builder.append(time);
		builder.append(", type=");
		builder.append(type);
		builder.append(", id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}
	
}
