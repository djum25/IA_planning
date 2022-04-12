package com.project.optaplanner.persistence;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.project.optaplanner.domain.Task;

public interface TaskRepository extends PagingAndSortingRepository<Task, Long>{

	@Override
	List<Task> findAll();
}
