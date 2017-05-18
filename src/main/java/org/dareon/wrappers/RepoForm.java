package org.dareon.wrappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import org.dareon.domain.Classification;
import org.dareon.domain.Repo;
import org.dareon.service.ClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class RepoForm
{
    private Repo repo;
    private String domains;
    private String pre;
  
    public RepoForm(ClassificationService classificationService)
    {
	super();
	repo = new Repo();
	domains = new String();
	// TODO Auto-generated constructor stub
    }
 
    public RepoForm()
    {
	super();
	repo = new Repo();
	domains = new String();
	// TODO Auto-generated constructor stub
    }
    
    public String getPre()
    {
        return pre;
    }

    public void setPre(String pre)
    {
        this.pre = pre;
    }

    public Repo getRepo()
    {
        return repo;
    }
    public void setRepo(Repo repo)
    {
        this.repo = repo;
    }
    public String getDomains()
    {
        return domains;
    }
    public void setDomains(String domains)
    {
        this.domains = domains;
    }
   
    public Collection<Long> getFORCollection()
    {
	String[] ids = domains.split(",");
	Collection<Long> fORs = new ArrayList<Long>();
	for(String id : ids)
	{
	    if(isNumeric(id))
	    {
		fORs.add(Long.parseLong(id));
	    }
	}
	return fORs;
	
    }
    
    public static boolean isNumeric(String str)  
    {  
      try  
      {  
        long d = Long.parseLong(str);  
      }  
      catch(NumberFormatException nfe)  
      {  
        return false;  
      }  
      return true;  
    }
}
