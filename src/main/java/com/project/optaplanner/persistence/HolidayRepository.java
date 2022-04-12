package com.project.optaplanner.persistence;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.project.optaplanner.domain.Holiday;

public interface HolidayRepository extends PagingAndSortingRepository<Holiday, Long>{

	@Override
	List<Holiday> findAll();
}
