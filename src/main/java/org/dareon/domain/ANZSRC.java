package org.dareon.domain;

import java.util.HashSet;
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
import javax.persistence.Transient;

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
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="LEVEL", discriminatorType=DiscriminatorType.STRING)
public abstract class ANZSRC
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String code;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//    @JsonBackReference
    private Set<ANZSRC> children;

    @ManyToOne()
    @JoinColumn()
//    @JsonManagedReference
    @JsonIgnore
    private ANZSRC parent;

    private String name;
    
    @ManyToMany(mappedBy = "domains")
    private Set<Repo> repos = new HashSet<Repo>();
    
//    public ANZSRC(String code, String name)
//    {
//	super();
//	this.code = code;
//	this.name = name;
//    }

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

    public Set<ANZSRC> getChildren()
    {
	return children;
    }

    public void setChildren(Set<ANZSRC> children)
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

    public ANZSRC getParent()
    {
	return parent;
    }

    public void setParent(ANZSRC parent)
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
    

}