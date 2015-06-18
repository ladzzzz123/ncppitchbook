package com.ncp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Type;
import org.joda.time.DateTime;
import org.springframework.data.domain.Auditable;


@MappedSuperclass
public class NcpEntity implements Auditable<String, Long>, Serializable {
	
	private static final long serialVersionUID = -3199307573144641249L;
	
	private Long id;
	private Long version;
	private String createdBy;
	private DateTime createdDate;
	private String lastModifiedBy;
	private DateTime lastModifiedDate;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Version
	public Long getVersion(){
		return version;
	}
	
	public void setVersion(Long version){
		this.version = version;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(DateTime createdDate) {
		this.createdDate = createdDate;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	@Type(type="org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(DateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	@Transient
	public boolean isNew() {
		if(id==null){
			return true;
		}
		else{
			return false;
		}
	}
	
	
	
	
}
