package org.dareon.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import org.dareon.domain.CFP;
import org.dareon.domain.Repo;

public interface CFPRepository extends CrudRepository<CFP, Long>
{

    CFP findById(Long id);
    CFP findByTitle(String title);
    List<CFP> findAllByOrderByCreatedOnDesc();

}
