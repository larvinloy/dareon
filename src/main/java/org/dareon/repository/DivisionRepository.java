package org.dareon.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import org.dareon.domain.CFP;
import org.dareon.domain.Division;
import org.dareon.domain.Field;
import org.dareon.domain.Repo;

public interface DivisionRepository extends CrudRepository<Division, Long>
{

    Division findById(Long id);
    Division findByName(String name);
    List<Division> findAllByOrderById();

}
