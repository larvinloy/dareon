package org.dareon.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import org.dareon.domain.CFP;
import org.dareon.domain.Field;
import org.dareon.domain.Repo;

public interface FieldRepository extends CrudRepository<Field, Long>
{

    Field findById(Long id);
    Field findByName(String name);
    List<Field> findAllByOrderById();

}
