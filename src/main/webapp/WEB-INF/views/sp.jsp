<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1251">
<title>СП-${sp.getNumber()}</title>

	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

</head>
<body>
	
	<jsp:include page="../views/fragments/header.jsp"></jsp:include>			

	<div class="container">
		
		
	<h1>Страница текущего СП-${sp.getNumber()}</h1>
	<br/><br/>
	<a href='<spring:url value="/addorder"/>'>Добавить заказ</a>
	<br/><br/>
	
	<c:if test="${orders!=null}">
		<c:forEach var="order" items="${orders}" varStatus="counter">
		
            ${order.getClient().getName()}
            <br/>

		</c:forEach>
	</c:if>
	
		
	</div>
</body>
</html> 