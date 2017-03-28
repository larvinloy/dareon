package org.dareon.service;

import java.util.List;

import org.dareon.domain.CallForProposals;
import org.dareon.domain.Repo;
import org.dareon.repository.CallForProposalsRepository;
import org.dareon.repository.RepoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CallForProposalsService {

	private CallForProposalsRepository callForProposalsRepository;
	
	@Autowired
	public CallForProposalsService(CallForProposalsRepository postRepository){
		this.callForProposalsRepository = postRepository;
	}


	public CallForProposals get(Long id) {
		return callForProposalsRepository.findOne(id);
	}

	public CallForProposals save(CallForProposals proposal) {
		return callForProposalsRepository.save(proposal);
	}
	
	 public List<CallForProposals> list()
	    {
		return callForProposalsRepository.findAllByOrderByCreatedOnDesc();
	    }
	
}
