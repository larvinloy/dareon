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
    public CFPService(CFPRepository postRepository)
    {
	this.cFPRepository = postRepository;
    }

    public CFP get(Long id)
    {
	return cFPRepository.findOne(id);
    }

    public CFP save(CFP proposal)
    {
	return cFPRepository.save(proposal);
    }

    public List<CFP> list()
    {
	return cFPRepository.findAllByOrderByCreatedOnDesc();
    }

    public CFP findByTitle(String title)
    {
	return cFPRepository.findByTitle(title);
    }
}
