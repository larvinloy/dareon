package org.dareon.controller;

import java.util.ArrayList;
import java.util.List;

import org.dareon.domain.CallForProposals;
import org.dareon.domain.Repo;
import org.dareon.domain.User;
import org.dareon.service.CallForProposalsService;
import org.dareon.service.RepoService;
import org.dareon.service.UserDetailsImpl;
import org.dareon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class HomeController
{

    private RepoService repoService;
    private UserService userService;
    private CallForProposalsService callForProposalsService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuth(AuthenticationManagerBuilder auth) throws Exception
    {
	auth.userDetailsService(userDetailsService);
    }

    @Autowired
    public HomeController(RepoService repoService, UserService userService,
	    CallForProposalsService callForProposalsService)
    {
	super();
	this.repoService = repoService;
	this.userService = userService;
	this.callForProposalsService = callForProposalsService;
    }

    @RequestMapping("/")
    public String home()
    {
	return "index";
    }

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

    @RequestMapping(value = "/repo/save", method = RequestMethod.POST)
    public String repoSave(Repo repo)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	UserDetailsImpl u = (UserDetailsImpl) auth.getPrincipal();
	if(repoService.findByTitle(repo.getTitle()) == null)
	    repo.setCreator(userService.findByEmail(auth.getName()));
	
	Repo savedRepo = repoService.save(repo);
	return "redirect:list";
    }

    @RequestMapping("/repo/edit/{title}")
    public String repoEdit(@PathVariable String title, Model model)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	model.addAttribute("repo", new Repo());
	List<User> users = userService.list();
	users.remove((userService.findByEmail(auth.getName())));
	model.addAttribute("repo", repoService.findByTitle(title));
	users.add(0,userService.findByEmail(auth.getName()));
	model.addAttribute("users",users);
	return "repo/create";
    }

    @RequestMapping("/repo/read/{title}")
    public String repoRead(@PathVariable String title, Model model)
    {
	model.addAttribute("repo", repoService.findByTitle(title));
	return "repo/read";
    }
    
    @RequestMapping("/repo/list")
    public String repoList(Model model)
    {
	model.addAttribute("repos", repoService.list());
	return "repo/list";
    }

    // @Secured("ROLE_ADMIN")
    @RequestMapping("/callforproposals/create")
    public String proposalCreate(Model model)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	model.addAttribute("repos", userService.findByEmail(auth.getName()).getCreatedRepos());
	model.addAttribute("callForProposals", new CallForProposals());
	return "callforproposals/create";
    }

    @RequestMapping(value = "/callforproposals/save", method = RequestMethod.POST)
    public String proposalSave(CallForProposals callForProposals)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	// UserDetailsImpl u = (UserDetailsImpl)auth.getPrincipal();
	// if (callForProposals.getUser() == null)
	// callForProposals.setUser(repoService.findByTitle(title)));
	// return callForProposals.getRepo().toString();
	CallForProposals savedCallForProposals = callForProposalsService.save(callForProposals);

	return "redirect:list";
    }

    @RequestMapping("/callforproposals/edit/{title}")
    public String callforproposalsEdit(@PathVariable String title, Model model)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	model.addAttribute("repos", userService.findByEmail(auth.getName()).getCreatedRepos());
	model.addAttribute("callForProposals", callForProposalsService.findByTitle(title));
	return "callforproposals/create";
    }
    
    @RequestMapping("/callforproposals/read/{title}")
    public String callforproposalsRead(@PathVariable String title, Model model)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	model.addAttribute("repos", userService.findByEmail(auth.getName()).getCreatedRepos());
	model.addAttribute("callForProposals", callForProposalsService.findByTitle(title));
	return "callforproposals/read";
    }

    @RequestMapping("/callforproposals/list")
    public String callForProposalsList(Model model)
    {
	model.addAttribute("callsForProposals", callForProposalsService.list());
	return "callforproposals/list";
    }

}