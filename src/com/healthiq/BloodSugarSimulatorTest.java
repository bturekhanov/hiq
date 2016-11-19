package com.healthiq;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.healthiq.entities.Entry;

/**
 * 
 * Tester of Blood Sugar Simulator.
 * 
 * @author Bakhy
 *
 */
public class BloodSugarSimulatorTest { 
	
	private static BloodSugarSimulator simulator;
	
	private static final int MINUTES_IN_DAY = 1440; // 24 * 60
	private static final Float BLOOD_SUGAR_START = 80F;
	
	@BeforeClass 
	public static void setUp(){ 
		@SuppressWarnings("deprecation")
		Date beginOfDay = new Date(2016, Calendar.NOVEMBER, 17, 6, 0); // 06:00 AM
		List<Entry> entries = new ArrayList<Entry>();
		simulator = BloodSugarSimulatorFactory.createBloodSugarSimulator(beginOfDay, entries);
		simulator.start();
	} 
	
	@AfterClass 
	public static void tearDown(){ 
		simulator = null; 
	} 
	
	@Test 
	public void testNoFoodNoExercises() { 
		
		/* If no food and no exercise, then blood sugar should be 80 for all 24 hours and glycation should be 0. */
		HashMap<Integer, Float> expectedBloodSugarMap = new HashMap<Integer, Float>();
		HashMap<Integer, Integer> expectedGlycationMap = new HashMap<Integer, Integer>();
		
		for(int i=0; i<MINUTES_IN_DAY; i++) {
			expectedBloodSugarMap.put(i, BLOOD_SUGAR_START);
			expectedGlycationMap.put(i, 0);
		}
		
		HashMap<Integer, Float> actualBloodSugarMap = simulator.getBloodSugarPerMinuteMap();
		HashMap<Integer, Integer> actualGlycationMap = simulator.getGlycationsPerMinuteMap();

		assertEquals(expectedBloodSugarMap, actualBloodSugarMap);
		assertEquals(expectedGlycationMap, actualGlycationMap);
	} 
}


