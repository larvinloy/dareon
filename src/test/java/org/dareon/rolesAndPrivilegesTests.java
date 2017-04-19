package org.dareon;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.annotation.Resource;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;



@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class rolesAndPrivilegesTests
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

    @After
    public void tearDown()
    {
	// clean up after each test method
    	
    }


    
    @Test
    @WithMockUser(username="admin@dareon.org", authorities={"REPO_READ_PRIVILEGE"})
    public void withRepoReadPrivilege() throws Exception
    {
    	//long numberOfRepositories = repoService.list().size();
    	
       	this.mockMvc.perform(get("/repo/list"))
    			.andExpect(status().is2xxSuccessful())
    			.andExpect(view().name("repo/list"));
    }

    @Test
    @WithMockUser(username="admin@dareon.org")
    public void withoutRepoReadPrivilege() throws Exception
    {
    	//long numberOfRepositories = repoService.list().size();
    	
       	this.mockMvc.perform(get("/repo/list"))
       			.andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username="admin@dareon.org", authorities={"CFP_READ_PRIVILEGE"})
    public void withCfpReadPrivilege() throws Exception
    {
    	//long numberOfRepositories = repoService.list().size();
    	
       	this.mockMvc.perform(get("/callforproposals/list"))
    			.andExpect(status().is2xxSuccessful())
    			.andExpect(view().name("callforproposals/list"));
    }

    @Test
    @WithMockUser(username="admin@dareon.org")
    public void withoutCfpReadPrivilege() throws Exception
    {
    	//long numberOfRepositories = repoService.list().size();
    	
       	this.mockMvc.perform(get("/callforproposals/list"))
    			.andExpect(status().is4xxClientError());
    }    
    
    
}

