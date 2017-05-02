package org.dareon.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import org.dareon.domain.ANZSRC;
import org.dareon.domain.CFP;
import org.dareon.domain.Division;
import org.dareon.domain.Field;
import org.dareon.domain.Repo;

public interface ANZSRCRepository extends CrudRepository<ANZSRC, Long>
{

    ANZSRC findById(Long id);
    ANZSRC findByName(String name);
    List<ANZSRC> findAllByOrderById();

}
