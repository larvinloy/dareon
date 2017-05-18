package org.dareon.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import org.dareon.domain.Classification;
import org.dareon.domain.CFP;
import org.dareon.domain.Repo;

public interface ClassificationRepository extends CrudRepository<Classification, Long>
{

    Classification findById(Long id);
    Classification findByName(String name);
    List<Classification> findAllByOrderById();
   

}
