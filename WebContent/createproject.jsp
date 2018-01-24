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
	<s:form action="createprojectfinished" method="post">
		<s:text name="Project Name:" />
		<s:textfield name="projectname" /><br>
		<s:text name="Project Description:" />
		<s:textfield name="description" /><br>
		<s:text name="Goal:" />
		<s:textfield name="goal" /><br>
		<s:text name="Year:" />
		<s:textfield name="year" /><br>
		<s:text name="Month:" />
		<s:textfield name="month" /><br>
		<s:text name="Day:" />
		<s:textfield name="day" /><br>
		<s:submit />
	</s:form>
</body>
</html>