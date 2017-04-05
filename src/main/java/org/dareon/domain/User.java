package org.dareon.domain;

import java.util.HashSet;
import java.util.Set;

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

    // Setting relation
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
	    @JoinColumn(name = "role_id") })
    private Set<Role> roles = new HashSet<Role>();

    @OneToMany(mappedBy="creator",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Repo> createdRepos = new HashSet<Repo>();
    
    @OneToMany(mappedBy="owner",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Repo> ownedRepos = new HashSet<Repo>();

    private User()
    {
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

    public Set<Role> getRoles()
    {
	return roles;
    }

    public void setRoles(Set<Role> roles)
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

    @Override
    public String toString()
    {
	return "User [id=" + id + ", email=" + email + ", password=" + password + "]";
    }

}
