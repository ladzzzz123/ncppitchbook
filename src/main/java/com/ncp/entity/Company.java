package com.ncp.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Company extends NcpEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	


	private String name;
	private String email;
	private String phone;
	private Location location;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	@ManyToOne
	@JoinColumn(name="locationId", referencedColumnName="id")
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	
	
}
