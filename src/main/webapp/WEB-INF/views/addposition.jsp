<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1251">
<title>Добавить позицию | СП Gillette</title>

	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/css/bootstrap.min.css">
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>

</head>
<body>
	
	<jsp:include page="../views/fragments/header.jsp"/>

	<div class="container">
		
		
	<form action='<spring:url value="addposition"/>' method="POST" class="form-horizontal">
	  <fieldset>
	    <div class="form-group">
	      <div class="col-lg-10">
	        <input type="text" name="query" class="form-control" placeholder="Наименование товара" autofocus>
	      </div>
	    </div>
	
	    <div class="form-group">
	      <div class="col-lg-10 col-lg-offset-0">
	        <button type="submit" class="btn btn-primary">Найти</button>
	      </div>
	    </div>
	  </fieldset>
	</form>
	
	
	<c:if test="&{orderPosition != null}">
	
	
	${orderPosition.product.name}
	
	</c:if>
	
		
	</div>
</body>
</html> 