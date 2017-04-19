package org.dareon.security;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.dareon.domain.Repo;
import org.dareon.domain.User;
import org.dareon.service.RepoService;
import org.dareon.service.UserDetailsImpl;
import org.dareon.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

public class CustomSecurityExpressionRoot implements MethodSecurityExpressionOperations
{
    protected final Authentication authentication;
    private AuthenticationTrustResolver trustResolver;
    private RoleHierarchy roleHierarchy;
    private Set<String> roles;
    private String defaultRolePrefix = "ROLE_";

    public final boolean permitAll = true;
    public final boolean denyAll = false;
    private PermissionEvaluator permissionEvaluator;

    //

    private Object filterObject;
    private Object returnObject;
    
    
    private RepoService repoService;

    public CustomSecurityExpressionRoot(Authentication authentication, RepoService repoService)
    {
	if (authentication == null)
	{
	    throw new IllegalArgumentException("Authentication object cannot be null");
	}
	this.authentication = authentication;
	this.repoService = repoService;
    }

    @Override
    public final boolean hasAuthority(String authority)
    {
	final Set<String> roleSet = getAuthoritySet();
	if(roleSet.contains(authority))
		return true;
	return false;
    }

    //
    @Transactional
    public boolean isRepoOwner(String title)
    {
//	final User user = ((UserDetailsImpl) this.getPrincipal()).getUser();
//	Collection<Repo> ownedRepos = user.getOwnedRepos();
//	System.out.println(ownedRepos);
//	for (Repo repo : ownedRepos)
//	{
//	    if (repo.getTitle().equals(title))
//		return true;
//	}
//	return false;
	final User user = ((UserDetailsImpl) this.getPrincipal()).getUser();
	Repo repo = repoService.findByTitle(title);
	
	
	    if (repo.getOwner().getEmail().equals(user.getEmail()))
		return true;
	
	return false;
    }
    
    @Transactional
    public boolean isRepoOwner(Repo repo)
    {
	final User user = ((UserDetailsImpl) this.getPrincipal()).getUser();
	if (repo.getOwner().getEmail().equals(user.getEmail()))
	    return true;
	return false;
    }

    //

    @Override
    public final boolean hasAnyAuthority(String... authorities)
    {
	return hasAnyAuthorityName(null, authorities);
    }

    @Override
    public final boolean hasRole(String role)
    {
	return hasAnyRole(role);
    }

    @Override
    public final boolean hasAnyRole(String... roles)
    {
	return hasAnyAuthorityName(defaultRolePrefix, roles);
    }

    private boolean hasAnyAuthorityName(String prefix, String... roles)
    {
	final Set<String> roleSet = getAuthoritySet();

	for (final String role : roles)
	{
	    final String defaultedRole = getRoleWithDefaultPrefix(prefix, role);
	    if (roleSet.contains(defaultedRole))
	    {
		return true;
	    }
	}

	return false;
    }

    @Override
    public final Authentication getAuthentication()
    {
	return authentication;
    }

    @Override
    public final boolean permitAll()
    {
	return true;
    }

    @Override
    public final boolean denyAll()
    {
	return false;
    }

    @Override
    public final boolean isAnonymous()
    {
	return trustResolver.isAnonymous(authentication);
    }

    @Override
    public final boolean isAuthenticated()
    {
	return !isAnonymous();
    }

    @Override
    public final boolean isRememberMe()
    {
	return trustResolver.isRememberMe(authentication);
    }

    @Override
    public final boolean isFullyAuthenticated()
    {
	return !trustResolver.isAnonymous(authentication) && !trustResolver.isRememberMe(authentication);
    }

    public Object getPrincipal()
    {
	return authentication.getPrincipal();
    }

    public void setTrustResolver(AuthenticationTrustResolver trustResolver)
    {
	this.trustResolver = trustResolver;
    }

    public void setRoleHierarchy(RoleHierarchy roleHierarchy)
    {
	this.roleHierarchy = roleHierarchy;
    }

    public void setDefaultRolePrefix(String defaultRolePrefix)
    {
	this.defaultRolePrefix = defaultRolePrefix;
    }

    private Set<String> getAuthoritySet()
    {
	if (roles == null)
	{
	    roles = new HashSet<String>();
	    Collection<? extends GrantedAuthority> userAuthorities = authentication.getAuthorities();

	    if (roleHierarchy != null)
	    {
		userAuthorities = roleHierarchy.getReachableGrantedAuthorities(userAuthorities);
	    }

	    roles = AuthorityUtils.authorityListToSet(userAuthorities);
	}

	return roles;
    }

    @Override
    public boolean hasPermission(Object target, Object permission)
    {
	return permissionEvaluator.hasPermission(authentication, target, permission);
    }

    @Override
    public boolean hasPermission(Object targetId, String targetType, Object permission)
    {
	return permissionEvaluator.hasPermission(authentication, (Serializable) targetId, targetType, permission);
    }

    public void setPermissionEvaluator(PermissionEvaluator permissionEvaluator)
    {
	this.permissionEvaluator = permissionEvaluator;
    }

    private static String getRoleWithDefaultPrefix(String defaultRolePrefix, String role)
    {
	if (role == null)
	{
	    return role;
	}
	if ((defaultRolePrefix == null) || (defaultRolePrefix.length() == 0))
	{
	    return role;
	}
	if (role.startsWith(defaultRolePrefix))
	{
	    return role;
	}
	return defaultRolePrefix + role;
    }

    @Override
    public Object getFilterObject()
    {
	return this.filterObject;
    }

    @Override
    public Object getReturnObject()
    {
	return this.returnObject;
    }

    @Override
    public Object getThis()
    {
	return this;
    }

    @Override
    public void setFilterObject(Object obj)
    {
	this.filterObject = obj;
    }

    @Override
    public void setReturnObject(Object obj)
    {
	this.returnObject = obj;
    }
}