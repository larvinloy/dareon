package org.dareon.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import org.dareon.domain.FOR;
import org.dareon.domain.CFP;
import org.dareon.domain.Repo;

public interface FORRepository extends CrudRepository<FOR, Long>
{

    FOR findById(Long id);
    FOR findByName(String name);
    List<FOR> findAllByOrderById();
   

}
