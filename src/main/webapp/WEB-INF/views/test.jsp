<%--
  Created by IntelliJ IDEA.
  User: Morozov
  Date: 18.05.2017
  Time: 16:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Документы</title>
    <%@ include file="fragments/links.jspf" %>
</head>
<body>
<%@include file="fragments/scripts.jspf" %>
<div class="container">

    <%@ include file="fragments/menu.jspf" %>

    <div class="row">
        <div class="col-md-12">
            <p></p>
            <form action="<c:url value="/docs/upload"/>" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label class="control-label" for="file">Истекает:</label>
                    <input type="file" class="form-control" id="file" name="file">
                </div>
                <input type="submit" value="Ok">
            </form>
        </div>
    </div>
</div>

</body>
</html>
