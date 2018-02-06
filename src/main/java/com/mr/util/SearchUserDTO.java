package com.mr.util;

import org.apache.solr.client.solrj.beans.Field;

public class SearchUserDTO {
	@Field
	private String userName;
	@Field
	private String email;
	@Field
	private String custNo;
	@Field
	private Integer ager;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	public Integer getAger() {
		return ager;
	}
	public void setAger(Integer ager) {
		this.ager = ager;
	}
}
