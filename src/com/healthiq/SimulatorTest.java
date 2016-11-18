package com.healthiq;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.healthiq.entities.Entry;
import com.healthiq.util.Type;

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
		BloodSugarSimulator simulator = new BloodSugarSimulatorImpl(80F, beginningOfDay, inputs);
		//simulator.inputData(inputs);
		simulator.start();
		simulator.printOutputs();
	}
	
	public static void addFoods(List<Entry> inputs) {
		
		/* Eat a sweet corn on the cob at [beginningOfDay + 10 minutes] */
		cal.setTime(beginningOfDay);
		cal.add(Calendar.MINUTE, 3);
		//Entry entry = new Entry(cal.getTime(), Type.FOOD.toString(), 46);
		Entry entry = new Entry(cal.getTime(), Type.FOOD.toString(), 124);
		inputs.add(entry);
	}
	
	public static void addExercises(List<Entry> inputs) {
		
		/* do squats at [beginningOfDay + 30 minutes] */
		cal.setTime(beginningOfDay);
		cal.add(Calendar.MINUTE, 5);
		//Entry entry = new Entry(cal.getTime(), Type.EXERCISE.toString(), 5); 
		Entry entry = new Entry(cal.getTime(), Type.EXERCISE.toString(), 7); 
		inputs.add(entry);
	}
}
