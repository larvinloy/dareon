package org.dareon.wrappers;

import org.dareon.domain.FOR;
import org.dareon.service.FORService;

public class FORForm
{
    private FOR foR;
    private String pre;
    private String parent;
  
    public FORForm(FORService fORService)
    {
    	super();
    	foR = new FOR();
    	parent = new String();
    }
 
    public FORForm()
    {
    	super();
    	foR = new FOR();
    	parent = new String();   	
    }

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}


	public String getPre() {
		return pre;
	}

	public void setPre(String pre) {
		this.pre = pre;
	}

	public FOR getFoR() {
		return foR;
	}

	public void setFoR(FOR foR) {
		this.foR = foR;
	}


	
}
