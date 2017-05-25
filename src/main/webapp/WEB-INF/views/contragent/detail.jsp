<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Morozov
  Date: 18.05.2017
  Time: 20:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Контакт: ${contragent.name}</title>
    <%@ include file="../fragments/links.jspf" %>
</head>
<body>
<%@include file="../fragments/scripts.jspf" %>
<div class="container">
    <%@ include file="../fragments/menu.jspf" %>

    <div class="row">
        <div class="col-md-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Контрагент:
                    <a href="<c:url value="/contragents/edit/${contragent.id}"/>">
                        <span class="glyphicon glyphicon-pencil pull-right"></span>
                    </a>
                </div>
                <div class="panel-body">
                    <div class="rov">
                        <div class="col-md-4"><strong>${contragent.name}</strong></div>
                        <div class="col-md-8">${contragent.description}</div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-6">
            <div class="panel panel-default">
                <div class="panel-heading">
                    Контактные данные:
                </div>
                <div class="panel-body">
                    <c:forEach var="detail" items="${contragent.contactDetails}">
                        <p><strong>${detail.typeName}:</strong> ${detail.value}</p>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div class="col-md-6">
            <div class="panel panel-default">
                <div class="panel-heading">Ключи:</div>
                <div class="panel-body">
                    <c:forEach var="digitalKey" items="${digitalKeys}">
                        <c:choose>
                            <c:when test="${digitalKey.state == 'EXPIRED'}"><c:set var="sClass" value="danger"/></c:when>
                            <c:when test="${digitalKey.state == 'WARNING'}"><c:set var="sClass" value="warning"/></c:when>
                            <c:otherwise><c:set var="sClass" value="success"/></c:otherwise>
                        </c:choose>
                        <p>
                            <a href="<c:url value="/keys/${digitalKey.id}"/> ">${digitalKey.name}</a>
                            <span class="label label-${sClass}"> ${digitalKey.expire}</span>
                            <c:if test="${digitalKey.holders.contains(contragent)}">
                                <span class="label label-primary">Держатель</span>
                            </c:if>
                            <c:if test="${digitalKey.contacts.contains(contragent)}">
                                <span class="label label-default">Контакт</span>
                            </c:if>
                        </p>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>

</div>
</body>
</html>
