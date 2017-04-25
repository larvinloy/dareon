package org.dareon.service;

import java.util.List;

import org.dareon.domain.CFP;
import org.dareon.domain.Repo;
import org.dareon.repository.CFPRepository;
import org.dareon.repository.RepoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CFPService
{

    private CFPRepository cFPRepository;

    @Autowired
    public CFPService(CFPRepository cFPRepository)
    {
	this.cFPRepository = cFPRepository;
    }

    public CFP get(Long id)
    {
	return cFPRepository.findOne(id);
    }

    public CFP save(CFP proposal)
    {
	CFP tempCFP = cFPRepository.findById(proposal.getId());
	if(tempCFP == null)
	    return cFPRepository.save(proposal);
	else
	{
	    CFP editCFP = new CFP();
	    editCFP.setId(proposal.getId());
	    editCFP.setDescription(proposal.getDescription());
	    editCFP.setDetails(proposal.getDetails());
	    editCFP.setRepo(tempCFP.getRepo());
	    editCFP.setCreatedOn(tempCFP.getCreatedOn());
	    editCFP.setTitle(proposal.getTitle());
	    return cFPRepository.save(editCFP);
	}
    }

    public List<CFP> list()
    {
	return cFPRepository.findAllByOrderByCreatedOnDesc();
    }

    public CFP findByTitle(String title)
    {
	return cFPRepository.findByTitle(title);
    }

    public CFP findById(Long id)
    {
	// TODO Auto-generated method stub
	return cFPRepository.findById(id);
    }

    public void delete(Long id)
    {
	// TODO Auto-generated method stub
	cFPRepository.delete(id);
	
    }
}
