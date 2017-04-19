package org.dareon.security;


import java.util.Collection;
import java.util.List;

import org.dareon.domain.Repo;
import org.dareon.domain.User;
import org.dareon.service.UserDetailsImpl;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    //
    public boolean isRepoOwner(String title) {
        final User user = ((UserDetailsImpl) this.getPrincipal()).getUser();
        Collection<Repo> ownedRepos = user.getOwnedRepos();
        for(Repo repo : ownedRepos)
        {
            if(repo.getTitle().equals(title))
        	return true;
        }
        return false;
    }

    //

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }

    @Override
    public void setFilterObject(Object obj) {
        this.filterObject = obj;
    }

    @Override
    public void setReturnObject(Object obj) {
        this.returnObject = obj;
    }

}
