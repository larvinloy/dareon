package org.dareon.domain;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.annotations.GeneratorType;

@Entity()
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="LEVEL", discriminatorType=DiscriminatorType.STRING)
public abstract class anzsrc
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String code;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<anzsrc> children;

    @ManyToOne()
    @JoinColumn()
    private anzsrc parent;

    private String name;

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

    public Set<anzsrc> getChildren()
    {
	return children;
    }

    public void setChildren(Set<anzsrc> children)
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

    public anzsrc getParent()
    {
	return parent;
    }

    public void setParent(anzsrc parent)
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
