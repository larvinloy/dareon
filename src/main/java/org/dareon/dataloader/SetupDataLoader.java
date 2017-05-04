package org.dareon.dataloader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dareon.domain.ANZSRC;
import org.dareon.domain.Division;
import org.dareon.domain.Group;
import org.dareon.domain.Privilege;
import org.dareon.domain.Role;
import org.dareon.domain.User;
import org.dareon.repository.ANZSRCRepository;
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
    private ANZSRCRepository aNZSRCRepository;

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
	Division div11 = new Division("RMIT02","Advanced Materials");
	Division div12 = new Division("RMIT08", "Urban Futures");
	
	Division div21 = new Division("RMIT02","Advanced Materials");
	Division div22 = new Division("RMIT08", "Urban Futures");

	Division div31 = new Division("RMIT02","Advanced Materials");
	Division div32 = new Division("RMIT08", "Urban Futures");

	Division div41 = new Division("RMIT02","Advanced Materials");
	Division div42 = new Division("RMIT08", "Urban Futures");

	Division div51 = new Division("RMIT02","Advanced Materials");
	Division div52 = new Division("RMIT08", "Urban Futures");

	Division div61 = new Division("RMIT02","Advanced Materials");
	Division div62 = new Division("RMIT08", "Urban Futures");

	Group gr1 = new Group("RMIT1","RMIT Groups 1");
	Group gr2 = new Group("RMIT2","RMIT Groups 2");
	Group gr3 = new Group("RMIT3","RMIT Groups 3");
	Group gr4 = new Group("RMIT4","RMIT Groups 4");
	Group gr5 = new Group("RMIT5","RMIT Groups 5");
	Group gr6 = new Group("RMIT6","RMIT Groups 6");

	
	gr1.setChildren(new HashSet<ANZSRC>(Arrays.asList(div11,div12)));
	div11.setParent(gr1);
	div12.setParent(gr1);
	aNZSRCRepository.save(div11);
	aNZSRCRepository.save(div12);
	aNZSRCRepository.save(gr1);
	
	gr2.setChildren(new HashSet<ANZSRC>(Arrays.asList(div21,div22)));
	div21.setParent(gr2);
	div22.setParent(gr2);
	aNZSRCRepository.save(div21);
	aNZSRCRepository.save(div22);
	aNZSRCRepository.save(gr2);
	
	gr3.setChildren(new HashSet<ANZSRC>(Arrays.asList(div31,div32)));
	div31.setParent(gr3);
	div32.setParent(gr3);
	aNZSRCRepository.save(div31);
	aNZSRCRepository.save(div32);
	aNZSRCRepository.save(gr3);	
	
	gr4.setChildren(new HashSet<ANZSRC>(Arrays.asList(div41,div42)));
	div41.setParent(gr4);
	div42.setParent(gr4);
	aNZSRCRepository.save(div41);
	aNZSRCRepository.save(div42);
	aNZSRCRepository.save(gr4);
	
	gr5.setChildren(new HashSet<ANZSRC>(Arrays.asList(div51,div52)));
	div51.setParent(gr5);
	div52.setParent(gr5);
	aNZSRCRepository.save(div51);
	aNZSRCRepository.save(div52);
	aNZSRCRepository.save(gr5);
	
	gr6.setChildren(new HashSet<ANZSRC>(Arrays.asList(div61,div62)));
	div61.setParent(gr6);
	div62.setParent(gr6);
	aNZSRCRepository.save(div61);
	aNZSRCRepository.save(div62);
	aNZSRCRepository.save(gr6);
	
	
	
	
	
	
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