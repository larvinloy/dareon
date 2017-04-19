package org.dareon;
import static org.mockito.Mockito.mock;

import java.util.Date;

import org.dareon.domain.Repo;
import org.dareon.repository.CFPRepository;
import org.dareon.repository.RepoRepository;
import org.dareon.repository.UserRepository;
import org.dareon.service.CFPService;
import org.dareon.service.RepoService;
import org.dareon.service.UserDetailsImpl;
import org.dareon.service.UserServiceImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class DareonWebAppApplicationTests {
	
private MockMvc mvc;
private UserRepository userRepository;
private RepoRepository repoRepository;
private CFPRepository  cFPRepository;
private RepoService repoService;
private UserServiceImpl userServiceImpl;
private UserDetailsImpl userDetailsImpl;
private CFPService cFPService;

@Autowired
WebApplicationContext webApplicationContext;

@Before
public void setUp(){
	userRepository =mock(UserRepository.class);
	repoRepository=mock(RepoRepository.class);
	cFPRepository=mock(CFPRepository.class);
	RepoService repoService=new RepoService(repoRepository);
	
    mvc=MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    
   
    }
	
@After
public void tearDown(){
	userRepository =null;
	repoRepository=null;
	cFPRepository=null;
	 mvc=null;
}

	
@Test
public void homePageTest(){
	
    String uri="/";
    int status = 0;
		
    try {
	
    status=mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn().getResponse().getStatus();
	
    } catch (Exception e) {

    e.printStackTrace();
	
    }
	
    Assert.assertEquals(200, status);
    
    }


@Test
public void repoCreatePageTest(){
	
	String uri="/repo/create";
	int status=0;
	try {
		status=mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn().getResponse().getStatus();
	
	} catch (Exception e) {
	
	e.printStackTrace();
	}
	Assert.assertEquals(200, status);
	
    }

/*@Test
public void  repoSavePageTest(){}
*/

/*@Test
public void repoEditPageTest(){}
*/

@Test
public void repoListPageTest(){
	
	String uri="/repo/list";
	int status=0;
	try {
		status=mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn().getResponse().getStatus();
	
	} catch (Exception e) {
	
	e.printStackTrace();
	}
	Assert.assertEquals(200, status);
	
	}


@Test
public void callforproposalsCreatPageTest(){
	
	String uri="/callforproposals/create";
	int status=0;
	try {
		status=mvc.perform(MockMvcRequestBuilders.get(uri)).andReturn().getResponse().getStatus();
	
	} catch (Exception e) {
	
	e.printStackTrace();
	}
	Assert.assertEquals(200, status);
	
	}

/* @Test
callforproposalsSavePageTest
*/


/* @Test
callforproposalsEditPageTest
*/


/*@Test
public void callforproposalsListPageTest(){}
*/

/*@Test
public void UsersServiceImplTest(){}
*/

//@Test
//public void RepoServiceTest(){
//	Date date=new Date();
//	Repo repo = new Repo();
//	long id=1234567890;
//	repo.setId(id);
//	repo.setCreatedOn(date);
//	repo.setDefinition("test");
//	repo.setDescription("test");
//    repo.setInstitution("test");
//    repo.setTitle("test");
//    repo.setUser(null);
//    //repoService.save(repo);
//    System.out.println(repo.getDescription());
//  //  Assert.assertEquals(repo, repoService.get(id));
//   // Assert.assertEquals(repo, repoService.save(repo));
//  //  Assert.assertEquals(repo,repoService.list().get(0));
//   
//}


}
