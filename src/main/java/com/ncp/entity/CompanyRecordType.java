package com.ncp.entity;

import javax.persistence.Entity;

@Entity
public class CompanyRecordType extends NcpEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	
	private String companyRecordType;

	public String getCompanyRecordType() {
		return companyRecordType;
	}

	public void setCompanyRecordType(String companyRecordType) {
		this.companyRecordType = companyRecordType;
	}
	
	
	
}
