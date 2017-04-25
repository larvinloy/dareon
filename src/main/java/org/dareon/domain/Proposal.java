package org.dareon.domain;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "proposals")
public class Proposal
{
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Basic(optional = false)
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String details;

    @ManyToOne()
    @JoinColumn(name = "cfp_id")
    private CFP cfp;

    public Proposal()
    {
	
    }
        
    public Proposal(String title, String description, String details, CFP cfp)
    {
	super();
	this.title = title;
	this.description = description;
	this.details = details;
	this.cfp = cfp;
    }


    public long getId()
    {
	return id;
    }

    public void setId(long id)
    {
	this.id = id;
    }

    public Date getCreatedOn()
    {
	return createdOn;
    }

    public void setCreatedOn(Date createdOn)
    {
	this.createdOn = createdOn;
    }

    public String getTitle()
    {
	return title;
    }

    public void setTitle(String title)
    {
	this.title = title;
    }

    public String getDescription()
    {
	return description;
    }

    public void setDescription(String description)
    {
	this.description = description;
    }

    public String getDetails()
    {
	return details;
    }

    public void setDetails(String details)
    {
	this.details = details;
    }

    public CFP getCfp()
    {
        return cfp;
    }

    public void setCfp(CFP cfp)
    {
        this.cfp = cfp;
    }

  

}
