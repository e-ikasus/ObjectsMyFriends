<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="locale" var="r"/>

<jsp:include page="header.jsp">
	<jsp:param name="cssFiles" value="subscribe,modify_profile"/>
	<jsp:param name="jsFiles" value=""/>
</jsp:include>

<form id="subscribe" method="post" action="${pageContext.request.contextPath}/modify_profile">

	<div id="formTitle"><fmt:message key="TITLE_MODIFY_PROFILE_FORM" bundle="${r}"/></div>

	<div id="inputsDiv">
		<div id="leftPart">
			<div>
				<div class="labelInputDiv">
					<label for="username"><fmt:message key="USERNAME" bundle="${r}"/></label>
					<input id="username" name="username" type="text" value="${requestScope.username}"/>
				</div>
				<div class="error">
					<c:if test="${requestScope.errorUsername != null}">${requestScope.errorUsername}</c:if>
					<c:if test="${requestScope.errorUsername == null}">&nbsp;</c:if>
				</div>
			</div>

			<div>
				<div class="labelInputDiv">
					<label for="firstName"><fmt:message key="FIRSTNAME" bundle="${r}"/></label>
					<input id="firstName" name="firstName" type="text" value="${requestScope.firstName}"/>
				</div>
				<div class="error">
					<c:if test="${requestScope.errorFirstName != null}">${requestScope.errorFirstName}</c:if>
					<c:if test="${requestScope.errorFirstName == null}">&nbsp;</c:if>
				</div>
			</div>

			<div>
				<div class="labelInputDiv">
					<label for="phoneNumber"><fmt:message key="PHONE_NUMBER" bundle="${r}"/></label>
					<input id="phoneNumber" name="phoneNumber" type="text" value="${requestScope.phoneNumber}"/>
				</div>
				<div class="error">
					<c:if test="${requestScope.errorPhoneNumber != null}">${requestScope.errorPhoneNumber}</c:if>
					<c:if test="${requestScope.errorPhoneNumber == null}">&nbsp;</c:if>
				</div>
			</div>

			<div>
				<div class="labelInputDiv">
					<label for="zipCode"><fmt:message key="ZIP_CODE" bundle="${r}"/></label>
					<input id="zipCode" name="zipCode" type="text" value="${requestScope.zipCode}"/>
				</div>
				<div class="error">
					<c:if test="${requestScope.errorZipCode != null}">${requestScope.errorZipCode}</c:if>
					<c:if test="${requestScope.errorZipCode == null}">&nbsp;</c:if>
				</div>
			</div>

			<div>
				<div class="labelInputDiv">
					<label for="newPassword"><fmt:message key="NEW_PASSWORD" bundle="${r}"/></label>
					<input id="newPassword" name="newPassword" type="text" value="${requestScope.newPassword}"/>
				</div>
				<div class="error">
					<c:if test="${requestScope.errorNewPassword != null}">${requestScope.errorNewPassword}</c:if>
					<c:if test="${requestScope.errorNewPassword == null}">&nbsp;</c:if>
				</div>
			</div>
		</div>

		<div id="rightPart">
			<div>
				<div class="labelInputDiv">
					<label for="lastName"><fmt:message key="LASTNAME" bundle="${r}"/></label>
					<input id="lastName" name="lastName" type="text" value="${requestScope.lastName}"/>
				</div>
				<div class="error">
					<c:if test="${requestScope.errorLastName != null}">${requestScope.errorLastName}</c:if>
					<c:if test="${requestScope.errorLastName == null}">&nbsp;</c:if>
				</div>
			</div>

			<div>
				<div class="labelInputDiv">
					<label for="email"><fmt:message key="EMAIL" bundle="${r}"/></label>
					<input id="email" name="email" type="text" value="${requestScope.email}"/>
				</div>
				<div class="error">
					<c:if test="${requestScope.errorEmail != null}">${requestScope.errorEmail}</c:if>
					<c:if test="${requestScope.errorEmail == null}">&nbsp;</c:if>
				</div>
			</div>

			<div>
				<div class="labelInputDiv">
					<label for="street"><fmt:message key="STREET" bundle="${r}"/></label>
					<input id="street" name="street" type="text" value="${requestScope.street}"/>
				</div>
				<div class="error">
					<c:if test="${requestScope.errorStreet != null}">${requestScope.errorStreet}</c:if>
					<c:if test="${requestScope.errorStreet == null}">&nbsp;</c:if>
				</div>
			</div>

			<div>
				<div class="labelInputDiv">
					<label for="city"><fmt:message key="CITY" bundle="${r}"/></label>
					<input id="city" name="city" type="text" value="${requestScope.city}"/>
				</div>
				<div class="error">
					<c:if test="${requestScope.errorCity != null}">${requestScope.errorCity}</c:if>
					<c:if test="${requestScope.errorCity == null}">&nbsp;</c:if>
				</div>
			</div>

			<div>
				<div class="labelInputDiv">
					<label for="confirmPassword"><fmt:message key="CONFIRM_PASSWORD" bundle="${r}"/></label>
					<input id="confirmPassword" name="confirmPassword" type="text" value="${requestScope.confirmPassword}"/>
				</div>
				<div class="error">
					<c:if test="${requestScope.errorConfirmPassword != null}">${requestScope.errorConfirmPassword}</c:if>
					<c:if test="${requestScope.errorConfirmPassword == null}">&nbsp;</c:if>
				</div>
			</div>
		</div>
	</div>

	<div id="creditDiv">
		<span id="credit"><fmt:message key="CREDITS" bundle="${r}"/> ${requestScope.credit}</span>
	</div>

	<div id="genericErrorDiv">
		<c:if test="${requestScope.errorGenericError != null}">${requestScope.errorGenericError}</c:if>
		<c:if test="${requestScope.errorGenericError == null}">&nbsp;</c:if>
	</div>

	<div id="buttonsDiv">
		<input id="update" name="update" type="submit" value="<fmt:message key="UPDATE" bundle="${r}"/>">
		<input id="cancel" name="cancel" type="submit" value="<fmt:message key="CANCEL" bundle="${r}"/>">
	</div>

</form>

<jsp:include page="footer.jsp"/>
