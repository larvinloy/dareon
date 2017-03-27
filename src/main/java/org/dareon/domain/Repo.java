package org.dareon.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table (name = "repos")
public class Repo
{
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;
    
    @Column (nullable= false, unique = true)
    private String name;
    
    @Column (nullable= false)
    private String definition;
    
    @Column (nullable= false)
    private String description;
    
    @OneToOne
    private User user;
    

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public Repo()
    {
    }
    
    public Repo(String name)
    {
	this.setName(name);
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

    public String getDefinition()
    {
        return definition;
    }

    public void setDefinition(String definition)
    {
        this.definition = definition;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
    
    @Override
    public String toString()
    {
	return "Repo [id=" + id + ", name=" + name + ", definition=" + definition + ", description=" + description
		+ ", user=" + user + "]";
    }
}
