/**
 * 
 */
package io.sinistral.example.models;

import javax.persistence.Entity;
import javax.persistence.Id;

import io.ebean.Model;

/**
 * @author jbauer
 *
 */

public class User  
{
	

 
	public enum UserType
	{
		GUEST,MEMBER,AMBASSADOR,ADMIN
	}
	
	@Id
	private Long id = 0L;
	  
	private UserType role = UserType.GUEST;

	public User()
	{

	}
	
	public User(Long id)
	{
		this.id = id;
	}
	
	public User(Long id, UserType type)
	{
		this.id = id;
		this.role = type;
	}


	/**
	 * @return the id
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

 

	/**
	 * @param role the role to set
	 */
	public void setRole(UserType role)
	{
		this.role = role;
	}

 
	/**
	 * @return the role
	 */
	public UserType getRole()
	{
		return role;
	}
	
	
}
