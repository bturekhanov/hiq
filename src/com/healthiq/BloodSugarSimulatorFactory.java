package com.healthiq;

import java.util.Date;
import java.util.List;
import com.healthiq.entities.Entry;

/**
 * Factory class to create instance of Blood Sugar Simulator, this can be extended to create instance of * different types of Blood Sugar Simulators.
 * 
 * @author Bakhy
 *
 */
public class BloodSugarSimulatorFactory {

	public static BloodSugarSimulator createBloodSugarSimulator(Date beginOfDay, List<Entry> entries){
		return new BloodSugarSimulatorImpl(beginOfDay, entries);
	}
}
