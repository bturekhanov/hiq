package com.healthiq;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.healthiq.entities.Entry;
import com.healthiq.service.SugarService;

/**
 * Sample implementation of Blood Sugar Simulator.
 * 
 * @author Bakhy
 *
 */
public class BloodSugarSimulatorImpl implements BloodSugarSimulator {

	private List<Entry> entries;
	private SugarService sugarService;
	
	/* key = minute, value = blood sugar value */
	private HashMap<Integer, Float> bloodSugarMap;
	
	/* key = minute, value = glycation */
	private HashMap<Integer, Integer> glycationsMap;
	
	/* Beginning of the day */
	private Date beginOfDay;
	
	public BloodSugarSimulatorImpl(Date beginOfDay, List<Entry> entries) {
		this.beginOfDay = beginOfDay;
		this.entries = entries;
		initialize();
	}
	
	private void initialize(){
		sugarService = new SugarService();
		glycationsMap = initGlycationMap();
		bloodSugarMap = initBloodSugarMap();
    }
	
	private HashMap<Integer, Float> initBloodSugarMap() {
		HashMap<Integer, Float> map = new HashMap<Integer, Float>();
		for(int i=0; i<SugarService.MINUTES_IN_DAY; i++) {
			map.put(i, SugarService.BLOOD_SUGAR_START);
		}
		return map;
	}
	
	private HashMap<Integer, Integer> initGlycationMap() {
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		for(int i=0; i<SugarService.MINUTES_IN_DAY; i++) {
			map.put(i, 0);
		}
		return map;
	}
	
	@Override
	public void start(){
		System.out.println("======================================================================");
		System.out.println("=================   Simulator is starting ... ========================\n");
		
		sugarService.printAllEntries(beginOfDay, entries);
		bloodSugarMap = sugarService.calculateSugar(beginOfDay, entries, bloodSugarMap, glycationsMap);
		
		System.out.println("\n=================   Simulator is done! ===============================");
		System.out.println("======================================================================");
		
		printBloodSugarPerMinuteMap();
		//printGlycationsPerMinuteMap();
	}
	
	@Override
	public void reset() {
		if (entries != null) entries.clear();
		glycationsMap = initGlycationMap();
		bloodSugarMap = initBloodSugarMap();
	}

	@Override
	public void printBloodSugarPerMinuteMap() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		Float bloodSugar;
		
		System.out.println("outputs in format [minute = blood_sugar]:");
		
		for(int i=0; i<SugarService.MINUTES_IN_DAY; i++) {
		
			Date time = sugarService.addMinute(beginOfDay, i);
			
			// Round a number to 2 decimal places
			bloodSugar = (float) (Math.round(bloodSugarMap.get(i) * 1e2) / 1e2);
			
			System.out.println(dateFormat.format(time) + " = " + bloodSugar);
			//System.out.println(i + " = " + bloodSugar);
		}
	}

	@Override
	public void printGlycationsPerMinuteMap() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
		
		System.out.println("\n glycations in format [minute = glycation]:");
		for(int i=0; i<SugarService.MINUTES_IN_DAY; i++) {
			Date time = sugarService.addMinute(beginOfDay, i);
			System.out.println(dateFormat.format(time) + " = " + glycationsMap.get(i));
		}
	}

	@Override
	public HashMap<Integer, Float> getBloodSugarPerMinuteMap() {
		return bloodSugarMap;
	}

	@Override
	public HashMap<Integer, Integer> getGlycationsPerMinuteMap() {
		return glycationsMap;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public void setEntries(List<Entry> entries) {
		this.entries = entries;
	}
}
