/*********************************************************************
 *
 * Licensed Materials - Property of IBM
 * Product ID = 5698-WSH
 *
 * Copyright IBM Corp. 2015. All Rights Reserved.
 *
 ********************************************************************/ 

/***
 * 
 */

package com.ibm.twa.bluemix.samples;

import org.apache.wink.json4j.JSONException;
import com.ibm.twa.applab.client.exceptions.InvalidRuleException;
import com.ibm.twa.applab.client.exceptions.WorkloadServiceException;
import com.ibm.twa.bluemix.samples.managers.CloudantManager;
import com.ibm.twa.bluemix.samples.managers.WorkloadSchedulerManager;

public class ProcessOrdersIWS {

	private CloudantManager cManager 			= new CloudantManager();
	private WorkloadSchedulerManager wsManager	= new WorkloadSchedulerManager();

	/**
	 * Methods called by start.jsp page
	 */
	
	/**
	 * appConnect
	 * 
	 * First method called by the start.jsp
	 * Connects and authenticates to the server, exploring the content of
	 * VCAP_SERVICES content.
	 * 
	 */
	public void appConnect() throws JSONException {
		cManager.initConnection();
		wsManager.initConnection();
	}
	
	/**
	 * isConnected
	 * 
	 * Second method called by the start.jsp
	 * Check if the services are connected to the server
	 * 
	 */
	public boolean isConnected() {
		return this.wsManager.isConnected() && this.cManager.isConnected();
	}
	
	/**
	 * existProcess
	 * 
	 * Fourth method called by the start.jsp
	 * Check if the process exists
	 * 
	 */
	public boolean existProcess() {
		return this.wsManager.isProcessExist();
	}
	
	/**
	 * appCheckOrCreateProcess
	 * 
	 * Third method called by the start.jsp
	 * Check if Workload Scheduler process exists and if not exists create it
	 * 
	 * @throws InvalidRuleException, Exception, WorkloadServiceException 
	 */
	public void appCheckOrCreateProcess()
			throws InvalidRuleException, WorkloadServiceException, Exception
	{
		this.wsManager.appCheckOrCreateProcess();
	}
	
	/**
	 * postSubmission
	 * 
	 * Last method called by the start.jsp
	 * 
	 * Create a Submission and save it on the Cloudant database
	 * 
	 * @param String address: email address to save
	 * @param String subject: email subject to save
	 * @param String body: email body to save
	 * 
	 * @throws Exception
	 */
	public void postSubmission(String address, String subject, String body)
			throws Exception
	{	
		this.cManager.postSubmission(address, subject, body);
	}
	
	/**
	 * isConnected
	 * 
	 * Check if the submission is saved in the Cloudant database
	 * 
	 */
	public boolean isSubmitted(){
		return this.cManager.isSubmitted();
	}

}