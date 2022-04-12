package com.project.optaplanner.domain;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.optaplanner.core.api.domain.lookup.PlanningId;

@Entity
public class Prototype {

	@PlanningId
	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	private String type;
	
	private String phase;
	
	private String usine;
	
	private LocalDate deliveryDate;

	public Prototype() {
	}

	public Prototype(String name, String type, String phase, LocalDate deliveryDate,String usine) {
		this.name = name;
		this.type = type;
		this.phase = phase;
		this.deliveryDate = deliveryDate;
		this.usine = usine;
	}

	public Prototype(Long id, String name, String type, String phase, LocalDate deliveryDate, String usine) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.phase = phase;
		this.deliveryDate = deliveryDate;
		this.usine = usine;
	}
	
	

	@Override
	public String toString() {
		return "Prototype [name=" + name + ", phase=" + phase + "]";
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getUsine() {
		return usine;
	}

	public void setUsine(String usine) {
		this.usine = usine;
	}
	
}
