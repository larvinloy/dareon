package org.dareon.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.dareon.domain.CFP;
import org.dareon.domain.FOR;
import org.dareon.domain.Repo;
import org.dareon.domain.User;
import org.dareon.json.JsonFORTree;
import org.dareon.service.CFPService;
import org.dareon.service.FORService;
import org.dareon.service.LevelService;
import org.dareon.service.RepoService;
import org.dareon.service.UserDetailsImpl;
import org.dareon.service.UserService;
import org.dareon.wrappers.RepoForm;
import org.json.JSONArray;
import org.json.JSONObject;
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
    private FORService fORService;
    private LevelService levelService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuth(AuthenticationManagerBuilder auth) throws Exception
    {
	auth.userDetailsService(userDetailsService);
    }

    @Autowired
    public RepoController(RepoService repoService, UserService userService,
	    CFPService cFPService, FORService fORService, LevelService levelService)
    {
	super();
	this.repoService = repoService;
	this.userService = userService;
	this.cFPService = cFPService;
	this.fORService = fORService;
	this.levelService = levelService;
    }

    @PreAuthorize("hasAuthority('REPO_CREATE_PRIVILEGE')")
    @RequestMapping("/repo/create")
    public String repoCreate(Model model)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	model.addAttribute("repoForm", new RepoForm());
	List<User> users = userService.list();
	users.remove((userService.findByEmail(auth.getName())));
	users.add(0,userService.findByEmail(auth.getName()));
	model.addAttribute("users",users);
	
	JSONObject obj = new JSONObject();
	JSONArray arr = new JSONArray();
//	return aNZSRCService.list();
	for(FOR a : fORService.listByLevel(levelService.findById((long)1)))
	{
	    obj.put("text", a.getCode() + " | " + a.getName());
	    obj.put("id", a.getId());
	    obj.put("tags", new JSONArray().put(String.valueOf(a.getChildren().size())));
	    if(a.getChildren().size() > 0)
		obj = JsonFORTree.addChildren(obj, a.getChildren());
	    arr.put(obj);
	    obj = new JSONObject();
	}
	model.addAttribute("message", arr.toString());
	model.addAttribute("domains", new String());
	model.addAttribute("pre", new String());
	return "repo/create";
    }

    @PreAuthorize("hasAuthority('REPO_CREATE_PRIVILEGE') OR isRepoOwner(#repoForm)")
    @RequestMapping(value = "/repo/create", method = RequestMethod.POST)
    public String repoSave(@ModelAttribute RepoForm repoForm, @ModelAttribute String domains)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	Repo repo = repoForm.getRepo();
	if(repoService.findById(repo.getId()) == null)
	    repo.setCreator(userService.findByEmail(auth.getName()));
	
//	repo.setDomains(repoForm.getFORCollection());
	Collection<FOR> fORs = new ArrayList<FOR>();
	for(Long id : repoForm.getFORCollection())
	{
	    fORs.add(fORService.findById(id));
	}
	repo.setDomains(fORs);
	
	
	Repo savedRepo = repoService.save(repo);
	
	Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), auth.getAuthorities());
	SecurityContextHolder.getContext().setAuthentication(newAuth);
	
	System.out.println("Domains: " + repoForm.getDomains());
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
	RepoForm repoForm = new RepoForm();
	repoForm.setRepo(repoService.findById(id));
	
	users.add(0,userService.findByEmail(auth.getName()));
	model.addAttribute("users",users);
	
	JSONObject obj = new JSONObject();
	JSONArray arr = new JSONArray();
//	return aNZSRCService.list();
	for(FOR a : fORService.listByLevel(levelService.findById((long)1)))
	{
	    obj.put("text", a.getCode() + " | " + a.getName());
	    obj.put("id", a.getId());
	    obj.put("tags", new JSONArray().put(String.valueOf(a.getChildren().size())));
	    if(a.getChildren().size() > 0)
		obj = JsonFORTree.addChildren(obj, a.getChildren());
	    arr.put(obj);
	    obj = new JSONObject();
	}
	model.addAttribute("message", arr.toString());
	
	
	JSONArray pre = new JSONArray();
	for(FOR a : repoService.findById(id).getDomains())
	{
	    pre.put(a.getId().toString());
	}
	repoForm.setPre(pre.toString());
	model.addAttribute("repoForm", repoForm);
	System.out.println(pre);
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