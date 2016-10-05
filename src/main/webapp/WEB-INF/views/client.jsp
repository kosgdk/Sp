<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>

    <jsp:include page="../views/fragments/styles.jsp"/>
    <script src="<c:url value="/resources/js/jquery.inputmask.bundle.js" />"></script>

    <title>
        <c:if test='${action=="create"}'>
            Создать нового клиента
        </c:if>
        <c:if test='${action=="profile"}'>
            ${client.name} - профиль клиента
        </c:if>
    </title>

</head>
<body>
    <jsp:include page="../views/fragments/header.jsp" />
    <div class="container">

        <c:if test='${action=="create"}'>
            <spring:url value="/create_client" var="formUrl" />
        </c:if>
        <c:if test='${action=="profile"}'>
            <spring:url value="/client/${client.id}" var="formUrl" />
        </c:if>

        <form:form action="${formUrl}" method="POST" modelAttribute="client" cssClass="form-horizontal">
            <fieldset>

                <legend>
                    <c:if test='${action=="create"}'>
                        Создать нового клиента
                    </c:if>
                    <c:if test='${action=="profile"}'>
                        Профиль клиента <b>${client.name}</b>
                    </c:if>
                </legend>

                <c:if test='${action=="profile"}'>
                    <input type="hidden" name="id" value="${client.id}"/>
                </c:if>

                <spring:bind path="name">
                    <div class="form-group <c:if test='${status.errors.hasFieldErrors("name")}'>has-error</c:if>">
                        <label for="client-name" class="col-lg-2 control-label">Ник*</label>
                        <div class="col-lg-3">
                            <form:input path="name" cssClass="form-control" id="client-name" autofocus="autofocus" minlength="3" maxlength="50"/>
                            <span class="help-block">
                                <form:errors path="name"/>
                            </span>
                        </div>
                    </div>
                </spring:bind>

                <spring:bind path="realName">
                    <div class="form-group <c:if test='${status.errors.hasFieldErrors("realName")}'>has-error</c:if>">
                        <label for="client-real-name" class="col-lg-2 control-label">Имя</label>
                        <div class="col-lg-3">
                            <form:input path="realName" cssClass="form-control" id="client-real-name" minlength="2" maxlength="50"/>
                            <span class="help-block">
                                <form:errors path="realName"/>
                            </span>
                        </div>
                    </div>
                </spring:bind>

                <spring:bind path="phone">
                    <div class="form-group <c:if test='${status.errors.hasFieldErrors("phone")}'>has-error</c:if>">
                        <label for="client-phone" class="col-lg-2 control-label">Телефон</label>
                        <div class="col-lg-3">
                            <form:input path="phone" cssClass="form-control" id="client-phone" minlength="11" placeholder="+7(978)123-45-67"/>
                            <span class="help-block">
                                <form:errors path="phone"/>
                            </span>
                        </div>
                    </div>
                </spring:bind>

                <spring:bind path="note">
                    <div class="form-group <c:if test='${status.errors.hasFieldErrors("note")}'>has-error</c:if>">
                        <label for="client-note" class="col-lg-2 control-label">Примечание</label>
                        <div class="col-lg-5">
                            <form:textarea path="note" cssClass="form-control" id="client-note" rows="3"/>
                            <span class="help-block">
                                <form:errors path="note"/>
                            </span>
                        </div>
                    </div>
                </spring:bind>

                <spring:bind path="referer">
                    <div class="form-group <c:if test='${status.errors.hasFieldErrors("referer")}'>has-error</c:if>">
                        <label class="col-lg-2 control-label">Откуда*</label>
                        <div class="col-lg-10">
                            <c:forEach var="referer" items="${referers}" varStatus="counter">
                                <div class="radio">
                                    <label>
                                        <input type="radio" name="referer" id="referer" value="${referer.id}"
                                        <c:if test="${referer.id == 1}"> checked</c:if>
                                        />
                                        ${referer.name}
                                    </label>
                                </div>
                            </c:forEach>
                            <span class="help-block">
                                <form:errors path="referer"/>
                            </span>
                        </div>
                    </div>
                </spring:bind>

                <input type="hidden" name="spId" value="${spId}" />

                <div class="form-group">
                    <div class="col-lg-10 col-lg-offset-2">
                        <c:if test='${action=="profile"}'>
                            <button type="reset" class="btn btn-default">Отмена</button>
                        </c:if>
                        <button type="submit" class="btn btn-primary" id="addOrderButton">
                            <c:if test='${action=="create"}'>
                                Создать
                            </c:if>
                            <c:if test='${action=="profile"}'>
                                Сохранить
                            </c:if>
                        </button>
                    </div>
                </div>

            </fieldset>
        </form:form>



        <!-- Отображение заказов -->
        <c:if test='${action=="profile"}'>
            <h3 id="navbar">Заказы:</h3>

            <c:forEach var="order" items="${client.orders}">
                <div class="panel panel-default" style="width: 1000px">

                    <div class="panel-heading">
                        <a href='<spring:url value="/sp/${order.sp.id}"/>'>СП-${order.sp.id}</a> - ${order.orderStatus.name}
                        &nbsp;|&nbsp;
                        <a href='<spring:url value="/order/${order.id}"/>'><i class="fa fa-pencil-square" aria-hidden="true"></i>&nbsp;Редактировать</a>
                        &nbsp;|&nbsp;
                        <a href='<spring:url value="/order/${order.id}"/>'><i class="fa fa-trash" aria-hidden="true"></i>&nbsp;Удалить</a>
                        <c:if test="${order.note!=null}">
                            <br/><i>${order.note}</i>
                        </c:if>
                    </div>
                    <div class="panel-body">
                        <c:forEach var="orderPosition" items="${order.orderPositions}">
                            - ${orderPosition.product.name} - ${orderPosition.priceOrdered} р.<br/>
                        </c:forEach>
                    </div>
                </div>
            </c:forEach>

        </c:if>


    </div>

    <!-- Phone mask script -->
    <script type="text/javascript">
        $(window).load(function () {
            $('#client-phone').inputmask({
                mask: {"mask": "+9(999)999-99-99"},
                greedy: false
            });
        });
    </script>

</body>
</html>
