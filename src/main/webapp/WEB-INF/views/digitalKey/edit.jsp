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
    <form action="<c:url value="/keys/save"/>" method="post" onsubmit="return verify();">
        <div class="row">
            <div class="col-md-12">
                <c:choose>
                    <c:when test="${digitalKey.state == 'EXPIRED'}"> <c:set var="sClass" value="danger"/> </c:when>
                    <c:when test="${digitalKey.state == 'WARNING'}"> <c:set var="sClass" value="warning"/> </c:when>
                    <c:otherwise><c:set var="sClass" value="default"/></c:otherwise>
                </c:choose>

                <input type="hidden" name="keyId" value="${digitalKey.id}">
                <div class="panel panel-${sClass}">
                    <div class="panel-heading">
                        Инормация по ключу:
                        <c:if test="${digitalKey.id != 0}">
                            <a id="btn-remove" href="<c:url value="/keys/remove/${digitalKey.id}"/>">
                                <span class="glyphicon glyphicon-trash pull-right"></span>
                            </a>
                        </c:if>
                    </div>
                    <div class="panel-body">
                        <div class="row">
                            <div class="col-md-5">
                                <div class="form-group">
                                    <label class="control-label" for="name">Наименование:</label>
                                    <input type="text" class="form-control" id="name" name="name"
                                           value="${digitalKey.name}">
                                </div>
                                <div class="form-group">
                                    <label class="control-label" for="serial">Номер:</label>
                                    <input type="text" class="form-control" id="serial" name="serial"
                                           value="${digitalKey.serial}">
                                </div>
                                <div class="form-group">
                                    <label class="control-label" for="expire">Истекает:</label>
                                    <input type="text" class="form-control" id="expire" name="expire"
                                           value="${digitalKey.expire}" placeholder="2017-01-30">
                                </div>
                            </div>
                            <div class="col-md-7">
                                <div class="form-group">
                                    <label class="control-label" for="description">Описание:</label>
                                    <textarea class="form-control"
                                              id="description" name="description"
                                              rows="8"
                                    >${digitalKey.description}</textarea>
                                </div>
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
                        <select class="selectize" name="holders" id="holders" multiple>
                            <c:forEach var="detail" items="${digitalKey.holders}">
                                <option value="${detail.id}" selected>${detail.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="panel panel-default">
                    <div class="panel-heading">Контакты</div>
                    <div class="panel-body">
                        <select class="selectize" name="contacts" id="contacts" multiple>
                            <c:forEach var="detail" items="${digitalKey.contacts}">
                                <option value="${detail.id}" selected>${detail.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
            </div>

        </div>
        <div class="row">
            <div class="col-md-12">
                <input class="btn btn-default" type="submit" value="Save">
                <a href="<c:url value="/keys/${digitalKey.id != 0 ? digitalKey.id : ''}"/>" class="btn btn-default">Cancel</a>
            </div>
        </div>
    </form>
    <script>
        $(function () {
            $(".selectize").selectize({
                valueField: 'id',
                labelField: 'name',
                searchField: 'name',
                options: [],
                create: false,
                persist: false,
                hideSelected: true,
                render: {
                    option: function (item, escape) {
                        return '<div>' + escape(item.name) + '</div>';
                    }
                },
                load: function (query, callback) {
                    if (query.length < 3) return callback();
                    $.ajax({
                        url: '<c:url value="/contragents.json"/>',
                        type: 'GET',
                        data: {
                            search: query
                        },
                        dataType: 'json',
                        error: function () {
                            callback();
                        },
                        success: function (res) {
                            console.log(res);
                            callback(res);
                        }
                    });
                }
            });

            $('#btn-remove').click(function (e) {
                if (!confirm('Вы уверены что хотите удалить запись?')) {
                    e.preventDefault();
                }
            });

            var simplyReg = /^[0-9A-zА-я][0-9A-zА-я\-. #№]{2,}$/;
            var expireReg = /^(\d{4})-(\d{2})-(\d{2})$/;
            var $forms = $('.form-group');
            var $name = $('#name');
            var $serial = $('#serial');
            var $expire = $('#expire');

            $expire.attr('placeholder', moment().format('YYYY-MM-DD'));

            $expire.datetimepicker({
                locale: 'ru',
                format: 'YYYY-MM-DD'
            });

            var inRange = function (num, start, end, _include) {
                if (_include) {
                    return start <= num && num <= end;
                } else {
                    return start < num && num < end;
                }
            };

            window.verify = function () {
                $forms.removeClass("has-error");
                var result = true;

                var name = $name.val().trim();
                var serial = $serial.val().trim();
                var expire = $expire.val().trim();

                if (!name || name.length < 3) {
                    result = false;
                    $name.parent().addClass("has-error");
                }
                if (!serial || serial.length < 3) {
                    result = false;
                    $serial.parent().addClass("has-error");
                }
                var match = expireReg.exec(expire);
                if (!match || match.length !== 4 ||
                    !inRange(match[1], 2000, 2100, true) || //year
                    !inRange(match[2], 1, 12, true) || //month
                    !inRange(match[3], 1, 31, true) //day
                ) {
                    result = false;
                    $expire.parent().addClass("has-error");
                }
                return result;
            }
        });

    </script>
</div>
</body>
</html>
