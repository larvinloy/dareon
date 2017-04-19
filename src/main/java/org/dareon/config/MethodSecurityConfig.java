package org.dareon.config;

import org.dareon.security.CustomMethodSecurityExpressionHandler;
import org.dareon.security.CustomPermissionEvaluator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.core.Authentication;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration
{

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler()
    {
	// final DefaultMethodSecurityExpressionHandler expressionHandler = new
	// DefaultMethodSecurityExpressionHandler();
	final CustomMethodSecurityExpressionHandler expressionHandler = new CustomMethodSecurityExpressionHandler();
	expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());
	return expressionHandler;
    }
}
