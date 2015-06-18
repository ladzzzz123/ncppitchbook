package com.ncp.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class CompanyRecordLink extends NcpEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private PitchbookRecord pitchbookRecord;
	private Company company;
	private CompanyRecordType companyRecordType;
	
	@ManyToOne
	@JoinColumn(name="pitchbookRecordId", referencedColumnName="id")
	public PitchbookRecord getPitchbookRecord() {
		return pitchbookRecord;
	}
	public void setPitchbookRecord(PitchbookRecord pitchbookRecord) {
		this.pitchbookRecord = pitchbookRecord;
	}
	
	@ManyToOne
	@JoinColumn(name="companyId", referencedColumnName="id")
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}
	
	@ManyToOne
	@JoinColumn(name="companyRecordTypeId", referencedColumnName="id")
	public CompanyRecordType getCompanyRecordType() {
		return companyRecordType;
	}
	public void setCompanyRecordType(CompanyRecordType companyRecordType) {
		this.companyRecordType = companyRecordType;
	}
	
	
	
	


	
	
	
	

	
	
}
