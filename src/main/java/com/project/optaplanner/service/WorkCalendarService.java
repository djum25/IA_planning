package com.project.optaplanner.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.project.optaplanner.domain.WorkCalendar;
import com.project.optaplanner.persistence.WorkCalendarRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkCalendarService {
    
    @Autowired
    private WorkCalendarRepository workCalendarRepository;
    @Autowired
    private HolidayService holidayService;

    public List<LocalDate> generateWorkingDay(){
        WorkCalendar workCalendar = workCalendarRepository.findAll().get(0);
        List<LocalDate> allDays = workCalendar.getFromDate().datesUntil(workCalendar.getToDate())
        .collect(Collectors.toList());
        List<LocalDate> holidays = holidayService.getHolidaysDate();
        List<LocalDate> weekDay = allDays.stream().filter(day-> !day.getDayOfWeek().equals(DayOfWeek.SATURDAY)
        && !day.getDayOfWeek().equals(DayOfWeek.SUNDAY)).collect(Collectors.toList());
        List<LocalDate> workingDay = weekDay.stream().filter(day-> !holidays.contains(day))
        .collect(Collectors.toList());
        return workingDay;
    }
}
