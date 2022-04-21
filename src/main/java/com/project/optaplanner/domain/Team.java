package com.project.optaplanner.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.optaplanner.core.api.domain.lookup.PlanningId;

@Entity
public class Team {

	@PlanningId
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	private String usine;
	
	private int sequence;
	
	

	public Team() {}

	public Team(String name, String usine, int sequence) {
		this.name = name;
		this.usine = usine;
		this.sequence = sequence;
	}

	public Team(Long id, String name, String usine, int sequence) {
		this.id = id;
		this.name = name;
		this.usine = usine;
		this.sequence = sequence;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsine() {
		return usine;
	}

	public void setUsine(String usine) {
		this.usine = usine;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	@Override
	public String toString() {
		return "Team ["+id + ", name=" + name + "]";
	}
	
}
