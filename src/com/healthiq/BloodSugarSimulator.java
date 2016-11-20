package com.healthiq;

import java.util.HashMap;
import java.util.List;

import com.healthiq.entities.Entry;


/**
 * Public API of Blood Sugar Simulator
 * 
 * @author Bakhy
 *
 */
public interface BloodSugarSimulator {
	
	public void printBloodSugarPerMinuteMap();
	public void printGlycationsPerMinuteMap();
	public void start();
	public void reset();
	public HashMap<Integer, Float> getBloodSugarPerMinuteMap();
	public HashMap<Integer, Integer> getGlycationsPerMinuteMap();
	public void setEntries(List<Entry> entries);
}
