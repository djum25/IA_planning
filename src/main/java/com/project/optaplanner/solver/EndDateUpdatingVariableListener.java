package com.project.optaplanner.solver;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.optaplanner.core.api.domain.variable.VariableListener;
import org.optaplanner.core.api.score.director.ScoreDirector;
import org.springframework.beans.factory.annotation.Autowired;

import com.project.optaplanner.domain.Programme;
import com.project.optaplanner.domain.Task;
import com.project.optaplanner.service.HolidayService;


public class EndDateUpdatingVariableListener implements VariableListener<Programme, Task>{

    @Autowired
    private HolidayService holidayService;

    @Override
    public void beforeEntityAdded(ScoreDirector<Programme> scoreDirector, Task task) {
        // Do nothing
    }

    @Override
    public void afterEntityAdded(ScoreDirector<Programme> scoreDirector, Task task) {
        updateEndDate(scoreDirector, task);
    }

    @Override
    public void beforeVariableChanged(ScoreDirector<Programme> scoreDirector, Task task) {
        // Do nothing
    }

    @Override
    public void afterVariableChanged(ScoreDirector<Programme> scoreDirector, Task task) {
        updateEndDate(scoreDirector, task);
    }

    @Override
    public void beforeEntityRemoved(ScoreDirector<Programme> scoreDirector, Task task) {
        // Do nothing
    }

    @Override
    public void afterEntityRemoved(ScoreDirector<Programme> scoreDirector, Task task) {
        // Do nothing
    }

    protected void updateEndDate(ScoreDirector<Programme> scoreDirector, Task task) {
        scoreDirector.beforeVariableChanged(task, "endDate");
        task.setEndDate(calculateEndDate(task.getStartDate(), task.getDuration()));
        scoreDirector.afterVariableChanged(task, "endDate");
    }

    public  LocalDate calculateEndDate(LocalDate startDate, int durationInDays) {
        /* LocalDate endDate = startDate.plusDays(durationInDays);
        boolean ref = true;
        List<LocalDate> dates =  startDate.datesUntil(endDate).collect(Collectors.toList());
        dates.stream().filter(date-> date.getDayOfWeek()==DayOfWeek.SATURDAY || date.getDayOfWeek()==DayOfWeek.SUNDAY
        || holidayService.getHolidaysDate().contains(date)).count(); */

        if (startDate == null) {
            return null;
        } else {
            // Skip weekends. Does not work for holidays.
            // To skip holidays too, cache all working days in scoreDirector.getWorkingSolution().getWorkCalendar().
            // Keep in sync with Programme.createStartDateList().
            int weekendPadding = 2 * ((durationInDays + (startDate.getDayOfWeek().getValue() - 1)) / 5);
            return startDate.plusDays(durationInDays + weekendPadding);
        }
    }
}
