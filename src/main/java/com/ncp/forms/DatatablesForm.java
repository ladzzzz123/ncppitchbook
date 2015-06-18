package com.ncp.forms;

public class DatatablesForm {
	
	private Long sEcho;
	private Integer iTotalRecords;
	private Integer iTotalDisplayRecords;
	private Object[] aaData;
	
	
	
	public DatatablesForm(Long sEcho, Integer iTotalRecords, Integer iTotalDisplayRecords, Object[] rowObjects){
		this.sEcho = sEcho;
		this.iTotalRecords = iTotalRecords;
		this.iTotalDisplayRecords = iTotalDisplayRecords;
		this.aaData = rowObjects;
	}
	
	public Long getsEcho() {
		return sEcho;
	}
	public void setsEcho(Long sEcho) {
		this.sEcho = sEcho;
	}
	public Integer getiTotalRecords() {
		return iTotalRecords;
	}
	public void setiTotalRecords(Integer iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}
	public Integer getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}
	public void setiTotalDisplayRecords(Integer iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}
	public Object[] getAaData() {
		return aaData;
	}
	public void setAaData(Object[] aaData) {
		this.aaData = aaData;
	}
	
	

}
