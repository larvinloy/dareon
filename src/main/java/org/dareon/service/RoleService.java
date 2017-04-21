package org.dareon.service;

import java.util.List;

import org.dareon.domain.Role;
import org.dareon.repository.RepoRepository;
import org.dareon.repository.RoleRepository;
import org.dareon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class RoleService
{
    
    private RoleRepository roleRepository;
    
    @Autowired
    public RoleService(RoleRepository roleRepository)
    {
	this.roleRepository = roleRepository;
    }
    
    public Role findByName(String name)
    {
	return roleRepository.findByName(name);
    }

    

}
