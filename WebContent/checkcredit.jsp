<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>CheckCredit!</title>
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
										<p>Checking balance of ${session.username}</p>
											<s:form action="checkbalancefinished" method="post">
												<p>${session.saldo} </p>
												 <!--<c:out value="${session.saldo}"/> -->
												<s:submit />
											</s:form>
									</c:when>
									<c:otherwise>
										<p>Welcome, anonymous user. Say HEY to someone.</p>
									</c:otherwise>
								</c:choose>
								</div>
					</div>
							
				</div>
				<div class="panel-footer" align="center"><font style="color: #111">Copyright @2014  <a href="http://mysite.com/">mysite.com</a>, All Rights Reserved. </font></div>
			</div>
		</div>
	</div>
								
</body>
</html>