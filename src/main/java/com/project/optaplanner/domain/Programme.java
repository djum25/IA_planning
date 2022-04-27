package com.project.optaplanner.domain;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.solution.ProblemFactProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.solver.SolverStatus;

@PlanningSolution
public class Programme {

	@ProblemFactProperty
	private WorkCalendar workCalendar;
	@ProblemFactProperty
	private List<LocalDate> holidays;
	@ProblemFactCollectionProperty
    @ValueRangeProvider(id = "prototypeRange")
	private List<Prototype> prototypeList;
	@ProblemFactCollectionProperty
	@ValueRangeProvider(id = "teamRange")
	private List<Team> teamList;
	@PlanningEntityCollectionProperty
	private List<Task> taskList;
    @PlanningScore
    private HardSoftLongScore score;

    // Ignored by OptaPlanner, used by the UI to display solve or stop solving button
    private SolverStatus solverStatus;

	public Programme() {
	}

	public Programme(WorkCalendar workCalendar, List<Prototype> prototypeList, List<Task> taskList,List<Team> teamList,List<LocalDate> holidays) {
		this.workCalendar = workCalendar;
		this.prototypeList = prototypeList;
		this.taskList = taskList;
		this.teamList = teamList;
		this.holidays = holidays;
	}
	
    @ValueRangeProvider(id = "startDateRange")
    public List<LocalDate> createStartDateList() {
        return workCalendar.getFromDate().datesUntil(workCalendar.getToDate())
                /* Skip weekends. Does not work for holidays.
                 Keep in sync with EndDateUpdatingVariableListener.updateEndDate().
                 To skip holidays too, cache all working days in WorkCalendar.*/
                .filter(date -> date.getDayOfWeek() != DayOfWeek.SATURDAY
                        && date.getDayOfWeek() != DayOfWeek.SUNDAY && !holidays.contains(date))
                .collect(Collectors.toList());
    }

	public WorkCalendar getWorkCalendar() {
		return workCalendar;
	}

	public List<Prototype> getPrototypeList() {
		return prototypeList;
	}

	public List<Task> getTaskList() {
		return taskList;
	}

	public List<Team> getTeamList() {
		return teamList;
	}

	public List<LocalDate> getHolidays() {
		return holidays;
	}

	public HardSoftLongScore getScore() {
		return score;
	}

	public void setScore(HardSoftLongScore score) {
		this.score = score;
	}

	public SolverStatus getSolverStatus() {
		return solverStatus;
	}

	public void setSolverStatus(SolverStatus solverStatus) {
		this.solverStatus = solverStatus;
	}
	
}
