package org.dareon.repository;

import org.springframework.data.repository.CrudRepository;

import org.dareon.domain.Repo;

public interface RepoRepository extends CrudRepository<Repo, Long> {

	Repo findById(Long id);
	
}
