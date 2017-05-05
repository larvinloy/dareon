package org.dareon.service;

import java.util.List;

import org.dareon.domain.Level;
import org.dareon.domain.Repo;
import org.dareon.repository.LevelRepository;
import org.dareon.repository.RepoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LevelService
{

    private LevelRepository levelRepository;

    @Autowired
    public LevelService(LevelRepository levelRepository)
    {
	this.levelRepository = levelRepository;
    }

    public Level get(Long id)
    {
	return levelRepository.findOne(id);
    }

    public Level save(Level level)
    {
	return levelRepository.save(level);
	
    }

    public List<Level> list()
    {
	List<Level> data = levelRepository.findAllByOrderById();
	return data;

    }


    public Level findById(Long id)
    {
	return levelRepository.findById(id);
    }

    public void delete(Long id)
    {
	// TODO Auto-generated method stub
	levelRepository.delete(id);
	
    }
}
