package org.dareon.controller;


import java.util.List;

import org.dareon.domain.FOR;
import org.dareon.json.JsonFORTree;
import org.dareon.service.FORService;
import org.dareon.wrappers.FORForm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.security.authentication.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;


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
    public String forCreate(Model model) throws JSONException
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
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

	model.addAttribute("fORForm", new FORForm());
	model.addAttribute("message", arr.toString());
	model.addAttribute("pre", new String());
	model.addAttribute("parent", new String());

	return "for/create";
    }


    
    @RequestMapping(value = "/for/create", method = RequestMethod.POST)
    public String forSave(@ModelAttribute FORForm fORForm)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();

	if (fORForm.getParent() == "")
	{
	    FOR newFOR = fORForm.getFoR();
	    FOR savedNewFOR = fORService.save(newFOR);
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

	    FOR savedNewFOR = fORService.save(newFOR);
	    FOR savedParent = fORService.save(parent);
	}

	return "redirect:list";
    }

    

    @RequestMapping("/for/edit/{id}")
    public String forEdit(@PathVariable Long id, Model model)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	FORForm fORForm = new FORForm();
	fORForm.setFoR(fORService.findById(id));
	
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

	if(fORService.findById(id).getParent() != null)
		pre.put(fORService.findById(id).getParent().getId().toString());

	fORForm.setPre(pre.toString());
	model.addAttribute("fORForm", fORForm);

	return "for/create";
    }
    
    
    
    
    
    
    
    @RequestMapping("/for/list")
    public String forList(Model model) throws JSONException
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
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

	model.addAttribute("fORForm", new FORForm());
	model.addAttribute("message", arr.toString());
	//model.addAttribute("pre", new String());

	return "for/list";

    }
    
    
    
    @RequestMapping("/for/deleteconfirmed/{id}")
    public RedirectView forDeleteConfirmed(@PathVariable Long id, Model model)
    {
	fORService.delete(id);
	 return new RedirectView("/for/list");
    }
    
    

}