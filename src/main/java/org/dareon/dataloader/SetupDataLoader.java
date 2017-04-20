package org.dareon.dataloader;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.dareon.domain.Privilege;
import org.dareon.domain.Role;
import org.dareon.domain.User;
import org.dareon.repository.PrivilegeRepository;
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
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

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
	
	// == create cfp privileges
	final Privilege sysAdminCretePrivilege = createPrivilegeIfNotFound("SYSADMIN_CREATE_PRIVILEGE");
	final Privilege userReadPrivilege = createPrivilegeIfNotFound("USER_READ_PRIVILEGE");
	
	// == create initial roles
	final List<Privilege> sdPrivileges = Arrays.asList(sysAdminCretePrivilege);
	
	final List<Privilege> saPrivileges = Arrays.asList(repoReadPrivilege, repoCreatePrivilege,
		repoEditPrivilege,repoDeletePrivilege, cfpReadPrivilege, cfpCreatePrivilege,
		cfpEditPrivilege,cfpDeletePrivilege);
	
	final List<Privilege> roPrivileges = Arrays.asList(cfpReadPrivilege,repoReadPrivilege);
	
	createRoleIfNotFound("ROLE_SD", sdPrivileges);
	createRoleIfNotFound("ROLE_SA", saPrivileges);
	createRoleIfNotFound("ROLE_RO", roPrivileges);

	final User user = new User();
	user.setFirstName("System");
	user.setLastName("Deployer");
	user.setPassword("sd");
	user.setEmail("sd@dareon.org");
	user.setInstitution("Dareon");
	user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_SD")));
	userRepository.save(user);
	
	InitUsersRepos initUsersRepos = new InitUsersRepos(roleRepository,userRepository, repoRepository);
	initUsersRepos.initUsers();
	initUsersRepos.initRepos();
	

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