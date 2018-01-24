<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Hey!</title>
</head>
<body>
	<p>Listing all messages of ${session.inboxprojectname}.</p>
	<s:form action="checkinboxfinished" method="post">
		<p>All messages: ${session.allmessageslist} </p>
		<p>Back to email menu? <p>
		<s:submit />
	</s:form>
	
	<s:form action="replyinbox" method="post">
		<p>Would you like to reply to a user?</p>
		<s:text name="User:" />
		<s:textfield name="replyusername" /><br>
		<s:text name="Message:" />
		<s:textfield name="replymessage" /><br>
		<s:submit />
	</s:form>
</body>
</html>