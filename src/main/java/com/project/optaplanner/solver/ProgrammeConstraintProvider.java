package com.project.optaplanner.solver;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.optaplanner.core.api.score.stream.ConstraintCollectors.count;

import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import com.project.optaplanner.domain.Task;

public class ProgrammeConstraintProvider implements ConstraintProvider{

	@Override
	public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
		return new Constraint[] {
				prototypeConstraint(constraintFactory),
				ordonateConstraint(constraintFactory),
				//endDateConstraint(constraintFactory),
				typeConstraint(constraintFactory),
				phaseConstraint(constraintFactory),
				afterIdealEndDate(constraintFactory),
				beforeIdealEndDate(constraintFactory),
				startedConstraint(constraintFactory),
				waitConstraint(constraintFactory),
				teamCapacityConstraint(constraintFactory),
				teamCompatibilityConstraint(constraintFactory),
				rightTeamConstraint(constraintFactory)
				//disponibilityConstraint(constraintFactory),
		};
	}

	public Constraint prototypeConstraint(ConstraintFactory constraintFactory) {
		//A prototype can't do 2 task of same sequence
		return constraintFactory.forEachUniquePair(Task.class,
				Joiners.equal(Task::getSequence),
				Joiners.equal(Task::getPrototype)).penalize("Prototype conflict",HardSoftLongScore.ONE_HARD,
						(task1,task2) -> task1.getId().intValue()-1);
	}
	
	public Constraint endDateConstraint(ConstraintFactory constraintFactory) {
		// use afterIdealEndDate(constraintFactory)
		return constraintFactory.
				forEach(Task.class)
                        .filter(task-> task.getEndDate().isAfter(task.getIdealEndDate()))
                .penalize("End Date conflict", HardSoftLongScore.ONE_HARD);
	}
	
	public Constraint disponibilityConstraint(ConstraintFactory constraintFactory) {
		// this one is wrong
		return constraintFactory.forEach(Task.class)
				.groupBy(Task::getSequence , Task::getStartDate, count())
				.filter((sequance,startat, val) -> sequance.equals(11) & val > 2)
				.penalize("Disponibility conflict", HardSoftLongScore.ONE_HARD,(sequance,startat, val)-> val-1);
	}
	
	public Constraint typeConstraint(ConstraintFactory constraintFactory) {
		// Don't affect prototype to task where the type is differrent
		return constraintFactory.forEach(Task.class)
				.filter(task -> !task.getType().equals(task.getPrototype().getType()))
				.penalize("Type Constraint", HardSoftLongScore.ONE_SOFT);
	}
	
	public Constraint phaseConstraint(ConstraintFactory constraintFactory) {
		// Don't affect prototype to task where the phase is differrent
		return constraintFactory.forEach(Task.class)
				.filter(task -> !task.getPhase().equals(task.getPrototype().getPhase()))
				.penalize("Phase Constraint", HardSoftLongScore.ONE_SOFT,task -> task.getId().intValue()-1);
	}
	
    public Constraint beforeIdealEndDate(ConstraintFactory constraintFactory) {
        // Early maintenance is expensive because the sooner maintenance is done, the sooner it needs to happen again.
        return constraintFactory.forEach(Task.class)
                .filter(task -> task.getIdealEndDate() != null
                        && task.getEndDate().isBefore(task.getIdealEndDate().plusDays(5)))
                .penalizeLong("Before ideal end date", HardSoftLongScore.ONE_SOFT
                		,task-> DAYS.between(task.getEndDate(), task.getIdealEndDate().plusDays(5)));
    }
    
    public Constraint afterIdealEndDate(ConstraintFactory constraintFactory) {
        // Late maintenance is risky because delays can push it over the due date.
        return constraintFactory.forEach(Task.class)
                .filter(task -> task.getIdealEndDate() != null
                        && task.getEndDate().isAfter(task.getIdealEndDate()))
                .penalizeLong("After ideal end date", HardSoftLongScore.ONE_SOFT,
                		task -> DAYS.between(task.getIdealEndDate(), task.getEndDate()));
    }
    
    public Constraint ordonateConstraint(ConstraintFactory constraintFactory) {
    	// Task have a dependency so the rihgt successor is required
    	return constraintFactory.forEachUniquePair(Task.class,
    				Joiners.equal(Task::getPrototype),
    				Joiners.filtering((A,B) -> 
    					( !A.equals(B) && A.getSequence() > B.getSequence() && A.getStartDate().isBefore(B.getStartDate())) ||
    					( !A.equals(B) && B.getSequence() > A.getSequence() && B.getStartDate().isBefore(A.getStartDate()))
    					))
    				.penalize("Ordonnate conflict", HardSoftLongScore.ONE_HARD);
	}
    
    public Constraint waitConstraint(ConstraintFactory constraintFactory) {
    	// never wait more than 3 days to start the next task
    	return constraintFactory.forEachUniquePair(Task.class,
    				Joiners.equal(Task::getPrototype),
    				Joiners.filtering((A,B) -> 
    					(A.getSequence() == B.getSequence()-1 && B.getStartDate().isAfter(A.getEndDate().plusDays(3))) ||
    					(B.getSequence() == A.getSequence()-1 && A.getStartDate().isAfter(B.getStartDate().plusDays(3)))
    					))
    				.penalize("Wait conflict", HardSoftLongScore.ONE_SOFT);
	}
    
    public Constraint startedConstraint(ConstraintFactory constraintFactory) {
    	// no overlapping date between task
    	return constraintFactory.forEachUniquePair(Task.class,
    				Joiners.equal(Task::getPrototype),
    				Joiners.filtering((A,B) -> 
    				( A.getSequence() < B.getSequence() && A.getEndDate().isAfter(B.getStartDate())) ||
    				(B.getSequence() < A.getSequence() && B.getEndDate().isAfter(A.getStartDate()))
    				))
    				.penalize("Start date conflict", HardSoftLongScore.ONE_HARD);
	}
    
    public Constraint teamCapacityConstraint(ConstraintFactory constraintFactory) {
    	// one team can't do more than one task at the same time
		return constraintFactory.forEachUniquePair(Task.class,
				Joiners.equal(Task::getTeam),
				Joiners.overlapping(Task::getStartDate, Task::getEndDate)).
				penalizeLong("Team Conflict", HardSoftLongScore.ONE_HARD,(task1, task2) -> DAYS.between(
						task1.getStartDate().isAfter(task2.getStartDate())
                        ? task1.getStartDate() : task2.getStartDate(),
                        task1.getEndDate().isBefore(task2.getEndDate())
                        ? task1.getEndDate() : task2.getEndDate()));			
	}
    
    public Constraint teamCompatibilityConstraint(ConstraintFactory constraintFactory) {
    	// Don't affect a team without having required skill
		return constraintFactory.forEach(Task.class).
				filter(task-> task.getSequence() != task.getTeam().getSequence()).
				penalize("Skill conflict", HardSoftLongScore.ONE_HARD);
	}
    
    public Constraint rightTeamConstraint(ConstraintFactory constraintFactory) {
		// Don't affect team to task without they have the same factory
    	return constraintFactory.forEach(Task.class).
    			filter(task-> !task.getFactory().equals(task.getTeam().getFactory())).
    			penalize("Right team conflict", HardSoftLongScore.ONE_HARD);
	}
}
