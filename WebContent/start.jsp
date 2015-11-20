<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@page import="com.ibm.twa.bluemix.samples.ProcessOrdersIWS"%>

<%!
	//Session key to register in the session
	private static final String WORKLOAD_APP_SESSION_KEY = "WORKLOAD_APP";
%>
<%
	ProcessOrdersIWS poApp;
	if (session.getAttribute(WORKLOAD_APP_SESSION_KEY)!=null){
		poApp = (ProcessOrdersIWS) session.getAttribute(WORKLOAD_APP_SESSION_KEY);
	}else{
		poApp = new ProcessOrdersIWS();
		session.setAttribute(WORKLOAD_APP_SESSION_KEY,poApp);
	}
%>
<title>Process orders every night using IWS service on Bluemix</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="style.css" />
</head>
<body>
	<%	
		poApp.appConnect();
		if (poApp.isConnected()) { 
			poApp.appCheckOrCreateProcess();
			if (poApp.existProcess()) { 
				String address = request.getParameter("email");
				String subject = request.getParameter("emailSubject");
				String body = request.getParameter("emailBody");
				poApp.postSubmission(address, subject, body);
				if(poApp.isSubmitted()){
				%>

				<%
				}
			}
			else {
			     response.setHeader("Refresh", "2; URL=index.jsp");
			}
 		}
		else {
			response.setHeader("Refresh", "2; URL=index.jsp");
		}
	%>
</body>
</html>