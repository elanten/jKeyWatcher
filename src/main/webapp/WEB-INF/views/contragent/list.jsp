<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Morozov
  Date: 18.05.2017
  Time: 20:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Контрагенты</title>
    <%@ include file="../fragments/links.jspf" %>
</head>
<body>
<%@include file="../fragments/scripts.jspf" %>
<div class="container">
    <%@ include file="../fragments/menu.jspf" %>
    <div id="toolbar">
        <div class="form-inline" role="form">
            <a href="<c:url value="/contragents/new"/>" class="btn btn-default">
                <span class="glyphicon glyphicon-plus"></span>
            </a>
        </div>
    </div>
    <table data-toggle="table"
           data-height="700"
           data-toolbar="#toolbar"
           data-search="true">
        <thead>
        <tr style="display:none;">
            <th data-sortable="true">Контрагент</th>
            <th>Описание</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="detail" items="${contragents}">
            <tr style="display:none;">
                <td><a href="<c:url value="/contragents/${detail.id}"/>">${detail.name}</a></td>
                <td>${detail.description}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

</body>
</html>
