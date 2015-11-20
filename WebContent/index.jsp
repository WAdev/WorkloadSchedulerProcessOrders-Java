<!DOCTYPE html>
<html lang="en">
   <head>
      <meta charset="utf-8">
      <title>Process customer orders every night using IBM Workload Scheduler</title>
      <meta name="viewport" content="width=device-width, initial-scale=1.0">
      <meta name="description" content="">
      <meta name="author" content="">
      <!--link rel="stylesheet/less" href="less/bootstrap.less" type="text/css" /-->
      <!--link rel="stylesheet/less" href="less/responsive.less" type="text/css" /-->
      <!--script src="js/less-1.3.3.min.js"></script-->
      <!--append ‘#!watch’ to the browser URL, then refresh the page. -->
      <link href="style/bootstrap.min.css" rel="stylesheet">
      <link href="style/sample-container.css" rel="stylesheet">
      <link href="style/jquery.dataTables.css" rel="stylesheet">
      <!-- HTML5 shim, for IE6-8 support of HTML5 elements -->
      <!--[if lt IE 9]>
      <script src="js/html5shiv.js"></script>
      <![endif]-->
      <!-- Fav and touch icons -->
      <link rel="apple-touch-icon-precomposed" sizes="144x144" href="img/apple-touch-icon-144-precomposed.png">
      <link rel="apple-touch-icon-precomposed" sizes="114x114" href="img/apple-touch-icon-114-precomposed.png">
      <link rel="apple-touch-icon-precomposed" sizes="72x72" href="img/apple-touch-icon-72-precomposed.png">
      <link rel="apple-touch-icon-precomposed" href="img/apple-touch-icon-57-precomposed.png">
      <!--link rel="shortcut icon" href="img/favicon.png"-->
      <script type="text/javascript" src="js/jquery.min.js"></script>
      <script type="text/javascript" src="js/bootstrap.min.js"></script>
      <script type="text/javascript" src="js/scripts.js"></script>
      <script type="text/javascript" src="js/jquery.dataTables.js"></script>
      
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
   </head>
    <body>
      <div class="container">
         <div class="row clearfix">
            <div class="col-md-12 column">
               <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
                  <div class="navbar-header">
                     <span class="application-title"> Workload Scheduler sample</span>
                     <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"> <span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span><span class="icon-bar"></span></button> <a class="navbar-brand" href="#"><img class="image-logo" src="http://www.graphicalweb.org/2013/assets/ibm_logo_small.png" /></a>
                  </div>
                  <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                     <ul class="nav navbar-nav">
                        <li class="active" onclick="selectProvisioning()" id="provisioningButton">
                           <a href="#" >Provisioning</a>
                        </li>
                        <li onclick="selectMonitoring()" id="monitoringButton">
                           <a href="#" >Monitor</a>
                        </li>
                     </ul>
                  </div>
               </nav>
               <div class="jumbotron well description">
                  <h1>
                     Java sample
                  </h1>
                  <p>
                   	Process customer orders every night using IBM Workload Scheduler service on Bluemix
                  </p>
                </div>
            </div>
         </div>
         <div class="row clearfix" id="provisioning-container">
            <div class="col-md-4 column provisioning-column">
               <form role="form" name="start" method="post">
                  <div class="form-group">
                     <label for="email">Email</label><input type="text" class="form-control" id="email" name="email" placeholder="insert your email">
                  	 <label for="emailSubject">Subject</label><input type="text" class="form-control" id="emailSubject" name="emailSubject" value="Order processed" readonly>
                  	 <label for="emailBody">Body</label><textarea rows="4" cols="50" class="form-control" id="emailBody" name="emailBody" readonly>Hi, this is an automatic email sent by IBM Workload Scheduler sample app on Bluemix to inform you that your order has been processed!" </textarea>
                  	 <input type="hidden" id="formSubmitted" name="formSubmitted" value="" />
                  	 <button type="submit" class="btn btn-primary" style="margin-top: 30px;" formaction="index.jsp">Start</button>
                  </div>
               </form>
            </div>
         </div>
         
         <div class="row clearfix submit-button-container">
            <div class="col-md-12 column">
               <div class="modal fade" id="actionSubmitted" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
                  <div class="modal-dialog">
                     <div class="modal-content">
                        <div class="modal-header">
                           <!--button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button-->
                           <h4 class="modal-title" id="myModalLabel">
                              Success
                           </h4>
                        </div>
                        <div class="modal-body">
                           Action submitted successfully
                        </div>
                        <div class="modal-footer">
                           <button type="button" class="btn btn-primary" data-dismiss="modal">Close</button> 
                        </div>
                     </div>
                  </div>
               </div>
            </div>
         </div>
         
         <div class="row clearfix">
            <div class="col-md-12 column">
               <div id="monitoring-container" style="display: none">
                  <table id="datatable" class="display" cellspacing="0" width="100%">
                     <thead>
                        <tr>
                           <th>Email</th>
                           <th>Subject</th>
                           <th>Status</th>
                           <th>Start date</th>
                        </tr>
                     </thead>
                     <tfoot>
                        <tr>
                           <th>Email</th>
                           <th>Subject</th>
                           <th>Status</th>
                           <th>Start date</th>
                        </tr>
                     </tfoot>
                     <tbody id ="monitoringTableBody">
                     </tbody>
                  </table>
               </div>
            </div>
         </div>
      </div>
      
      <div class="loading-hide" id="loadingDiv"> 
         Loading data...
      </div>
      
      <div class="loading-hide" id="loadingSubmitDiv"> 
         Submitting request...
      </div>
      
      	<%
      	if(request.getParameter("formSubmitted") != null){
      		request.removeAttribute("formSubmitted");
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
					<script>
						jQuery('#actionSubmitted').modal('show');
						jQuery("#provisioning-container input").blur();
					</script>
					<%
					} else{
						%>
						<script>
						 	alert("Error during the submission (document not submitted)");
						</script>
						<%
					}
				}
				else {
					%>
					<script>
					 	alert("Error during the submission (process doesn't exists)");
					</script>
					<%
				}
	 		}
			else {
				%>
				<script>
				 	alert("Error during the submission (app not connected)");
				</script>
				<%
			}
      	}
	%>
	
   </body>
