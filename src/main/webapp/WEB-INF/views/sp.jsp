<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <br/><br/>

            <!-- Форма добавления нового заказа -->
            <spring:url value="/sp/${sp.id}" var="formUrl" />

            <form:form action="${formUrl}" method="POST" modelAttribute="order" cssClass="form-horizontal">
                <fieldset>
                    <legend>Добавить заказ</legend>

                    <input type="hidden" name="action" value="addOrder">

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
        <c:if test="${sp.orders.size()>0}">

            <c:forEach var="order" items="${sp.orders}" varStatus="counter">

                <b>${counter.count}. ${order.client.name}</b> - ${order.summaryPrice} р.
                <br/>

                <c:forEach var="orderPosition" items="${order.orderPositions}" varStatus="counter2">
                    - ${orderPosition.product.name}; ${orderPosition.priceOrdered} р.<br/>
                </c:forEach>


            </c:forEach>
        </c:if>

        <c:if test="${sp.orders.size()==0}">
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
                    delimiter: ",",
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


    <!-- Стиль для скрытия формы
    <style>
        .newClient{display: none}
    </style>
    -->

    <!-- Скрипт отображения формы нового клиента
    <script type="text/javascript">
        function showHide() {
            var checkbox = document.getElementById("newClientCheckbox");
            var hiddenInputs = document.getElementsByClassName("newClient");
            var label = document.getElementById("clientNameLabel");
            var button = document.getElementById("addOrderButton");

            if (checkbox.checked) {
                hiddenInputs[0].style.display = "block";
                label.innerHTML = "Клиент (ник)";
                button.innerHTML = "Создать клиента и добавить заказ";
            }
            else {
                hiddenInputs[0].style.display = "none";
                label.innerHTML = "Клиент";
                button.innerHTML = "Добавить";
                }
        }
    </script>
    -->

</body>
</html> 