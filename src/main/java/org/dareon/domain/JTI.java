package org.dareon.domain;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class JTI
{

    @Id
    private String id;

    public JTI()
    {
	super();
    }

    public JTI(final String name)
    {
	super();
	this.id = name;
    }

    //

    public String getId()
    {
	return id;
    }

    public void setId(final String id)
    {
	this.id = id;
    }

    @Override
    public String toString()
    {
	final StringBuilder builder = new StringBuilder();
	builder.append("Privilege [name=").append(id).append("]").append("[id=").append(id).append("]");
	return builder.toString();
    }
}
