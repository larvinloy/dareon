package org.dareon.repository;

import org.springframework.data.repository.CrudRepository;

import org.dareon.domain.CallForProposals;

public interface CallForProposalsRepository extends CrudRepository<CallForProposals, Long> {

	CallForProposals findById(Long id);
	
}
