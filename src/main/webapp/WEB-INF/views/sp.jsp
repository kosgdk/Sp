<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1251">
<title>СП-${sp.number}</title>

	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
	<link href="<c:url value="/resources/core/autocomplete.css" />" rel="stylesheet">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
	<script src="<c:url value="/resources/core/jquery.1.10.2.min.js" />"></script>
	<script src="<c:url value="/resources/core/jquery.autocomplete.min.js" />"></script>

</head>
<body>

	<jsp:include page="../views/fragments/header.jsp"/>
	<div class="container">
		
		
	<h1>Страница текущего СП-${sp.number}</h1>
	<br/><br/>

		<form class="form-horizontal" action='<spring:url value="/addorder"/>'>
			<fieldset>
				<legend>Добавить заказ</legend>

				<div class="form-group">
					<label for="client-search" class="col-lg-2 control-label">Клиент</label>
					<div class="col-lg-10">
						<input type="text" class="form-control" id="client-search" name="clientId">
					</div>
				</div>

				<div class="form-group">
					<label for="product-search" class="col-lg-2 control-label">Товар</label>
					<div class="col-lg-10">
						<input type="text" class="form-control" id="product-search" name="productId">
					</div>
				</div>

				<div class="form-group">
					<label for="quantity" class="col-lg-2 control-label">Количество</label>
					<div class="col-lg-10">
						<input type="number" class="form-control" id="quantity" name="quantity" min="1" value="1">
					</div>
				</div>

				<div class="form-group">
					<div class="col-lg-10 col-lg-offset-2">
						<button type="submit" class="btn btn-primary">Добавить</button>
					</div>
				</div>
			</fieldset>
		</form>





	
	<c:if test="${orders!=null}">
		<c:forEach var="order" items="${orders}" varStatus="counter">
		
            ${order.getClient().getName()}
            <br/>

		</c:forEach>
	</c:if>
	
		
	</div>



	<!-- Client autocomplete -->
	<script>
		$(document).ready(function() {

			$('#client-search').autocomplete({
				serviceUrl: '${pageContext.request.contextPath}/getClients',
				paramName: "query",
				delimiter: ",",
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

	<!-- Product autocomplete -->
	<script>
		$(document).ready(function() {

			$('#product-search').autocomplete({
				serviceUrl: '${pageContext.request.contextPath}/getProducts',
				paramName: "query",
				delimiter: ",",
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