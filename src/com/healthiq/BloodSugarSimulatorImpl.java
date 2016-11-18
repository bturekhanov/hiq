package com.healthiq;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.healthiq.entities.Entry;
import com.healthiq.service.SugarService;

public class BloodSugarSimulatorImpl implements BloodSugarSimulator {

	private List<Entry> inputs;
	private SugarService sugarService;
	
	/* key = minute, value = blood sugar value */
	private HashMap<Integer, Float> outputs;
	
	/* Blood sugar at the beginning of the day */
	private Float sugarStart;
	
	/* Beginning of the day */
	private Date beginOfDay;
	
	public BloodSugarSimulatorImpl(Float sugarStart, Date beginOfDay, List<Entry> inputs) {
		this.sugarStart = sugarStart;
		this.beginOfDay = beginOfDay;
		this.inputs = inputs;
		initialize();
	}
	
	private void initialize(){
		sugarService = new SugarService();
		outputs = initializeMap();
    }
	
	private HashMap<Integer, Float> initializeMap() {
		HashMap<Integer, Float> map = new HashMap<Integer, Float>();
		for(int i=0; i<SugarService.MINUTES_IN_DAY; i++) {
			map.put(i, sugarStart);
		}
		return map;
	}
	
	@Override
	public void start(){
		System.out.println("======================================================================");
		System.out.println("=================   Simulator is starting ... ========================\n");
		
		sugarService.printAllEntries(beginOfDay, inputs);
		outputs = sugarService.calculateSugar(inputs, outputs, beginOfDay, sugarStart);
		
		System.out.println("\n=================   Simulator is done! ===============================");
		System.out.println("======================================================================");
	}
	
	@Override
	public void printOutputs() {
		for(int i=0; i<SugarService.MINUTES_IN_DAY; i++) {
			System.out.println(i + " = " + outputs.get(i));
		}
	}
	
	@Override
	public void reset() {
		// TODO Auto-generated method stub
	}

	@Override
	public HashMap<Integer, Float> getResult() {
		return outputs;
	}
}
