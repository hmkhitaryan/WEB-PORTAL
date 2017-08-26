<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Registration Confirmation Page</title>
    <link href="<c:url value='/resources/css/bootstrap.css' />" rel="stylesheet"></link>
    <link href="<c:url value='/resources/css/app.css' />" rel="stylesheet"></link>
</head>
<body>
<div class="pull-right" style="padding-right:50px">
    <a href="?language=en">English</a>|<a href="?language=fr">French</a>
</div>
<div class="generic-container">
    <div class="alert alert-success lead">
        ${success}
    </div>

    <span class="well pull-left">
			<a href="<c:url value='/add-document-${userForm.id}' />"><spring:message code="button.upload.text"/></a>
		</span>
    <span class="well pull-right">
			Go to <a href="<c:url value='/welcome'/>" class="btn btn-success custom-width"><spring:message
            code="button.yourPage.label"/></a>
			<a href="<c:url value='/login'/>" class="btn btn-success custom-width"><spring:message
                    code="button.loginPage.label"/></a>
		</span>
</div>
</body>

</html>