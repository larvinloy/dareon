package org.dareon.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.GeneratorType;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

//@JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id")

@Entity()
@Table(name = "classification")
public class Classification
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String code;

    
//    @JsonBackReference
    @OneToMany(mappedBy = "parent", fetch = FetchType.EAGER)
    private List<Classification> children;
    
   
    //    @JsonManagedReference
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="parent_id")
    private Classification parent;

    private String name;
    
    @ManyToMany(mappedBy = "domains")
    private Set<Repo> repos = new HashSet<Repo>();
    
    @OneToMany(mappedBy="classification", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH})
    @Fetch(value = FetchMode.SUBSELECT)
    private Set<Expertise> expertises = new HashSet<Expertise>();
    
    public Classification()
    {
	
    }
    public Classification(String code, String name)
    {
	super();
	this.code = code;
	this.name = name;
	
    }

    @Transient()
    public boolean isLeaf()
    {
	return (children == null || children.size() == 0);
    }

    @Transient()
    public boolean isRoot()
    {
	return (parent == null);
    }

    public List<Classification> getChildren()
    {
	return children;
    }

    public void setChildren(List<Classification> children)
    {
	this.children = children;
    }

    public String getName()
    {
	return name;
    }

    public void setName(String name)
    {
	this.name = name;
    }

    public Classification getParent()
    {
	return parent;
    }

    public void setParent(Classification parent)
    {
	this.parent = parent;
    }

    public Long getId()
    {
	return id;
    }

    protected void setId(Long id)
    {
	this.id = id;
    }

    public String getCode()
    {
	return code;
    }

    public void setCode(String code)
    {
	this.code = code;
    }

    public Set<Repo> getRepos()
    {
        return repos;
    }

    public void setRepos(Set<Repo> repos)
    {
        this.repos = repos;
    }
    
    public Set<Expertise> getExpertises()
    {
        return expertises;
    }
    
    public void setExpertises(Set<Expertise> expertises)
    {
        this.expertises = expertises;
    }
    
     

}
