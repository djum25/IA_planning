package com.project.optaplanner.persistence;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.project.optaplanner.domain.Resource;

public interface ResourceRepository extends PagingAndSortingRepository<Resource, Long>{

	@Override
	List<Resource> findAll();
}
