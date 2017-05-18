package org.dareon.wrappers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.dareon.domain.Expertise;
import org.dareon.domain.Classification;
import org.dareon.domain.Repo;
import org.dareon.domain.User;
import org.dareon.service.ClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public class AddExpertiseValueForm
{
    private User user;
    private List<Expertise> expertises;
    private String pre;
  
    public AddExpertiseValueForm()
    {
	super();
	expertises = new ArrayList<Expertise>();
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

  
    public User getUser()
    {
        return user;
    }


    public void setUser(User user)
    {
        this.user = user;
    }
	
    public List<Expertise> getExpertises()
    {
        return expertises;
    }


    public void setExpertises(List<Expertise> expertises)
    {
        this.expertises = expertises;
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
