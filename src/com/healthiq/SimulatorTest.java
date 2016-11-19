package com.healthiq;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.healthiq.entities.Entry;
import com.healthiq.util.Type;

/**
 * Tester of Blood Sugar Simulator.
 * 
 * @author Bakhy
 *
 */
public class SimulatorTest {

	@SuppressWarnings("deprecation")
	public static Date beginningOfDay = new Date(2016, Calendar.NOVEMBER, 17, 6, 0); // 06:00 AM
	public static Calendar cal = Calendar.getInstance();
	
	public static void main(String[] args) {
		
		/* Create inputs by adding foods and exercises. */
		List<Entry> inputs = new ArrayList<Entry>();
		addFoods(inputs);
		addExercises(inputs);
		
		/* Create a simulator and start it. Check outputs on the console then. */
		BloodSugarSimulator simulator = new BloodSugarSimulatorImpl(beginningOfDay, inputs);
		simulator.start();
		simulator.printBloodSugarPerMinuteMap();
		simulator.printGlycationsPerMinuteMap();
	}
	
	public static void addFoods(List<Entry> inputs) {
		
		/* Eat a sweet corn on the cob at [beginningOfDay + 10 minutes] */
		Entry entry = new Entry(addMinute(beginningOfDay, 10), Type.FOOD.toString(), 9); // testing my specific case.
		inputs.add(entry);
		
		Entry entry2 = new Entry(addMinute(beginningOfDay, 12), Type.FOOD.toString(), 46); // testing my specific case.
		inputs.add(entry2);
	}
	
	public static void addExercises(List<Entry> inputs) {
		
		/* do squats at [beginningOfDay + 30 minutes] */
		Entry entry = new Entry(addMinute(beginningOfDay, 30), Type.EXERCISE.toString(), 5); // testing my specific case.  
		inputs.add(entry);
	}
	
	public static Date addMinute(Date date, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minute);
		return cal.getTime();
	}
}
