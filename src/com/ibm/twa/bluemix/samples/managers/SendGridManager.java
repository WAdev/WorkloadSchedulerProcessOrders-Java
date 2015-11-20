/*********************************************************************
 *
 * Licensed Materials - Property of IBM
 * Product ID = 5698-WSH
 *
 * Copyright IBM Corp. 2015. All Rights Reserved.
 *
 ********************************************************************/ 

package com.ibm.twa.bluemix.samples.managers;

import java.util.List;
import org.apache.wink.json4j.JSONException;
import com.ibm.twa.bluemix.samples.helpers.Submission;
import com.sendgrid.SendGrid;
import com.sendgrid.SendGridException;

public class SendGridManager extends Manager {
	
	final static String sendgridServiceName = "sendgrid";
	private SendGrid sendgrid;
	private SendGrid.Email sgemail;
	
	private List<Submission> submissions;
	private CloudantManager cManager;
	
	public SendGridManager(){
		super(sendgridServiceName);
		this.cManager = new CloudantManager();
	}
	
	/**
	 * send
	 * 
	 * Check all the submissions
	 * and send an email to those has the status not set on "completed"
	 * 
	 * @return SendGrid response
	 * 
	 * @throws JSONException
	 */
	public String send() throws JSONException{
				
		if(!this.isConnected()){
			this.initConnection();
		}
		
		if(!this.cManager.isConnected()){
			this.cManager.initConnection();
		}
		
		System.out.println("Sending emails...");
		
		this.submissions = this.cManager.getAllSubmissions();
		
		String resp = "Email sent to: </br>";
		SendGrid.Response response = null;
		
		for(Submission sub : this.submissions){
			if(!sub.getStatus().equalsIgnoreCase("completed")){
				this.sgemail = new SendGrid.Email();
				try {
					System.out.println("Sending email to: " + sub.getAddress());
					this.sgemail.addTo(sub.getAddress());
					this.sgemail.setFrom("gabdibonaventura@gmail.com");
					this.sgemail.setSubject(sub.getSubject());
					this.sgemail.setText(sub.getBody());
					response = this.sendgrid.send(this.sgemail);	
				} catch (SendGridException e) {
					e.printStackTrace();
					sub.setStatus("failed");
					this.cManager.getDb().update(sub);
				}
				if(response.getStatus()){
					resp += sub.getAddress() + "</br>";
					sub.setStatus("completed");
					this.cManager.getDb().update(sub);
				} else{
					resp += sub.getAddress() + " not sent because: " + response.getMessage();
					sub.setStatus("failed");
					this.cManager.getDb().update(sub);
				}
			}
			this.sgemail = null;
		}
		return resp;
	}
	
	public void connect(){
		this.sendgrid = new SendGrid(this.getUser(), this.getPassword());
	}
	
}