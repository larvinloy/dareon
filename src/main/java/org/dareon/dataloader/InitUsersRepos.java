package org.dareon.dataloader;

import java.util.Arrays;

import org.dareon.domain.Repo;
import org.dareon.domain.User;
import org.dareon.repository.PrivilegeRepository;
import org.dareon.repository.RepoRepository;
import org.dareon.repository.RoleRepository;
import org.dareon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class InitUsersRepos
{

  
    private UserRepository userRepository;

 
    private RoleRepository roleRepository;
    

    private RepoRepository repoRepository;
    
    public InitUsersRepos(RoleRepository roleRepository2, UserRepository userRepository2, RepoRepository repoRepository2)
    {
	this.roleRepository = roleRepository2;
	this.userRepository = userRepository2;
	this.repoRepository = repoRepository2;
    }

    public void initUsers()
    {
	final User user1 = new User();
	user1.setFirstName("System");
	user1.setLastName("Admin");
	user1.setPassword("admin");
	user1.setEmail("admin@dareon.org");
	user1.setInstitution("Dareon");
	user1.setRoles(Arrays.asList(roleRepository.findByName("ROLE_SA")));
	userRepository.save(user1);
	
	final User user2 = new User();
	user2.setFirstName("Repo");
	user2.setLastName("Owner");
	user2.setPassword("repoowner");
	user2.setEmail("repoowner@rmit.edu.au");
	user2.setInstitution("RMIT");
	user2.setRoles(Arrays.asList(roleRepository.findByName("ROLE_RO")));
	userRepository.save(user2);
    }
    
    public void initRepos()
    {
	final Repo repo1 = new Repo();
	repo1.setTitle("Test Title 1");
	repo1.setDefinition("Test Definition 1");
	repo1.setDescription("Test Description 1");
	repo1.setInstitution("Test Institution 1");
	repo1.setCreator(userRepository.findByEmail("admin@dareon.org"));
	repo1.setOwner(userRepository.findByEmail("admin@dareon.org"));
	repoRepository.save(repo1);
	
	final Repo repo2 = new Repo();
	repo2.setTitle("Test Title 2");
	repo2.setDefinition("Test Definition 2");
	repo2.setDescription("Test Description 2");
	repo2.setInstitution("Test Institution 2");
	repo2.setCreator(userRepository.findByEmail("admin@dareon.org"));
	repo2.setOwner(userRepository.findByEmail("repoowner@rmit.edu.au"));
	repoRepository.save(repo2);
    }
}
