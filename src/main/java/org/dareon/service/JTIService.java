package org.dareon.service;

import java.util.List;

import org.dareon.domain.JTI;
import org.dareon.domain.Role;
import org.dareon.repository.JTIRepository;
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
public class JTIService
{
    
    private JTIRepository jTIRepository;
    
    @Autowired
    public JTIService(JTIRepository jTIRepository)
    {
	this.jTIRepository = jTIRepository;
    }
    
    public JTI save(String jti)
    {
	return jTIRepository.save(new JTI(jti));
    }
    
    public boolean exists(String jti)
    {
	if(jTIRepository.findByJti(jti) == null)
	    return false;
	return true;
    }

}
