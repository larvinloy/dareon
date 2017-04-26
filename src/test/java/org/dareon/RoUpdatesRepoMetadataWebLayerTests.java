package org.dareon;

import org.dareon.domain.CFP;
import org.dareon.domain.Repo;
import org.dareon.service.CFPService;
import org.dareon.service.RepoService;
import org.dareon.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class RoUpdatesRepoMetadataWebLayerTests
{

	@Autowired
	CFPService cFPService; 
	
	@Autowired
	RepoService repoService;
	
	@Autowired
	UserService userService;
	
    @Resource
    private WebApplicationContext webApplicationContext;
	private MockMvc mockMvc;
    
    @Before
    public void setUp() {
	mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
		.apply(springSecurity())
        .build();
    }
   

    @Test
    @WithUserDetails("repoowner@rmit.edu.au")
    public void roCanUpdateRepoMetadataTest() throws Exception
    {
    	long numberOfRepositories = repoService.list().size();
    	
    	this.mockMvc.perform(post("/repo/create")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .param("id", "2") //search for repository to edit
		.param("title", "Test Repository 2")
		.param("institution", "Test Institution 2")
		.param("definition", "Modified definition") //modified contents
		.param("description", "Test Description 2")
		.param("status", "true")
		.param("creator", "2")
    	.param("owner", "3"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("read/2"));
 
	
		// check if the repository was updated 
		MvcResult response = mockMvc.perform(get("/repo/read/2"))
		        .andExpect(status().isOk()).andExpect(view().name("repo/read"))
		        .andExpect(model().attributeExists("repo"))
		        .andReturn();
		Object repoValue = response.getModelAndView().getModel().get("repo");
		Repo rv = (Repo)repoValue;
		
		assertNotNull("failure - not null", repoValue);
		//number of repositories should have not increased
		assertEquals("failure - repository increased", numberOfRepositories, repoService.list().size());
		assertEquals("failure - ID attribute not match", rv.getId(), 2);
		assertEquals("failure - Repository attribute not match", rv.getTitle(), "Test Repository 2");
		//the definition should have changed
		assertEquals("failure - Definition attribute not match", rv.getDefinition(), "Modified definition");
		assertEquals("failure - Description attribute not match", rv.getDescription(), "Test Description 2");
		assertEquals("failure - Institution attribute not match", rv.getInstitution(), "Test Institution 2");
		assertEquals("failure - Status attribute not match", rv.getStatus(), true);
		assertEquals("failure - Creator attribute not match", rv.getCreator().getEmail(), "admin@dareon.org");
		assertEquals("failure - Owner attribute not match", rv.getOwner().getEmail(), "repoowner@rmit.edu.au");	
    }
    
}
