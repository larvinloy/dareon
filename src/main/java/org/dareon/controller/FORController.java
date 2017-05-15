package org.dareon.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.dareon.domain.FOR;
import org.dareon.domain.CFP;
import org.dareon.domain.Repo;
import org.dareon.domain.User;
import org.dareon.json.JsonFORTree;
import org.dareon.service.FORService;
import org.dareon.service.CFPService;
import org.dareon.service.RepoService;
import org.dareon.service.UserDetailsImpl;
import org.dareon.service.UserService;
import org.dareon.wrappers.FORForm;
import org.dareon.wrappers.RepoForm;
import org.json.JSONArray;
import org.json.JSONException;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class FORController
{

    private FORService fORService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuth(AuthenticationManagerBuilder auth) throws Exception
    {
	auth.userDetailsService(userDetailsService);
    }

    @Autowired
    public FORController(FORService fORService)
    {
	super();
	this.fORService = fORService;
    }

    // @PreAuthorize("hasAuthority('REPO_CREATE_PRIVILEGE')")
    @RequestMapping("/for/create")
    public String repoCreate(Model model) throws JSONException
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	JSONObject obj = new JSONObject();
	JSONArray arr = new JSONArray();
	// return aNZSRCService.list();
	// for(FOR a : fORService.listByLevel(levelService.findById((long)1)))
	for (FOR a : fORService.list())
	{
	    obj.put("text", a.getCode() + " | " + a.getName());
	    obj.put("id", a.getId());
	    obj.put("tags", new JSONArray().put(String.valueOf(a.getChildren().size())));
	    if (a.getChildren().size() > 0)
		obj = JsonFORTree.addChildren(obj, a.getChildren());
	    arr.put(obj);
	    obj = new JSONObject();
	}

	model.addAttribute("fORForm", new FORForm());
	model.addAttribute("message", arr.toString());
	model.addAttribute("pre", new String());
	model.addAttribute("parent", new String());

	return "for/create";

    }

    @RequestMapping(value = "/for/create", method = RequestMethod.POST)
    public String fORSave(@ModelAttribute FORForm fORForm)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();

	if (fORForm.getParent() == "")
	{
	    FOR newFOR = fORForm.getFoR();
	    FOR savedNewFOR = fORService.save(newFOR);
	    System.out.println("Child ===============>" + newFOR.getCode() + " " + newFOR.getCode() + " "
		    + newFOR.getName() + " " + " " + newFOR.getParent());
	} 
	else
	{
	    Long p = Long.valueOf(fORForm.getParent()).longValue();
	    FOR parent = fORService.findById((long) p);
	    System.out.println(parent.getName());
	    FOR newFOR = fORForm.getFoR();
	    List<FOR> newChildren = parent.getChildren();
	    newChildren.add(newFOR);
	    System.out.println(newChildren.size());
	    parent.setChildren(newChildren);
	    newFOR.setParent(parent);

	    //
	    FOR savedNewFOR = fORService.save(newFOR);
	    FOR savedParent = fORService.save(fORService.findById((long) p));

	    System.out.println("Parent ===============>" + parent.getId() + " " + parent.getCode() + " "
		    + parent.getName() + " " + " " + parent.getChildren());
	    System.out.println("Child ===============>" + newFOR.getCode() + " " + newFOR.getCode() + " "
		    + newFOR.getName() + " " + " " + newFOR.getParent());
	}

	return "index";
    }

    @RequestMapping("/for/edit/{id}")
    public String fOREdit(@PathVariable Long id, Model model)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	// model.addAttribute("fOR", new FOR());
	FORForm fORForm = new FORForm();

	JSONObject obj = new JSONObject();
	JSONArray arr = new JSONArray();

	for (FOR a : fORService.list())
	{
	    obj.put("text", a.getCode() + " | " + a.getName());
	    obj.put("id", a.getId());
	    obj.put("tags", new JSONArray().put(String.valueOf(a.getChildren().size())));
	    if (a.getChildren().size() > 0)
		obj = JsonFORTree.addChildren(obj, a.getChildren());
	    arr.put(obj);
	    obj = new JSONObject();
	}

	model.addAttribute("message", arr.toString());

	JSONArray pre = new JSONArray();

	pre.put(fORService.findById(id).getParent().getId().toString());

	fORForm.setPre(pre.toString());
	fORForm.setFoR(fORService.findById(id));
	model.addAttribute("FORForm", fORForm);
	System.out.println("PRE =====>" + pre);
	System.out.println("FOR =====>" + fORForm.getFoR().getName());
	return "for/create";
    }

}