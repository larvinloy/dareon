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