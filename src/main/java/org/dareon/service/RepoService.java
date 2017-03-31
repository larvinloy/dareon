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

    public Repo save(Repo post)
    {
	return repoRepository.save(post);
    }

    public List<Repo> list()
    {
	return repoRepository.findAllByOrderByCreatedOnDesc();
    }
    
    public Repo findByTitle(String title)
    {
	return repoRepository.findByTitle(title);
    }

}
