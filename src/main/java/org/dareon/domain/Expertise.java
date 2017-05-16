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
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

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
@Table(name = "expertise", uniqueConstraints = { @UniqueConstraint(columnNames = { "for_id", "user_id" }) })
public class Expertise
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

  
    @ManyToOne(fetch = FetchType.EAGER)
    private FOR fOR;

  
    @ManyToOne()
    private User user;

    private int value;
 

    public Expertise()
    {

    }

   
    public Expertise(User user, FOR fOR, int value)
    {
	super();
	this.fOR = fOR;
	this.user = user;
	this.value = value;
    }


    public FOR getfOR()
    {
        return fOR;
    }


    public void setfOR(FOR fOR)
    {
        this.fOR = fOR;
    }


    public User getUser()
    {
        return user;
    }


    public void setUser(User user)
    {
        this.user = user;
    }


    public int getValue()
    {
        return value;
    }


    public void setValue(int value)
    {
        this.value = value;
    }


    public Long getId()
    {
        return id;
    }


    public void setId(Long id)
    {
        this.id = id;
    }


  

}
