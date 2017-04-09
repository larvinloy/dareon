package org.dareon;

import org.dareon.domain.Repo;
import org.dareon.domain.User;
import org.dareon.repository.RepoRepository;
import org.dareon.repository.UserRepository;
import org.dareon.service.RepoService;
import org.dareon.service.UserService;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;


/**
 * Unit test methods for Dareon - Repository.
 * 
 * @author Rommel Gaddi
 */


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class repoServiceTest {
    @Autowired
    private RepoRepository repoRepository;
    private UserRepository userRepository;
    
    @Before
    public void setUp() {
       
    }

    @After
    public void tearDown() {
        // clean up after each test method
    }

  
    @Test
    //	@WithMockUser(username="s3562412@student.rmit.edu.au")    
    public void testRepoRead() {
    	
    	//Search for repository with ID=1
        Repo repoEntity = repoRepository.findOne((long) 1);
        //User userEntity = userRepository.findByEmail("s3562412@student.rmit.edu.au");

        //Repo expectedRepoDetails = new Repo (1, "test_repo1", "test_definition1", "test_description1", "test_institution1", true, false, null, null);
        //For verification only, check console
        //System.out.println(userEntity);
        
        //Verify results should be the same as:
        //(1, "test_repo1", "test_definition1", "test_description1", "test_institution1", true, false, 1, 1)
        assertNotNull("failure - not null", repoEntity);
        assertEquals("failure - ID attribute not match", repoEntity.getId(), 1);
        assertEquals("failure - Repository attribute not match", repoEntity.getTitle(), "test_repo1");
        assertEquals("failure - Definition attribute not match", repoEntity.getDefinition(), "test_definition1");
        assertEquals("failure - Description attribute not match", repoEntity.getDescription(), "test_description1");
        assertEquals("failure - Institution attribute not match", repoEntity.getInstitution(), "test_institution1");
        assertEquals("failure - Status attribute not match", repoEntity.getStatus(), true);
        assertEquals("failure - Delete Status attribute not match", repoEntity.getDeleteStatus(), false);
        //assertEquals("failure - Creator attribute not match", repoEntity.getCreator(), 1);
        //assertEquals("failure - Owner attribute not match", repoEntity.getOwner() , 1);
        //or use this
        //assertEquals("failure - Creator attribute not match", repoEntity.getCreator(), userRepository.findById((long)1));
        //assertEquals("failure - Owner attribute not match", repoEntity.getOwner() , userRepository.findById((long)1));   
    }


 
    
}    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
/*    @Test
    @WithMockUser(username="s3562412@student.rmit.edu.au")    
	public void testRepoRead() {
    	
   	
    	Search for repository with ID=1
        Repo repoEntity = repoService.findOne((long) 1);
       Repo expectedRepoDetails = new Repo (1, "test_repo1", "test_definition1", "test_description1", "test_institution1", true, false, null, null);
        For verification only, check console
        System.out.println(repoEntity);
        
        Verify results should be the same as:
        Repo [id=1, name=test_repo1, definition=test_definition1, description=test_description1, institution=test_institution1, status=true, deleteStatus=false, creator=User [id=1, email=s3562412@student.rmit.edu.au, password=password], owner=User [id=1, email=s3562412@student.rmit.edu.au, password=password]]
        Assert.assertNotNull("failure - not null", repoEntity);
        Assert.assertEquals("failure - attribute not match", expectedRepoDetails, repoEntity);
    } */

