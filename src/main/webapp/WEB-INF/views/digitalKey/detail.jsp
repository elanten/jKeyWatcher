<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Morozov
  Date: 18.05.2017
  Time: 20:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${digitalKey.name}</title>
    <%@ include file="../fragments/links.jspf" %>
</head>
<body>
<%@include file="../fragments/scripts.jspf" %>
<div class="container">
    <%@ include file="../fragments/menu.jspf" %>
    <div class="row">
        <div class="col-md-12">
            <c:choose>
                <c:when test="${digitalKey.state == 'EXPIRED'}"> <c:set var="sClass" value="danger"/> </c:when>
                <c:when test="${digitalKey.state == 'WARNING'}"> <c:set var="sClass" value="warning"/> </c:when>
                <c:otherwise><c:set var="sClass" value="default"/></c:otherwise>
            </c:choose>
            <div class="panel panel-${sClass}">
                <div class="panel-heading">
                    Инормация по ключу:
                    <a href="<c:url value="/keys/edit/${digitalKey.id}"/>">
                        <span class="glyphicon glyphicon-pencil pull-right"></span>
                    </a>
                </div>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-5">
                            <p><strong>Наименование:</strong> ${digitalKey.name}</p>
                            <p><strong>Номер:</strong> ${digitalKey.serial}</p>
                            <p><strong>Истекает:</strong> <span class="text-${sClass}">${digitalKey.expire}</span></p>
                        </div>
                        <div class="col-md-7">
                            <p><strong>Описание:</strong></p>
                            <pre>${digitalKey.description}</pre>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-6">
            <div class="panel panel-default">
                <div class="panel-heading">Держатели ключа</div>
                <div class="panel-body">
                    <c:forEach var="detail" items="${digitalKey.holders}">
                        <p><a href="<c:url value="/contragents/${detail.id}" />">${detail.name}</a></p>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="panel panel-default">
                <div class="panel-heading">Контакты</div>
                <div class="panel-body">
                    <c:forEach var="detail" items="${digitalKey.contacts}">
                        <p><a href="<c:url value="/contragents/${detail.id}" />">${detail.name}</a></p>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
