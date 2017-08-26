<%@ page import="com.egs.account.mapping.UrlMapping" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:set var="LOGOUT_URL" value="<%=UrlMapping.LOGOUT%>"/>
<c:set var="ENGLISH_LANG_URL" value="<%=UrlMapping.ENGLISH_LANG%>"/>
<c:set var="FRENCH_LANG_URL" value="<%=UrlMapping.FRENCH_LANG%>"/>

<html>
<head>
    <title>deleted successfully</title>
</head>
<body>
<h1><spring:message code= "user.successfully.deleted"/></h1>

<a href="<c:url value='${LOGOUT_URL}'/>" class="btn btn-success custom-width"><spring:message
        code="button.home.label"/></a>
<div class="pull-right" style="padding-right:50px">
    <a href="${ENGLISH_LANG_URL}">English</a>|<a href="${FRENCH_LANG_URL}">French</a>
</div>
</body>
</html>
