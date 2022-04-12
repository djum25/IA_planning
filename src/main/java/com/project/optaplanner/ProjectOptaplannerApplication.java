package com.project.optaplanner;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.project.optaplanner.domain.Team;
import com.project.optaplanner.domain.WorkCalendar;
import com.project.optaplanner.persistence.TeamRepository;
import com.project.optaplanner.persistence.WorkCalendarRepository;


@SpringBootApplication
public class ProjectOptaplannerApplication {
	
	LocalDate fromDate = LocalDate.parse("2022-01-01");
    LocalDate toDate = LocalDate.parse("2022-12-31");

	public static void main(String[] args) {
		SpringApplication.run(ProjectOptaplannerApplication.class, args);
	}
	
	@Bean
    public CommandLineRunner demoData(WorkCalendarRepository workCalendarRepository,TeamRepository teamRepository) {
		return (args) -> {
            workCalendarRepository.save(new WorkCalendar(fromDate, toDate));
            
            teamRepository.save(new Team("M11","M11",11));
            teamRepository.save(new Team("M11","M11",11));
		};
	}

}
