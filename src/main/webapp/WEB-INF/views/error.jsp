<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
	<jsp:include page="../views/fragments/styles.jsp"/>
	<title>Ошибка</title>
</head>
<body>
	<jsp:include page="../views/fragments/header.jsp"/>
	<div class="container">
		<%-- Сообщения об ошибках --%>
		<jsp:include page="../views/fragments/error_alert.jsp"/>
	</div>
</body>
</html> 