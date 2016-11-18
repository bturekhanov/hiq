package com.healthiq.service;

import java.util.ArrayDeque;
import java.util.ArrayList;
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
	
	//private static final int MINUTES_IN_DAY = 1440; // 24 * 60
	public static final int MINUTES_IN_DAY = 30; // 24 * 60
	
	private HashMap<Integer, Food> foods;
	private HashMap<Integer, Exercise> exercises;
	private HealthiqDAO healthiqDAO;
	
	public SugarService(){
		healthiqDAO = new HealthiqDAO();
		foods = healthiqDAO.findAllFood();
		exercises = healthiqDAO.findAllExercise();
	}
	
	public HashMap<Integer, Float> calculateSugar(List<Entry> inputs, HashMap<Integer, Float> map, Date beginOfDay) {
		
		Queue<Entry> queue = new ArrayDeque<Entry>();
		for(Entry entry : inputs){
			queue.add(entry);
		}
		
		/* validation */
		if (inputs == null || inputs.size() == 0) return map;
		
		Entry entry = queue.poll();
		List<Activity> activities = new ArrayList<Activity>();
		Float value = 0F;
		
		for(int i=1; i<MINUTES_IN_DAY; i++) {
			
			if (i == entry.getTime(beginOfDay)) {
				if (entry.getType().toString().equals(Type.FOOD.toString())) {
					Food food = foods.get(entry.getId());
					Activity activity = new Activity(food.getSugarPerMinute(), Food.PERIOD);
					activities.add(activity);
				} else if (entry.getType().toString().equals(Type.EXERCISE.toString())) {
					Exercise exercise = exercises.get(entry.getId());
					Activity activity = new Activity(exercise.getSugarPerMinute(), Exercise.PERIOD);
					activities.add(activity);
				}
			}
			
			value = getValueAndUpdateActivites(activities); 
			map.put(i, map.get(i-1) + value);
			
			if (i == entry.getTime(beginOfDay) && !queue.isEmpty()) {
				entry = queue.poll();
			}
		}
		
		return map;
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

	public void increase(int startPosition, int foodId, HashMap<Integer, Float> map) {
		Food food = foods.get(foodId);
		for(int i=startPosition; i<Food.PERIOD; i++){
			map.put(i, map.get(i) + food.getSugarPerMinute());
		}
	}

	public void decrease(int startPosition, int exerciseId, HashMap<Integer, Float> map) {
		Exercise exercise = exercises.get(exerciseId);
		for(int i=startPosition; i<Exercise.PERIOD; i++){
			map.put(i, map.get(i) + exercise.getSugarPerMinute());
		}
	}
}
