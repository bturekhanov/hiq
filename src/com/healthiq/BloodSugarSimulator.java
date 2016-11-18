package com.healthiq;

import java.util.HashMap;


/**
 * Public API of Blood Sugar Simulator
 * 
 * @author Bakhy
 *
 */
public interface BloodSugarSimulator {
	
	public void printOutputs();
	public void start();
	public void reset();
	public HashMap<Integer, Float> getResult();
}
