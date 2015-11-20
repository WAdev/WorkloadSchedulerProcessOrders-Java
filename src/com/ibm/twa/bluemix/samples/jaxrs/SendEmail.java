package com.ibm.twa.bluemix.samples.jaxrs;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.wink.json4j.JSONException;

import com.ibm.twa.bluemix.samples.managers.SendGridManager;
import com.sendgrid.SendGridException;

@ApplicationPath("api")
@Path("/sendemail")
public class SendEmail {
	
	private SendGridManager sgManager = new SendGridManager();
	
	/**
	 * sendEmail
	 * 
	 * Method called by the Workload Scheduler restful step
	 * that check in a Cloudant database all the submissions
	 * and send an email to not completed orders
	 * 
	 * @throws SendGridException, JSONException
	 */
	@GET
	@Produces({MediaType.TEXT_HTML})
	public Response sendEmail() throws SendGridException, JSONException {
		this.sgManager = new SendGridManager();
		this.sgManager.initConnection();
		String response = this.sgManager.send();
		return Response.ok().entity(response).build();
	}
	
}