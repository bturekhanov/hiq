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
import com.healthiq.util.Type;

/**
 * 
 * Tester of Blood Sugar Simulator.
 * 
 * @author Bakhy
 *
 */
public class BloodSugarSimulatorTest { 
	
	@SuppressWarnings("deprecation")
	private static Date beginOfDay = new Date(2016, Calendar.NOVEMBER, 17, 6, 0); // 06:00 AM
	private static BloodSugarSimulator simulator;
	
	private static final int MINUTES_IN_DAY = 1440; // 24 * 60
	private static final Float BLOOD_SUGAR_START = 80F;
	
	@BeforeClass 
	public static void setUp(){ 
		
		List<Entry> entries = new ArrayList<Entry>();
		simulator = BloodSugarSimulatorFactory.createBloodSugarSimulator(beginOfDay, entries);
	} 
	
	@AfterClass 
	public static void tearDown(){ 
		simulator = null; 
	} 
	
	/**
	 * If no food and no exercise, then blood sugar should be 80 for all 24 hours and glycation should be 0.
	 */
	@Test 
	public void testNoFoodNoExercises() { 
		
		HashMap<Integer, Float> expectedBloodSugarMap = new HashMap<Integer, Float>();
		HashMap<Integer, Integer> expectedGlycationMap = new HashMap<Integer, Integer>();
		
		for(int i=0; i<MINUTES_IN_DAY; i++) {
			expectedBloodSugarMap.put(i, BLOOD_SUGAR_START);
			expectedGlycationMap.put(i, 0);
		}
		
		simulator.start();
		
		HashMap<Integer, Float> actualBloodSugarMap = simulator.getBloodSugarPerMinuteMap();
		HashMap<Integer, Integer> actualGlycationMap = simulator.getGlycationsPerMinuteMap();

		assertEquals(expectedBloodSugarMap, actualBloodSugarMap);
		assertEquals(expectedGlycationMap, actualGlycationMap);
	}
	
	/**
	 * Testing eating Food
	 * 
	 * Food [id=46, name=Sweet corn on the cob, index=60]
	 * Eating time: 06.30 AM
	 */
	@Test 
	public void testSweetCornAndNormalizaton() { 
		int eatingTime = 30; // in minutes after begining of the day
		Float foodIndex = 60F; // Sweet corn on the cub index
		Float sugarPerMinute = foodIndex / (float) Type.FOOD.getPeriod();
		
		HashMap<Integer, Float> expectedBloodSugarMap = initBloodSugarMap();
		
		Float lastBloodSugarValue = 0F;
		
		// after eating the sweet cord, it will increase blood sugar linearly for two hours
		for(int i=eatingTime; i<eatingTime + Type.FOOD.getPeriod(); i++){
			lastBloodSugarValue = expectedBloodSugarMap.get(i-1) + sugarPerMinute;
			expectedBloodSugarMap.put(i, lastBloodSugarValue);
		}
		
		// put the last blood sugar in the remaining minutes
		for(int i=eatingTime + Type.FOOD.getPeriod(); i<MINUTES_IN_DAY; i++) {
			expectedBloodSugarMap.put(i, lastBloodSugarValue);
		}
		
		int tempPosition = 0;
		
		// after food process nothing happened, and its been two hours, so do the normalization.
		for(int i=eatingTime + (Type.FOOD.getPeriod() * 2); i<MINUTES_IN_DAY; i++) {
			Float value = expectedBloodSugarMap.get(i - 1);
			if ((value - 1F) < BLOOD_SUGAR_START) {
				value = BLOOD_SUGAR_START;
				expectedBloodSugarMap.put(i, value);
				break;
			}
			expectedBloodSugarMap.put(i, value - 1F); 
			tempPosition = i;
		}
		
		// after the normalization, put 80 till end of the day.
		for(int i=tempPosition; i<MINUTES_IN_DAY; i++){
			expectedBloodSugarMap.put(i, BLOOD_SUGAR_START);
		}
		
		// add the Sweet Corn to the Simulator
		List<Entry> entries = new ArrayList<Entry>();
		Entry entry = new Entry(addMinute(eatingTime), Type.FOOD.toString(), 46);
		entries.add(entry);
		
		simulator.reset();
		simulator.setEntries(entries);
		simulator.start();
		
		HashMap<Integer, Float> actualBloodSugarMap = simulator.getBloodSugarPerMinuteMap();
		assertEquals(expectedBloodSugarMap, actualBloodSugarMap);
	}

	/**
	 * Testing exercise:
	 * 
	 * Exercise [id=5, name=Squats, index=60]
	 * Eating time: 06.40 AM
	 */
	@Test 
	public void testSqautsAndNormalizaton() { 
		int exerciseTime = 40; // in minutes after begining of the day
		Float exerciseIndex = 60F; // Squats index
		Float sugarPerMinute = exerciseIndex / (float) Type.EXERCISE.getPeriod();
		
		HashMap<Integer, Float> expectedBloodSugarMap = initBloodSugarMap();
		
		Float lastBloodSugarValue = 0F;
		
		// doing Sqauts, it will decrease blood sugar linearly for one hours
		for(int i=exerciseTime; i<exerciseTime + Type.EXERCISE.getPeriod(); i++){
			lastBloodSugarValue = expectedBloodSugarMap.get(i-1) - sugarPerMinute;
			expectedBloodSugarMap.put(i, lastBloodSugarValue);
		}
		
		// put the last blood sugar in the remaining minutes
		for(int i=exerciseTime + Type.EXERCISE.getPeriod(); i<MINUTES_IN_DAY; i++) {
			expectedBloodSugarMap.put(i, lastBloodSugarValue);
		}
		
		int tempPosition = 0;
		
		// after Squats process nothing happened, and its been one hour, so do the normalization.
		for(int i=exerciseTime + (Type.EXERCISE.getPeriod() * 2); i<MINUTES_IN_DAY; i++) {
			Float value = expectedBloodSugarMap.get(i - 1);
			if ((value + 1F) > BLOOD_SUGAR_START) {
				value = BLOOD_SUGAR_START;
				expectedBloodSugarMap.put(i, value);
				break;
			}
			expectedBloodSugarMap.put(i, value + 1F); 
			tempPosition = i;
		}
		
		// after the normalization, put 80 till end of the day.
		for(int i=tempPosition; i<MINUTES_IN_DAY; i++){
			expectedBloodSugarMap.put(i, BLOOD_SUGAR_START);
		}
		
		// add Sqauts to the Simulator
		List<Entry> entries = new ArrayList<Entry>();
		Entry entry = new Entry(addMinute(exerciseTime), Type.EXERCISE.toString(), 5);
		entries.add(entry);
		
		simulator.reset();
		simulator.setEntries(entries);
		simulator.start();
		
		HashMap<Integer, Float> actualBloodSugarMap = simulator.getBloodSugarPerMinuteMap();
		assertEquals(expectedBloodSugarMap, actualBloodSugarMap);
	}
	
	private Date addMinute(int minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(beginOfDay);
		cal.add(Calendar.MINUTE, minute);
		return cal.getTime();
	}
	
	private HashMap<Integer, Float> initBloodSugarMap() {
		HashMap<Integer, Float> map = new HashMap<Integer, Float>();
		for(int i=0; i<MINUTES_IN_DAY; i++) {
			map.put(i, BLOOD_SUGAR_START);
		}
		return map;
	}
	
	/*private void printMe(HashMap<Integer, Float> map) {
		for(int i=0; i<MINUTES_IN_DAY; i++) {
			System.out.println(i + "=" + map.get(i));
		}
	}*/
}


