package org.dareon.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table (name = "cfp")
public class CFP
{
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private long id;
    
    @Column (nullable= false, unique = true)
    private String title;
    
    @Basic(optional = false)
    @Column(columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;
    
    @Column (nullable= false,columnDefinition = "TEXT")
    private String description;
    
    @Column (nullable= false,columnDefinition = "TEXT")
    private String details;
    
    @ManyToOne()
    @JoinColumn(name="repo_id")
    private Repo repo;
    
    @OneToMany(mappedBy="cfp", fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH})
    private Set<Proposal> proposals = new HashSet<Proposal>();

   
    public CFP()
    {
	
    }
    
    public CFP(String title, String description, String details, Repo repo)
    {
	super();
	this.title = title;
	this.description = description;
	this.details = details;
	this.repo = repo;
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

    public Repo getRepo()
    {
        return repo;
    }

    public void setRepo(Repo repo)
    {
        this.repo = repo;
    }

    public Set<Proposal> getProposals()
    {
        return proposals;
    }

    public void setProposals(Set<Proposal> proposals)
    {
        this.proposals = proposals;
    }

    
}
