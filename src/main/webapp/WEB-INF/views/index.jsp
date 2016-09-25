<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>

	<jsp:include page="../views/fragments/styles.jsp"/>

	<title>СП Gillette</title>

</head>
<body>
	
	<jsp:include page="../views/fragments/header.jsp"/>

	<div class="container">
	
	Текущее СП: <a href="<spring:url value="/sp/${sp.number}"/>" >СП-${sp.number}</a> - ${sp.status.name}
	
	
	

	</div>
</body>
</html> 