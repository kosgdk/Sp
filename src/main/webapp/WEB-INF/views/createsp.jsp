<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1251">
<title>Создать СП-${nextSpNumber}</title>

	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

</head>
<body>
	
	<jsp:include page="../views/fragments/header.jsp"></jsp:include>			

	<div class="container">
		
	
	<form action='<spring:url value="/savesp" />' method="POST" class="form-horizontal">
		<fieldset>
			<legend>Создать СП-${nextSpNumber}</legend>
				
				<input type="hidden" class="form-control" id="number" name="number" value="${nextSpNumber}">
				
				<div class="form-group">
					<label for="dateStart" class="col-lg-2 control-label">Дата начала сбора</label>
					<div class="col-lg-10">
						<input type="date" class="form-control" id="dateStart" name="dateStart" placeholder="ДД.ММ.ГГГГ">
					</div>
				</div>
				
				<div class="form-group">
					<label for="dateEnd" class="col-lg-2 control-label">Дата окончания сбора</label>
					<div class="col-lg-10">
						<input type="date" class="form-control" id="dateEnd" name="dateEnd" placeholder="ДД.ММ.ГГГГ">
					</div>
				</div>

				<div class="form-group">
					<label for="percent" class="col-lg-2 control-label">Комиссия, %</label>
					<div class="col-lg-10">
						<input type="number" value="15" class="form-control" id="percent" name="percent" placeholder="0-15"
							min="0" max="15" step="1">
					</div>
				</div>
    
				<div class="form-group">
					<div class="col-lg-10 col-lg-offset-2">
						<button type="submit" class="btn btn-primary">Ок</button>
					</div>
				</div>
			</fieldset>
		</form>
	
		
	</div>
</body>
</html> 