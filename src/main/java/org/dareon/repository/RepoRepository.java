package org.dareon.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import org.dareon.domain.Repo;

public interface RepoRepository extends CrudRepository<Repo, Long>
{

    Repo findById(Long id);

    List<Repo> findAllByOrderByCreatedOnDesc();

}
