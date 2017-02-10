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

		<!-- Линейка статусов -->
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
					<!-- TODO: link? -->
					<li>${status.toString()}</li>
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
			<input type="hidden" name="weight" value="${order.weight}"/>
			<c:forEach var="orderPositionFromList" items="${order.orderPositions}" varStatus="i">
			    <input type="hidden" name="orderPositions[${i.count-1}]" value="${orderPositionFromList.id}"/>
			</c:forEach>

			<fieldset>
				<c:set var="field" value="status"/>
				<spring:bind path="${field}">
					<div class="form-group">
						<label for="${field}" class="col-lg-2 control-label">Статус</label>
						<div class="col-lg-2">
							<form:select path="${field}" items="${orderStatuses}" itemValue="id" itemLabel="name" onchange="enableDisableOrderFields()" id="${field}" cssClass="form-control"/>
							<span class="help-block nowrap"><form:errors path="${field}"/></span>
						</div>
					</div>
				</spring:bind>

				<c:set var="field" value="sp"/>
				<spring:bind path="${field}">
					<div class="form-group">
						<label for="${field}" class="col-lg-2 control-label">СП</label>
						<div class="col-lg-2">
							<form:select path="${field}" id="${field}" itemLabel="id" itemValue="id" cssClass="form-control">
								<c:forEach var="spId" items="${availableSpIds}">
									<c:if test="${order.sp.id == spId}">
										<form:option  value="${order.sp}" label="${order.sp.id}"/>
									</c:if>
									<c:if test="${order.sp.id != spId}">
										<form:option value="${spId}"/>
									</c:if>
								</c:forEach>
							</form:select>
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
					<div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
						<label for="${field}" class="col-lg-2 control-label">Предоплата</label>
						<div class="col-lg-2">
							<form:input path="${field}" id="${field}" cssClass="form-control decimal" />
							<span class="help-block nowrap"><form:errors path="${field}"/></span>
						</div>
					</div>
				</spring:bind>

				<c:set var="field" value="deliveryPrice"/>
				<spring:bind path="${field}">
					<div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
						<label for="${field}" class="col-lg-2 control-label">Доставка</label>
						<div class="col-lg-2">
							<form:input path="${field}" id="${field}" cssClass="form-control" />
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

			<!-- Заголовок таблицы -->
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

			<!-- Отображение позиций -->
			<c:forEach var="orderPositionFromList" items="${order.orderPositions}" varStatus="counter">

				<!-- Строка позиции -->
				<c:if test='${orderPosition.id != orderPositionFromList.id}'>
					<tr>
						<td class="text-nowrap">
							<c:if test='${action != "edit_position"}'>
								<a title="Редактировать" href='<spring:url value="/order/${order.id}?action=edit_position&order_position_id=${orderPositionFromList.id}"/>' class="inline-block"><i class="fa fa-pencil-square fa-lg" aria-hidden="true"></i></a>&nbsp;|
								<a title="Удалить" style="color:red" href='<spring:url value="/order/${order.id}?action=delete_position&order_position_id=${orderPositionFromList.id}"/>' class="inline-block"><i class="fa fa-trash fa-lg" aria-hidden="true"></i></a>
							</c:if>
						</td>

						<td align="center">
							${counter.count}
						</td>

						<td>
							${orderPositionFromList.product.name}
							<c:if test="${orderPositionFromList.note != null}">
								<br/><i>(${orderPositionFromList.note})</i>
							</c:if>
						</td>

						<td align="center">${orderPositionFromList.quantity}</td>
						<td>${orderPositionFromList.priceOrdered}</td>
						<td>${orderPositionFromList.priceVendor}</td>
						<td>${orderPositionFromList.priceSp}</td>
						<td>${orderPositionFromList.priceSpSummary}</td>
						<td>${orderPositionFromList.income}</td>
                        <c:set var="danger" value=""/>
                        <c:if test="${orderPositionFromList.product.weight == 0}">
                            <c:set var="danger" value="danger"/>
                            <c:set var="incompleteWeight" value="true"/>
                        </c:if>
						<td class="${danger}">
							${orderPositionFromList.weight}
						</td>

					</tr>
				</c:if>

				<!-- Форма редактирования позиции -->
				<c:if test='${action == "edit_position" and orderPosition.id == orderPositionFromList.id}'>
					<tr class="valign-top">
						<spring:url value="/order/${order.id}" var="formUrl" />
						<form:form action="${formUrl}" id="editOrderPositionForm" method="POST" modelAttribute="orderPosition" cssClass="form-horizontal">
							<input type="hidden" name="action" value="edit_position"/>
							<input type="hidden" name="order" value="${order.id}"/>
							<input type="hidden" name="id" value="${orderPosition.id}"/>

                            <c:set var="submitOrderPositionId" value="submitOrderPosition"/>
							<td class="text-nowrap" style="padding-top: 17px">
                                <button type="submit" title="Сохранить" id="${submitOrderPositionId}" class="btn-link"><i class="fa fa-check" aria-hidden="true" style="color: green"></i></button>&nbsp;|
								<a title="Отмена" style="color:red" href='<spring:url value="/order/${order.id}"/>' class="inline-block"><i class="fa fa-times fa-lg" aria-hidden="true"></i></a>
							</td>

							<td style="padding-top: 17px">
								${counter.count}
							</td>

							<td style="width:auto; white-space: nowrap">

								<c:set var="field" value="product"/>
								<spring:bind path="${field}">
									<div class="form-group no-vmargin <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
										<form:input path="${field}" id="${field}" oninput="clearFieldsIfInvalidProduct()" cssClass="form-control" minlength="3" maxlength="250" cssStyle="display:inline-block; width: 700px"/>
										<a title="Добавить комментарий" href="" onclick="showHideNoteField();return false" style="margin-left: 3px"><i class="fa fa-commenting-o" aria-hidden="true" style="display: inline-block"></i></a>
										<span class="help-block"><form:errors path="${field}"/></span>
									</div>
								</spring:bind>

								<c:set var="field" value="note"/>
								<spring:bind path="${field}">
									<div id="orderPosition-${field}-div"  class="form-group no-vmargin <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="display:none">
										<form:input path="${field}" id="orderPosition-${field}" oninput="clearFieldsIfInvalidProduct()" cssClass="form-control" minlength="3" maxlength="250" cssStyle="width: 700px; font-style:italic" placeholder="Примечание"/>
										<span class="help-block"><form:errors path="${field}"/></span>
									</div>
								</spring:bind>
							</td>

							<td>
								<c:set var="field" value="quantity"/>
								<spring:bind path="${field}">
									<div class="form-group no-vmargin <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="width:60px;">
										<form:input path="${field}" id="${field}" onchange="reCalculate()" cssClass="form-control" type="number" min="1" step="1"/>
										<span class="help-block"><form:errors path="${field}"/></span>
									</div>
								</spring:bind>
							</td>

							<td>
								<c:set var="field" value="priceOrdered"/>
								<div class="form-group no-vmargin" style="width:80px;">
									<form:input path="${field}" id="${field}" cssClass="form-control" readonly="true"/>
								</div>
							</td>

							<td>
								<c:set var="field" value="priceVendor"/>
								<div class="form-group no-vmargin" style="width:80px;">
									<form:input path="${field}" id="${field}" cssClass="form-control" readonly="true"/>
								</div>
							</td>

							<td>
								<c:set var="field" value="priceSp"/>
								<spring:bind path="${field}">
									<div class="form-group no-vmargin <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="width:80px;">
										<form:input path="${field}" id="${field}" oninput="reCalculate()" cssClass="form-control decimal"/>
										<span class="help-block"><form:errors path="${field}"/></span>
									</div>
								</spring:bind>
							</td>

							<td>
								<c:set var="field" value="priceSpSummary"/>
									<div class="form-group no-vmargin <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="width:80px;">
										<form:input path="${field}" id="${field}" readonly="true" cssClass="form-control decimal"/>
									</div>
							</td>

							<td>
								<c:set var="field" value="income"/>
                                <div class="form-group no-vmargin <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="width:80px;">
                                    <form:input path="${field}" id="${field}" readonly="true" cssClass="form-control decimal"/>
                                </div>
							</td>

							<td>
								<c:set var="field" value="weight"/>
                                <div class="form-group no-vmargin <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="width:60px;">
                                    <form:input path="${field}" id="orderPosition-${field}" readonly="true" cssClass="form-control decimal"/>
                                </div>
							</td>
						</form:form>
					</tr>
				</c:if>

			</c:forEach>

			<!-- Форма добавления позиции -->
			<c:if test='${action!="edit_position"}'>
				<tr class="valign-top">
					<spring:url value="/order/${order.id}" var="formUrl" />
					<form:form action="${formUrl}" method="POST" modelAttribute="orderPosition" cssClass="form-horizontal">

						<input type="hidden" name="action" value="add_position"/>
						<input type="hidden" name="order" value="${order.id}"/>

						<td>
							<div class="form-group no-vmargin">
								<c:set var="submitOrderPositionId" value="submitOrderPosition"/>
								<button type="submit" disabled class="btn btn-primary" id="${submitOrderPositionId}" title="Добавить позицию">+</button>
							</div>
						</td>

						<td style="padding-top: 17px">
								${order.orderPositions.size() + 1}
						</td>

						<td style="width:auto; white-space: nowrap">
							<c:set var="field" value="product"/>
							<spring:bind path="${field}">
								<div class="form-group no-vmargin<c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
									<form:input path="${field}" id="${field}" oninput="clearFieldsIfInvalidProduct()" cssClass="form-control" autofocus="autofocus" minlength="3" maxlength="250" cssStyle="display:inline-block; width: 700px"/>
									<a title="Добавить комментарий" href="" onclick='showHideNoteField(); return false' style="margin-left: 3px"><i class="fa fa-commenting-o" aria-hidden="true" style="display: inline-block"></i></a>
									<span class="help-block"><form:errors path="${field}"/></span>
								</div>
							</spring:bind>

							<c:set var="field" value="note"/>
							<spring:bind path="${field}">
								<div id="orderPosition-${field}-div" class="form-group no-vmargin <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="display:none">
									<form:input path="${field}" id="orderPosition-${field}" oninput="clearFieldsIfInvalidProduct()" cssClass="form-control" minlength="3" maxlength="250" cssStyle="width: 700px; font-style:italic" placeholder="Примечание"/>
									<span class="help-block"><form:errors path="${field}"/></span>
								</div>
							</spring:bind>
						</td>

						<td>
							<c:set var="field" value="quantity"/>
							<spring:bind path="${field}">
								<div class="form-group no-vmargin <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="width:60px;">
									<form:input path="${field}" id="${field}" onchange="reCalculate()" cssClass="form-control" type="number" readonly="true" min="1" step="1" value="1"/>
									<span class="help-block"><form:errors path="${field}"/></span>
								</div>
							</spring:bind>
						</td>

						<td>
							<c:set var="field" value="priceOrdered"/>
							<div class="form-group no-vmargin" style="width:80px;">
								<form:input path="${field}" id="${field}" cssClass="form-control" readonly="true"/>
							</div>
						</td>

						<td>
							<c:set var="field" value="priceVendor"/>
							<div class="form-group no-vmargin" style="width:80px;">
								<form:input path="${field}" id="${field}" cssClass="form-control" readonly="true"/>
							</div>
						</td>

						<td>
							<c:set var="field" value="priceSp"/>
							<spring:bind path="${field}">
								<div class="form-group no-vmargin <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="width:80px;">
									<form:input path="${field}" id="${field}" readonly="true" oninput="reCalculate()" cssClass="form-control decimal"/>
									<span class="help-block"><form:errors path="${field}"/></span>
								</div>
							</spring:bind>
						</td>

						<td>
							<c:set var="field" value="priceSpSummary"/>
							<div class="form-group no-vmargin" style="width:80px;">
								<form:input path="${field}" id="${field}" readonly="true" cssClass="form-control"/>
							</div>
						</td>

						<td>
							<c:set var="field" value="income"/>
							<div class="form-group no-vmargin" style="width:80px;">
								<form:input path="${field}" id="${field}" readonly="true" cssClass="form-control"/>
							</div>
						</td>

						<td>
							<c:set var="field" value="weight"/>
							<div class="form-group no-vmargin" style="width:60px;">
								<form:input path="${field}" id="orderPosition-${field}" readonly="true" cssClass="form-control"/>
							</div>
						</td>
					</form:form>
				</tr>
			</c:if>

			<!-- Итоговая строка -->
			<tr>
                <c:set var="danger" value=""/>
                <c:if test='${incompleteWeight == "true"}'>
                    <c:set var="danger" value="danger"/>
                </c:if>
				<td colspan="7" align="right">ИТОГО</td>
				<td><b>${order.summaryPrice}</b></td>
				<td><b>${order.income}</b></td>
				<td class="${danger}"><b>${order.weight}</b></td>

			</tr>

			</tbody>
		</table>


	</div>

	<!-- Отображение полей заказа -->
	<script type="text/javascript">
		var prepaidField;
		var deliveryPriceField;

		$(document).ready(function () {
			prepaidField = document.getElementById("prepaid");
			deliveryPriceField = document.getElementById("deliveryPrice");
			enableDisableOrderFields();
			}
		);

		function disableFields(fields) {
			for (var i = 0; i < fields.length; i++) {
				fields[i].setAttribute("readonly", "readonly");
			}
		}

		function enableFields(fields) {
			for (var i = 0; i < fields.length; i++) {
				fields[i].removeAttribute("readonly");
			}
		}

		function enableDisableOrderFields() {
			var orderStatus = document.getElementById("status").value;
			switch (orderStatus) {
				case "1": //Не оплачен
					disableFields([prepaidField, deliveryPriceField]);
					break;
				case "2": //Оплачен
					disableFields([deliveryPriceField]);
					enableFields([prepaidField]);
					break;
				case "3": //Комплектуется
					disableFields([deliveryPriceField]);
					enableFields([prepaidField]);
					break;
				case "4": //Отправлен
					disableFields([deliveryPriceField]);
					enableFields([prepaidField]);
					break;
				case "5": //Приехал
					enableFields([prepaidField, deliveryPriceField]);
					break;
				case "6": //Получен
					enableFields([prepaidField, deliveryPriceField]);
					break;

			}
		}

	</script>

	<!-- Product autocomplete script -->
	<script type="text/javascript">
		$(document).ready(function () {
			$('#product').autocomplete({
				serviceUrl: '${pageContext.request.contextPath}/getProducts',
				paramName: "query",
				minChars: "3",
				orientation: "auto",
				transformResult: function(response) {
					return {
						suggestions: $.map($.parseJSON(response), function(item) {
							return { value: item.name, data: [item.price, item.weight] };
						})
					};
				},
				onSelect: function (suggestion) {calculate(suggestion)}
                //onInvalidateSelection: function () {} - глючит
            });
		});

	</script>

	<!-- Рассчёт и отображение полей позиции -->
	<script type="text/javascript">

        // Параметры СП
        var percentSp;
        var discount;
        var bankCommission;

        // Значения полей формы
        var quantity;
        var priceOrdered;
        var priceVendor;
        var priceSp;
        var priceSpSummary;
		var weight;

        // Поля формы
		var addButton;
        var productField;
        var quantityField;
        var priceOrderedField;
        var priceVendorField;
        var priceSpField;
        var priceSpSummaryField;
        var incomeField;
		var orderPositionNoteField;
		var orderPositionNoteFieldDiv;
		var weightField;

        var pickedProduct;
		var action;

        $(document).ready(function () {
        	addButton = document.getElementById("submitOrderPosition");
            productField = document.getElementById("product");
            quantityField = document.getElementById("quantity");
            priceOrderedField = document.getElementById("priceOrdered");
            priceVendorField = document.getElementById("priceVendor");
            priceSpField = document.getElementById("priceSp");
            priceSpSummaryField = document.getElementById("priceSpSummary");
            incomeField = document.getElementById("income");
			orderPositionNoteField = document.getElementById("orderPosition-note");
			orderPositionNoteFieldDiv = document.getElementById("orderPosition-note-div");
			weightField = document.getElementById("orderPosition-weight");
			action = "${action}";
			if(action == "edit_position"){
				priceVendor = priceVendorField.value;
				weight = "${orderPosition.product.weight}";
			}else {
				weightField.value = "";
			}
			if(orderPositionNoteField.value != "" && orderPositionNoteField.value != undefined){
				orderPositionNoteFieldDiv.setAttribute("style", "display:block");
			}


        });

        function calculatePriceSpSummary() {
            return priceSp * quantity;
        }

        function calculateIncome() {
            return (priceSp - priceVendor) * quantity;
        }

        function calculateWeight() {
            return weight * quantity;
        }

		function calculate(suggestion) {
			pickedProduct = suggestion;

            percentSp = ${properties.percentSp};
            discount = ${properties.percentDiscount};
            bankCommission = ${properties.percentBankCommission};

            quantity = quantityField.value;
            weight = suggestion.data[1];

            if(productField.value != '${orderPosition.product.name}'){
                priceOrdered = suggestion.data[0];
                priceVendor = priceOrdered*(1-discount+bankCommission);
                priceSp = priceOrdered * (1 + percentSp);
                priceSpSummary = calculatePriceSpSummary();
            } else {
                priceOrdered = "${orderPosition.priceOrdered}";
                priceVendor = "${orderPosition.priceVendor}";
                priceSp = "${orderPosition.priceSp}";
                priceSpSummary = "${orderPosition.priceSpSummary}";
            }

			quantityField.removeAttribute("readonly");
			priceSpField.removeAttribute("readonly");
			addButton.removeAttribute("disabled");

			priceOrderedField.value = suggestion.data[0].toFixed(2);
			priceVendorField.value = priceVendor.toFixed(2);
			priceSpField.value = priceSp.toFixed(2);
			priceSpSummaryField.value = priceSpSummary.toFixed(2);
			incomeField.value = calculateIncome().toFixed(2);
			weightField.value = weight * quantity;

		}

		function reCalculate(){
			if(pickedProduct!=null || action == "edit_position") {
				quantity = quantityField.value;
				priceSp = priceSpField.value;
				priceSpSummaryField.value = calculatePriceSpSummary().toFixed(2);
				incomeField.value = calculateIncome().toFixed(2);
				weightField.value = weight * quantity;
			}
        }

        function clearAllFields() {
            priceOrderedField.value = "";
            priceVendorField.value = "";
            priceSpField.value = "";
            priceSpSummaryField.value = "";
            incomeField.value = "";
			weightField.value = "";

			addButton.setAttribute("disabled","");
        	quantityField.setAttribute("readonly", "readonly");
			priceSpField.setAttribute("readonly", "readonly");
        }

        function clearFieldsIfInvalidProduct() {
            if (productField.value != pickedProduct.value){
                clearAllFields();
            }
        }

        function showHideNoteField() {
        	var style = orderPositionNoteFieldDiv.getAttribute("style");
			switch(style.indexOf("none")){
				case -1:
					orderPositionNoteFieldDiv.setAttribute("style", "display:none");
					break;
				default:
					orderPositionNoteFieldDiv.setAttribute("style", "display:block");
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