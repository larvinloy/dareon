package org.dareon.controller;

import java.util.ArrayList;
import java.util.Arrays;
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
public class RepoController
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
    public RepoController(RepoService repoService, UserService userService,
	    CFPService cFPService)
    {
	super();
	this.repoService = repoService;
	this.userService = userService;
	this.cFPService = cFPService;
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
}