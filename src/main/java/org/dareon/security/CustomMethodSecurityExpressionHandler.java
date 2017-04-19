package org.dareon.security;

import org.aopalliance.intercept.MethodInvocation;
import org.dareon.service.RepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.AuthenticationTrustResolverImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler
{
    @Autowired
    RepoService repoService;
    
    private final AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication,
	    MethodInvocation invocation)
    {
	if(repoService == null)
	    System.out.println("ERROR");
	// final CustomMethodSecurityExpressionRoot root = new
	// CustomMethodSecurityExpressionRoot(authentication);
	final CustomSecurityExpressionRoot root = new CustomSecurityExpressionRoot(authentication,repoService);
	root.setPermissionEvaluator(getPermissionEvaluator());
	root.setTrustResolver(this.trustResolver);
	root.setRoleHierarchy(getRoleHierarchy());
	return root;
    }
}
