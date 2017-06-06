package org.dareon.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import org.dareon.domain.Repo;
import org.dareon.domain.User;
/**
 * 
 * @author Ayush Garg
 *defines User repository interface extending  CrudRepository defining create retrieve update and delete functionality
 */
public interface UserRepository extends CrudRepository<User, Long>
{

    User findByEmail(String email);
    List<User> findAllByOrderByIdAsc();
}
