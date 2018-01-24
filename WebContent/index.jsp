<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hey!</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <br>
    <div class="container-fluid">
        <div class="panel panel-success">
            <div class="panel-heading" align="center">
                <h4><b><font color="black" style="font-family: fantasy;">FundStarter</font> </b></h4>
            </div>
            <div class="panel-body"align="center">
                  
                <div class="container " style="margin-top: 10%; margin-bottom: 10%;">
    
                    <div class="panel panel-success" style="max-width: 45%;" align="left">
                        
                        <div class="panel-heading form-group">
                            <b><font color="white">
                                Login Form</font> </b>
                        </div>
                    
                        <div class="panel-body" >

					  <s:form action="login" method="post">
						<p><u>Login</u></p>
							<s:text name="Username:" />
							<s:textfield name="username" /><br>
							<s:text name="Password:" />
							<s:password name="password" /><br>
							<s:submit />
						</s:form>
						</div>
                    
                        <div class="panel-body" >
						<s:form action="register" method="post">
						<p><u>Register</u></p>
							<s:text name="Username:" />
							<s:textfield name="username" /><br>
							<s:text name="Password:" />
							<s:textfield name="password" /><br>
							<s:submit />
						</s:form>

                        </div>
                    </div>
                    
                </div>
                <div class="panel-footer" align="center"><font style="color: #111">Copyright @2014  <a href="http://mysite.com/">mysite.com</a>, All Rights Reserved. </font></div>
            </div>
            
        </div>
    </div>
    </body>
</html>