<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1251">
<title>Выбор позиции | СП Gillette</title>

	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

</head>
<body>
	
	<jsp:include page="../views/fragments/header.jsp"/>

	<div class="container">
		
		Запрос: ${query}
		<br/>
		
		Результаты: ${products.size()}
		<br/>
		<br/>
		
		<form>
		
		<c:forEach var="product" items="${products}" varStatus="counter">
		
            <a href='<spring:url value="/addposition?productid=${product.id}"/>'>${product.name}</a>
            <br>

		</c:forEach>
		
		</form>
		
	</div>
</body>
</html> 