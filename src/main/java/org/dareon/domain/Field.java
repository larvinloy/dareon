package org.dareon.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.Table;

@Entity
@Inheritance
@DiscriminatorValue("FIELD")
public class Field extends ANZSRC
{

    public Field()
    {
	
    }
    
    public Field(String code, String name)
    {
	this.setCode(code);
	this.setName(name);
	// TODO Auto-generated constructor stub
    }

}
