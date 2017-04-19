package org.dareon;

import org.dareon.domain.Repo;
import org.dareon.domain.CallForProposals;
import org.dareon.service.CallForProposalsService;
import org.dareon.service.RepoService;
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
public class cfpServiceWebLayerTests
{

	@Autowired
	RepoService repoService;
	
	@Autowired
	CallForProposalsService cfpService;	
	
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
    public void CfpCreateAndReadWebTest() throws Exception
    {
    	long numberOfCfpPriorToCreate = cfpService.list().size();
    	
    	this.mockMvc.perform(post("/callforproposals/create")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
		.param("title", "sample cfp 2")
		.param("institution", "Sample institution for cfp 2")
		.param("details", "Sample details for cfp 2")
		.param("description", "Sample description for cfp 2")
		.param("repo", "1")) // assign to repository: test_repo1
     	//check if redirected to CFP list page - /callforproposals/list
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("list"))
		.andReturn();
    	
    	long numberOfCfpAfterCreate = cfpService.list().size();
    	
		// check if the CFP was created 
		MvcResult response = mockMvc.perform(get("/callforproposals/read/sample cfp 2"))
		        .andExpect(status().isOk()).andExpect(view().name("callforproposals/read"))
		        .andExpect(model().attributeExists("callForProposals"))
		        .andReturn();
		Object cfpValue = response.getModelAndView().getModel().get("callForProposals");
		assertTrue(cfpValue instanceof CallForProposals);
		CallForProposals cv = (CallForProposals)cfpValue;
				
		assertNotNull("failure - not null", cfpValue);
		//number of CFPs should have increased
		assertNotEquals("failure - repository did not increased", numberOfCfpPriorToCreate, numberOfCfpAfterCreate);
		assertEquals("failure - ID attribute not match", cv.getId(), 2);
		assertEquals("failure - Repository attribute not match", cv.getTitle(), "sample cfp 2");
		assertEquals("failure - Details attribute not match", cv.getDetails(), "Sample details for cfp 2");
		assertEquals("failure - Description attribute not match", cv.getDescription(), "Sample description for cfp 2");
		assertEquals("failure - Institution attribute not match", cv.getInstitution(), "Sample institution for cfp 2");
		assertEquals("failure - Repository attribute not match", cv.getRepo().getTitle(), "Test Title 1");
    }


    @Test
    @WithUserDetails("admin@dareon.org")
    public void CfpUpdateAndReadWebTest() throws Exception
    {
	this.mockMvc.perform(post("/callforproposals/create")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .param("id", "1") //create CFP to update
		.param("title", "test_cfp1")
		.param("institution", "test_cfp_institution1")
		.param("details", "test_cfp_details") // modified contents
		.param("description", "test_cfp_description1")
		.param("repo", "1")) // test_repo1
     	//check if redirected to CFP list page - /callforproposals/list
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("list"))
		.andReturn();
    	
	
    	long numberOfCfpPriorToUpdate = cfpService.list().size();
    	
    	this.mockMvc.perform(post("/callforproposals/create")
		.contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .param("id", "1") //search for CFP to update
		.param("title", "test_cfp1")
		.param("institution", "test_cfp_institution1")
		.param("details", "Modified details") // modified contents
		.param("description", "test_cfp_description1")
		.param("repo", "1")) // test_repo1
     	//check if redirected to CFP list page - /callforproposals/list
		.andExpect(status().is3xxRedirection())
		.andExpect(redirectedUrl("list"))
		.andReturn();
    	
    	long numberOfCfpAfterUpdate = cfpService.list().size();    	
    	
		// check if the CFP was created 
		MvcResult response = mockMvc.perform(get("/callforproposals/read/test_cfp1"))
		        .andExpect(status().isOk()).andExpect(view().name("callforproposals/read"))
		        .andExpect(model().attributeExists("callForProposals"))
		        .andReturn();
		Object cfpValue = response.getModelAndView().getModel().get("callForProposals");
		assertTrue(cfpValue instanceof CallForProposals);
		CallForProposals cv = (CallForProposals)cfpValue;
		
		assertNotNull("failure - not null", cfpValue);
		//number of CFPs should have not increased
		assertEquals("failure - Number CFPs increased", numberOfCfpPriorToUpdate, numberOfCfpAfterUpdate);
		assertEquals("failure - ID attribute not match", cv.getId(), 1);
		assertEquals("failure - Repository attribute not match", cv.getTitle(), "test_cfp1");
		assertEquals("failure - Details attribute not match", cv.getDetails(), "Modified details");
		assertEquals("failure - Description attribute not match", cv.getDescription(), "test_cfp_description1");
		assertEquals("failure - Institution attribute not match", cv.getInstitution(), "test_cfp_institution1");
		assertEquals("failure - Repository attribute not match", cv.getRepo().getTitle(), "Test Title 1");  
    }

}






/*    @Test
@WithUserDetails("admin@dareon.org")
public void testCfpReadWeb() throws Exception
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


