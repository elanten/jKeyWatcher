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
    <form action="<c:url value="/contragents/save"/>" method="post" onsubmit="return verify();">
        <input type="hidden" name="id" value="${contragent.id}">
        <div class="row">
            <div class="col-md-12">
                <div class="panel panel-default">
                    <div class="panel-heading">
                        Контрагент:
                        <c:if test="${contragent.id != 0}">
                            <a id="btn-remove" href="<c:url value="/contragents/remove/${contragent.id}"/>">
                                <span class="glyphicon glyphicon-trash pull-right"></span>
                            </a>
                        </c:if>
                    </div>
                    <div class="panel-body">
                        <div class="rov">
                            <div class="col-md-4">
                                <div class="form-group">
                                    <label class="control-label" for="name">Наименование:</label>
                                    <input type="text" class="form-control" id="name" name="name"
                                           value="${contragent.name}">
                                </div>
                            </div>
                            <div class="col-md-8">
                                <div class="form-group">
                                    <label class="control-label" for="description">Описание:</label>
                                    <textarea class="form-control"
                                              id="description" name="description"
                                              rows="3"
                                    >${contragent.description}</textarea>
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
                    <div class="panel-heading">
                        Контактные данные:
                        <a id="contact-add" href="#">
                            <span class="glyphicon glyphicon-plus pull-right"></span>
                        </a>
                    </div>
                    <div class="panel-body" style="min-height:250px;">
                        <table id="contact-table" class="contact-table">
                            <tbody>
                            <tr id="tr-proto">
                                <td>
                                    <select name="ctype" class="form-control select-noarrow">
                                        <c:forEach var="type" items="${types}">
                                            <option value="${type.key}"> ${type.value} </option>
                                        </c:forEach>
                                    </select>
                                </td>
                                <td>
                                    <input name="cvalue" class="form-control" type="text" value="">
                                </td>
                                <td><span class="btn glyphicon glyphicon-remove" data-id=""></span></td>
                            </tr>
                            <c:forEach var="detail" items="${contragent.contactDetails}">
                                <tr>
                                    <td>
                                        <select name="ctype" class="form-control select-noarrow">
                                            <c:forEach var="type" items="${types}">
                                                <option value="${type.key}" ${type.key == detail.typeId ? 'selected' : ''}>
                                                        ${type.value}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </td>
                                    <td>
                                        <input name="cvalue" class="form-control" type="text" value="${detail.value}">
                                    </td>
                                    <td><span class="btn glyphicon glyphicon-remove"></span></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <input type="submit" value="Save" class="btn btn-default">
                <a href="<c:url value="/contragents/${contragent.id != 0 ? contragent.id : ''}"/>"
                   class="btn btn-default">Cancel</a>
            </div>
        </div>
    </form>

    <script>
        $(function () {
            var $tRow = $('#tr-proto').removeAttr('id').detach();
            var $table = $('#contact-table');

            var $forms = $('.form-group');
            var $name = $('#name');

            var onRemove = function () {
                $(this).parent().parent().remove();
            };
            var addDetail = function (e) {
                e.preventDefault();
                var $new = $tRow.clone();
                $new.find('span.btn').click(onRemove);
                $table.append($new);
            };
            $table.find('span.btn').click(onRemove);
            $('#contact-add').click(addDetail);

            $('#btn-remove').click(function (e) {
               if(!confirm('Вы уверены что хотите удалить запись?')){
                   e.preventDefault();
               }
            });

            window.verify = function (e) {
                var result = true;
                var name = $name.val().trim();
                $forms.removeClass("has-error");

                if(!name || name.length < 3){
                    $name.parent().addClass("has-error");
                    result = false;
                }

                return result;
            };

        });
    </script>
</div>
</body>
</html>
