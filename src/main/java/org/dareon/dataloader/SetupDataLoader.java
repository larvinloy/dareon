package org.dareon.dataloader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dareon.domain.FOR;
import org.dareon.domain.Privilege;
import org.dareon.domain.Role;
import org.dareon.domain.User;
import org.dareon.repository.FORRepository;
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
	
	
	FOR for1 = new FOR("01","MATHEMATICAL SCIENCES");
	FOR for2 = new FOR("0101", "PURE MATHEMATICS");
	FOR for3 = new FOR("0102","APPLIED MATHEMATICS");
	FOR for4 = new FOR("0103","NUMERICAL AND COMPUTATIONAL MATHEMATICS");
	FOR for5 = new FOR("0104","STATISTICS");
	FOR for6 = new FOR("0105","MATHEMATICAL PHYSICS");
	FOR for7 = new FOR("0199","OTHER MATHEMATICAL SCIENCES");
	for1.setChildren( Arrays.asList(for1,for2,for3,for4,for5,for6,for7));
	for2.setParent(for1);
	for3.setParent(for1);
	for4.setParent(for1);
	for5.setParent(for1);
	for6.setParent(for1);
	for7.setParent(for1);
	
	
	
	
	FOR for8 = new FOR("010501","Algebraic Structures in Mathematical Physics");
	FOR for9 = new FOR("010502","Integrable Systems (Classical and Quantum)");
	FOR for10 = new FOR("010503","Mathematical Aspects of Classical Mechanics, Quantum Mechanics and Quantum Information Theory");
	FOR for11 = new FOR("010504","Mathematical Aspects of General Relativity");
	FOR for12 = new FOR("010506","Statistical Mechanics, Physical Combinatorics and Mathematical Aspects of Condensed Matter ");
	FOR for13 = new FOR("010599","Mathematical Physics not elsewhere classified ");
	
	for6.setChildren(Arrays.asList(for8,for9,for10,for11,for12,for13));
	for8.setParent(for6);
	for9.setParent(for6);
	for10.setParent(for6);
	for11.setParent(for6);
	for12.setParent(for6);
	for13.setParent(for6);
	
	
	FOR for14 = new FOR("02","PHYSICAL SCIENCES");
	FOR for15 = new FOR("03","CHEMICAL SCIENCES");
	FOR for16 = new FOR("04","EARTH SCIENCES");
	FOR for17 = new FOR("05","ENVIRONMENTAL SCIENCES");
	FOR for18 = new FOR("06","BIOLOGICAL SCIENCES");
	FOR for19 = new FOR("07","AGRICULTURAL AND VETERINARY SCIENCES");
	FOR for20 = new FOR("08","INFORMATION AND COMPUTING SCIENCES");
	FOR for21 = new FOR("09","ENGINEERING");
	FOR for22 = new FOR("10","TECHNOLOGY");
	
	fORRepository.save(for1);
	fORRepository.save(for2);
	fORRepository.save(for3);
	fORRepository.save(for4);
	fORRepository.save(for5);
	fORRepository.save(for6);
	fORRepository.save(for7);
	fORRepository.save(for8);
	fORRepository.save(for9);
	fORRepository.save(for10);
	fORRepository.save(for11);
	fORRepository.save(for12);
	fORRepository.save(for13);
	fORRepository.save(for14);
	fORRepository.save(for15);
	fORRepository.save(for16);
	fORRepository.save(for17);
	fORRepository.save(for18);
	fORRepository.save(for19);
	fORRepository.save(for20);
	fORRepository.save(for21);
	fORRepository.save(for22);
//
//	fORRepository.save(for8);
//	fORRepository.save(for9);
//	fORRepository.save(for10);
//	fORRepository.save(for11);
//	fORRepository.save(for12);
//	fORRepository.save(for13);
//	fORRepository.save(for14);
//	fORRepository.save(for15);
//	fORRepository.save(for16);
//	fORRepository.save(for17);
//	fORRepository.save(for18);
//	fORRepository.save(for19);
	
	
	
	
	
	
	
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