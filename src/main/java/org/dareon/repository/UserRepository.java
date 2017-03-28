package org.dareon.repository;

import org.springframework.data.repository.CrudRepository;

import org.dareon.domain.User;

public interface UserRepository extends CrudRepository<User, Long>
{

    User findByEmail(String email);

}
