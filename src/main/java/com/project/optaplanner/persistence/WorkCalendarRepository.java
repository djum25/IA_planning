package com.project.optaplanner.persistence;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.project.optaplanner.domain.WorkCalendar;

public interface WorkCalendarRepository extends PagingAndSortingRepository<WorkCalendar, Long>{

	@Override
	List<WorkCalendar> findAll();
}
