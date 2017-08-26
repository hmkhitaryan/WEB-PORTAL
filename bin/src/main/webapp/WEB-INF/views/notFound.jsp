<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/htmlcharset=ISO-8859-1">
    <link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap.min.css">
    <title><spring:message code="account.welcome.label"/></title>
</head>
<body>
<section>
    <div class="jumbotron">
        <div class="container">
            <h1 class="alert alert-danger"><spring:message code="user.notFound.text" arguments="${domain}"
                                                           htmlEscape="false" argumentSeparator=";"/></h1>
        </div>
    </div>
</section>
<section>
    <div class="container">
        <p>
            <a href="<spring:url value="/login"/>" class="btn btnprimary">
                <span class="glyphicon-hand-left glyphicon"></span>
                <spring:message code="button.loginPage.label"/>
            </a>
        </p>
    </div>
</section>
</body>
</html>