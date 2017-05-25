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
	<%--TODO: добавить checkbox, позволяющий править поля независимо от статуса--%>
	<%--TODO: заменить валидационные сообщения на tooltip'ы--%>

	<jsp:include page="../views/fragments/header.jsp"/>
	<div class="container">
        
		<legend>
            Заказ <a href='<spring:url value="/client/${order.client.id}"/>'>${order.client.name}</a> в <a href='<spring:url value="/sp/${order.sp.id}"/>'>СП-${order.sp.id}</a>
        </legend>

		<%-- Сообщения об ошибках --%>
		<jsp:include page="../views/fragments/error_alert.jsp"/>

		<%-- Линейка статусов --%>
		<ul class="breadcrumb">
			<c:set var="currentStatus" value="${order.status}"/>
			<c:set var="i" value="0"/>
			<c:forEach var="status" items="${orderStatuses}">
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
					<c:choose>
						<c:when test="${status.id < 3 || status.id > 5}">
							<spring:url value="/order/${order.id}" var="changeStatusUrl"><spring:param name="action" value="change_status"/></spring:url>
							<li><a href="${changeStatusUrl}" class="red-tooltip" data-toggle="tooltip" data-html="true" title="${statusError}" data-placement="top">${status.toString()}</a></li>
						</c:when>
						<c:otherwise>
							<li>${status.toString()}</li>
						</c:otherwise>
					</c:choose>
					<c:set var="i" value="2"/>
				</c:if>
			</c:forEach>
		</ul>

		<%-- Форма заказа --%>
		<spring:url value="/order/${order.id}" var="formUrl" />
		<form:form action="${formUrl}" method="POST" modelAttribute="order" cssClass="form-horizontal">
			<input type="hidden" id="action" name="action" value="update"/>
			<input type="hidden" id="goback" name="goback" value="false"/>
			<input type="hidden" id="id" name="id" value="${order.id}"/>

			<fieldset>
				<%-- sp --%>
				<c:choose>
					<c:when test="${order.status.id == 1}">
						<c:set var="field" value="sp"/>
						<spring:bind path="${field}">
							<div class="form-group">
								<label for="${field}" class="col-lg-2 control-label">СП</label>
								<div class="col-lg-2">
									<form:select path="${field}" id="${field}" itemLabel="id" itemValue="id" cssClass="form-control red-tooltip" data-toggle="tooltip" title='${status.errorMessage}' data-placement="right">
										<c:forEach var="spId" items="${availableSpIds}">
											<c:if test="${order.sp.id == spId}">
												<form:option  value="${order.sp}" label="${order.sp.id}"/>
											</c:if>
											<c:if test="${order.sp.id != spId}">
												<form:option value="${spId}"/>
											</c:if>
										</c:forEach>
									</form:select>
								</div>
							</div>
						</spring:bind>
					</c:when>
					<c:otherwise>
						<input type="hidden" id="sp" name="sp" value="${order.sp.id}"/>
						<%-- TODO: отображать disabled выпадающий список --%>
					</c:otherwise>
				</c:choose>

				<%-- dateOrdered --%>
                <c:set var="dateOrdered"><fmt:formatDate pattern="yyyy-MM-dd" value="${order.dateOrdered}"/></c:set>
                <c:set var="field" value="dateOrdered"/>
                <spring:bind path="${field}">
                    <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" id="${field}-formGroup">
                        <label for="${field}" class="col-lg-2 control-label">Дата заказа</label>
                        <div class="col-lg-2">
                            <form:input path="${field}" type="date" id="${field}" cssClass="form-control red-tooltip" value="${dateOrdered}" readonly="true" data-toggle="tooltip" title='${status.errorMessage}' data-placement="right"/>
                        </div>
                    </div>
                </spring:bind>

				<%-- prepaid --%>
				<c:set var="field" value="prepaid"/>
				<spring:bind path="${field}">
					<div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
						<label for="${field}" class="col-lg-2 control-label">Предоплата</label>
						<div class="col-lg-2">
							<form:input path="${field}" id="${field}" cssClass="form-control decimal red-tooltip" data-toggle="tooltip" title='${status.errorMessage}' data-placement="right"/>
						</div>
					</div>
				</spring:bind>

				<%-- deliveryPrice --%>
				<c:set var="field" value="deliveryPrice"/>
				<spring:bind path="${field}">
					<div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
						<label for="${field}" class="col-lg-2 control-label">Доставка</label>
						<div class="col-lg-2">
							<form:input path="${field}" id="${field}" cssClass="form-control red-tooltip" data-toggle="tooltip" title='${status.errorMessage}' data-placement="right"/>
						</div>
					</div>
				</spring:bind>

				<%-- note --%>
				<c:set var="field" value="note"/>
				<spring:bind path="${field}">
					<div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
					<label for="${field}" class="col-lg-2 control-label">Примечание</label>
						<div class="col-lg-5">
							<form:textarea path="${field}" id="${field}" cssClass="form-control red-tooltip" rows="3" maxlength="250" data-toggle="tooltip" title='${status.errorMessage}' data-placement="right"/>
						</div>
					</div>
				</spring:bind>

				<%-- кнопки формы --%>
				<c:if test="${currentStatus.id != 6}">
					<%-- TODO: Вместо скрывания кнопок - тображать disabled --%>
					<div class="form-group">
						<div class="col-lg-10 col-lg-offset-2">
							<button type="reset" class="btn btn-default">Отмена</button>
							<button type="submit" class="btn btn-primary">Сохранить</button>
							<button type="submit" class="btn btn-primary" id="saveAndGoBackButton" onclick="goBack();">Сохранить и вернуться к СП</button>
						</div>
					</div>
				</c:if>

			</fieldset>
		</form:form>
		<br/>

		<%-- Таблица позиций --%>
		<div style="width: 1500px">
			<table class="table table-striped table-hover">
			<%-- Заголовок таблицы --%>
			<thead>
			<tr>
				<th></th>
				<th>#</th>
				<th style="width: 733px">Наименование</th>
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
			<%-- Отображение позиций --%>
			<c:forEach var="orderPositionFromList" items="${order.orderPositions}" varStatus="counter">
				<%-- Строка позиции --%>
				<c:if test='${orderPosition.id != orderPositionFromList.id}'>
					<tr>
							<%-- Конпки редактировать/удалить позицию --%>
						<td class="text-nowrap">
							<c:if test='${action != "edit_position" && order.status.id == 1}'>
								<a title="Редактировать" href='<spring:url value="/order/${order.id}?action=edit_position&order_position_id=${orderPositionFromList.id}"/>' class="inline-block"><i class="fa fa-pencil-square fa-lg" aria-hidden="true"></i></a>&nbsp;|
								<a title="Удалить" style="color:red" href='<spring:url value="/order/${order.id}?action=delete_position&order_position_id=${orderPositionFromList.id}"/>' class="inline-block"><i class="fa fa-trash fa-lg" aria-hidden="true"></i></a>
							</c:if>
						</td>
						<td align="center">
								${counter.count}
						</td>
						<td class="wrapable">
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
						<c:if test="${orderPositionFromList.weight == 0}">
							<c:set var="danger" value="danger"/>
							<c:set var="incompleteWeight" value="true"/>
						</c:if>
						<td class="${danger}">
								${orderPositionFromList.productWeight*orderPositionFromList.quantity}
						</td>
					</tr>
				</c:if>

				<%-- Форма редактирования позиции --%>
				<c:if test='${action == "edit_position" and orderPosition.id == orderPositionFromList.id}'>
					<tr class="valign-top">
						<spring:url value="/order/${order.id}" var="formUrl" />
						<form:form action="${formUrl}" id="editOrderPositionForm" method="POST" modelAttribute="orderPosition" cssClass="form-horizontal">
							<input type="hidden" name="action" value="edit_position"/>
							<input type="hidden" name="order" value="${order.id}"/>
							<input type="hidden" name="id" value="${orderPosition.id}"/>

							<%-- Кнопки Сохранить/Отменить --%>
							<c:set var="submitOrderPositionId" value="submitOrderPosition"/>
							<td class="text-nowrap" style="padding-top: 17px">
								<button type="submit" title="Сохранить" id="${submitOrderPositionId}" class="btn-link"><i class="fa fa-check" aria-hidden="true" style="color: green"></i></button>&nbsp;|
								<a title="Отмена" style="color:red" href='<spring:url value="/order/${order.id}"/>' class="inline-block"><i class="fa fa-times fa-lg" aria-hidden="true"></i></a>
							</td>
							<%-- Счётчик --%>
							<td style="padding-top: 17px">
									${counter.count}
							</td>
							<%-- product & note --%>
							<td style="white-space: nowrap">
								<%-- product --%>
								<c:set var="field" value="product"/>
								<spring:bind path="${field}">
									<div class="form-group no-vmargin <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
										<form:input path="${field}" id="${field}" oninput="clearFieldsIfInvalidProduct()" cssClass="form-control red-tooltip"
													minlength="3" maxlength="250" cssStyle="display:inline-block; width: 700px"
													data-toggle="tooltip" title='${status.errorMessage}' data-placement="top"/>
										<a title="Добавить комментарий" href="" onclick="showHideNoteField();return false" style="margin-left: 3px"><i class="fa fa-commenting-o" aria-hidden="true" style="display: inline-block"></i></a>
									</div>
								</spring:bind>
								<%-- note --%>
								<c:set var="field" value="note"/>
								<spring:bind path="${field}">
									<div id="orderPosition-${field}-div"  class="form-group no-vmargin <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="display:none">
										<form:input path="${field}" id="orderPosition-${field}" oninput="clearFieldsIfInvalidProduct()" cssClass="form-control red-tooltip"
													minlength="3" maxlength="250" cssStyle="width: 700px; font-style:italic" placeholder="Примечание"
													data-toggle="tooltip" title='${status.errorMessage}' data-placement="bottom"/>
									</div>
								</spring:bind>
							</td>
							<%-- quantity --%>
							<td>
								<c:set var="field" value="quantity"/>
								<spring:bind path="${field}">
									<div class="form-group no-vmargin <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="width:60px;">
										<form:input path="${field}" id="${field}" onchange="reCalculate()" cssClass="form-control red-tooltip" onkeydown="return false"
													type="number" min="1" step="1" data-toggle="tooltip" title='${status.errorMessage}' data-placement="top"/>
									</div>
								</spring:bind>
							</td>
							<%-- priceOrdered --%>
							<td>
								<c:set var="field" value="priceOrdered"/>
								<div class="form-group no-vmargin" style="width:80px;">
									<form:input path="${field}" id="${field}" cssClass="form-control" readonly="true"/>
								</div>
							</td>
							<%-- priceVendor --%>
							<td>
								<c:set var="field" value="priceVendor"/>
								<div class="form-group no-vmargin" style="width:80px;">
									<form:input path="${field}" id="${field}" cssClass="form-control" readonly="true"/>
								</div>
							</td>
							<%-- priceSp --%>
							<td style="width:auto; white-space: nowrap">
								<c:set var="field" value="priceSp"/>
								<spring:bind path="${field}">
									<div class="form-group no-vmargin <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="width:auto">
										<form:input path="${field}" id="${field}" oninput="reCalculate()" cssClass="form-control decimal red-tooltip"
													data-toggle="tooltip" title='${status.errorMessage}' data-placement="top" cssStyle="display: inline-block; width:80px"/>
										<a title="Рассчитать значение" href="" onclick="reCalculatePriceSp();return false" style="margin-left: 3px"><i class="fa fa-calculator" aria-hidden="true" style="display: inline-block"></i></a>
									</div>
								</spring:bind>
							</td>
							<%-- priceSpSummary --%>
							<td>
								<c:set var="field" value="priceSpSummary"/>
								<div class="form-group no-vmargin <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="width:80px;">
									<form:input path="${field}" id="${field}" readonly="true" cssClass="form-control decimal"/>
								</div>
							</td>
							<%-- income --%>
							<td>
								<c:set var="field" value="income"/>
								<div class="form-group no-vmargin <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="width:80px;">
									<form:input path="${field}" id="${field}" readonly="true" cssClass="form-control"/>
								</div>
							</td>
							<%-- weight --%>
							<td>
								<c:set var="field" value="weight"/>
								<div class="form-group no-vmargin <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="width:60px;">
									<form:input path="${field}" id="orderPosition-productWeight" readonly="true" cssClass="form-control decimal"/>
								</div>
							</td>
							<%-- TODO: Помечать вес позиции, если он отличается от веса продукта --%>
							<%-- TODO: Добавить кнопку обновления веса и цен позиций --%>
						</form:form>
					</tr>
				</c:if>
			</c:forEach>

			<%-- Форма добавления позиции --%>
			<c:if test='${action!="edit_position" && currentStatus.id == 1}'>
				<tr class="valign-top">
					<spring:url value="/order/${order.id}" var="formUrl" />
					<form:form action="${formUrl}" method="POST" modelAttribute="orderPosition" cssClass="form-horizontal">
						<input type="hidden" name="action" value="add_position"/>
						<input type="hidden" name="order" value="${order.id}"/>

						<%-- Кнопка добавления позиции --%>
						<td>
							<div class="form-group no-vmargin">
								<c:set var="submitOrderPositionId" value="submitOrderPosition"/>
								<button type="submit" disabled class="btn btn-primary" id="${submitOrderPositionId}" title="Добавить позицию">+</button>
							</div>
						</td>
						<%-- Счётчик --%>
						<td style="padding-top: 17px">
								${order.orderPositions.size() + 1}
						</td>
						<%-- product & note --%>
						<td>
								<%-- product --%>
							<c:set var="field" value="product"/>
							<spring:bind path="${field}">
								<div class="form-group no-vmargin<c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
									<form:input path="${field}" id="${field}" oninput="clearFieldsIfInvalidProduct()" cssClass="form-control red-tooltip"
												autofocus="autofocus" minlength="3" maxlength="250" cssStyle="display:inline-block; width: 700px"
												data-toggle="tooltip" title='${status.errorMessage}' data-placement="top"/>
									<a title="Добавить комментарий" href="" onclick='showHideNoteField(); return false' style="margin-left: 3px"><i class="fa fa-commenting-o" aria-hidden="true" style="display: inline-block"></i></a>
								</div>
							</spring:bind>
								<%-- note --%>
							<c:set var="field" value="note"/>
							<spring:bind path="${field}">
								<div id="orderPosition-${field}-div" class="form-group no-vmargin <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="display:none">
									<form:input path="${field}" id="orderPosition-${field}" oninput="clearFieldsIfInvalidProduct()" cssClass="form-control red-tooltip"
												minlength="3" maxlength="250" cssStyle="width: 700px; font-style:italic" placeholder="Примечание"
												data-toggle="tooltip" title='${status.errorMessage}' data-placement="bottom"/>
								</div>
							</spring:bind>
						</td>
						<%-- quantity --%>
						<td>
							<c:set var="field" value="quantity"/>
							<spring:bind path="${field}">
								<div class="form-group no-vmargin <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="width:60px;">
									<form:input path="${field}" id="${field}" onchange="reCalculate()" cssClass="form-control red-tooltip"
												onkeydown="return false" type="number" readonly="true" min="1" step="1" value="1"
												data-toggle="tooltip" title='${status.errorMessage}' data-placement="top"/>
								</div>
							</spring:bind>
						</td>
						<%-- priceOrdered --%>
						<td>
							<c:set var="field" value="priceOrdered"/>
							<div class="form-group no-vmargin" style="width:80px;">
								<form:input path="${field}" id="${field}" cssClass="form-control" readonly="true"/>
							</div>
						</td>
						<%-- priceVendor --%>
						<td>
							<c:set var="field" value="priceVendor"/>
							<div class="form-group no-vmargin" style="width:80px;">
								<form:input path="${field}" id="${field}" cssClass="form-control" readonly="true"/>
							</div>
						</td>
						<%-- priceSp --%>
						<td>
							<c:set var="field" value="priceSp"/>
							<spring:bind path="${field}">
								<div class="form-group no-vmargin <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="width:auto">
									<form:input path="${field}" id="${field}" readonly="true" oninput="reCalculate()" cssClass="form-control decimal red-tooltip"
												data-toggle="tooltip" title='${status.errorMessage}' data-placement="top" cssStyle="display: inline-block; width:80px"/>
									<a title="Рассчитать значение" href="" onclick="reCalculatePriceSp();return false" style="margin-left: 3px"><i class="fa fa-calculator" aria-hidden="true" style="display: inline-block"></i></a>
								</div>
							</spring:bind>
						</td>
						<%-- priceSpSummary --%>
						<td>
							<c:set var="field" value="priceSpSummary"/>
							<div class="form-group no-vmargin" style="width:80px;">
								<form:input path="${field}" id="${field}" readonly="true" cssClass="form-control"/>
							</div>
						</td>
						<%-- income --%>
						<td>
							<c:set var="field" value="income"/>
							<div class="form-group no-vmargin" style="width:80px;">
								<form:input path="${field}" id="${field}" readonly="true" cssClass="form-control"/>
							</div>
						</td>
						<%-- productWeight --%>
						<td>
							<c:set var="field" value="productWeight"/>
							<div class="form-group no-vmargin" style="width:60px;">
								<form:input path="${field}" id="orderPosition-${field}" readonly="true" cssClass="form-control"/>
							</div>
						</td>
					</form:form>
				</tr>
			</c:if>

			<%-- Итоговая строка --%>
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

	</div>


	<%-- Управление отображением полями заказа --%>
	<script type="text/javascript">
		var spField;
		var prepaidField;
		var deliveryPriceField;
		var noteField;

		$(document).ready(function () {
			spField = document.getElementById("sp");
			prepaidField = document.getElementById("prepaid");
			deliveryPriceField = document.getElementById("deliveryPrice");
			noteField = document.getElementById("note");
			disableOrderFields();
			}
		);

		function disableFields(fields) {
			for (var i = 0; i < fields.length; i++) {
				fields[i].setAttribute("readonly", "readonly");
			}
		}

		function disableOrderFields() {
			var orderStatus = "${currentStatus.id}";
			switch (orderStatus) {
				case "1": //Не оплачен
					disableFields([deliveryPriceField]);
					spField.removeAttribute("disabled");
					break;
				case "2": //Оплачен
					disableFields([prepaidField, deliveryPriceField]);
                    spField.removeAttribute("disabled");
					break;
				case "3": //Комплектуется
					disableFields([spField, prepaidField, deliveryPriceField]);
					break;
				case "4": //Отправлен
					disableFields([spField, prepaidField, deliveryPriceField]);
					break;
				case "5": //Приехал
                    disableFields([spField, prepaidField]);
					break;
				case "6": //Получен
                    disableFields([spField, prepaidField, deliveryPriceField, noteField]);
                    break;
			}
		}
	</script>

	<%-- Автозаполнение форы выбора продукта --%>
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

	<%-- Рассчёт и отображение полей позиции --%>
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
			weightField = document.getElementById("orderPosition-productWeight");
			action = "${action}";
			if(action == "edit_position"){
				priceVendor = priceVendorField.value;
				weight = "${orderPosition.product.weight}";
				calculate();
				console.log("orderPositionNoteField.value = " + orderPositionNoteField.value);
				if (orderPositionNoteField.value != "") {
				    showHideNoteField();
				}
            }else {
				weightField.value = "";
			}
			if(orderPositionNoteField.value != "" && orderPositionNoteField.value != undefined){
				orderPositionNoteFieldDiv.setAttribute("style", "display:block");
			}
        });

        function calculatePriceVendor() {
            return (priceOrdered * (1 - discount + bankCommission)).toFixed(2);
        }

        function calculatePriceSp() {
            return (priceOrdered * (1 + percentSp)).toFixed(2);
        }

        function calculatePriceSpSummary() {
            return (priceSp * quantity).toFixed(2);
        }

        function calculateIncome() {
            return ((priceSp - priceVendor) * quantity).toFixed(2);
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
            if(productField.value != '${orderPosition.product.name}'){
                priceOrdered = suggestion.data[0].toFixed(2);
				priceVendor = calculatePriceVendor();
                priceSp = calculatePriceSp();
                priceSpSummary = calculatePriceSpSummary();
            	weight = suggestion.data[1];
            } else {
                priceOrdered = "${orderPosition.priceOrdered}";
				priceVendor = calculatePriceVendor();
                priceSp = "${orderPosition.priceSp}";
                priceSpSummary = "${orderPosition.priceSpSummary}";
            	weight = "${orderPosition.weight / orderPosition.quantity}";
            }

			quantityField.removeAttribute("readonly");
			priceSpField.removeAttribute("readonly");
			addButton.removeAttribute("disabled");

			priceOrderedField.value = priceOrdered;
			priceVendorField.value = priceVendor;
			priceSpField.value = priceSp;
			priceSpSummaryField.value = priceSpSummary;
			incomeField.value = calculateIncome();
			weightField.value = calculateWeight();
        }

		function reCalculate(){
			if(pickedProduct!=null || action == "edit_position") {
				quantity = quantityField.value;
				priceSp = priceSpField.value;
				priceVendor = priceVendorField.value;
				priceSpSummaryField.value = calculatePriceSpSummary();
				incomeField.value = calculateIncome();
				weightField.value = weight * quantity;
			}
        }

        function reCalculatePriceSp() {
            priceSpField.value = calculatePriceSp();
            reCalculate();
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
            if (pickedProduct != null && productField.value != pickedProduct.value){
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

	<%-- Формат децимальных полей --%>
	<script type="text/javascript">
		$(window).load(function () {
			var decimalField = document.getElementsByClassName("decimal");
			$(decimalField).inputmask({'mask':"9{1,4}[.9{1,2}]", greedy: false,"placeholder": ""});
		});
	</script>

	<%-- Отображение tooltip'ов с валидационными сообщениями --%>
	<script type="text/javascript">
        $('.red-tooltip').tooltip()
            .tooltip({container: 'body'})
            .tooltip('show');
        $(window).on('resize', function () {
            $('.red-tooltip').tooltip('show')
        })
	</script>

	<%-- Возврат к СП после сохранения --%>
	<script type="text/javascript">
        function goBack(){
			$('#goback').val('true');
		}
	</script>

</body>
</html>