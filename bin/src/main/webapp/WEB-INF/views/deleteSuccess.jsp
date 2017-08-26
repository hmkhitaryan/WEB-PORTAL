<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>deleted successfully</title>
</head>
<body>
<h1><spring:message code="user.successfully.deleted"/></h1>

<a href="<c:url value='/login'/>" class="btn btn-success custom-width"><spring:message code="button.home.label"/></a>
<div class="pull-right" style="padding-right:50px">
    <a href="?language=en">English</a>|<a href="?language=fr">French</a>
</div>
</body>
</html>
