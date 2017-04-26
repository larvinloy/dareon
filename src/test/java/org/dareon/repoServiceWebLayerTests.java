package org.dareon;

import org.dareon.domain.CFP;
import org.dareon.domain.Repo;
import org.dareon.service.CFPService;
import org.dareon.service.RepoService;
import org.dareon.service.UserService;
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
public class repoServiceWebLayerTests
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
    @WithUserDetails("admin@dareon.org")
    public void RepoCreateAndReadTest() throws Exception
    {
    	long numberOfRepositoriesPriorToCreate = repoService.list().size();
    	
    	this.mockMvc.perform(post("/repo/create")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
		.param("title", "sample repository 3")
		.param("institution", "Sample institution for repository 3")
		.param("definition", "Sample definition for repository 3")
		.param("description", "Sample description for repository 3")
		.param("owner", "3"))
     	//check if redirected to repository details page
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("read/3"));

    	long numberOfRepositoriesAfterCreate = repoService.list().size();
    	
		// check if the repository was created 
		MvcResult response = mockMvc.perform(get("/repo/read/3"))
		        .andExpect(status().isOk()).andExpect(view().name("repo/read"))
		        .andExpect(model().attributeExists("repo"))
		        .andReturn();
		Object repoValue = response.getModelAndView().getModel().get("repo");
		assertTrue(repoValue instanceof Repo);
		Repo rv = (Repo)repoValue;
		
		assertNotNull("failure - not null", repoValue);
		//number of repositories should have increased
		assertEquals("failure - repository did not increased", numberOfRepositoriesPriorToCreate+1, numberOfRepositoriesAfterCreate);
		//The ID should be automatically created with an incremental value
		assertEquals("failure - ID attribute not match", rv.getId(), 3);
		assertEquals("failure - Title attribute not match", "sample repository 3", rv.getTitle());
		assertEquals("failure - Definition attribute not match", "Sample definition for repository 3", rv.getDefinition());
		assertEquals("failure - Description attribute not match", "Sample description for repository 3", rv.getDescription());
		assertEquals("failure - Institution attribute not match", "Sample institution for repository 3", rv.getInstitution());
		//The Status should be automatically created with the default: true
		assertEquals("failure - Status attribute not match", true, rv.getStatus());
		assertEquals("failure - Owner attribute not match", "repoowner@rmit.edu.au", rv.getOwner().getEmail());	
		//The Creator should be automatically created with the name of the currently logged-in user
		assertEquals("failure - Creator attribute not match", "admin@dareon.org", rv.getCreator().getEmail());    
    }


    @Test
    @WithUserDetails("admin@dareon.org")
    public void RepoUpdateAndReadTest() throws Exception
    {
    	long numberOfRepositoriesPriorToUpdate = repoService.list().size();
    	
    	this.mockMvc.perform(post("/repo/create")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .param("id", "1") //search for repository to edit
		.param("title", "Test Repo 1")
		.param("institution", "Test Institution 1")
		.param("definition", "Modified definition") //modified contents
		.param("description", "Test Description 1")
		.param("status", "true")
		.param("creator", "2")
    	.param("owner", "2"))
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("read/1"));

    	long numberOfRepositoriesAfterUpdate = repoService.list().size();
    	
		// check if the repository was updated 
		MvcResult response = mockMvc.perform(get("/repo/read/1"))
		        .andReturn();
		Object repoValue = response.getModelAndView().getModel().get("repo");
		Repo rv = (Repo)repoValue;
		
		assertNotNull("failure - not null", repoValue);
		//number of repositories should have not increased
		assertEquals("failure - repository increased", numberOfRepositoriesPriorToUpdate, numberOfRepositoriesAfterUpdate);
		assertEquals("failure - ID attribute not match", 1, rv.getId());
		assertEquals("failure - Repository attribute not match", "Test Repo 1", rv.getTitle());
		//the definition should have changed
		assertEquals("failure - Definition attribute not match", "Modified definition", rv.getDefinition());
		assertEquals("failure - Description attribute not match", "Test Description 1", rv.getDescription());
		assertEquals("failure - Institution attribute not match", "Test Institution 1", rv.getInstitution());
		assertEquals("failure - Status attribute not match", true, rv.getStatus());
		assertEquals("failure - Creator attribute not match", "admin@dareon.org", rv.getCreator().getEmail());
		assertEquals("failure - Owner attribute not match", "admin@dareon.org", rv.getOwner().getEmail());	
    }

    
    @Test
    @WithUserDetails("admin@dareon.org")
    public void RepoDeleteTest() throws Exception
    {
    	long numberOfRepositoriesPriorToDelete = repoService.list().size();
    	
		// check if the repository is existing 
		MvcResult responseRepo = mockMvc.perform(get("/repo/read/2"))
		        .andReturn();
		Object repoValue = responseRepo.getModelAndView().getModel().get("repo");
		Repo rv = (Repo)repoValue;
		assertNotNull("failure - repo not existing", repoValue);
		assertEquals("failure - repo not existing", "Test Repo 2", rv.getTitle());
 
		// check for the associated CFP
		MvcResult responseCFP = mockMvc.perform(get("/callforproposals/read/2"))
		        .andReturn();
		Object cfpValue = responseCFP.getModelAndView().getModel().get("callForProposals");
		CFP cv = (CFP)cfpValue;
		assertNotNull("failure - CFP not existing", cfpValue);
		assertEquals("failure - CFP not existing", "Test CFP 2", cv.getTitle());
		//validate association with the repo
		assertEquals("failure - CFP not associated with the repo", "Test Repo 2", cv.getRepo().getTitle());
		
		
		// delete repository 2
		this.mockMvc.perform(get("/repo/deleteconfirmed/2"))
				.andExpect(status().is3xxRedirection())
				.andExpect(redirectedUrl("/repo/list"));

		long numberOfRepositoriesAfterDelete = repoService.list().size();
		
		//check if the repository was deleted
		Repo repository = repoService.findByTitle("Test Repo 2");
		CFP cfp = cFPService.findByTitle("Test CFP 2");
		//number of repositories should have decreased
		assertEquals("failure - repositories did not decreased", numberOfRepositoriesPriorToDelete-1, numberOfRepositoriesAfterDelete);
		//repository should not be found
		assertNull("failure - repository is still existing", repository);
		//associated CFP should not be found
		assertNull("failure - CFP is still existing", cfp);		

    }    
  
    
}
