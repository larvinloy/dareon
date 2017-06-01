package org.dareon.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.dareon.domain.Proposal;
import org.dareon.domain.Repo;
import org.dareon.domain.User;
import org.dareon.service.CFPService;
import org.dareon.service.ProposalService;
import org.dareon.service.ProposalService;
import org.dareon.service.CFPService;
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
public class ProposalController
{

    private CFPService cFPService;
    private UserService userService;
    private ProposalService proposalService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuth(AuthenticationManagerBuilder auth) throws Exception
    {
	auth.userDetailsService(userDetailsService);
    }

    @Autowired
    public ProposalController(CFPService cFPService, UserService userService,
	    ProposalService proposalService)
    {
	super();
	this.cFPService = cFPService;
	this.userService = userService;
	this.proposalService = proposalService;
    }

    @PreAuthorize("hasRole('ROLE_SA') OR hasAuthority('PROPOSAL_CREATE_PRIVILEGE')")
    @RequestMapping("/proposal/create")
    public String proposalCreate(Model model)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	model.addAttribute("cfps", cFPService.list());
	model.addAttribute("proposal", new Proposal());
	return "proposal/create";
    }
    
    @PreAuthorize("hasRole('ROLE_SA') OR hasAuthority('PROPOSAL_CREATE_PRIVILEGE')")
    @RequestMapping("/proposal/create/{id}")
    public String proposalCreateWithPreselectedRepo(Model model,@PathVariable Long id)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	model.addAttribute("repos", Arrays.asList(cFPService.findById(id)));
	model.addAttribute("proposal", new Proposal());
	return "proposal/create";
    }

    @PreAuthorize("hasRole('ROLE_SA') OR hasAuthority('PROPOSAL_CREATE_PRIVILEGE')")
    @RequestMapping(value = "/proposal/create", method = RequestMethod.POST)
    public String proposalSave(@ModelAttribute Proposal proposal)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	if(proposalService.findById(proposal.getId()) == null)
	    proposal.setCreator(userService.findByEmail(auth.getName()));
	Proposal savedproposal = proposalService.save(proposal);

	return "redirect:read/" + savedproposal.getId();
    }

    @PreAuthorize("hasRole('ROLE_SA') OR (hasAuthority('PROPOSAL_EDIT_PRIVILEGE'))")
    @RequestMapping("/proposal/edit/{id}")
    public String proposalEdit(@PathVariable Long id, Model model)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	model.addAttribute("cfps", Arrays.asList(proposalService.findById(id).getCfp()));
	model.addAttribute("proposal", proposalService.findById(id));
	return "proposal/create";
    }
    
    @PreAuthorize("hasAuthority('PROPOSAL_READ_PRIVILEGE')")
    @RequestMapping("/proposal/read/{id}")
    public String proposalRead(@PathVariable Long id, Model model)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	model.addAttribute("proposal", proposalService.findById(id));
	return "proposal/read";
    }

    @PreAuthorize("hasAuthority('PROPOSAL_READ_PRIVILEGE')")
    @RequestMapping("/proposal/list")
    public String proposalList(Model model)
    {
	model.addAttribute("proposals", proposalService.list());
	return "proposal/list";
    }
    
    
    @PreAuthorize("hasRole('ROLE_SA') OR (hasAuthority('PROPOSAL_DELETE_PRIVILEGE'))")
    @RequestMapping("/proposal/delete/{id}")
    public String proposalDelete(@PathVariable Long id, Model model)
    {
	model.addAttribute("proposal", proposalService.findById(id));
	return "proposal/delete";
    }
    
    @PreAuthorize("hasRole('ROLE_SA') OR (hasAuthority('PROPOSAL_DELETE_PRIVILEGE'))")
    @RequestMapping("/proposal/deleteconfirmed/{id}")
    public RedirectView proposalDeleteConfirmed(@PathVariable Long id, Model model)
    {
	System.out.println(id);
	proposalService.delete(id);
	 return new RedirectView("/proposal/list");
    }
   
}