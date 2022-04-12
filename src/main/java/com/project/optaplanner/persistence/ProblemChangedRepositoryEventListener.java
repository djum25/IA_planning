package com.project.optaplanner.persistence;


import org.optaplanner.core.api.solver.SolverStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.project.optaplanner.domain.Prototype;
import com.project.optaplanner.domain.Task;
import com.project.optaplanner.rest.ProgrammeController;

@Component
@RepositoryEventHandler
public class ProblemChangedRepositoryEventListener {

	@Autowired
	private ProgrammeController programmeController;
	
	@HandleBeforeCreate
    @HandleBeforeSave
    @HandleBeforeDelete
    private void workCalendarCreateSaveDelete(WorkCalendarRepository workCalendar) {
        assertNotSolving();
    }
	


    @HandleBeforeCreate
    @HandleBeforeSave
    @HandleBeforeDelete
    private void prototypeCreateSaveDelete(Prototype prototype) {
        assertNotSolving();
    }

    @HandleBeforeCreate
    @HandleBeforeSave
    @HandleBeforeDelete
    private void taskCreateSaveDelete(Task task) {
        assertNotSolving();
    }

    public void assertNotSolving() {
        // TODO Race condition: if a timeTableSolverService.solve() call arrives concurrently,
        // the solver might start before the CRUD transaction completes. That's not very harmful, though.
        if (programmeController.getSolverStatus() != SolverStatus.NOT_SOLVING) {
            throw new IllegalStateException("The solver is solving. Please stop solving first.");
        }
    }
}
