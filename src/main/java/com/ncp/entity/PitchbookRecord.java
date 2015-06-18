package com.ncp.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
public class PitchbookRecord extends NcpEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Category category;
	private Industry industry;
	private String monetaryUnit;
	private float monetaryValue;
	private Location location;
	private String description;
	private DateTime recordDate;
	private List<CompanyRecordLink> companyRecordLinks;
	private String city;
	private String state;
	private String country;
	
	@ManyToOne
	@JoinColumn(name="categoryId", referencedColumnName="id")
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	
	@ManyToOne
	@JoinColumn(name="industryId", referencedColumnName="id")
	public Industry getIndustry() {
		return industry;
	}
	public void setIndustry(Industry industry) {
		this.industry = industry;
	}
	
	public String getMonetaryUnit() {
		return monetaryUnit;
	}
	public void setMonetaryUnit(String monetaryUnit) {
		this.monetaryUnit = monetaryUnit;
	}
	public float getMonetaryValue() {
		return monetaryValue;
	}
	public void setMonetaryValue(float monetaryValue) {
		this.monetaryValue = monetaryValue;
	}
	@ManyToOne
	@JoinColumn(name="locationId", referencedColumnName="id")
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(DateTime recordDate) {
		this.recordDate = recordDate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@OneToMany(mappedBy="pitchbookRecord")
	public List<CompanyRecordLink> getCompanyRecordLinks() {
		return companyRecordLinks;
	}
	public void setCompanyRecordLinks(List<CompanyRecordLink> companyRecordLinks) {
		this.companyRecordLinks = companyRecordLinks;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	
}
