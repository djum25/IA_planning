package com.project.optaplanner.domain;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Resource {

	@Id
	@GeneratedValue
	private Long id;
	
	private int capacity;
	
	private Type type;
	
	private LocalDate workDay;
	
	public Resource() {};

	public Resource(int capacity, Type type, LocalDate workDay) {
		super();
		this.capacity = capacity;
		this.type = type;
		this.workDay = workDay;
	}

	public Resource(Long id, int capacity, Type type, LocalDate workDay) {
		super();
		this.id = id;
		this.capacity = capacity;
		this.type = type;
		this.workDay = workDay;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public LocalDate getWorkDay() {
		return workDay;
	}

	public void setWorkDay(LocalDate workDay) {
		this.workDay = workDay;
	}
}
