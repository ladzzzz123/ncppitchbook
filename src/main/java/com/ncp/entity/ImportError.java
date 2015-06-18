package com.ncp.entity;

import javax.persistence.Entity;

@Entity
public class ImportError extends NcpEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String importText;


	public String getImportText() {
		return importText;
	}


	public void setImportText(String importText) {
		this.importText = importText;
	}


	
}
