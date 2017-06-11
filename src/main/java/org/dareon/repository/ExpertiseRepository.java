package org.dareon.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import org.dareon.domain.Expertise;
import org.dareon.domain.Classification;
import org.dareon.domain.CFP;
import org.dareon.domain.Repo;
import org.dareon.domain.User;

/**
 * 
 *defines Expertise repository repository extending super class CrudRepository defining create retrieve update and delete functionality
 */
public interface ExpertiseRepository extends CrudRepository<Expertise, Long>
{

    Expertise findById(Long id);
    List<Expertise> findByUser(User user);
    Expertise findByClassification(Classification classification);
    List<Expertise> findAllByOrderById();
    List<Expertise> findAllByUser(User user);
    
   

}
