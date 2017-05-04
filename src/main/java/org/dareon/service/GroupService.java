package org.dareon.service;

import static org.hamcrest.CoreMatchers.instanceOf;

import java.util.List;

import org.dareon.domain.Group;
import org.dareon.domain.Repo;
import org.dareon.repository.GroupRepository;
import org.dareon.repository.RepoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService
{

    private GroupRepository GroupRepository;

    @Autowired
    public GroupService(GroupRepository GroupRepository)
    {
	this.GroupRepository = GroupRepository;
    }

    public Group get(Long id)
    {
	return GroupRepository.findOne(id);
    }

    public Group save(Group Group)
    {
	return GroupRepository.save(Group);
	
    }

    public List<Group> list()
    {
	return GroupRepository.findAllByOrderById();

    }


    public Group findById(Long id)
    {
	return GroupRepository.findById(id);
    }

    public void delete(Long id)
    {
	// TODO Auto-generated method stub
	GroupRepository.delete(id);
	
    }
}
