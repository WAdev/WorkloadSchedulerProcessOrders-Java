/*********************************************************************
 *
 * Licensed Materials - Property of IBM
 * Product ID = 5698-WSH
 *
 * Copyright IBM Corp. 2015. All Rights Reserved.
 *
 ********************************************************************/ 

package com.ibm.twa.bluemix.samples.managers;

import java.net.MalformedURLException;
import java.util.List;
import java.util.TimeZone;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ibm.twa.applab.client.WorkloadService;
import com.ibm.twa.applab.client.exceptions.InvalidRuleException;
import com.ibm.twa.applab.client.exceptions.WorkloadServiceException;
import com.ibm.twa.applab.client.helpers.TriggerFactory;
import com.ibm.twa.applab.client.helpers.WAProcess;
import com.ibm.twa.applab.client.helpers.steps.RestfulStep;

import com.ibm.tws.simpleui.bus.Task;
import com.ibm.tws.simpleui.bus.TaskLibrary;
import com.ibm.tws.simpleui.bus.Trigger;

public class WorkloadSchedulerManager extends Manager{
	
    final static String workloadServiceName = "WorkloadScheduler";
    final static String processLibraryName = "wslib";
    final static String processName = "wspoj";
    
	private boolean debugMode;
	
	private boolean processExist;
	private long tasklibraryid;
	private WorkloadService ws;
	
	private long myProcessId;
	
	private String agentName = "_CLOUD";
	final static String JOB_SECTION_SEPARATOR = "===============================================================";
	
	public WorkloadSchedulerManager(){
		super(workloadServiceName);
		this.setDebugMode(false);
	}
	
	public void connect() {
		try {
			this.ws = new WorkloadService(this.getUrl());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (WorkloadServiceException e) {
			e.printStackTrace();
		}
	}

	/** 
	 * getProcessLibraryByName
	 * 
	 * @param String libName: process library name used for find the process library
	 * @return TaskLibrary
	 * 
	 * @throws WorkloadServiceException 
	 * 
	 * return a process library found by name or null
	 * 
	 */
	private TaskLibrary getProcessLibraryByName(String libName) throws WorkloadServiceException{
		TaskLibrary taskLib = null;
		List<TaskLibrary> libraries = ws.getAllLibraries();
		for(TaskLibrary l : libraries){
			if(l.getName().equals(libName)){
				taskLib = l;
			}
		}
		return taskLib;
	}
	
	/** 
	 * getProcessByName
	 * 
	 * @param TaskLibrary library: process library where find a process by name
	 * @param String processName: process name used for find the process
	 * @return Task
	 * 
	 * @throws Exception, WorkloadServiceException 
	 * 
	 * return a process in a library found by name or null
	 * 
	 */
	public Task getProcessByName(TaskLibrary library, String processName) throws Exception, WorkloadServiceException{
		Task task = null;
		List<Task> tasks = ws.getTasks(library.getId());
		for(Task t: tasks){
			if(t.getName().equals(processName)){
				task =  t;
			}
		}	
		return task;
	}
	
	/**
	 * appCheckOrCreateProcess
	 * 
	 * Check if Workload Scheduler process exists and if not exists create it
	 * 
	 * @throws InvalidRuleException, Exception, WorkloadServiceException 
	 */
	public void appCheckOrCreateProcess() throws InvalidRuleException, Exception, WorkloadServiceException {
		if (this.isConnected()) 
		{	
			this.ws.setTimezone(TimeZone.getTimeZone("America/Chicago"));
			TaskLibrary lib = this.getProcessLibraryByName(WorkloadSchedulerManager.processLibraryName);
			if(lib == null){
				System.out.println("Library not found");
				System.out.println("Creating library...");
				lib = new TaskLibrary();
				lib.setName(WorkloadSchedulerManager.processLibraryName);
				lib.setParentId(-1);
				lib = ws.createTaskLibrary(lib);
			} else{
				System.out.println(lib.getName() + " process library found");
			}
			
			this.tasklibraryid = lib.getId();
			
			Task task = this.getProcessByName(lib, WorkloadSchedulerManager.processName);
			if(task == null){
				System.out.println("Process not found");
				System.out.println("Creating process...");
				
				// Create the Workload Automation Process 
				WAProcess process = new WAProcess("SingleTriggerJavaSample", "Sample application that use IBM Workload Scheduler"); // Process name and description
				
				// Retrieve the tenantId value (from the url property)
				int index = super.getUrl().indexOf("tenantId=") + 9;
				String prefix = super.getUrl().substring(index, index + 2);
				this.agentName = prefix + "_CLOUD";
				
				// Set the Restful URL 
				String url = "http://" + this.getAppURI() + "/api/sendemail/";
				// Create the RestfulStep object
				RestfulStep restStep = new RestfulStep(agentName, url, "application/json", "application/json", RestfulStep.GET_METHOD);
				// Add the RestfulStep to the Workload Automation Process 			
				process.addStep(restStep);
				
				process.setTaskLibraryId(this.tasklibraryid);
				
				// Create triggers to add to the Workload Automation process
				// it has been used the API TriggerFactory.scheduleOn
				Trigger trigger = TriggerFactory.everyDayAt(23, 00);
				
				// Add the Trigger to the Workload Automation process
				process.addTrigger(trigger);

				//
				// Create and Enable the Workload Automation Process
				//
				// After being instantiated, a process has to be created and activated 
				// on the server before it can be triggered to run according 
				// to the specified schedule.
				//
				try {
					System.out.println("Creating and enabling the process");
					Task createdTask = ws.createAndEnableTask(process);
					this.myProcessId = createdTask.getId();
					this.processExist = true;
				} catch (Exception e) {
					System.out.println("Could not connect complete the operation: " + e.getClass().getName() + " " + e.getMessage());
				}
			} else{
				System.out.println(task.getName() + " process found");
				this.processExist = true;
			}
		} else {
			System.out.println("Service not connected, please try again");
		}
	}
	
	public String getAppURI() {
		String uri = null;
		String vcapJSONString = System.getenv("VCAP_APPLICATION");
		if (vcapJSONString != null) {
			JsonObject app = new JsonParser().parse(vcapJSONString).getAsJsonObject();
			JsonArray uris = app.get("application_uris").getAsJsonArray();
			uri = uris.get(0).getAsString();
		}
		return uri;
	}
	
	public WorkloadService getWs() {
		return ws;
	}
	
	public void setWs(WorkloadService ws) {
		this.ws = ws;
	}

	public boolean isProcessExist() {
		return processExist;
	}
	
	public void setProcessExist(boolean processExist) {
		this.processExist = processExist;
	}
	
	public long getTasklibraryid() {
		return tasklibraryid;
	}
	
	public void setTasklibraryid(long tasklibraryid) {
		this.tasklibraryid = tasklibraryid;
	}
	
	public long getMyProcessId() {
		return myProcessId;
	}
	
	public void setMyProcessId(long myProcessId) {
		this.myProcessId = myProcessId;
	}
	
	public String getAgentName() {
		return agentName;
	}
	
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public boolean isDebugMode() {
		return debugMode;
	}

	public void setDebugMode(boolean debugMode) {
		this.debugMode = debugMode;
	}	
}
