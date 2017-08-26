<%@ page import="com.egs.account.mapping.UrlMapping" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="LOGOUT_URL" value="<%=UrlMapping.LOGOUT%>"/>
<c:set var="ADD_DOC_URL" value="<%=UrlMapping.ADD_DOCUMENT%>"/>
<c:set var="ENGLISH_LANG_URL" value="<%=UrlMapping.ENGLISH_LANG%>"/>
<c:set var="FRENCH_LANG_URL" value="<%=UrlMapping.FRENCH_LANG%>"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="comment" content="">
    <meta name="author" content="">

    <title>Create an account</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet">

    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
</head>

<body>
<div class="pull-right" style="padding-right:50px">
    <a href="${ENGLISH_LANG_URL}">English</a>|<a href="${FRENCH_LANG_URL}">French</a>
</div>

<div class="container">
    <form id="logoutForm" method="POST" action="${contextPath}/${LOGOUT_URL}">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
    </form>

    <form:form method="POST" modelAttribute="userForm" class="form-signin">
        <h2 class="form-signin-heading"><spring:message code="account.edit.label"/></h2>

        <spring:bind path="firstName">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="firstName" class="form-control" placeholder="First name"
                            autofocus="true"/>
                <form:errors path="firstName"/>
            </div>
        </spring:bind>

        <spring:bind path="lastName">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="lastName" class="form-control" placeholder="Last name"
                            autofocus="true"/>
                <form:errors path="lastName"/>
            </div>
        </spring:bind>

        <spring:bind path="email">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="email" class="form-control" placeholder="Email"
                            autofocus="true"/>
                <form:errors path="email"/>
            </div>
        </spring:bind>

        <spring:bind path="skypeID">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="text" path="skypeID" class="form-control" placeholder="Skype ID"
                            autofocus="true"/>
                <form:errors path="skypeID"/>
            </div>
        </spring:bind>

        <spring:bind path="password">
            <div class="form-group ${status.error ? 'has-error' : ''}">
                <form:input type="password" path="password" class="form-control" placeholder="Password"/>
                <form:errors path="password"/>
            </div>
        </spring:bind>

        <span class="well pull-left">
				<a href="<c:url value='${ADD_DOC_URL}/${userForm.id}'/>" class="btn btn-primary btn-sm"><spring:message
                        code="button.upload.text"/></a>
			</span>

        <button class="btn btn-lg btn-primary btn-block" type="submit"><spring:message code="button.submit.label"/></button>

        <h3><a onclick="document.forms['logoutForm'].submit()"><spring:message code="button.logout.label"/></a></h3>
    </form:form>

</div>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
