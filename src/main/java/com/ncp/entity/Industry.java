package com.ncp.entity;

import javax.persistence.Entity;

@Entity
public class Industry extends NcpEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String industry;


	public String getIndustry() {
		return industry;
	}


	public void setIndustry(String industry) {
		this.industry = industry;
	}


	
	
	
	

	
	
}
