package com.project.optaplanner.persistence;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.project.optaplanner.domain.Team;

public interface TeamRepository extends PagingAndSortingRepository<Team, Long>{

	@Override
	List<Team> findAll();
}
