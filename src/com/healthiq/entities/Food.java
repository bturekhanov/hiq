package com.healthiq.entities;

public class Food {
	
	public Integer id;
	public String name;
	public Integer index;
	
	public Food(Integer id, String name, Integer index) {
		this.id = id;
		this.name = name;
		this.index = index;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Food [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", index=");
		builder.append(index);
		builder.append("]");
		return builder.toString();
	}
	
	public Float getSugarPerMinute(int period) {
		return (float) index / (float) period;
	}
}
