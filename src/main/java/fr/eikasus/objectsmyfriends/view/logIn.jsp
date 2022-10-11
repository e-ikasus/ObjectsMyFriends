<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="locale" var="r"/>

<jsp:include page="header.jsp">
	<jsp:param name="cssFiles" value="logIn"/>
	<jsp:param name="jsFiles" value=""/>
</jsp:include>

<form id="logIn" method="post" action="${pageContext.request.contextPath}/login">

	<div>
		<div id="pseudoDiv">
			<label for="pseudo"><fmt:message key="USERNAME" bundle="${r}"/></label>
			<input id="pseudo" name="pseudo" type="text" value="${cookie.username.value}"/>
		</div>
		<div class="error">
			<c:if test="${requestScope.error != null}"><fmt:message key="WRONG_DATA" bundle="${r}"/></c:if>
			<c:if test="${requestScope.error == null}">&nbsp;</c:if>
		</div>
	</div>

	<div>
		<div id="passwordDiv">
			<label for="password"><fmt:message key="PASSWORD" bundle="${r}"/></label>
			<input id="password" name="password" type="password"/>
		</div>
		<div class="error">
			<c:if test="${requestScope.error != null}"><fmt:message key="WRONG_DATA" bundle="${r}"/></c:if>
			<c:if test="${requestScope.error == null}">&nbsp;</c:if>
		</div>
	</div>

	<div>
		<div id="rememberMeDiv">
			<label for="rememberMe"><fmt:message key="REMEMBER_ME" bundle="${r}"/></label>
			<input id="rememberMe" name="rememberMe" type="checkbox"/>
		</div>
	</div>

	<div id="buttonsDiv">
		<input id="confirm" name="confirm" type="submit" value="<fmt:message key="CONFIRM" bundle="${r}"/>">
		<input id="cancel" name="cancel" type="submit" value="<fmt:message key="CANCEL" bundle="${r}"/>">
	</div>

</form>

<jsp:include page="footer.jsp"/>
