<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>
<html>
<head>

	<jsp:include page="../views/fragments/styles.jsp"/>

	<title>Продукты с неуказанным весом в СП ${spId}</title>
</head>
<body>
	
	<jsp:include page="../views/fragments/header.jsp"/>

	<div class="container col-md-6">

		<legend>
			Продукты с неуказанным весом в <a href='<spring:url value="/sp/${spId}"/>'>СП-${spId}</a>
		</legend>

		<c:if test="${zeroWeightOrderPositionsForm.orderPositions.size() == 0}">
			Ура - все продукты в данном СП имеют свой вес!
		</c:if>

		<c:if test="${zeroWeightOrderPositionsForm.orderPositions.size() != 0}">

			<spring:url value="/zeroweightorderpositions" var="formUrl" />
			<form:form action="${formUrl}" method="POST" modelAttribute="zeroWeightOrderPositionsForm">
				<input type="hidden" name="action" value="save"/>
				<input type="hidden" name="sp" value="${spId}"/>

				<table class="table table-striped table-hover">
					<thead>
						<tr>
							<th>№</th>
							<th>Наименование</th>
							<th>Вес, г</th></tr>
					</thead>
					<tbody>
						<c:forEach var="orderPosition" items="${zeroWeightOrderPositionsForm.orderPositions}" varStatus="counter">
							<input type="hidden" name="orderPositions[${counter.index}].id" value="${orderPosition.id}"/>
							<tr>
								<td align="center">${counter.count}</td>
								<td>${orderPosition.product.name}</td>
								<td align="center">
									<c:set var="field" value="orderPositions[${counter.index}].productWeight"/>
									<spring:bind path="${field}">
										<c:set var="errorMessage" value="${status.errorMessage}"/>
										<div class="form-group no-vmargin <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>" style="width:80px;">
											<form:input path="${field}" id="${field}" cssClass="form-control decimal red-tooltip" type="number" min="0" data-toggle="tooltip" title="${errorMessage}" data-placement="right"/>
										</div>
									</spring:bind>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
				<br>

				<div class="form-group">
					<div class="col-lg-offset-0" align="right">
						<button type="reset" class="btn btn-default">Отмена</button>
						<button type="submit" class="btn btn-primary">Сохранить</button>
					</div>
				</div>

			</form:form>

		</c:if>

	</div>

	<spring:hasBindErrors name="zeroWeightOrderPositionsForm">
		<!-- Show validation error tooltips -->
		<script type="text/javascript">
			$('input').tooltip({trigger: 'manual'})
					.tooltip({container: 'body'})
					.tooltip('show');
			$(window).on('resize', function () {
				$('input').tooltip('show')
			})
		</script>
	</spring:hasBindErrors>

</body>
</html> 