package org.dareon.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import org.dareon.domain.Repo;
import org.dareon.domain.User;

public interface UserRepository extends CrudRepository<User, Long>
{

    User findByEmail(String email);
    List<User> findAllByOrderByIdAsc();
}
