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
</head>
<body>
	<c:choose>
		<c:when test="${session.loggedin == true}">
			<p>Listing rewards of ${session.username}</p>
				<s:form action="listrewardsuserfinished" method="post">
					<p>Your rewards: ${session.userrewardslist} </p>
					<s:submit />
				</s:form>
		</c:when>
		<c:otherwise>
			<p>Welcome, anonymous user. Say HEY to someone.</p>
		</c:otherwise>
	</c:choose>
</body>
</html>