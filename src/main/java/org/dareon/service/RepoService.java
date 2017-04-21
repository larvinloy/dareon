package org.dareon.service;

import java.util.List;

import org.dareon.domain.Repo;
import org.dareon.repository.RepoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepoService
{

    private RepoRepository repoRepository;

    @Autowired
    public RepoService(RepoRepository postRepository)
    {
	this.repoRepository = postRepository;
    }

    public Repo get(Long id)
    {
	return repoRepository.findOne(id);
    }

    public Repo save(Repo repo)
    {
	return repoRepository.save(repo);
    }
    
    public void delete(Repo repo)
    {
	repoRepository.delete(repo.getId());
    }
    
    public void delete(Long id)
    {
	System.out.println(id);
	repoRepository.delete(id);
    }

    public List<Repo> list()
    {
	return repoRepository.findAllByOrderByCreatedOnDesc();
    }
    
    public Repo findByTitle(String title)
    {
	return repoRepository.findByTitle(title);
    }
    
    public Repo findById(Long Id)
    {
	return repoRepository.findById(Id);
    }

}
