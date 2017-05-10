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

    <title>СП-${sp.id}</title>

</head>
<body>

	<jsp:include page="../views/fragments/header.jsp"/>

	<div class="container">

        <h2>СП-${sp.id}</h2>

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

            <c:set var="field" value="status"/>
            <spring:bind path="${field}">
                <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
                    <label for="${field}" class="col-lg-2 control-label">Статус</label>
                    <div class="col-lg-2">
                        <form:select path="${field}" items="${SpStatuses}" itemValue="id" itemLabel="name" id="${field}" cssClass="form-control"/>
                        <span class="help-block nowrap"><form:errors htmlEscape="false" path="${field}"/></span>
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
                    <div class="col-lg-date">
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
                    <div class="col-lg-date">
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
                    <div class="col-lg-date">
                        <form:input path="${field}" type="date" id="${field}" cssClass="form-control" value="${dateToPay}"/>
                        <span class="help-block nowrap"><form:errors path="${field}"/></span>
                    </div>
                </div>
            </spring:bind>

            <c:set var="field" value="deliveryPrice"/>
            <spring:bind path="${field}">
                <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
                    <label for="${field}" class="col-lg-2 control-label">Доставка, р.</label>
                    <div class="col-lg-1">
                        <form:input path="${field}" id="${field}" cssClass="form-control decimal" type="number" min="0"/>
                        <span class="help-block nowrap"><form:errors htmlEscape="false" path="${field}"/></span>
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
                    <c:if test='${status.errors.hasFieldErrors("client")}'>
                        <c:set var="errorClass" value="has-error"/>
                        <c:set var="lineBreak" value="<br/>"/>
                    </c:if>
                    <div class="form-group ${errorClass}">
                        <label for="client-search" class="col-lg-2 control-label">Клиент</label>
                        <div class="col-lg-3">
                            <form:input path="client" cssClass="form-control" id="client-search" autofocus="autofocus" minlength="3" value="${newClientName}"/>
                            <span class="help-block">
                                <form:errors path="client"/>${lineBreak}
                                <a href='<spring:url value="/create_client?sp=${sp.id}"/>'>Создать нового клиента</a>
                            </span>
                        </div>
                    </div>
                </spring:bind>

                <spring:bind path="dateOrdered">
                    <div class="form-group <c:if test='${status.errors.hasFieldErrors("dateOrdered")}'>has-error</c:if>">
                        <label for="date-picker" class="col-lg-2 control-label">Дата заказа</label>
                        <div class="col-lg-date">
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
                <!-- Панель заказа -->
                <div class="panel panel-default" style="width: 800px">

                    <!-- Заголовок -->
                    <div class="panel-heading min-vpadding">
                        <c:if test='${order.status == "UNPAID"}'>
                            <c:set var="labelType" value="label-default"/>
                            <c:set var="orderInfoBlock1_class" value='class="hidden"'/>
                            <c:set var="orderInfoBlock2_class" value='class="hidden"'/>
                        </c:if>
                        <c:if test='${order.status == "PAID"}'>
                            <c:set var="labelType" value="label-info"/>
                            <c:set var="orderInfoBlock1_class" value=""/>
                            <c:set var="orderInfoBlock2_class" value='class="hidden"'/>
                        </c:if>
                        <c:if test='${order.status == "PACKING"}'>
                            <c:set var="labelType" value="label-info"/>
                            <c:set var="orderInfoBlock1_class" value=""/>
                            <c:set var="orderInfoBlock2_class" value='class="hidden"'/>
                        </c:if>
                        <c:if test='${order.status == "SENT"}'>
                            <c:set var="labelType" value="label-warning"/>
                            <c:set var="orderInfoBlock1_class" value=""/>
                            <c:set var="orderInfoBlock2_class" value='class="hidden"'/>
                        </c:if>
                        <c:if test='${order.status == "ARRIVED"}'>
                            <c:set var="labelType" value="label-primary"/>
                            <c:set var="orderInfoBlock1_class" value=""/>
                            <c:set var="orderInfoBlock2_class" value=""/>
                        </c:if>
                        <c:if test='${order.status == "COMPLETED"}'>
                            <c:set var="labelType" value="label-success"/>
                            <c:set var="orderInfoBlock1_class" value=""/>
                            <c:set var="orderInfoBlock2_class" value=""/>
                        </c:if>
                        <span class="label size-normal ${labelType}">${order.status.toString()}</span>&nbsp;|
                        <a href='<spring:url value="/client/${order.client.id}"/>'>${order.client.name}</a>&nbsp;|
                        <a href='<spring:url value="/order/${order.id}"/>'><i class="fa fa-pencil-square" aria-hidden="true"></i>&nbsp;Редактировать</a>&nbsp;|
                        <a href='<spring:url value="/order/${order.id}?action=delete"/>'><i class="fa fa-trash" aria-hidden="true" style="color:red"></i>&nbsp;Удалить</a>
                        <span ${orderInfoBlock1_class}>
                            <br/>
                            Предоплата:&nbsp;${order.prepaid} р.&nbsp;|
                            Долг:&nbsp;${order.debt} р.
                        </span>
                        <span ${orderInfoBlock2_class}>
                            |&nbsp;Доставка:&nbsp;${order.deliveryPrice} р.
                            |&nbsp;К оплате:&nbsp;${order.total} р.
                        </span>

                        <c:if test="${order.note!=null}">
                            <br/>Комментарий: <i>${order.note}</i>
                        </c:if>
                    </div>

                    <!-- Содержание -->
                    <div class="panel-body">
                        <c:if test="${order.orderPositions.size() > 0}">
                            <table class="table table-ultra-condensed table-hover">
                                <c:forEach var="orderPosition" items="${order.orderPositions}" varStatus="orderPositionCounter">
                                    <tr class="no-vmargin">
                                        <td style="width: 20px">${orderPositionCounter.count}.</td>
                                        <td>
                                            ${orderPosition.product.name}
                                            <c:if test='${orderPosition.note != ""}'>
                                                <br/><i>${orderPosition.note}</i>
                                            </c:if>
                                        </td>
                                        <td class="text-right" style="width: 60px">${orderPosition.quantity} шт</td>
                                        <td class="text-right" style="width: 85px">${orderPosition.priceSpSummary} р.</td>
                                    </tr>
                                </c:forEach>
                                <!-- Итоговая строка -->
                                <tr class="border_top">
                                    <td colspan="3">
                                        Прибыль:&nbsp;${order.income} р.&nbsp;|
                                        Вес:&nbsp;${order.weight} г.
                                    </td>
                                    <td class="text-right"><b>${order.summaryPrice} р.</b></td>
                                </tr>
                            </table>
                        </c:if>

                        <c:if test="${order.orderPositions.size() == 0}">
                            <i>В этом заказе пока нет позиций</i>
                        </c:if>
                    </div>
                </div>
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