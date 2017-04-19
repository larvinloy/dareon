package org.dareon;

import org.dareon.domain.Repo;
import org.dareon.domain.User;
import org.dareon.service.RepoService;
import org.dareon.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
public class repoServiceWebLayerTests
{

	@Autowired
	RepoService repoService;
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

    @After
    public void tearDown()
    {
	// clean up after each test method
    	
    }


    
    @Test
    @WithUserDetails("admin@dareon.org")
    public void RepoCreateAndReadWebTest() throws Exception
    {
    	long numberOfRepositories = repoService.list().size();
    	
    	this.mockMvc.perform(post("/repo/create")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
		.param("title", "sample repository 2")
		.param("institution", "Sample institution for repository 2")
		.param("definition", "Sample definition for repository 2")
		.param("description", "Sample description for repository 2")
		.param("owner", "3"))
     	//check if redirected to repository list page - /repo/list
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("list"))
		.andReturn();
	
		// check if the repository was created 
		MvcResult response = mockMvc.perform(get("/repo/read/sample repository 2"))
		        .andExpect(status().isOk()).andExpect(view().name("repo/read"))
		        .andExpect(model().attributeExists("repo"))
		        .andReturn();
		Object repoValue = response.getModelAndView().getModel().get("repo");
		assertTrue(repoValue instanceof Repo);
		Repo rv = (Repo)repoValue;
		
		assertNotNull("failure - not null", repoValue);
		//number of repositories should have increased
		assertNotEquals("failure - repository did not increased", numberOfRepositories, repoService.list().size());
		//The ID should be automatically created with an incremental value
		assertEquals("failure - ID attribute not match", rv.getId(), 3);
		assertEquals("failure - Repository attribute not match", rv.getTitle(), "sample repository 2");
		assertEquals("failure - Definition attribute not match", rv.getDefinition(), "Sample definition for repository 2");
		assertEquals("failure - Description attribute not match", rv.getDescription(), "Sample description for repository 2");
		assertEquals("failure - Institution attribute not match", rv.getInstitution(), "Sample institution for repository 2");
		//The Status should be automatically created with the default: true
		assertEquals("failure - Status attribute not match", rv.getStatus(), true);
		//The Delete Status should be automatically created with the default: false		
		assertEquals("failure - Delete Status attribute not match", rv.getDeleteStatus(), false);
		assertEquals("failure - Owner attribute not match", rv.getOwner().getEmail(), "repoowner@rmit.edu.au");	
		//The Creator should be automatically created with the name of the currently logged-in user
		assertEquals("failure - Creator attribute not match", rv.getCreator().getEmail(), "admin@dareon.org");    
    }


    @Test
    @WithUserDetails("admin@dareon.org")
    public void RepoUpdateDeleteAndReadWeb() throws Exception
    {
    	long numberOfRepositories = repoService.list().size();
    	
    	this.mockMvc.perform(post("/repo/create")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .param("id", "1") //search for repository to edit
		.param("title", "test_repo1")
		.param("institution", "test_institution1")
		.param("definition", "Modified definition") //modified contents
		.param("description", "test_description1")
		.param("status", "true")
		.param("deleteStatus", "true") //modified as deleted
		.param("creator", "1")
    	.param("owner", "2"))
     	//check if redirected to repository list page - /repo/list
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("list"))
		.andReturn();
 
	
		// check if the repository was created 
		MvcResult response = mockMvc.perform(get("/repo/read/test_repo1"))
		        .andExpect(status().isOk()).andExpect(view().name("repo/read"))
		        .andExpect(model().attributeExists("repo"))
		        .andReturn();
		Object repoValue = response.getModelAndView().getModel().get("repo");
		assertTrue(repoValue instanceof Repo);
		Repo rv = (Repo)repoValue;
		
		assertNotNull("failure - not null", repoValue);
		//number of repositories should have not increased
		assertEquals("failure - repository increased", numberOfRepositories, repoService.list().size());
		assertEquals("failure - ID attribute not match", rv.getId(), 1);
		assertEquals("failure - Repository attribute not match", rv.getTitle(), "test_repo1");
		assertEquals("failure - Definition attribute not match", rv.getDefinition(), "Modified definition");
		assertEquals("failure - Description attribute not match", rv.getDescription(), "test_description1");
		assertEquals("failure - Institution attribute not match", rv.getInstitution(), "test_institution1");
		assertEquals("failure - Status attribute not match", rv.getStatus(), true);
		assertEquals("failure - Delete Status attribute not match", rv.getDeleteStatus(), true);
		assertEquals("failure - Creator attribute not match", rv.getCreator().getEmail(), "admin@dareon.org");
		assertEquals("failure - Owner attribute not match", rv.getOwner().getEmail(), "admin@dareon.org");	
    }
    
}






/*    @Test
@WithUserDetails("admin@dareon.org")
public void testRepoReadWeb() throws Exception
{
	MvcResult response = mockMvc.perform(get("/repo/read/test_repo1"))
	        .andExpect(status().isOk()).andExpect(view().name("repo/read"))
	        .andExpect(model().attributeExists("repo"))
	        .andReturn();
	Object repoValue = response.getModelAndView().getModel().get("repo");
	
	assertTrue(repoValue instanceof Repo);
	assertNotNull("failure - not null", repoValue);
	assertEquals("failure - ID attribute not match", ((Repo)repoValue).getId(), 1);
	assertEquals("failure - Repository attribute not match", ((Repo)repoValue).getTitle(), "test_repo1");
	assertEquals("failure - Definition attribute not match", ((Repo)repoValue).getDefinition(), "test_definition1");
	assertEquals("failure - Description attribute not match", ((Repo)repoValue).getDescription(), "test_description1");
	assertEquals("failure - Institution attribute not match", ((Repo)repoValue).getInstitution(), "test_institution1");
	assertEquals("failure - Status attribute not match", ((Repo)repoValue).getStatus(), true);
	assertEquals("failure - Delete Status attribute not match", ((Repo)repoValue).getDeleteStatus(), false);
	assertEquals("failure - Creator attribute not match", ((Repo)repoValue).getCreator().getEmail(), "s3562412@student.rmit.edu.au");
	assertEquals("failure - Owner attribute not match", ((Repo)repoValue).getOwner().getEmail(), "admin@dareon.org");		
}
*/  

