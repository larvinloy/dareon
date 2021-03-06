package org.dareon.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.dareon.domain.Classification;
import org.dareon.domain.CFP;
import org.dareon.domain.Expertise;
import org.dareon.domain.Repo;
import org.dareon.domain.User;
import org.dareon.json.JsonClassificationTree;
import org.dareon.service.ExpertiseService;
import org.dareon.service.ClassificationService;
import org.dareon.service.CFPService;
import org.dareon.service.RepoService;
import org.dareon.service.UserDetailsImpl;
import org.dareon.service.UserService;
import org.dareon.wrappers.AddExpertiseForm;
import org.dareon.wrappers.AddExpertiseValueForm;
import org.dareon.wrappers.ClassificationForm;
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
/**
 *This is the Controller for Expertise field of dareon website ,
 *this defines request mapping for the functionalities of expertise service model.
 */

@Controller
public class ExpertiseController
{

    private ExpertiseService expertiseService;
    private ClassificationService classificationService;
    private UserService userService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configureAuth(AuthenticationManagerBuilder auth) throws Exception
    {
	auth.userDetailsService(userDetailsService);
    }
    /**
    *
    * @param expertiseService details about the expertise of user in different selected fields
    * @param classificationService  details about the classified fields selected by the user
    * @param userService details about the particular authenticated user
    */
    @Autowired
    public ExpertiseController(ExpertiseService expertiseService, ClassificationService classificationService, UserService userService)
    {
	super();
	this.expertiseService = expertiseService;
	this.classificationService = classificationService;
	this.userService = userService;
    }
    /**
     * 
     * @param model defines the expertise model in model class of MVC
     * @return the system to "expertise/addexpertise" url
     * @throws JSONException throws exception for missing values
     */
    @RequestMapping("/expertise/add")
    public String addExpertise(Model model) throws JSONException
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	

	
	
	JSONArray pre = new JSONArray();
	for(Expertise a : expertiseService.findByUser(userService.findByEmail(auth.getName())))
	{
	    pre.put(a.getClassification().getId());
	}
	
	AddExpertiseForm addExpertiseForm = new AddExpertiseForm();
	addExpertiseForm.setPre(pre.toString());
	model.addAttribute("addExpertiseForm", addExpertiseForm);
	model.addAttribute("classificationTree", JsonClassificationTree.getClassificationTreeAsString(classificationService.list()));

	return "expertise/addexpertise";

    }
    /**
     * 
     * @param addExpertiseForm details to be filled for the expertise of user
     * @param model defines the model for add expertise url 
     * @return redirect the system to set value field
     */
    @RequestMapping(value = "/expertise/add", method = RequestMethod.POST)
    public String addExpertise(@ModelAttribute AddExpertiseForm addExpertiseForm, Model model)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	System.out.println("Controller: "+userService.findByEmail(auth.getName()).getId());
	System.out.println(expertiseService.findByUser(userService.findByEmail(auth.getName())));
	expertiseService.saveAll(userService.findByEmail(auth.getName()),addExpertiseForm.getFORCollection());
	return "redirect:setvalue";
    }
    /**
     * 
     * @param model details about the setvalue mdel for expertise field
     * @return redirects system to"expertise/setexpertisevalue" url 
     */
    @RequestMapping(value = "/expertise/setvalue")
    public String setExpertiseValue(Model model)
    {
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	AddExpertiseValueForm addExpertiseValueForm = new AddExpertiseValueForm();
	if(expertiseService.findByUser(userService.findByEmail(auth.getName())).size() == 0)
	    return "redirect:add";
	addExpertiseValueForm.setExpertises(expertiseService.findByUser(userService.findByEmail(auth.getName())));
	model.addAttribute("addExpertiseValueForm", addExpertiseValueForm);
	return "expertise/setexpertisevalue";
    }
    /**
     * 
     * @param addExpertiseValueForm details to be filled in fields of form
     * @param model  defines expertise add form  
     * @return the index to the node 
     */
    @RequestMapping(value = "/expertise/setvalue", method = RequestMethod.POST)
    public String setExpertiseValue(@ModelAttribute AddExpertiseValueForm addExpertiseValueForm, Model model)
    {
	List<Expertise> expertises = addExpertiseValueForm.getExpertises();
	expertiseService.setValues(expertises);
	
	return "index";
    }
}