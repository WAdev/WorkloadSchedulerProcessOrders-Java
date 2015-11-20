/*********************************************************************
 *
 * Licensed Materials - Property of IBM
 * Product ID = 5698-WSH
 *
 * Copyright IBM Corp. 2015. All Rights Reserved.
 *
 ********************************************************************/ 

package com.ibm.twa.bluemix.samples.helpers;

import java.util.Calendar;

public class Submission {
	
	private String _id;
	private String _rev;
	private String address;
	private String subject;
	private String body;
	private String status;
	private Calendar startDate;
	
	public Submission(String _id, String address, String subject, String body){
		this._id 		= _id;
		this.address	= address;
		this.subject	= subject;
		this.body 		= body;
		this.status 	= "submitted";
		this.setStartDate(Calendar.getInstance());
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return this.body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public String get_id() {
		return this._id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String get_rev() {
		return _rev;
	}

	public void set_rev(String _rev) {
		this._rev = _rev;
	}

}