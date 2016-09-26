<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>

<head>
    <div class="pull-right" style="padding-right:50px">
        <a href="?language=en" >English</a>|<a href="?language=fr" >French</a>
    </div>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
    <title>Upload/Download/Delete Documents</title>
    <link href="<c:url value='/resources/css/bootstrap.css' />" rel="stylesheet"></link>
    <link href="<c:url value='/resources/css/app.css' />" rel="stylesheet"></link>
</head>

<body>
<div class="generic-container">
    <div class="panel panel-default">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="lead"><spring:message code= "document.list.text"/></span></div>
        <div class="tablecontainer">
            <table class="table table-hover">
                <c:if test="${documents.size() != 0}">
                    <thead>
                    <tr>
                        <th><spring:message code= "document.number.label"/></th>
                        <th><spring:message code= "document.link.label"/></th>
                        <th><spring:message code= "document.type.label"/></th>
                        <th><spring:message code= "document.insertDate.label"/></th>
                        <th><spring:message code= "document.comment.label"/></th>
                        <th width="100"></th>
                        <th width="100"></th>
                    </tr>
                    </thead>
                </c:if>

                <tbody>
                <c:forEach items="${documents}" var="doc" varStatus="counter">
                    <tr>
                        <td>${counter.index + 1}</td>
                        <td>${doc.link}</td>
                        <td>${doc.type}</td>
                        <td>${doc.insertDate}</td>
                        <td>${doc.comment}</td>
                        <td><a href="<c:url value='/download-document-${user.id}-${doc.id}' />"
                               class="btn btn-success custom-width"><spring:message code= "button.download.label"/></a></td>
                        <td><a href="<c:url value='/delete-document-${user.id}-${doc.id}' />"
                               class="btn btn-danger custom-width"><spring:message code= "button.delete.label"/></a></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
    <div class="panel panel-default">

        <div class="panel-heading"><span class="lead"><spring:message code= "document.new.upload.text"/></span></div>
        <div class="uploadcontainer">
            <form:form method="POST" modelAttribute="fileBucket" enctype="multipart/form-data" class="form-horizontal">

                <div class="row">
                    <div class="form-group col-md-12">
                        <label class="col-md-3 control-lable" for="file"><spring:message code= "document.upload.text"/></label>
                        <div class="col-md-7">
                            <form:input type="file" path="file" id="file" class="form-control input-sm"/>
                            <div class="has-error">
                                <form:errors path="file" class="help-inline"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="form-group col-md-12">
                        <label class="col-md-3 control-lable" for="file"><spring:message code= "account.description.label"/></label>
                        <div class="col-md-7">
                            <form:input type="text" path="comment" id="comment" class="form-control input-sm"/>
                        </div>

                    </div>
                </div>

                <div class="row">
                    <div class="form-actions floatRight">
                        <spring:message code="button.upload.label" var="submitText"/>
                        <input type="submit" class="btn btn-primary btn-sm" value="${submitText}"/>
                    </div>
                </div>

            </form:form>
        </div>
    </div>
    <div class="well">
        <a href="<c:url value='/edit-user-${user.id}'/>"><spring:message code= "document.finnish.upload.text"/></a>
    </div>
</div>
</body>
</html>