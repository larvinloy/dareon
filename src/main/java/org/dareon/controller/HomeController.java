package org.dareon.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.dareon.domain.CFP;
import org.dareon.domain.Repo;
import org.dareon.domain.User;
import org.dareon.service.CFPService;
import org.dareon.service.RepoService;
import org.dareon.service.UserDetailsImpl;
import org.dareon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties.Session;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.security.authentication.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class HomeController
{

    private RepoService repoService;
    private UserService userService;
    private CFPService cFPService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuth(AuthenticationManagerBuilder auth) throws Exception
    {
	auth.userDetailsService(userDetailsService);
    }

    @Autowired
    public HomeController(RepoService repoService, UserService userService,
	    CFPService cFPService)
    {
	super();
	this.repoService = repoService;
	this.userService = userService;
	this.cFPService = cFPService;
    }

    @RequestMapping("/")
    public String home()
    {
	return "index";
    }

    @PreAuthorize("hasAuthority('REPO_CREATE_PRIVILEGE')")
    @RequestMapping("/repo/create")
    public String repoCreate(Model model)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	model.addAttribute("repo", new Repo());
	List<User> users = userService.list();
	users.remove((userService.findByEmail(auth.getName())));
	users.add(0,userService.findByEmail(auth.getName()));
	model.addAttribute("users",users);
	return "repo/create";
    }

    @PreAuthorize("hasAuthority('REPO_CREATE_PRIVILEGE') OR isRepoOwner(#repo)")
    @RequestMapping(value = "/repo/create", method = RequestMethod.POST)
    public String repoSave(@ModelAttribute Repo repo)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	if(repoService.findById(repo.getId()) == null)
	    repo.setCreator(userService.findByEmail(auth.getName()));
	
	Repo savedRepo = repoService.save(repo);
	
	Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), auth.getAuthorities());
	SecurityContextHolder.getContext().setAuthentication(newAuth);
	
	return "redirect:read/" + savedRepo.getId();
    }
    
    @PreAuthorize("hasAuthority('REPO_CREATE_PRIVILEGE') OR isRepoOwner(#id)")
    @RequestMapping("/repo/edit/{id}")
    public String repoEdit(@PathVariable Long id, Model model)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	model.addAttribute("repo", new Repo());
	List<User> users = userService.list();
	users.remove((userService.findByEmail(auth.getName())));
	model.addAttribute("repo", repoService.findById(id));
	users.add(0,userService.findByEmail(auth.getName()));
	model.addAttribute("users",users);
	return "repo/create";
    }

    @PreAuthorize("hasAuthority('REPO_READ_PRIVILEGE')")
    @RequestMapping("/repo/read/{id}")
    public String repoRead(@PathVariable Long id, Model model)
    {
	model.addAttribute("repo", repoService.findById(id));
	return "repo/read";
    }
    
    @PreAuthorize("hasAuthority('REPO_DELETE_PRIVILEGE')")
    @RequestMapping("/repo/delete/{id}")
    public String repoDelete(@PathVariable Long id, Model model)
    {
	model.addAttribute("repo", repoService.findById(id));
	return "repo/delete";
    }
    
    @PreAuthorize("hasAuthority('REPO_DELETE_PRIVILEGE')")
    @RequestMapping("/repo/deleteconfirmed/{id}")
    public RedirectView repoDeleteConfirmed(@PathVariable Long id, Model model)
    {
	System.out.println(id);
	repoService.delete(id);
	 return new RedirectView("/repo/list");
    }
    
    @PreAuthorize("hasAuthority('REPO_READ_PRIVILEGE')")
    @RequestMapping("/repo/list")
    public String repoList(Model model)
    {
	model.addAttribute("repos", repoService.list());
	return "repo/list";
    }

    @PreAuthorize("hasAuthority('CFP_CREATE_PRIVILEGE')")
    @RequestMapping("/callforproposals/create")
    public String callForProposalsCreate(Model model)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	model.addAttribute("repos", userService.findByEmail(auth.getName()).getCreatedRepos());
	model.addAttribute("callForProposals", new CFP());
	return "callforproposals/create";
    }

    @PreAuthorize("hasAuthority('CFP_CREATE_PRIVILEGE')")
    @RequestMapping(value = "/callforproposals/create", method = RequestMethod.POST)
    public String proposalSave(@ModelAttribute CFP cFP)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	// UserDetailsImpl u = (UserDetailsImpl)auth.getPrincipal();
	// if (callForProposals.getUser() == null)
	// callForProposals.setUser(repoService.findByTitle(title)));
	// return callForProposals.getRepo().toString();
	CFP savedCallForProposals = cFPService.save(cFP);

	return "redirect:read/" + savedCallForProposals.getTitle();
    }

    @PreAuthorize("hasAuthority('CFP_EDIT_PRIVILEGE')")
    @RequestMapping("/callforproposals/edit/{title}")
    public String callForProposalsEdit(@PathVariable String title, Model model)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	model.addAttribute("repos", userService.findByEmail(auth.getName()).getCreatedRepos());
	model.addAttribute("callForProposals", cFPService.findByTitle(title));
	return "callforproposals/create";
    }
    
    @PreAuthorize("hasAuthority('CFP_CREATE_PRIVILEGE')")
    @RequestMapping("/callforproposals/read/{title}")
    public String callForProposalsRead(@PathVariable String title, Model model)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	model.addAttribute("repos", userService.findByEmail(auth.getName()).getCreatedRepos());
	model.addAttribute("callForProposals", cFPService.findByTitle(title));
	return "callforproposals/read";
    }

    @PreAuthorize("hasAuthority('CFP_READ_PRIVILEGE')")
    @RequestMapping("/callforproposals/list")
    public String callForProposalsList(Model model)
    {
	model.addAttribute("callsForProposals", cFPService.list());
	return "callforproposals/list";
    }
    
    @PreAuthorize("hasAuthority('SYSADMIN_CREATE_PRIVILEGE')")
    @RequestMapping("/user/sysadmin")
    public String sysAdminCreate(Model model)
    {
	model.addAttribute("sysAdmin",new User());
	return "user/sysadmin";
	
    }
    
    @PreAuthorize("hasAuthority('SYSADMIN_CREATE_PRIVILEGE')")
    @RequestMapping(value = "/user/sysadmin", method = RequestMethod.POST)
    public String sysAdminSave(@ModelAttribute User sysAdmin)
    {
	User newUser = userService.save(sysAdmin);
	return "redirect:read/" + newUser.getEmail();
	
    }
    
    @PreAuthorize("hasAuthority('USER_READ_PRIVILEGE')")
    @RequestMapping("/user/read/{email}")
    public String userRead(@PathVariable String email, Model model)
    {
	model.addAttribute("user",userService.findByEmail(email));
	return "user/read";
	
    } 
   
}