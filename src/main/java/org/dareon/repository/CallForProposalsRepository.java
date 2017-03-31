package org.dareon.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

import org.dareon.domain.CallForProposals;
import org.dareon.domain.Repo;

public interface CallForProposalsRepository extends CrudRepository<CallForProposals, Long>
{

    CallForProposals findById(Long id);
    CallForProposals findByTitle(String title);
    List<CallForProposals> findAllByOrderByCreatedOnDesc();

}
