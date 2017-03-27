package org.dareon.controller;

import org.dareon.domain.Repo;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController
{

    private RepoService repoService;
    private UserService userSerice;
    

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuth(AuthenticationManagerBuilder auth) throws Exception
    {
	auth.userDetailsService(userDetailsService);
    }

    @Autowired
    public HomeController(RepoService repoService, UserService userService)
    {
	super();
	this.repoService = repoService;
	this.userSerice = userService;
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
	return "repo/postRepo";
    }

    @RequestMapping(value = "/repo/save", method = RequestMethod.POST)
    public String repoSave(Repo repo)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//	UserDetailsImpl u = (UserDetailsImpl)auth.getPrincipal();
	repo.setUser(userSerice.findByEmail(auth.getName()));
	Repo savedRepo = repoService.save(repo);
	return "repo/postRepo";
    }

}