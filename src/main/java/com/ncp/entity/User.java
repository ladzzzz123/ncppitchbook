package com.ncp.entity;

import java.beans.Transient;

import javax.persistence.Entity;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class User extends NcpEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String username;
	private String password;
	private String emailAddress;
	private String emailPassword;
	private String firstName;
	private String lastName;
	private DateTime dateLastRan;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmailAddress() {
		return emailAddress;
	}
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	public String getEmailPassword() {
		return emailPassword;
	}
	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getDateLastRan() {
		return dateLastRan;
	}
	public void setDateLastRan(DateTime dateLastRan) {
		this.dateLastRan = dateLastRan;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
	
	
}
