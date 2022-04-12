package com.project.optaplanner.persistence;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.project.optaplanner.domain.Prototype;

public interface PrototypeRepository extends PagingAndSortingRepository<Prototype, Long>{

	@Override
	List<Prototype> findAll();
}
