package org.dareon;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserLoginTests
{
	
    @Resource
    private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
    
    @Before
    public void setUp() {
	mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
		.apply(springSecurity())
        .build();
    }

 
    //User logs in with the right credentials
    @Test
    public void correctLoginCredentialsTest() throws Exception
    {
  	   	this.mockMvc.perform(formLogin("/login")
  	   		.userParameter("email").user("admin@dareon.org").password("admin"))
  	   		.andExpect(authenticated()) //.withRoles("USER","ADMIN").withAuthorities(expected)
  	   		.andExpect(redirectedUrl("/"));
  	   	
  	   	this.mockMvc.perform(logout())
  	   		.andExpect(redirectedUrl("/login?logout"));
    }   
    
    //User logs in with the right username but wrong password
    @Test
    public void wrongLoginCredentialsTest() throws Exception
    {
  	   	this.mockMvc.perform(formLogin("/login")
  	   		.userParameter("email").user("admin@dareon.org").password("wrongpassword"))
  	   		//.andExpect(status().reason(containsString("Bad credentials")))
  	   		.andExpect(redirectedUrl("/login?error"));
    } 

    //Authenticated user can access different web pages
    @Test
    @WithUserDetails("admin@dareon.org")
    public void authenticatedUserAcessTest() throws Exception
    {
    	this.mockMvc.perform(get("/"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(view().name("index"));

    	this.mockMvc.perform(get("/repo/list"))
			.andExpect(status().is2xxSuccessful())
			.andExpect(view().name("repo/list"));
    }
    
    //Anonymous users can only access the login page
    @Test
    public void anonymousUserAcessTest() throws Exception
    {
    	this.mockMvc.perform(get("/"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/login"));
    	
    	this.mockMvc.perform(get("/repo/list"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("http://localhost/login"));
    }
   
}
