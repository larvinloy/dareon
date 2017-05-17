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
public class CFPController
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
    public CFPController(RepoService repoService, UserService userService,
	    CFPService cFPService)
    {
	super();
	this.repoService = repoService;
	this.userService = userService;
	this.cFPService = cFPService;
    }

    @PreAuthorize("hasAuthority('CFP_CREATE_PRIVILEGE')")
    @RequestMapping("/callforproposals/create")
    public String cFPCreate(Model model)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	model.addAttribute("repos", userService.findByEmail(auth.getName()).getOwnedRepos());
	model.addAttribute("callForProposals", new CFP());
	return "callforproposals/create";
    }
    
    @PreAuthorize("hasAuthority('CFP_CREATE_PRIVILEGE') AND isRepoOwner(#id)")
    @RequestMapping("/callforproposals/create/{id}")
    public String cFPCreateWithPreselectedRepo(Model model,@PathVariable Long id)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	model.addAttribute("repos", Arrays.asList(repoService.findById(id)));
	model.addAttribute("callForProposals", new CFP());
	return "callforproposals/create";
    }

    @PreAuthorize("hasAuthority('CFP_CREATE_PRIVILEGE') AND isRepoOwner(#cFP)")
    @RequestMapping(value = "/callforproposals/create", method = RequestMethod.POST)
    public String cFPSave(@ModelAttribute CFP cFP)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	// UserDetailsImpl u = (UserDetailsImpl)auth.getPrincipal();
	// if (callForProposals.getUser() == null)
	// callForProposals.setUser(repoService.findByTitle(title)));
	// return callForProposals.getRepo().toString();
	CFP savedCallForProposals = cFPService.save(cFP);

	return "redirect:read/" + savedCallForProposals.getId();
    }

    @PreAuthorize("hasAuthority('CFP_EDIT_PRIVILEGE') AND isCFPOwner(#id)")
    @RequestMapping("/callforproposals/edit/{id}")
    public String cFPEdit(@PathVariable Long id, Model model)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	model.addAttribute("repos", Arrays.asList((cFPService.findById(id)).getRepo()));
	model.addAttribute("callForProposals", cFPService.findById(id));
	return "callforproposals/create";
    }
    
    @PreAuthorize("hasAuthority('CFP_READ_PRIVILEGE')")
    @RequestMapping("/callforproposals/read/{id}")
    public String cFPRead(@PathVariable Long id, Model model)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	model.addAttribute("callForProposals", cFPService.findById(id));
	return "callforproposals/read";
    }

    @PreAuthorize("hasAuthority('CFP_READ_PRIVILEGE')")
    @RequestMapping("/callforproposals/list")
    public String cFPList(Model model)
    {
	model.addAttribute("callsForProposals", cFPService.list());
	return "callforproposals/list";
    }
    
    
    @PreAuthorize("hasAuthority('CFP_DELETE_PRIVILEGE') AND isCFPOwner(#id)")
    @RequestMapping("/callforproposals/delete/{id}")
    public String cFPDelete(@PathVariable Long id, Model model)
    {
	model.addAttribute("callForProposals", cFPService.findById(id));
	return "callforproposals/delete";
    }
    
    @PreAuthorize("hasAuthority('CFP_DELETE_PRIVILEGE') AND isCFPOwner(#id)")
    @RequestMapping("/callforproposals/deleteconfirmed/{id}")
    public RedirectView cFPDeleteConfirmed(@PathVariable Long id, Model model)
    {
	System.out.println(id);
	cFPService.delete(id);
	 return new RedirectView("/callforproposals/list");
    }
   
}