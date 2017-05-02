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
    private Set<ANZSRC> children;

    @ManyToOne()
    @JoinColumn()
    private ANZSRC parent;

    private String name;
    
    @ManyToMany(mappedBy = "domains")
    private Set<Repo> repos = new HashSet<Repo>();

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

}
