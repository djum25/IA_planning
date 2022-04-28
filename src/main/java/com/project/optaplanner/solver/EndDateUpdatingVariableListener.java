package com.project.optaplanner.solver;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.optaplanner.core.api.domain.variable.VariableListener;
import org.optaplanner.core.api.score.director.ScoreDirector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.project.optaplanner.domain.Programme;
import com.project.optaplanner.domain.Task;
import com.project.optaplanner.service.HolidayService;

@Component
public class EndDateUpdatingVariableListener implements VariableListener<Programme, Task>{

    private static HolidayService holiday;

    @Autowired
    private HolidayService holidayService;

    @PostConstruct
    public void init() {
        EndDateUpdatingVariableListener.holiday = holidayService;
    }

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

    public static LocalDate calculateEndDate(LocalDate startDate, int durationInDays) {
        
        if (startDate == null) {
            return null;
        } else {
            LocalDate endDate = startDate.plusDays(durationInDays);
            boolean ref = true;
            List<LocalDate> dates =  startDate.datesUntil(endDate).collect(Collectors.toList());
            Long  notUse = dates.stream().filter(date-> date.getDayOfWeek()==DayOfWeek.SATURDAY || date.getDayOfWeek()==DayOfWeek.SUNDAY
            || holiday.getHolidaysDate().contains(date)).count();
            while(ref) {
                if(dates.size() < durationInDays + notUse) {
                    LocalDate newDate = dates.get(dates.size() - 1).plusDays(1);
                    dates.add(newDate);
                } else {
                    notUse = dates.stream().filter(date-> date.getDayOfWeek()==DayOfWeek.SATURDAY 
                    || date.getDayOfWeek()==DayOfWeek.SUNDAY
                    || holiday.getHolidaysDate().contains(date)).count();
                    if(dates.size() == (durationInDays + notUse)) {
                        ref = false;
                        endDate = dates.get(dates.size() - 1);
                    }
                }
            }
            return endDate;
        }

        /*if (startDate == null) {
            return null;
        } else {
            // Skip weekends. Does not work for holidays.
            // To skip holidays too, cache all working days in scoreDirector.getWorkingSolution().getWorkCalendar().
            // Keep in sync with Programme.createStartDateList().
            int weekendPadding = 2 * ((durationInDays + (startDate.getDayOfWeek().getValue() - 1)) / 5);
            return startDate.plusDays(durationInDays + weekendPadding);
        }*/
    }
}
