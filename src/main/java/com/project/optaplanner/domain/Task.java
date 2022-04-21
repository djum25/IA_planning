package com.project.optaplanner.domain;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.CustomShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableReference;

import com.project.optaplanner.solver.EndDateUpdatingVariableListener;


@PlanningEntity
@Entity
public class Task {

	@Id
	@GeneratedValue
	private Long id;
	
	private String name;
	
	private String usine;
	
	private LocalDate idealStartDate;
	
	private LocalDate idealEndDate;
	
	@PlanningVariable(valueRangeProviderRefs = {"startDateRange"})
	private LocalDate startDate;
	@CustomShadowVariable(variableListenerClass = EndDateUpdatingVariableListener.class,
            sources = @PlanningVariableReference(variableName = "startDate"))
	private LocalDate endDate;
	
	private int duration;
	
	private int sequence;
	
	private int margin;
	
	private String phase;
	
	private String type;
	
	@PlanningVariable(valueRangeProviderRefs = {"prototypeRange"})
	@ManyToOne
	private Prototype prototype;
	
	@PlanningVariable(valueRangeProviderRefs = {"teamRange"})
	@ManyToOne
	private Team team;

	public Task() {
	}

	public Task(String name,LocalDate idealStartDate,LocalDate idealEndDate,int duration, int sequence, int margin, String phase, String type, String usine) {
		this.name = name;
		this.idealStartDate = idealStartDate;
		this.idealEndDate = idealEndDate;
		this.duration = duration;
		this.sequence = sequence;
		this.margin = margin;
		this.phase = phase;
		this.type = type;
		this.usine = usine;
	}

	public Task(Long id, String name,LocalDate idealStartDate,LocalDate idealEndDate, LocalDate startDate, 
			LocalDate endDate, int duration, int sequence, int margin, String phase, String type, String usine) {
		this.id = id;
		this.name = name;
		this.idealStartDate = idealStartDate;
		this.idealEndDate = idealEndDate;
		this.startDate = startDate;
		this.duration = duration;
		this.sequence = sequence;
		this.margin = margin;
		this.phase = phase;
		this.type = type;
		this.usine = usine;
		this.endDate = EndDateUpdatingVariableListener.calculateEndDate(startDate, duration);
	}
	
	

	@Override
	public String toString() {
		return "Task " + id + ", name=" + name;
	}

	@PlanningId
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

	public LocalDate getIdealStartDate() {
		return idealStartDate;
	}

	public void setIdealStartDate(LocalDate idealStartDate) {
		this.idealStartDate = idealStartDate;
	}

	public LocalDate getIdealEndDate() {
		return idealEndDate;
	}

	public void setIdealEndDate(LocalDate idealEndDate) {
		this.idealEndDate = idealEndDate;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Prototype getPrototype() {
		return prototype;
	}

	public void setPrototype(Prototype prototype) {
		this.prototype = prototype;
	}

	public Team getTeam() {
		return team;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public String getUsine() {
		return usine;
	}

	public void setUsine(String usine) {
		this.usine = usine;
	}
	
}
