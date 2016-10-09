<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="../views/fragments/styles.jsp"/>
	<link href="<c:url value="/resources/css/autocomplete.css" />" rel="stylesheet">
	<script src="<c:url value="/resources/js/jquery.autocomplete.min.js" />"></script>
	<script src="<c:url value="/resources/js/jquery.inputmask.bundle.js" />"></script>

	<title>Заказ ${order.client.name} в СП-${order.sp.id}</title>

</head>
<body>
	<jsp:include page="../views/fragments/header.jsp"/>
	<div class="container">
        
		<legend>
            Заказ <a href='<spring:url value="/client/${order.client.id}"/>'>${order.client.name}</a> в <a href='<spring:url value="/sp/${order.sp.id}"/>'>СП-${order.sp.id}</a>
        </legend>

		<!-- Отображение линейки статусов -->
		<ul class="breadcrumb">
			<c:set var="i" value="0"/>
			<c:forEach var="status" items="${orderStatuses}">
				<c:if test="${order.status != status and i == 0}">
					<li class="before-status"><i class="fa fa-check" aria-hidden="true"></i>${status.toString()}</li>
				</c:if>
				<c:if test="${order.status == status}">
					<li><span class="active">${status.toString()}</span></li>
					<c:set var="i" value="1"/>
				</c:if>
				<c:if test="${order.status != status and i == 2}">
					<li>${status.toString()}</li>
				</c:if>
				<c:if test="${order.status != status and i == 1}">
					<li><a href="">${status.toString()}</a></li>
					<c:set var="i" value="2"/>
				</c:if>
			</c:forEach>
		</ul>

		<!-- Форма заказа -->
		<spring:url value="/order/${order.id}" var="formUrl" />
		<form:form action="${formUrl}" method="POST" modelAttribute="order" cssClass="form-horizontal">

			<input type="hidden" name="action" value="update"/>
			<input type="hidden" name="id" value="${order.id}"/>
			<input type="hidden" name="client" value="${order.client.name}"/>
			<input type="hidden" name="sp" value="${order.sp.id}"/>
			<c:forEach var="orderPosition" items="${order.orderPositions}" varStatus="i">
			<input type="hidden" name="orderPositions[${i.count-1}]" value="${orderPosition.id}"/>
			</c:forEach>

			<fieldset>
				<c:set var="field" value="status"/>
				<spring:bind path="${field}">
					<div class="form-group">
						<label for="${field}" class="col-lg-2 control-label">Статус</label>
						<div class="col-lg-2">
							<form:select path="${field}" items="${orderStatuses}" itemValue="id" itemLabel="name" onchange="showHideOrderFields()" id="${field}" cssClass="form-control"/>
							<span class="help-block nowrap"><form:errors path="${field}"/></span>
						</div>
					</div>
				</spring:bind>

                <c:set var="dateOrdered"><fmt:formatDate pattern="yyyy-MM-dd" value="${order.dateOrdered}"/></c:set>
                <c:set var="field" value="dateOrdered"/>
                <spring:bind path="${field}">
                    <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" id="${field}-formGroup">
                        <label for="${field}" class="col-lg-2 control-label">Дата заказа</label>
                        <div class="col-lg-2">
                            <form:input path="${field}" type="date" id="${field}" cssClass="form-control" value="${dateOrdered}"/>
                            <span class="help-block nowrap"><form:errors path="${field}"/></span>
                        </div>
                    </div>
                </spring:bind>

				<c:set var="field" value="prepaid"/>
				<spring:bind path="${field}">
					<div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" id="${field}-formGroup">
						<label for="${field}" class="col-lg-2 control-label">Предоплата</label>
						<div class="col-lg-2">
							<form:input path="${field}" id="${field}" cssClass="form-control decimal"/>
							<span class="help-block nowrap"><form:errors path="${field}"/></span>
						</div>
					</div>
				</spring:bind>

				<c:set var="field" value="weight"/>
				<spring:bind path="${field}">
					<div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" id="${field}-formGroup">
						<label for="${field}" class="col-lg-2 control-label">Вес, г</label>
						<div class="col-lg-2">
							<form:input path="${field}" type="number" min="0" step="1" id="${field}" cssClass="form-control"/>
							<span class="help-block nowrap"><form:errors path="${field}"/></span>
						</div>
					</div>
				</spring:bind>

				<c:set var="field" value="deliveryPrice"/>
				<spring:bind path="${field}">
					<div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" id="${field}-formGroup" style="display:none">
						<label for="${field}" class="col-lg-2 control-label">Доставка</label>
						<div class="col-lg-2">
							<form:input path="${field}" id="${field}" cssClass="form-control"/>
							<span class="help-block nowrap"><form:errors path="${field}"/></span>
						</div>
					</div>
				</spring:bind>

				<c:set var="field" value="note"/>
				<spring:bind path="${field}">
					<div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
					<label for="${field}" class="col-lg-2 control-label">Примечание</label>
						<div class="col-lg-5">
							<form:textarea path="${field}" id="${field}" cssClass="form-control" rows="3" maxlength="250"/>
							<span class="help-block nowrap"><form:errors path="${field}"/></span>
						</div>
					</div>
				</spring:bind>

				<div class="form-group">
					<div class="col-lg-10 col-lg-offset-2">
						<button type="reset" class="btn btn-default">Отмена</button>
						<button type="submit" class="btn btn-primary">Сохранить</button>
					</div>
				</div>
			</fieldset>

		</form:form>


		<br/>

		<!-- Таблица позиций -->
		<table class="table table-striped table-hover">
			<thead>
				<tr>
					<th></th>
					<th>#</th>
					<th>Наименование</th>
					<th>Кол-во</th>
					<th>Цена магазина</th>
					<th>Цена закупки</th>
					<th>Цена СП</th>
					<th>Сумма СП</th>
					<th>Прибыль</th>
					<th>Вес, г</th>
				</tr>
			</thead>
			<tbody>

				<!-- Форма добавления позиции -->
				<tr class="valign-top">
					<spring:url value="/order/${order.id}" var="formUrl" />
					<form:form action="${formUrl}" method="POST" modelAttribute="orderPosition" cssClass="form-horizontal">

						<input type="hidden" name="action" value="add_position"/>
						<input type="hidden" name="order" value="${order.id}"/>

						<td></td>

						<td>
							<div class="form-group">
								<button type="submit" class="btn btn-primary" id="addOrderButton" title="Добавить позицию">+</button>
							</div>
						</td>

						<td style="width:auto; white-space: nowrap">
							<c:set var="field" value="product"/>
							<spring:bind path="${field}">
								<div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
									<form:input path="${field}" oninput="clearFieldsIfInvalidProduct()" id="product-search" cssClass="form-control" autofocus="autofocus" minlength="3" maxlength="250" cssStyle="display:inline-block; width: 700px"/>
									<a title="Добавить комментарий" href="" onclick="javascript:showHideNoteField();return false" style="margin-left: 3px"><i class="fa fa-commenting-o" aria-hidden="true" style="display: inline-block"></i></a>
									<span class="help-block"><form:errors path="${field}"/></span>
								</div>
							</spring:bind>

							<c:set var="field" value="note"/>
							<spring:bind path="${field}">
								<div id="orderPosition-${field}" class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="display:none">
									<form:input path="${field}" oninput="clearFieldsIfInvalidProduct()" id="product-search" cssClass="form-control" autofocus="autofocus" minlength="3" maxlength="250" cssStyle="width: 700px" placeholder="Примечание"/>
									<span class="help-block"><form:errors path="${field}"/></span>
								</div>
							</spring:bind>
						</td>
						<td>
							<c:set var="field" value="quantity"/>
							<spring:bind path="${field}">
								<div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="width:60px;">
									<form:input path="${field}" onchange="reCalculate()" cssClass="form-control" type="number" readonly="true" min="1" step="1" value="1"/>
									<span class="help-block"><form:errors path="${field}"/></span>
								</div>
							</spring:bind>
						</td>
						<td>
							<c:set var="field" value="priceOrdered"/>
								<div class="form-group" style="width:80px;">
									<form:input path="${field}" cssClass="form-control" readonly="true"/>
								</div>
						</td>
						<td>
							<c:set var="field" value="priceVendor"/>
								<div class="form-group" style="width:80px;">
									<form:input path="${field}" cssClass="form-control" readonly="true"/>
								</div>
						</td>
						<td>
							<c:set var="field" value="priceSp"/>
							<spring:bind path="${field}">
								<div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="width:80px;">
									<form:input path="${field}" readonly="true" oninput="reCalculate()" cssClass="form-control decimal"/>
									<span class="help-block"><form:errors path="${field}"/></span>
								</div>
							</spring:bind>
						</td>
						<td>
							<div class="form-group" style="width:80px;">
								<input type="text" readonly="readonly" id="priceSpSummary" class="form-control" />
							</div>
						</td>
						<td>
							<div class="form-group" style="width:80px;">
								<input type="text" readonly="readonly" id="income" class="form-control" width="10px"/>
							</div>
						</td>
						<td></td>
					</form:form>
				</tr>

				<!-- Отображение позиций -->

				<c:set var="priceSpSummary" value="0"/>
				<c:set var="incomeSummary" value="0"/>

				<c:forEach var="orderPosition" items="${order.orderPositions}" varStatus="counter">

					<c:set var="priceSpSummary" value="${priceSpSummary + orderPosition.priceSp * orderPosition.quantity}"/>
					<c:set var="incomeSummary" value="${incomeSummary + orderPosition.income}"/>

					<tr>
						<td class="text-nowrap">
							<a title="Редактировать" href='<spring:url value="/order_position/${orderPosition.id}?action=edit"/>' class="inline-block"><i class="fa fa-pencil-square" aria-hidden="true"></i></a>&nbsp;|
							<a title="Удалить" style="color:red" href='<spring:url value="/order_position/${orderPosition.id}?action=delete&from_order=${order.id}"/>' class="inline-block"><i class="fa fa-trash" aria-hidden="true"></i></a></td>
						<td align="center">${counter.count}</td>
						<td>
							${orderPosition.product.name}
							<c:if test="${orderPosition.note != null}">
								<br/><i>(${orderPosition.note})</i>
							</c:if>
						</td>
						<td align="center">${orderPosition.quantity}</td>
						<td>${orderPosition.priceOrdered}</td>
						<td>${orderPosition.priceVendor}</td>
						<td>${orderPosition.priceSp}</td>
						<td>${orderPosition.priceSp * orderPosition.quantity}</td>
						<td>${orderPosition.income}</td>

						<c:if test="${orderPosition.product.weight == 0}">
							<c:set var="classDanger" value="danger"/>
						</c:if>
						<td class="${classDanger}">${orderPosition.product.weight}</td>
					</tr>
				</c:forEach>

				<tr>
					<td colspan="7" align="right">
						ИТОГО
					</td>
					<td><b>${priceSpSummary}</b></td>
					<td><b>${incomeSummary}</b></td>
					<td></td>

				</tr>

			</tbody>
		</table>


	</div>

	<!-- Отображение полей заказа -->
	<script type="text/javascript">
		var prepaidField;
		var weightField;
		var deliveryPriceField;

		$(document).ready(function () {
			prepaidField = document.getElementById("prepaid-formGroup");
			weightField = document.getElementById("weight-formGroup");
			deliveryPriceField = document.getElementById("deliveryPrice-formGroup");
			showHideOrderFields();
			}
		);

		function hideFields(fields) {
			for (var i = 0; i < fields.length; i++) {
				fields[i].setAttribute("style", "display:none");
			}
		}

		function showFields(fields) {
			for (var i = 0; i < fields.length; i++) {
				fields[i].setAttribute("style", "display:block");
			}
		}

		function showHideOrderFields() {
			var orderStatus = document.getElementById("status").value;
			switch (orderStatus) {
				case "1": //Не оплачен
					hideFields([prepaidField, weightField, deliveryPriceField]);
					break;
				case "2": //Оплачен
					hideFields([weightField, deliveryPriceField]);
					showFields([prepaidField]);
					break;
				case "3": //Комплектуется
					hideFields([weightField, deliveryPriceField]);
					showFields([prepaidField]);
					break;
				case "4": //Отправлен
					hideFields([weightField, deliveryPriceField]);
					showFields([prepaidField]);
					break;
				case "5": //Приехал
					showFields([prepaidField, weightField, deliveryPriceField]);
					break;
				case "6": //Получен
					showFields([prepaidField, weightField, deliveryPriceField]);
					break;

			}
		}

	</script>


	<!-- Client autocomplete script -->
	<script type="text/javascript">
		$(document).ready(function () {
			$('#product-search').autocomplete({
				serviceUrl: '${pageContext.request.contextPath}/getProducts',
				paramName: "query",
				delimiter: ",",
				minChars: "3",
				transformResult: function(response) {
					return {
						suggestions: $.map($.parseJSON(response), function(item) {
							return { value: item.name, data: item.price };
						})
					};
				},
				onSelect: function (suggestion) {calculate(suggestion)}
                //onInvalidateSelection: function () {} - глючит
            });
		});

	</script>

	<!-- Рассчёт полей позиции -->
	<script type="text/javascript">
        // Параметры СП
        var percentSp;
        var discount;
        var bankComission;

        // Значения полей формы
        var quantity;
        var priceOrdered;
        var priceVendor;
        var priceSp;

        // Поля формы
        var quantityField;
        var priceOrderedField;
        var priceVendorField;
        var priceSpField;
        var priceSpSummaryField;
        var incomeField;
		var orderPositionNoteField;

        var pickedProduct;

        $(document).ready(function () {
            quantityField = document.getElementById("quantity");
            priceOrderedField = document.getElementById("priceOrdered");
            priceVendorField = document.getElementById("priceVendor");
            priceSpField = document.getElementById("priceSp");
            priceSpSummaryField = document.getElementById("priceSpSummary");
            incomeField = document.getElementById("income");
			orderPositionNoteField = document.getElementById("orderPosition-note");
        });

        function calculatePriceSpSummary() {
            return priceSp * quantity;
        }

        function calculateIncome() {
            return (priceSp - priceVendor) * quantity;
        }

		function calculate (suggestion) {
		    pickedProduct = suggestion;

            percentSp = ${properties.percentSp};
            discount = ${properties.percentDiscount};
            bankComission = ${properties.percentBankComission};

            priceOrdered = suggestion.data;
            quantity = quantityField.value;
            priceVendor = priceOrdered*(1-discount+bankComission);
            priceSp = priceOrdered * (1 + percentSp);

			quantityField.removeAttribute("readonly");
			priceSpField.removeAttribute("readonly");

			priceOrderedField.value = suggestion.data;
			priceVendorField.value = priceVendor.toFixed(2);
			priceSpField.value = priceSp.toFixed(0);
			priceSpSummaryField.value = calculatePriceSpSummary().toFixed(2);
			incomeField.value = calculateIncome().toFixed(2);
		}

		function reCalculate(){
			if(pickedProduct!=null){
				quantity = quantityField.value;
				priceSp = priceSpField.value;
				priceSpSummaryField.value = calculatePriceSpSummary().toFixed(2);
				incomeField.value = calculateIncome().toFixed(2);
			}
        }

        function clearAllFields() {
            priceOrderedField.value = "";
            priceVendorField.value = "";
            priceSpField.value = "";
            priceSpSummaryField.value = "";
            incomeField.value = "";

        	quantityField.setAttribute("readonly", "readonly");
			priceSpField.setAttribute("readonly", "readonly");
        }

        function clearFieldsIfInvalidProduct() {
            if (document.getElementById("product-search").value != pickedProduct.value){
                clearAllFields();
            }
        }

        function showHideNoteField() {
        	var style = orderPositionNoteField.getAttribute("style");
			switch(style.indexOf("none")){
				case -1:
					hideFields([orderPositionNoteField]);
					break;
				default:
					showFields([orderPositionNoteField]);
			}
		}

	</script>


	<!-- Decimals fields mask script -->
	<script type="text/javascript">
		$(window).load(function () {
			var decimalField = document.getElementsByClassName("decimal");
			$(decimalField).inputmask({'mask':"9{1,4}[.9{1,2}]", greedy: false,"placeholder": ""});
		});
	</script>


</body>
</html> 