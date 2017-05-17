<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${errorMessage != null}">
    <div class="alert alert-danger">
        ${errorMessage}
    </div>
</c:if>
<c:if test="${errorMessages != null && errorMessages.size() > 0}">
    <c:forEach items="${errorMessages}" var="errorMessage">
        <div class="alert alert-danger">
            ${errorMessage}
        </div>
    </c:forEach>
</c:if>
