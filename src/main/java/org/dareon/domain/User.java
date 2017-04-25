package org.dareon.domain;

import java.util.Collection;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "users")
public class User
{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false)
    private String institution;
    
    @Basic(optional = false)
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdOn;

    // Setting relation
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
	    @JoinColumn(name = "role_id") })
    private Collection<Role> roles;

    @OneToMany(mappedBy = "creator", cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Repo> createdRepos = new HashSet<Repo>();

    @OneToMany(mappedBy = "owner", cascade = {CascadeType.MERGE, CascadeType.REMOVE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    private Set<Repo> ownedRepos = new HashSet<Repo>();

    public User()
    {
	super();
	
    }

    
    public User(String email, String password, String firstName, String lastName, String institution,
	    Collection<Role> roles)
    {
	super();
	this.email = email;
	this.password = password;
	this.firstName = firstName;
	this.lastName = lastName;
	this.institution = institution;
	this.roles = roles;
    }


    public Long getId()
    {
	return id;
    }

    public void setId(Long id)
    {
	this.id = id;
    }

    public String getEmail()
    {
	return email;
    }

    public void setEmail(String email)
    {
	this.email = email;
    }

    public Set<Repo> getOwnedRepos()
    {
	return ownedRepos;
    }

    public void setOwnedRepos(Set<Repo> ownedRepos)
    {
	this.ownedRepos = ownedRepos;
    }

    public String getPassword()
    {
	return password;
    }

    public void setPassword(String password)
    {
	this.password = password;
    }

    public String getFirstName()
    {
	return firstName;
    }

    public void setFirstName(String firstName)
    {
	this.firstName = firstName;
    }

    public String getLastName()
    {
	return lastName;
    }

    public void setLastName(String lastName)
    {
	this.lastName = lastName;
    }

    public Collection<Role> getRoles()
    {
	return roles;
    }

    public void setRoles(Collection<Role> roles)
    {
	this.roles = roles;
    }

    public Set<Repo> getCreatedRepos()
    {
	return createdRepos;
    }

    public void setCreatedRepos(Set<Repo> createdRepos)
    {
	this.createdRepos = createdRepos;
    }
    
    public String getInstitution()
    {
        return institution;
    }

    public void setInstitution(String institution)
    {
        this.institution = institution;
    }

    public Date getCreatedOn()
    {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn)
    {
        this.createdOn = createdOn;
    }

    
    @Override
    public String toString()
    {
	return "User [id=" + id + ", email=" + email + ", password=" + password + "]";
    }

}
