package com.healthiq.service;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
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
import com.healthiq.util.Type;

public class SugarService {
	
	public static final int MINUTES_IN_DAY = 1440; // 24 * 60
	//public static final int MINUTES_IN_DAY = 30; // testing
	
	private HashMap<Integer, Food> foods;
	private HashMap<Integer, Exercise> exercises;
	private HealthiqDAO healthiqDAO;
	
	public SugarService(){
		healthiqDAO = new HealthiqDAO();
		foods = healthiqDAO.findAllFood();
		exercises = healthiqDAO.findAllExercise();
	}
	
	public HashMap<Integer, Float> calculateSugar(List<Entry> entries, HashMap<Integer, Float> map, Date beginOfDay, Float sugarStart) {
		
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
		
		for(int i=1; i<MINUTES_IN_DAY; i++) {
			
			if (i == entry.getTime(beginOfDay)) {
				if (entry.getType().toString().equals(Type.FOOD.toString())) {
					Food food = foods.get(entry.getId());
					Activity activity = new Activity(food.getSugarPerMinute(Type.FOOD.getPeriod()), Type.FOOD.getPeriod());
					activities.add(activity);
				} else if (entry.getType().toString().equals(Type.EXERCISE.toString())) {
					Exercise exercise = exercises.get(entry.getId());
					Activity activity = new Activity(exercise.getSugarPerMinute(Type.EXERCISE.getPeriod()), Type.EXERCISE.getPeriod());
					activities.add(activity);
				}
			}
			
			value = getValueAndUpdateActivites(activities); 
			/* sugar level never goes down than start level 80. If it's possible, I will handle it later. */
			Float newValue = ((map.get(i-1) + value) < sugarStart) ? sugarStart : (map.get(i-1) + value); 
			map.put(i, newValue);
			
			if (i == entry.getTime(beginOfDay) && !queue.isEmpty()) {
				entry = queue.poll();
			}
		}
		
		return map;
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
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		System.out.println("Beginning of day: " + dateFormat.format(beginOfDay) + "\n");

		System.out.println("Input data:");
		if (entries == null) {
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
}
