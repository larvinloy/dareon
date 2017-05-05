package org.dareon.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import org.dareon.domain.Level;
import org.dareon.domain.CFP;
import org.dareon.domain.Repo;

public interface LevelRepository extends CrudRepository<Level, Long>
{

    Level findById(Long id);
    Level findByName(String name);
    List<Level> findAllByOrderById();

}
