package org.dareon.dataloader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dareon.domain.FOR;
import org.dareon.domain.Level;
import org.dareon.domain.Privilege;
import org.dareon.domain.Role;
import org.dareon.domain.User;
import org.dareon.repository.FORRepository;
import org.dareon.repository.LevelRepository;
import org.dareon.repository.CFPRepository;
import org.dareon.repository.PrivilegeRepository;
import org.dareon.repository.ProposalRepository;
import org.dareon.repository.RepoRepository;
import org.dareon.repository.RoleRepository;
import org.dareon.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent>
{

    private boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RepoRepository repoRepository;
    
    @Autowired
    private CFPRepository cfpRepository;  
    
    @Autowired
    private ProposalRepository proposalRepository;   

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;
    
    @Autowired
    private FORRepository fORRepository;
    
    @Autowired
    private LevelRepository levelRepository;

    // API

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event)
    {
	if (alreadySetup)
	{
	    return;
	}

	// == create repo privileges
	final Privilege repoReadPrivilege = createPrivilegeIfNotFound("REPO_READ_PRIVILEGE");
	final Privilege repoCreatePrivilege = createPrivilegeIfNotFound("REPO_CREATE_PRIVILEGE");
	final Privilege repoEditPrivilege = createPrivilegeIfNotFound("REPO_EDIT_PRIVILEGE");
	final Privilege repoDeletePrivilege = createPrivilegeIfNotFound("REPO_DELETE_PRIVILEGE");

	// == create cfp privileges
	final Privilege cfpReadPrivilege = createPrivilegeIfNotFound("CFP_READ_PRIVILEGE");
	final Privilege cfpCreatePrivilege = createPrivilegeIfNotFound("CFP_CREATE_PRIVILEGE");
	final Privilege cfpEditPrivilege = createPrivilegeIfNotFound("CFP_EDIT_PRIVILEGE");
	final Privilege cfpDeletePrivilege = createPrivilegeIfNotFound("CFP_DELETE_PRIVILEGE");

	// == create proposal privileges
	final Privilege proposalReadPrivilege = createPrivilegeIfNotFound("PROPOSAL_READ_PRIVILEGE");
	final Privilege proposalCreatePrivilege = createPrivilegeIfNotFound("PROPOSAL_CREATE_PRIVILEGE");
	final Privilege proposalEditPrivilege = createPrivilegeIfNotFound("PROPOSAL_EDIT_PRIVILEGE");
	final Privilege proposalDeletePrivilege = createPrivilegeIfNotFound("PROPOSAL_DELETE_PRIVILEGE");

	// == create sysadmin privileges
	final Privilege sysAdminCretePrivilege = createPrivilegeIfNotFound("SYSADMIN_CREATE_PRIVILEGE");
	final Privilege userReadPrivilege = createPrivilegeIfNotFound("USER_READ_PRIVILEGE");

	// == create initial roles
	final List<Privilege> sdPrivileges = Arrays.asList(sysAdminCretePrivilege);

	final List<Privilege> saPrivileges = Arrays.asList(repoReadPrivilege, repoCreatePrivilege, repoEditPrivilege,
		repoDeletePrivilege, cfpReadPrivilege, cfpCreatePrivilege, cfpEditPrivilege, cfpDeletePrivilege,
		proposalCreatePrivilege, proposalDeletePrivilege, proposalEditPrivilege, proposalReadPrivilege);

	final List<Privilege> roPrivileges = Arrays.asList(cfpReadPrivilege, cfpCreatePrivilege, cfpEditPrivilege,
		cfpDeletePrivilege, proposalDeletePrivilege, proposalReadPrivilege, repoReadPrivilege);
	
	final List<Privilege> doPrivileges = Arrays.asList(proposalReadPrivilege, proposalCreatePrivilege, proposalEditPrivilege,
			proposalDeletePrivilege, cfpReadPrivilege);

	createRoleIfNotFound("ROLE_SD", sdPrivileges);
	createRoleIfNotFound("ROLE_SA", saPrivileges);
	createRoleIfNotFound("ROLE_RO", roPrivileges);
	createRoleIfNotFound("ROLE_DO", doPrivileges);

	final User user = new User();
	user.setFirstName("System");
	user.setLastName("Deployer");
	user.setPassword("sd");
	user.setEmail("sd@dareon.org");
	user.setInstitution("Dareon");
	user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_SD")));
	userRepository.save(user);

	InitUsersRepos initUsersRepos = new InitUsersRepos(roleRepository, userRepository, repoRepository, cfpRepository, proposalRepository);
	initUsersRepos.initUsers();
	initUsersRepos.initRepos();
	initUsersRepos.initCFPs();
	initUsersRepos.initProposals();
	
	// WARNING!
	
	Level group = new Level("GROUP");
	levelRepository.save(group);
	
	Level division = new Level("DIVISION");
	levelRepository.save(division);
	
	Level field = new Level("FIELD");
	levelRepository.save(field);
	
	
	FOR for1 = new FOR("RMIT02","Advanced Materials",levelRepository.findByName("DIVISION"));
	FOR for2 = new FOR("RMIT08", "Urban Futures",levelRepository.findByName("DIVISION"));
	FOR for3 = new FOR("RMIT","RMIT Group",levelRepository.findByName("GROUP"));
	FOR for4 = new FOR("RMITG","Another RMIT Group",levelRepository.findByName("GROUP"));
	FOR for5 = new FOR("RMITD","Another RMIT DIVISION",levelRepository.findByName("DIVISION"));
	FOR for6 = new FOR("RMITF","Another RMIT FILED",levelRepository.findByName("FIELD"));
	for3.setChildren(new HashSet<FOR>(Arrays.asList(for1,for2)));
	for1.setParent(for3);
	for2.setParent(for3);
	for6.setParent(for5);
	for5.setParent(for4);
	fORRepository.save(for3);
	fORRepository.save(for1);
	fORRepository.save(for2);
	fORRepository.save(for4);
	fORRepository.save(for5);
	fORRepository.save(for6);
	
	
	//

	alreadySetup = true;
    }

    @Transactional
    private final Privilege createPrivilegeIfNotFound(final String name)
    {
	Privilege privilege = privilegeRepository.findByName(name);
	if (privilege == null)
	{
	    privilege = new Privilege(name);
	    privilegeRepository.save(privilege);
	}
	return privilege;
    }

    @Transactional
    private final Role createRoleIfNotFound(final String name, final Collection<Privilege> privileges)
    {
	Role role = roleRepository.findByName(name);
	if (role == null)
	{
	    role = new Role(name);
	    role.setPrivileges(privileges);
	    roleRepository.save(role);
	}
	return role;
    }

}