<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="../views/fragments/styles.jsp"/>
	<link href="<c:url value="/resources/css/autocomplete.css" />" rel="stylesheet">
	<script src="<c:url value="/resources/js/jquery.autocomplete.min.js" />"></script>

	<title>Заказ ${order.client.name} в СП-${order.sp.id}</title>

</head>
<body>
	<jsp:include page="../views/fragments/header.jsp"/>
	<div class="container">

		<legend>Заказ ${order.client.name} в СП-${order.sp.id}</legend>

		<spring:url value="/order/${order.id}" var="formUrl" />
		<form:form action="${formUrl}" method="POST" modelAttribute="orderPosition" cssClass="form-horizontal">

			<fieldset>

				<spring:bind path="orderPosition.product">
					<div class="form-group <c:if test='${status.errors.hasFieldErrors("product")}'>has-error</c:if>">
						<label for="product-picker" class="col-lg-2 control-label">Товар</label>
						<div class="col-lg-8">
							<form:input path="product" cssClass="form-control" id="product-picker" autofocus="autofocus" minlength="3" maxlength="250"/>
							<span class="help-block">
                                <form:errors path="product"/>
                            </span>
						</div>
					</div>
				</spring:bind>

				<spring:bind path="quantity">
					<div class="form-group <c:if test='${status.errors.hasFieldErrors("quantity")}'>has-error</c:if>">
						<label for="quantity-picker" class="col-lg-2 control-label">Кол-во</label>
						<div class="col-lg-1">
							<form:input path="quantity" cssClass="form-control" id="quantity-picker" type="number" min="1" step="1" value="1"/>
							<span class="help-block">
                                <form:errors path="quantity"/>
                            </span>
						</div>

						<label for="quantity-picker2" class="col-lg-2 control-label">Цена магазина</label>
						<div class="col-lg-1">
							<form:input path="priceOrdered" cssClass="form-control" id="quantity-picker2" type="number" min="1" step="1" value="1"/>
							<span class="help-block">
                                <form:errors path="priceOrdered"/>
                            </span>
						</div>

						<label for="quantity-picker3" class="col-lg-2 control-label">Цена закупки</label>
						<div class="col-lg-1">
							<form:input path="priceVendor" cssClass="form-control" id="quantity-picker3" type="number" min="1" step="1" value="1"/>
							<span class="help-block">
                                <form:errors path="priceVendor"/>
                            </span>
						</div>

					</div>
				</spring:bind>
				
			</fieldset>
		
		</form:form>
		
		
		
		
		
		
		
		

		<table class="table table-striped table-hover">
			<thead>
				<tr>
					<th>#</th>
					<th>Наименование</th>
					<th>Кол-во</th>
					<th>Цена магазина</th>
					<th>Цена закупки</th>
					<th>Цена СП</th>
					<th>Сумма СП</th>
					<th>Прибыль</th>
				</tr>
			</thead>
			<tbody>

				<c:set var="priceSpSummary" value="0"/>
				<c:set var="incomeSummary" value="0"/>

				<c:forEach var="orderPosition" items="${order.orderPositions}" varStatus="counter">

					<c:set var="priceSpSummary" value="${priceSpSummary + orderPosition.priceSp * orderPosition.quantity}"/>
					<c:set var="incomeSummary" value="${incomeSummary + orderPosition.income}"/>

					<tr>
						<td>${counter.count}</td>
						<td>
							${orderPosition.product.name}
							<c:if test="${orderPosition.note != null}">
								<br/><i>(${orderPosition.note})</i>
							</c:if>
						</td>
						<td>${orderPosition.quantity}</td>
						<td>${orderPosition.priceOrdered}</td>
						<td>${orderPosition.priceVendor}</td>
						<td>${orderPosition.priceSp}</td>
						<td>${orderPosition.priceSp * orderPosition.quantity}</td>
						<td>${orderPosition.income}</td>
					</tr>

				</c:forEach>

				<tr>
					<td colspan="6" align="right">
						ИТОГО
					</td>
					<td><b>${priceSpSummary}</b></td>
					<td><b>${incomeSummary}</b></td>

				</tr>

			</tbody>
		</table>


	</div>


	<!-- Client autocomplete script -->
	<script type="text/javascript">
		$(document).ready(function () {
			$('#product-picker').autocomplete({
				serviceUrl: '${pageContext.request.contextPath}/getProducts',
				paramName: "query",
				delimiter: ",",
				minChars: "3",
				transformResult: function(response) {
					return {
						suggestions: $.map($.parseJSON(response), function(item) {
							return { value: item.name, data: item.id };
						})
					};
				}
			});
		});

	</script>


</body>
</html> 