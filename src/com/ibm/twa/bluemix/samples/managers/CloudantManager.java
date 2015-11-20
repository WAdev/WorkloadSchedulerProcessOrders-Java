/*********************************************************************
 *
 * Licensed Materials - Property of IBM
 * Product ID = 5698-WSH
 *
 * Copyright IBM Corp. 2015. All Rights Reserved.
 *
 ********************************************************************/ 

package com.ibm.twa.bluemix.samples.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.wink.json4j.JSONException;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.Response;
import com.ibm.twa.bluemix.samples.helpers.Submission;

public class CloudantManager extends Manager {
	
	final static String cloudantServiceName = "cloudantNoSQLDB";
	final static String cloudantDatabaseName = "single-trigger-java-sample";

    private Database db;
    private String subId;
    private CloudantClient cloudantClient;
    private List<Submission> submissions;
    private boolean submitted;

	public CloudantManager(){
		super(cloudantServiceName);
		this.db = null;
		this.cloudantClient = null;
		this.submissions = new ArrayList<Submission>();
		this.subId = "";
		this.submitted = false;
	}
	
	/**
	 * postSubmission
	 * 
	 * Create a Submission and save it on the Cloudant database
	 * 
	 * @param String address: email address to save
	 * @param String subject: email subject to save
	 * @param String body: email body to save
	 * 
	 * @return Cloudant response
	 * 
	 * @throws Exception
	 * 
	 */
	public Response postSubmission(String address, String subject, String body) throws Exception {
		if(this.cloudantClient == null){
			this.connect();
		}
		this.subId = UUID.randomUUID().toString();
		Submission submission = new Submission(this.subId, address, subject, body);
			
		Database db = this.getDbByName(CloudantManager.cloudantDatabaseName);
		Response response = db.post(submission);
		
		this.submitted = true;
		
		return response;
	}
	
	/**
	 * retrieveDatabaseByName
	 * 
	 * Retrieve and return a Database found by name
	 * if not exists it will be created
	 * 
	 * @throws JSONException
	 * 
	 */
	public Database getDbByName(String dbName) throws JSONException{
		Database db = null;
		if(this.cloudantClient==null){
			this.initConnection();
		}
		try{
			db = cloudantClient.database(dbName, true);
		} catch(Exception e){
			e.printStackTrace();
		}
		return db;
	}
	
	public void connect(){
		this.cloudantClient = new CloudantClient(this.getUrl(), this.getUser(), this.getPassword());
	}
	
	/**
	 * getAllSubmissions
	 * 
	 * Retrieve all the submissions in Cloudant database
	 * 
	 * @throws JSONException
	 * 
	 */
	public List<Submission> getAllSubmissions() throws JSONException{
		this.db = this.getDbByName(CloudantManager.cloudantDatabaseName);
		if(db != null){
			this.submissions = db.view("_all_docs").includeDocs(true).query(Submission.class);
		}	
		return this.submissions;
	}
	
	public String getSubId() {
		return subId;
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	public Database getDb() {
		return db;
	}

	public void setDb(Database db) {
		this.db = db;
	}

	public boolean isSubmitted() {
		return submitted;
	}

	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}
}
