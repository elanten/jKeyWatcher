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
    <title>Ключи</title>
    <%@ include file="../fragments/links.jspf" %>
</head>
<body>
<%@include file="../fragments/scripts.jspf" %>
<div class="container">

    <%@ include file="../fragments/menu.jspf" %>

    <div class="row">
        <div class="col-md-12">
            <div id="toolbar">
                <div class="form-inline" role="form">
                    <a href="<c:url value="/keys/new"/>" class="btn btn-default">
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
                    <th data-sortable="true">Название</th>
                    <th data-sortable="true">Номер</th>
                    <th data-sortable="true">Истекает</th>
                    <th>Описание</th>
                    <th data-visible="false" data-searchable="false">State</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="digitalKey" items="${digitalKeys}">
                    <c:choose>
                        <c:when test="${digitalKey.state == 'EXPIRED'}"><c:set var="rowClass" value="danger"/></c:when>
                        <c:when test="${digitalKey.state == 'WARNING'}"><c:set var="rowClass" value="warning"/></c:when>
                        <c:otherwise><c:set var="rowClass" value=""/></c:otherwise>
                    </c:choose>
                    <tr class="${rowClass}" style="display:none;">
                        <td><a href="<c:url value="/keys/${digitalKey.id}"/>">${digitalKey.name}</a></td>
                        <td>${digitalKey.serial}</td>
                        <td>${digitalKey.expire}</td>
                        <td>${digitalKey.description}</td>
                        <td>${digitalKey.state}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>
</html>
