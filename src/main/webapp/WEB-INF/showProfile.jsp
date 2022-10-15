<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="locale" var="r"/>

<jsp:include page="header.jsp">
	<jsp:param name="cssFiles" value="forms,show_profile"/>
	<jsp:param name="jsFiles" value=""/>
</jsp:include>

<form id="show" class="formApp" method="get" action="${pageContext.request.contextPath}/modify_profile">

	<div id="formTitle"><fmt:message key="TITLE_SHOW_PROFILE_FORM" bundle="${r}"/></div>

	<div id="contentDiv">
		<div class="labelInputDiv">
			<p class="propertyName"><fmt:message key="USERNAME" bundle="${r}"/></p>
			<p class="propertyValue" id="username">${requestScope.user.username}</p>
		</div>

		<div class="labelInputDiv">
			<p class="propertyName"><fmt:message key="LASTNAME" bundle="${r}"/></p>
			<p class="propertyValue" id="lastName">${requestScope.user.lastName}</p>
		</div>

		<div class="labelInputDiv">
			<p class="propertyName"><fmt:message key="FIRSTNAME" bundle="${r}"/></p>
			<p class="propertyValue" id="firstName">${requestScope.user.firstName}</p>
		</div>

		<div class="labelInputDiv">
			<p class="propertyName"><fmt:message key="EMAIL" bundle="${r}"/></p>
			<p class="propertyValue" id="email">${requestScope.user.email}</p>
		</div>

		<div class="labelInputDiv">
			<p class="propertyName"><fmt:message key="PHONE_NUMBER" bundle="${r}"/></p>
			<p class="propertyValue" id="phoneNumber">${requestScope.user.phoneNumber}</p>
		</div>

		<div class="labelInputDiv">
			<p class="propertyName"><fmt:message key="STREET" bundle="${r}"/></p>
			<p class="propertyValue" id="street">${requestScope.user.street}</p>
		</div>

		<div class="labelInputDiv">
			<p class="propertyName"><fmt:message key="ZIP_CODE" bundle="${r}"/></p>
			<p class="propertyValue" id="zipCode">${requestScope.user.zipCode}</p>
		</div>

		<div class="labelInputDiv">
			<p class="propertyName"><fmt:message key="CITY" bundle="${r}"/></p>
			<p class="propertyValue" id="city">${requestScope.user.city}</p>
		</div>
	</div>

	<div id="buttonsDiv">
		<input id="modify" name="modify" type="submit" value="<fmt:message key="MODIFY" bundle="${r}"/>">
	</div>

</form>

<jsp:include page="footer.jsp"/>