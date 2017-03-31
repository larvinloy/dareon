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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.dareon.json.JsonDateSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Entity
@Table(name = "repos")
public class Repo
{
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String institution;

    @Basic(optional = false)
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    @Column(nullable = false)
    private String definition;

    @Column(nullable = false)
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="user_id")
    private User user;
    
    private Boolean deleteStatus = true;
    
    @OneToMany(mappedBy="repo",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<CallForProposals> callForProposals = new HashSet<CallForProposals>();

    public Boolean getDeleteStatus()
    {
        return deleteStatus;
    }

    public void setDeleteStatus(Boolean deleteStatus)
    {
        this.deleteStatus = deleteStatus;
    }

    public Repo()
    {
    }

    public User getUser()
    {
	return user;
    }

    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getCreatedOn()
    {
	return createdOn;
    }

    public void setCreatedOn(Date createdOn)
    {
	this.createdOn = createdOn;
    }

    public String getInstitution()
    {
	return institution;
    }

    public void setInstitution(String institution)
    {
	this.institution = institution;
    }

    public void setUser(User user)
    {
	this.user = user;
    }

    public Repo(String name)
    {
	this.setTitle(name);
    }

    public long getId()
    {
	return id;
    }

    public void setId(long id)
    {
	this.id = id;
    }

    public String getTitle()
    {
	return title;
    }

    public void setTitle(String title)
    {
	this.title = title;
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
    
    public Set<CallForProposals> getCallForProposals()
    {
        return callForProposals;
    }

    public void setCallForProposals(Set<CallForProposals> callForProposals)
    {
        this.callForProposals = callForProposals;
    }


    @Override
    public String toString()
    {
	return "Repo [id=" + id + ", name=" + title + ", definition=" + definition + ", description=" + description
		+ ", user=" + user + "]";
    }
}
