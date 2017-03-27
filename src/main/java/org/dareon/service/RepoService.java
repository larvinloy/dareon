package org.dareon.service;

import java.util.List;

import org.dareon.domain.Repo;
import org.dareon.repository.RepoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepoService {

	private RepoRepository postRepository;
	
	@Autowired
	public RepoService(RepoRepository postRepository){
		this.postRepository = postRepository;
	}


	public Repo get(Long id) {
		return postRepository.findOne(id);
	}

	public Repo save(Repo post) {
		return postRepository.save(post);
	}
	
}
