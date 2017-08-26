<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <a href="<c:url value='/toMap'/>"><spring:message code="document.tomap.text"/></a>

    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="comment" content="">
    <meta name="author" content="">

    <title>Welcome Page</title>
    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">

    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
</head>
<body>

<div class="generic-container">
    <div class="panel panel-default">
        <c:if test="${pageContext.request.userPrincipal.name != null}">
        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <h2>Welcome ${userForm.username}</h2>
        <div class="tablecontainer">
            <table class="table table-hover">
                <thead>
                <tr>
                    <th><spring:message code="id.label"/></th>
                    <th><spring:message code="firstName.label"/></th>
                    <th><spring:message code="lastName.label"/></th>
                    <th><spring:message code="email.label"/></th>
                    <th><spring:message code="dateCreated.label"/></th>
                    <th><spring:message code="skypeID.label"/></th>
                    <th width="100"></th>
                    <th width="100"></th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>${userForm.id} </td>
                    <td>${userForm.firstName}</td>
                    <td>${userForm.lastName} </td>
                    <td>${userForm.email} </td>
                    <td>${userForm.dateRegistered} </td>
                    <td>${userForm.skypeID} </td>
                    <td><a href="<c:url value='/edit-user-${userForm.id}' />"
                           class="btn btn-success custom-width"><spring:message code="button.edit.label"/></a></td>
                    <td><a href="<c:url value='/delete-user-${userForm.id}' />"
                           class="btn btn-danger custom-width"><spring:message code="button.delete.label"/></a>
                    </td>
                </tr>
                <div class="pull-right" style="padding-right:50px">
                    <a href="?language=en">English</a>|<a href="?language=fr">French</a>
                </div>
                </tbody>
            </table>
        </div>
    </div>

    <span class="well pull-left">
		    <a href="<c:url value='/add-document-${userForm.id}'/>"><spring:message code="document.upload.text"/></a>
		</span>

    <span class="well pull-left">
            <a onclick="document.forms['logoutForm'].submit()"><spring:message code="button.logout.label"/></a>
        </span>
    </c:if>
</div>


<!-- /container -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
