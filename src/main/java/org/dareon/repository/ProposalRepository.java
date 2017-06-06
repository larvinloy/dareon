package org.dareon.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import org.dareon.domain.CFP;
import org.dareon.domain.Proposal;
import org.dareon.domain.Repo;
/**
 * 
 * @author Ayush Garg
 *defines Proposal repository extending CrudRepository defining create retrieve update and delete functionality
 */
public interface ProposalRepository extends CrudRepository<Proposal, Long>
{

    Proposal findById(Long id);
    Proposal findByTitle(String title);
    List<Proposal> findAllByOrderByCreatedOnDesc();

}
