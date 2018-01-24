<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Hey!</title>
<link rel="stylesheet" type="text/css" href="style.css">
<script type="text/javascript"></script>

<script src="js/npm.js"></script>
<script src="js/start.js"></script>
<link href="css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <br>
    <div class="container-fluid">
        <div class="panel panel-success">
            <div class="panel-heading" align="center">
                <h4><b><font color="black" style="font-family: fantasy;">Welcome to FundStarter</font> </b></h4>
            </div>
            <div class="panel-body"align="center">
                  
                <div class="container " style="margin-top: 10%; margin-bottom: 10%;">
    
                    <div class="panel panel-success" style="max-width: 45%;" align="left">
                        
                        <div class="panel-heading form-group">
                            <b><font color="white">
                                Main Menu</font> </b>
                        </div>
						           
						   <div class="panel-body" >
							<c:choose>
								<c:when test="${session.loggedin == true}">
									<p>Welcome, ${session.username}. </p>
									
									<div class="panel-body" >
									<p><u>Would you like to check your balance?</u></p>
									<s:form action="checkbalance" method="post">
										<s:submit />
									</s:form>
									</div>
									
									<div class="panel-body" >
									<p><u>Would you like to create a new project?</u></p>
									<s:form action="createproject" method="post">
										<s:submit />
									</s:form>
									</div>
									
									<div class="panel-body" >
									<p><u>Would you like to list your projects?</u></p>
									<s:form action="listprojectsuser" method="post">
										<s:submit />
									</s:form>
									</div>
									
									<div class="panel-body" >
									<p><u>Would you like to list all projects in the system?</u></p>
									<s:form action="listallprojects" method="post">
										<s:submit />
									</s:form>
									</div>
									
									<div class="panel-body" >
									<p><u>Would you like to list all old projects in the system?</u></p>
									<s:form action="listallprojectsold" method="post">
										<s:submit />
									</s:form>
									</div>
									
									<div class="panel-body" >
									<p><u>Would you like to list all of your rewards?</u></p>
									<s:form action="listrewardsuser" method="post">
										<s:submit />
									</s:form>
									</div>
									
									<div class="panel-body" >
									<p><u>Would you like to add a reward to a Project?</u></p>
									<s:form action="addreward" method="post">
										<s:submit />
									</s:form>
									</div>
									
									<div class="panel-body" >
									<p><u>Would you like to remove a reward from a Project?</u></p>
									<s:form action="removereward" method="post">
										<s:submit />
									</s:form>
									</div>
									
									<div class="panel-body" >
									<p><u>Would you like to pledge?</u></p>
									<s:form action="pledge" method="post">
										<s:submit />
									</s:form>
									</div>
									
									<div class="panel-body" >
									<p><u>Would you like to cancel a project?</u></p>
									<s:form action="cancelproject" method="post">
										<s:submit />
									</s:form>
									</div>
									
									<div class="panel-body" >
									<p><u>Project </u></p>
									<s:form action="projectinbox" method="post">
										<s:submit />
									</s:form>
									</div>
									
								</c:when>
								<c:otherwise>
									<p>Please login.</p>
								</c:otherwise>
							</c:choose>
							</div>
					</div>
							
				</div>
				<div class="panel-footer" align="center"><font style="color: #111">Copyright @2014  <a href="http://mysite.com/">mysite.com</a>, All Rights Reserved. </font></div>
			</div>
		</div>
	</div>

<div>
    <div id="container"><div id="history"></div></div>
    <p><input type="text" placeholder="type to chat" id="myid"></p>
</div>


    </body>


</html>