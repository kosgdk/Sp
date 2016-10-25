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


        <!-- Форма клиента -->
        <c:if test='${action=="create"}'>
            <spring:url value="/create_client" var="formUrl" />
        </c:if>
        <c:if test='${action=="profile"}'>
            <spring:url value="/client/${client.id}" var="formUrl" />
        </c:if>
        <form:form action="${formUrl}" method="POST" modelAttribute="client" cssClass="form-horizontal">

                <legend>
                    <c:if test='${action=="create"}'>
                        <c:set var="autofocus" value="autofocus"/>
                        Создать нового клиента
                    </c:if>
                    <c:if test='${action=="profile"}'>
                        <c:set var="autofocus" value=""/>
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
                            <form:input path="name" cssClass="form-control" id="client-name" autofocus="${autofocus}" minlength="3" maxlength="50"/>
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

                <c:set var="field" value="clientReferrer"/>
                <spring:bind path="${field}">
                    <div class="form-group <c:if test='${status.errors.hasFieldErrors(field)}'>has-error</c:if>">
                        <label class="col-lg-2 control-label">Откуда*</label>
                        <div class="col-lg-10">
                            <c:forEach var="clientReferrerFromList" items="${clientReferrers}" varStatus="counter">
                                <div class="radio">
                                    <label>
                                        <c:set var="checked" value=""/>
                                        <c:if test="${client.clientReferrer == null && clientReferrerFromList.id == 1}"><c:set var="checked" value="checked"/></c:if>
                                        <c:if test="${clientReferrerFromList.id == client.clientReferrer.id}"><c:set var="checked" value="checked"/></c:if>
                                        <input type="radio" name="${field}" id="${field}" value="${clientReferrerFromList.id}" ${checked}/>
                                        ${clientReferrerFromList.name}
                                    </label>
                                </div>
                            </c:forEach>
                            <span class="help-block"><form:errors path="${field}"/></span>
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
            <h4 style="margin-bottom: 20px">Заказы:</h4>

            <c:forEach var="order" items="${client.orders}">

                <!-- Панель заказа -->
                <div class="panel panel-default" style="width: 800px">

                    <!-- Заголовок панели заказа -->
                    <div class="panel-heading min-vpadding">
                        <c:if test='${order.status == "UNPAID"}'><c:set var="labelType" value="label-default"/></c:if>
                        <c:if test='${order.status == "PAID"}'><c:set var="labelType" value="label-info"/></c:if>
                        <c:if test='${order.status == "PACKING"}'><c:set var="labelType" value="label-info"/></c:if>
                        <c:if test='${order.status == "SENT"}'><c:set var="labelType" value="label-warning"/></c:if>
                        <c:if test='${order.status == "ARRIVED"}'><c:set var="labelType" value="label-primary"/></c:if>
                        <c:if test='${order.status == "COMPLETED"}'><c:set var="labelType" value="label-success"/></c:if>

                        <span class="label size-normal ${labelType}">${order.status.toString()}</span>&nbsp;|
                        <a href='<spring:url value="/sp/${order.sp.id}"/>'>СП-${order.sp.id}</a>&nbsp;|
                        Сумма:&nbsp;${order.summaryPrice} р.&nbsp;|
                        Прибыль:&nbsp;${order.income} р.&nbsp;|
                        <a href='<spring:url value="/order/${order.id}"/>'><i class="fa fa-pencil-square" aria-hidden="true"></i>&nbsp;Редактировать</a>&nbsp;|
                        <a href='<spring:url value="/order/${order.id}"/>'><i class="fa fa-trash" aria-hidden="true" style="color:red"></i>&nbsp;Удалить</a>
                        <c:if test="${order.note!=null}">
                            <br/>Комментарий: <i>${order.note}</i>
                        </c:if>
                    </div>

                    <!-- Содержание панели заказа -->
                    <div class="panel-body">
                        <table class="table table-ultra-condensed table-hover">
                            <c:forEach var="orderPosition" items="${order.orderPositions}" varStatus="orderPositionCounter">
                                <tr class="no-vmargin">
                                    <td style="width: 20px">${orderPositionCounter.count}.</td>
                                    <td>
                                        ${orderPosition.product.name}
                                        <c:if test='${orderPosition.note != ""}'>
                                            <br/><i>${orderPosition.note}</i>
                                        </c:if>
                                    </td>
                                    <td class="text-right" style="width: 60px">${orderPosition.quantity} шт</td>
                                    <td class="text-right" style="width: 85px">${orderPosition.priceSpSummary} р.</td>
                                </tr>
                            </c:forEach>
                        </table>
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
