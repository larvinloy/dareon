package org.dareon.service;

import java.util.ArrayList;
import java.util.List;

import org.dareon.domain.FOR;
import org.dareon.domain.Repo;
import org.dareon.repository.FORRepository;
import org.dareon.repository.RepoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FORService
{

    private FORRepository fORRepository;

    @Autowired
    public FORService(FORRepository fORRepository)
    {
	this.fORRepository = fORRepository;
    }

    public FOR get(Long id)
    {
	return fORRepository.findOne(id);
    }

    public FOR save(FOR fOR)
    {
    	
	return fORRepository.save(fOR);
	
    }

    public List<FOR> list()
    {
	List<FOR> data = fORRepository.findAllByOrderById();
	List<FOR> factoredData = new ArrayList<FOR>();
	for(FOR e : data)
	{
	    if(e.getParent() == null)
	    {
		factoredData.add(e);
	    }
	}
	return factoredData;

    }
    
   


    public FOR findById(Long id)
    {
	return fORRepository.findById(id);
    }

    public void delete(Long id)
    {
	// TODO Auto-generated method stub
	fORRepository.delete(id);
	
    }
}
