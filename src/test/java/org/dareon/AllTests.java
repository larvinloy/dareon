package org.dareon;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.springframework.test.annotation.DirtiesContext;

@RunWith(Suite.class)
@SuiteClasses({ aafAuthenticationTest.class, authorizeDeauthorizeROWebLayerTest.class, cfpServiceWebLayerTests.class,
	proposalServiceWebLayerTests.class, repoServiceWebLayerTests.class, RoUpdatesRepoMetadataWebLayerTests.class,
	UserLoginTests.class })
public class AllTests
{

}
