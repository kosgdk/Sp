<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>

	<jsp:include page="../views/fragments/styles.jsp"/>

	<title>Страница не найдена</title>

</head>
<body>
	<jsp:include page="../views/fragments/header.jsp"/>
	<div class="container">

		<div class="alert alert-dismissible alert-danger">
			${message}
		</div>
	
	

	</div>
</body>
</html> 