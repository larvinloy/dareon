package org.dareon.service;

import java.util.List;

import org.dareon.domain.CFP;
import org.dareon.domain.Proposal;
import org.dareon.domain.Repo;
import org.dareon.repository.CFPRepository;
import org.dareon.repository.ProposalRepository;
import org.dareon.repository.RepoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProposalService
{

    private ProposalRepository proposalRepository;

    @Autowired
    public ProposalService(ProposalRepository proposalRepository)
    {
	this.proposalRepository = proposalRepository;
    }

    public Proposal get(Long id)
    {
	return proposalRepository.findOne(id);
    }

    public Proposal save(Proposal proposal)
    {
	Proposal tempProposal = proposalRepository.findById(proposal.getId());
	if(tempProposal == null)
	    return proposalRepository.save(proposal);
	else
	{
	    Proposal editProposal = new Proposal();
	    editProposal.setId(proposal.getId());
	    editProposal.setDescription(proposal.getDescription());
	    editProposal.setDetails(proposal.getDetails());
	    editProposal.setCfp(tempProposal.getCfp());
	    editProposal.setCreatedOn(tempProposal.getCreatedOn());
	    editProposal.setTitle(proposal.getTitle());
	    return proposalRepository.save(editProposal);
	}
    }

    public List<Proposal> list()
    {
	return proposalRepository.findAllByOrderByCreatedOnDesc();
    }

    public Proposal findByTitle(String title)
    {
	return proposalRepository.findByTitle(title);
    }

    public Proposal findById(Long id)
    {
	// TODO Auto-generated method stub
	return proposalRepository.findById(id);
    }

    public void delete(Long id)
    {
	// TODO Auto-generated method stub
	proposalRepository.delete(id);
	
    }
}
