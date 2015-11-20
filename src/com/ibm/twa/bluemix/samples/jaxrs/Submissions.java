package com.ibm.twa.bluemix.samples.jaxrs;

import java.util.List;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.wink.json4j.JSONException;

import com.ibm.twa.bluemix.samples.helpers.Submission;
import com.ibm.twa.bluemix.samples.managers.CloudantManager;


@ApplicationPath("api")
@Path("/submissions")
public class Submissions {
	
	private CloudantManager cManager = new CloudantManager();
	
	/**
	 * getSubmissions
	 * 
	 * @return all the documents in the Cloudant database in JSON 
	 * 
	 * @throws JSONException
	 */
	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public Response getSubmissions() throws JSONException{
		this.cManager.initConnection();
		List<Submission> allSubmissions = this.cManager.getAllSubmissions();
		return Response.status(200).entity(allSubmissions).build();
	}
	
//	@GET
//	@Path("{id}")
//	public Response getSubmissionById(@PathParam("id") String id) {
//
//	   return Response.status(200).entity("getSubmissionById is called, id : " + id).build();
//
//	}
}
