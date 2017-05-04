package org.dareon.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import org.dareon.domain.Group;
import org.dareon.domain.CFP;
import org.dareon.domain.Division;
import org.dareon.domain.Field;
import org.dareon.domain.Group;
import org.dareon.domain.Repo;

public interface GroupRepository extends CrudRepository<Group, Long>
{

    Group findById(Long id);
    Group findByName(String name);
    List<Group> findAllByOrderById();
}
