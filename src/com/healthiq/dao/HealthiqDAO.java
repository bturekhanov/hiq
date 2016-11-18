package com.healthiq.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import com.healthiq.entities.Exercise;
import com.healthiq.entities.Food;

/**
 * 
 * Simulation of Database
 * 
 * @author Bakhy
 *
 */
public class HealthiqDAO{

	/**
	 * Reads CVS file using BufferedReader and String split()
	 * and puts the data into a HashMap<Integer, Food>
	 * 	map.key   = food id
	 * 	map.value = food object
	 * 
	 * @return map<Integer, Food>
	 */
	public HashMap<Integer, Food> findAllFood(){
		HashMap<Integer, Food> foods = new HashMap<Integer, Food>();
		String workingDir = System.getProperty("user.dir");
		String csvFile = workingDir + "/foods.csv";
        String line = "";
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                String[] fields = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                int id = Integer.parseInt(fields[0]);
                String name = fields[1];
                int index = Integer.parseInt(fields[2]);
                
                Food food = new Food(id, name, index);
                foods.put(id, food);
            }

        } catch (IOException e) {
        	System.out.println("Error while reading foods from CSV file.");
            e.printStackTrace();
        }
        return foods;
	}
	
	public HashMap<Integer, Exercise> findAllExercise(){
		HashMap<Integer, Exercise> exercises = new HashMap<Integer, Exercise>();
		String workingDir = System.getProperty("user.dir");
		String csvFile = workingDir + "/exercises.csv";
        String line = "";
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            while ((line = br.readLine()) != null) {

                String[] fields = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                int id = Integer.parseInt(fields[0]);
                String name = fields[1];
                int index = Integer.parseInt(fields[2]);
                
                Exercise exercise = new Exercise(id, name, index);
                exercises.put(id, exercise);
            }

        } catch (IOException e) {
        	System.out.println("Error while reading exercises from CSV file.");
            e.printStackTrace();
        }
        return exercises;
	}
	
}
