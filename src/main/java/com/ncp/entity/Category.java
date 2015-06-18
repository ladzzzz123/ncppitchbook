package com.ncp.entity;

import javax.persistence.Entity;

@Entity
public class Category extends NcpEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String category;


	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}
	
	
	

	
	
}
