package org.dareon.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.dareon.domain.ANZSRC;
import org.dareon.domain.CFP;
import org.dareon.domain.Group;
import org.dareon.domain.Repo;
import org.dareon.domain.User;
import org.dareon.service.ANZSRCService;
import org.dareon.service.CFPService;
import org.dareon.service.GroupService;
import org.dareon.service.RepoService;
import org.dareon.service.UserDetailsImpl;
import org.dareon.service.UserService;
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
public class ANZSRCController
{

    private ANZSRCService aNZSRCService;
    private GroupService groupService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuth(AuthenticationManagerBuilder auth) throws Exception
    {
	auth.userDetailsService(userDetailsService);
    }

    @Autowired
    public ANZSRCController(ANZSRCService aNZSRCService,GroupService groupService)
    {
	super();
	this.aNZSRCService = aNZSRCService;
	this.groupService = groupService;
    }

//    @PreAuthorize("hasAuthority('REPO_CREATE_PRIVILEGE')")
    @RequestMapping("/anzsrc/list")
    public String repoCreate(Model model) throws JSONException
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	model.addAttribute("repo", new Repo());
	
	JSONObject obj = new JSONObject();
	JSONArray arr = new JSONArray();
//	return aNZSRCService.list();
	for(ANZSRC a : groupService.list())
	{
	    obj.put("text", a.getCode() + " | " + a.getName());
	    obj.put("id", a.getId());
	    obj.put("tags", new JSONArray().put(String.valueOf(a.getChildren().size())));
	    if(a.getChildren().size() > 0)
		obj = addChildren(obj, a.getChildren());
	    arr.put(obj);
	    obj = new JSONObject();
	}
	model.addAttribute("message", arr.toString());
	return "repo/treeview";
//	return arr.toString();
//	
//	List<User> users = userService.list();
//	users.remove((userService.findByEmail(auth.getName())));
//	users.add(0,userService.findByEmail(auth.getName()));
//	model.addAttribute("users",users);
	
    }
    
    public JSONObject addChildren(JSONObject obj, Set<ANZSRC> children)
    {
	System.out.println(obj);
	JSONArray ch = new JSONArray();
	for(ANZSRC a : children)
	{
	    JSONObject o = new JSONObject();
	    o.put("text", a.getCode() + " | " + a.getName());
	    o.put("id", a.getId());
	    o.put("tags", new JSONArray().put(String.valueOf(a.getChildren().size())));
	    if(a.getChildren().size() > 0)
		addChildren(o, a.getChildren());
	    System.out.println(o);
	    ch.put(o);
	}
	
	obj.put("nodes", ch);
	System.out.println("final: "+obj);
	return obj;
    }

   
}