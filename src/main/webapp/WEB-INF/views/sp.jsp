<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>

    <jsp:include page="../views/fragments/styles.jsp"/>

    <link href="<c:url value="/resources/css/autocomplete.css" />" rel="stylesheet">
    <script src="<c:url value="/resources/js/jquery.autocomplete.min.js" />"></script>

    <title>СП-${sp.number}</title>

</head>
<body>

	<jsp:include page="../views/fragments/header.jsp"/>

	<div class="container">

        <h2>СП-${sp.number}</h2>

        <!-- Линейка статусов -->
        <ul class="breadcrumb">
            <c:set var="currentStatus" value="${currentSpStatus}"/>
            <c:set var="i" value="0"/>
            <c:forEach var="status" items="${spStatuses}">
                <c:if test="${currentStatus != status and i == 0}">
                    <li class="before-status"><i class="fa fa-check" aria-hidden="true"></i>${status.toString()}</li>
                </c:if>
                <c:if test="${currentStatus == status}">
                    <li><span class="active">${status.toString()}</span></li>
                    <c:set var="i" value="1"/>
                </c:if>
                <c:if test="${currentStatus != status and i == 2}">
                    <li>${status.toString()}</li>
                </c:if>
                <c:if test="${currentStatus != status and i == 1}">
                    <!-- TODO: link? -->
                    <li>${status.toString()}</li>
                    <c:set var="i" value="2"/>
                </c:if>
            </c:forEach>
        </ul>

        <!-- Форма СП -->
        <spring:url value="/sp/${sp.id}" var="formUrl" />
        <form:form action="${formUrl}" method="POST" modelAttribute="sp" cssClass="form-horizontal">

            <input type="hidden" name="action" value="edit_sp"/>
            <input type="hidden" name="id" value="${sp.id}"/>
            <input type="hidden" name="number" value="${sp.number}"/>
            <c:forEach var="order" items="${sp.orders}" varStatus="i">
                <input type="hidden" name="orders" value="${order.id}"/>
            </c:forEach>

            <c:set var="field" value="status"/>
            <spring:bind path="${field}">
                <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
                    <label for="${field}" class="col-lg-2 control-label">Статус</label>
                    <div class="col-lg-2">
                        <form:select path="${field}" items="${SpStatuses}" itemValue="id" itemLabel="name" id="${field}" cssClass="form-control"/>
                        <span class="help-block nowrap"><form:errors path="${field}"/></span>
                    </div>
                </div>
            </spring:bind>

            <c:set var="field" value="percent"/>
            <spring:bind path="${field}">
                <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
                    <label for="${field}" class="col-lg-2 control-label">Коэффициент</label>
                    <div class="col-lg-1">
                        <form:input path="${field}" id="${field}" cssClass="form-control decimal" type="number" min="0" max="0.16" step="0.01"/>
                        <span class="help-block nowrap"><form:errors path="${field}"/></span>
                    </div>
                </div>
            </spring:bind>

            <c:set var="dateStart"><fmt:formatDate pattern="yyyy-MM-dd" value="${sp.dateStart}"/></c:set>
            <c:set var="field" value="dateStart"/>
            <spring:bind path="${field}">
                <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" id="${field}-formGroup">
                    <label for="${field}" class="col-lg-2 control-label">Дата начала сбора</label>
                    <div class="col-lg-2">
                        <form:input path="${field}" type="date" id="${field}" cssClass="form-control" value="${dateStart}"/>
                        <span class="help-block nowrap"><form:errors path="${field}"/></span>
                    </div>
                </div>
            </spring:bind>

            <c:set var="dateEnd"><fmt:formatDate pattern="yyyy-MM-dd" value="${sp.dateEnd}"/></c:set>
            <c:set var="field" value="dateEnd"/>
            <spring:bind path="${field}">
                <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" id="${field}-formGroup">
                    <label for="${field}" class="col-lg-2 control-label">Дата окончания сбора</label>
                    <div class="col-lg-2">
                        <form:input path="${field}" type="date" id="${field}" cssClass="form-control" value="${dateEnd}"/>
                        <span class="help-block nowrap"><form:errors path="${field}"/></span>
                    </div>
                </div>
            </spring:bind>

            <c:set var="dateToPay"><fmt:formatDate pattern="yyyy-MM-dd" value="${sp.dateToPay}"/></c:set>
            <c:set var="field" value="dateToPay"/>
            <spring:bind path="${field}">
                <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" id="${field}-formGroup">
                    <label for="${field}" class="col-lg-2 control-label">Дата окончания оплаты</label>
                    <div class="col-lg-2">
                        <form:input path="${field}" type="date" id="${field}" cssClass="form-control" value="${dateToPay}"/>
                        <span class="help-block nowrap"><form:errors path="${field}"/></span>
                    </div>
                </div>
            </spring:bind>

            <div class="form-group">
                <div class="col-lg-10 col-lg-offset-2">
                    <button type="submit" class="btn btn-primary">Сохранить</button>
                </div>
            </div>

        </form:form>

        <!-- Форма добавления нового заказа -->
        <spring:url value="/sp/${sp.id}" var="formUrl" />
        <form:form action="${formUrl}" method="POST" modelAttribute="order" cssClass="form-horizontal">
            <fieldset>
                <legend>Добавить заказ</legend>

                <input type="hidden" name="action" value="add_order">

                <spring:bind path="client">
                    <div class="form-group <c:if test='${status.errors.hasFieldErrors("client")}'>has-error</c:if>">
                        <label for="client-search" class="col-lg-2 control-label">Клиент</label>
                        <div class="col-lg-3">
                            <form:input path="client" cssClass="form-control" id="client-search" autofocus="autofocus" minlength="3" value="${newClientName}"/>
                            <span class="help-block">
                                <form:errors path="client"/>
                                <a href='<spring:url value="/create_client?sp=${sp.id}"/>'>Создать нового клиента</a>
                            </span>
                        </div>
                    </div>
                </spring:bind>

                <spring:bind path="dateOrdered">
                    <div class="form-group <c:if test='${status.errors.hasFieldErrors("dateOrdered")}'>has-error</c:if>">
                        <label for="date-picker" class="col-lg-2 control-label">Дата заказа</label>
                        <div class="col-lg-2">
                            <form:input path="dateOrdered" type="date" cssClass="form-control" id="date-picker"/>
                            <span class="help-block">
                                <form:errors path="dateOrdered"/>
                            </span>
                        </div>
                    </div>
                </spring:bind>

                <input type="hidden" name="sp" value="${sp.id}">
                <input type="hidden" name="orderStatus" value="1">

                <div class="form-group">
                    <div class="col-lg-10 col-lg-offset-2">
                        <button type="submit" class="btn btn-primary" id="addOrderButton">Создать</button>
                    </div>
                </div>

            </fieldset>
        </form:form>

        <legend>Заказы</legend>

        <!-- Отображение заказов -->
        <c:if test="${sp.orders.size() > 0}">
            <c:forEach var="order" items="${sp.orders}" varStatus="counter">
                <b>
                    <a href='<spring:url value="/order/${order.id}"/>'>${counter.count}</a>
                    . ${order.client.name}</b> - ${order.summaryPrice} р.<br/>
                <c:forEach var="orderPositionFromList" items="${order.orderPositions}" varStatus="counter2">
                    - ${orderPositionFromList.product.name}; ${orderPositionFromList.priceOrdered} р.<br/>
                    <c:if test="${orderPositionFromList.note != null}">
                        <span class="text-muted"><i>&nbsp;&nbsp;${orderPositionFromList.note}</i></span><br/>
                    </c:if>
                </c:forEach>
            </c:forEach>
        </c:if>

        <c:if test="${sp.orders.size() == 0}">
            В этом СП пока нет заказов.
        </c:if>


    </div>


    <!-- Автозаполнение текущей даты -->
    <script type="text/javascript">
        document.getElementById('date-picker').valueAsDate = new Date();
    </script>

	<!-- Client autocomplete script -->
    <script type="text/javascript">
		$(document).ready(function () {
                $('#client-search').autocomplete({
                    serviceUrl: '${pageContext.request.contextPath}/getClients',
                    paramName: "query",
                    minChars: "3",
                    transformResult: function(response) {
                        return {
                            suggestions: $.map($.parseJSON(response), function(item) {
                                return { value: item.name, data: item.id };
                            })
                        };
                    },
                    onSelect: function (suggestion) {document.getElementById("client-id").setAttribute("value", suggestion.data)}
                });
		});

	</script>

</body>
</html> 