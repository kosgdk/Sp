<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1251">
<title>Добавить клиента</title>

	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

</head>
<body>
	
	<jsp:include page="../views/fragments/header.jsp"></jsp:include>			

	<div class="container">
		
	
	<form action='<spring:url value="/createclient" />' method="POST" class="form-horizontal">
		<fieldset>
			<legend>Добавить клиента</legend>
				
				<input type="hidden" class="form-control" id="action" name="action" value="create">
				
				<div class="form-group">
					<label for="name" class="col-lg-2 control-label">Ник*</label>
					<div class="col-lg-10">
						<input type="text" class="form-control" id="name" name="name" placeholder="">
					</div>
				</div>
				
				<div class="form-group">
					<label for="realName" class="col-lg-2 control-label">Имя</label>
					<div class="col-lg-10">
						<input type="text" class="form-control" id="realName" name="realName" placeholder="">
					</div>
				</div>
				
				<div class="form-group">
					<label for="phone" class="col-lg-2 control-label">Телефон</label>
					<div class="col-lg-10">
						<input type="tel" class="form-control" id="phone" name="phone" placeholder="79781234567 - 11 цифр">
					</div>
				</div>

				<div class="form-group">
					<label class="col-lg-2 control-label">Откуда</label>
					<div class="col-lg-10">
					
						<c:forEach var="referer" items="${referers}" varStatus="counter">
							<div class="radio">
								<label> <input type="radio" name="optionsRadios"
									id="optionsRadios2" value="option2"> ${referer.getName()}
								</label>
							</div>
						</c:forEach>
						
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