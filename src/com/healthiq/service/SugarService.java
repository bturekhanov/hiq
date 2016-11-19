package com.healthiq.service;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;

import com.healthiq.dao.HealthiqDAO;
import com.healthiq.entities.Activity;
import com.healthiq.entities.Entry;
import com.healthiq.entities.Exercise;
import com.healthiq.entities.Food;
import com.healthiq.exceptions.BelowMinLimitException;
import com.healthiq.exceptions.ExceedMaxLimitException;
import com.healthiq.util.Type;

public class SugarService {
	
	public static final int NORMALIZATION_RATE = 1;
	public static final Float BLOOD_SUGAR_START = 80F;
	public static final Float BLOOD_SUGAR_MIN_LIMIT = 5F;
	public static final Float BLOOD_SUGAR_MAX_LIMIT = 1000F;
	public static final Float GLYCATION_LIMIT = 150F;
	
	public static final int MINUTES_IN_DAY = 1440; // 24 * 60
	//public static final int MINUTES_IN_DAY = 400; // testing
	
	private HashMap<Integer, Food> foods;
	private HashMap<Integer, Exercise> exercises;
	private HealthiqDAO healthiqDAO;
	private Activity lastActivity = null;
	
	public SugarService(){
		healthiqDAO = new HealthiqDAO();
		foods = healthiqDAO.findAllFood();
		exercises = healthiqDAO.findAllExercise();
	}
	
	public HashMap<Integer, Float> calculateSugar(Date beginOfDay, List<Entry> entries, HashMap<Integer, Float> map, HashMap<Integer, Integer> glycations) {
		
		/* validation */
		if (entries == null || entries.size() == 0) return map;
		
		sortByDate(entries);
		
		Queue<Entry> queue = new ArrayDeque<Entry>();
		for(Entry entry : entries){
			queue.add(entry);
		}
		
		Entry entry = queue.poll();
		List<Activity> activities = new ArrayList<Activity>();
		Float value = 0F;
		int glycation = 0;
		int noActivityPeriod = 0;
		
		/* Let's assume: At the very first minute of day, people can not eat foods and do exercises. 
		 * So start calculating blood sugar from a second minute. */
		glycations.put(0, 0);
		for(int i=1; i<MINUTES_IN_DAY; i++) {
			
			if (i == entry.getTime(beginOfDay)) {
				lastActivity = null;
				noActivityPeriod = 0;
				if (entry.getType().toString().equals(Type.FOOD.toString())) {
					
					/* put the food into activities as an activity */
					Food food = foods.get(entry.getId());
					Activity activity = new Activity(food.getSugarPerMinute(Type.FOOD.getPeriod()), Type.FOOD);
					activities.add(activity);
				} else if (entry.getType().toString().equals(Type.EXERCISE.toString())) {
					
					/* put the exercise into activities as an activity */
					Exercise exercise = exercises.get(entry.getId());
					Activity activity = new Activity(exercise.getSugarPerMinute(Type.EXERCISE.getPeriod()), Type.EXERCISE);
					activities.add(activity);
				}
			}
			
			/* Update the blood sugar value in the map */
			value = getValueAndUpdateActivites(activities); 
			map.put(i, map.get(i-1) + value);
			
			/* count no activity period */
			if (lastActivity != null) {
				noActivityPeriod++;
			}
			
			/* normalization after food */
			if (lastActivity != null && lastActivity.getType() == Type.FOOD && noActivityPeriod >= Type.FOOD.getPeriod()) {
				doNormalization(i, map);
			}
			
			/* normalization after exercise */
			if (lastActivity != null && lastActivity.getType() == Type.EXERCISE && noActivityPeriod >= Type.EXERCISE.getPeriod()) {
				doNormalization(i, map);
			}
			
			/* move to next entry */
			if (i == entry.getTime(beginOfDay) && !queue.isEmpty()) {
				entry = queue.poll();
			}
			
			/* tracking glycation */
			if (map.get(i) > GLYCATION_LIMIT) {
				glycation++;
			}
			glycations.put(i, glycation);
			
			/* Check blood sugar limits */
			if (map.get(i) < BLOOD_SUGAR_MIN_LIMIT) {
				throw new BelowMinLimitException("Emergency! Below than BLOOD_SUGAR_MIN_LIMIT: " + BLOOD_SUGAR_MIN_LIMIT + ", CURRENT_BLOOD_SUGAR: ", map.get(i), addMinute(beginOfDay, i + 1));
			}
			if (map.get(i) > BLOOD_SUGAR_MAX_LIMIT) {
				throw new ExceedMaxLimitException("Emergency! Exceeds BLOOD_SUGAR_MAX_LIMIT: " + BLOOD_SUGAR_MAX_LIMIT + ", CURRENT_BLOOD_SUGAR: ", map.get(i), addMinute(beginOfDay, i + 1));
			}
		}
		return map;
	}
	
	/* Approaches 80 linearly at a rate of 1 per minute. */
	private void doNormalization(int minute, HashMap<Integer, Float> map) {
		Float currentValue = map.get(minute);
		if (currentValue < BLOOD_SUGAR_START) {
			Float newValue = currentValue + NORMALIZATION_RATE;
			if (newValue > BLOOD_SUGAR_START) newValue = BLOOD_SUGAR_START;
			map.put(minute, newValue);
		} else if (currentValue > BLOOD_SUGAR_START) {
			Float newValue = currentValue - NORMALIZATION_RATE;
			if (newValue < BLOOD_SUGAR_START) newValue = BLOOD_SUGAR_START;
			map.put(minute, newValue);
		}
	}
	
	/* sort from earlier to later. */
	private void sortByDate(List<Entry> entries) {
		
		Collections.sort(entries, new Comparator<Entry>() {
			public int compare(Entry o1, Entry o2) {
			      if (o1.getTimestamp() == null || o2.getTimestamp() == null)
			        return 0;
			      return o1.getTimestamp().compareTo(o2.getTimestamp());
			  }
			});
	}
	
	private Float getValueAndUpdateActivites(List<Activity> activities) {
		Float value = 0F;
		
		if (activities == null || activities.size() == 0) return value;
		
		/* We need a lastActivity for the normalization. */
		if (activities.size() == 1 && activities.get(0).getQuantity() == 1) {
			lastActivity = activities.get(0);
		}
		
		for (Iterator<Activity> iter = activities.listIterator(); iter.hasNext(); ) {
			Activity activity = iter.next();
			value += activity.getSugerPerMinute();
			int quantity  = activity.getQuantity();
		    if (quantity == 1) {
		        iter.remove();
		    } else {
		    	activity.setQuantity(quantity - 1);
		    }
		}
		return value;
	}
	
	public void printAllEntries(Date beginOfDay, List<Entry> entries) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm a");
		System.out.println("Beginning of day: " + dateFormat.format(beginOfDay) + "\n");

		System.out.println("Input data:");
		if (entries == null || entries.size() == 0) {
			System.out.println("\t No entries!");
		} else {
			for(Entry entry : entries) {
				if (entry.getType().toString().equals(Type.FOOD.toString())) {
					Food food = foods.get(entry.getId());
					System.out.println("\t " + food.toString() + " at " + dateFormat.format(entry.getTimestamp()));
				} else if (entry.getType().toString().equals(Type.EXERCISE.toString())) {
					Exercise exercise = exercises.get(entry.getId());
					System.out.println("\t " + exercise.toString() + " at " + dateFormat.format(entry.getTimestamp()));
				} else {
					System.out.println("\t Unknown entry: " + entry.toString() + " at " + dateFormat.format(entry.getTimestamp()));
				}
			}
		}
		
	}
	
	public Date addMinute(Date date, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minute);
		return cal.getTime();
	}
}
