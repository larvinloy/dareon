package org.dareon.controller;

import org.dareon.domain.CallForProposals;
import org.dareon.domain.Repo;
import org.dareon.service.CallForProposalsService;
import org.dareon.service.RepoService;
import org.dareon.service.UserDetailsImpl;
import org.dareon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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
	model.addAttribute("repo", new Repo());
	return "repo/create";
    }

    @RequestMapping(value = "/repo/save", method = RequestMethod.POST)
    public String repoSave(Repo repo)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	// UserDetailsImpl u = (UserDetailsImpl)auth.getPrincipal();
//	repo.setUser(userService.findByEmail(auth.getName()));
	Repo savedRepo = repoService.save(repo);
	return "repo/create";
    }

    @RequestMapping("/repo/edit/{id}")
    public String repoEdit(@PathVariable Long id, Model model)
    {
	model.addAttribute("repo", repoService.get(id));
	return "repo/create";
    }

    @RequestMapping("/repo/list")
    public String repoList(Model model)
    {
	model.addAttribute("repos", repoService.list());
	return "repo/list";
    }
    
    @RequestMapping("/callforproposals/create")
    public String proposalCreate(Model model)
    {
	model.addAttribute("callForProposals", new CallForProposals());
	return "callforproposals/create";
    }

    @RequestMapping(value = "/callforproposals/save", method = RequestMethod.POST)
    public String proposalSave(CallForProposals callForProposals)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	// UserDetailsImpl u = (UserDetailsImpl)auth.getPrincipal();
	if(callForProposals.getUser() == null)
	    callForProposals.setUser(userService.findByEmail(auth.getName()));
	CallForProposals savedCallForProposals = callForProposalsService.save(callForProposals);
	return "callforproposals/create";
    }
    
    @RequestMapping("/callforproposals/edit/{id}")
    public String callforproposalsEdit(@PathVariable Long id, Model model)
    {
	model.addAttribute("callForProposals", callForProposalsService.get(id));
	return "callforproposals/create";
    }
    
    @RequestMapping("/callforproposals/list")
    public String callForProposalsList(Model model)
    {
	model.addAttribute("callsForProposals", callForProposalsService.list());
	return "callforproposals/list";
    }

    

}