package org.dareon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

import org.dareon.domain.Repo;
import org.dareon.domain.User;

public interface RepoRepository extends CrudRepository<Repo, Long>
{

    Repo findById(Long id);
    Repo findByTitle(String title);
    List<Repo> findAllByOrderByCreatedOnDesc();
    List<Repo> findAllByOwnerAndStatusOrderByCreatedOnDesc(User owner, Boolean status);
    List<Repo> findAllByStatusOrderByCreatedOnDesc(Boolean status);

}
