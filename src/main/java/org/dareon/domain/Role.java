package org.dareon.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
public class Role
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String role;

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<User>();
    
    @ManyToMany
    @JoinTable(name = "roles_privileges", joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "privilege_id", referencedColumnName = "id"))
    private Collection<Privilege> privileges;

    private Role()
    {

    } //

    public Long getId()
    {
	return id;
    }

    public void setId(Long id)
    {
	this.id = id;
    }

    public String getRole()
    {
	return role;
    }

    public void setRole(String role)
    {
	this.role = role;
    }

    public Set<User> getUsers()
    {
	return users;
    }

    public void setUsers(Set<User> users)
    {
	this.users = users;
    }
    
    public Collection<Privilege> getPrivileges()
    {
        return privileges;
    }

    public void setPrivileges(Collection<Privilege> privileges)
    {
        this.privileges = privileges;
    }

    @Override
    public String toString()
    {
	return "Role [id=" + id + ", role=" + role + "]";
    }

}
