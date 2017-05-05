package org.dareon.domain;

import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Level
{

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;
    
    private String name;
    
    @OneToMany(mappedBy="level", fetch = FetchType.EAGER)
    private Collection<FOR> fORs;

    
    public Level()
    {
	super();
    }

    public Level(final String name)
    {
	super();
	this.name = name;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Collection<FOR> getfORs()
    {
        return fORs;
    }

    public void setfORs(Collection<FOR> fORs)
    {
        this.fORs = fORs;
    }

  


}
