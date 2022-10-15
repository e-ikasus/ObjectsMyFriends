<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="locale" var="r"/>

<jsp:include page="header.jsp">
	<jsp:param name="cssFiles" value="forms,item_sell"/>
	<jsp:param name="jsFiles" value=""/>
</jsp:include>

<form id="itemSell" class="formApp" method="post" action="${pageContext.request.contextPath}/item_sell">

	<div id="formTitle"><fmt:message key="TITLE_ITEM_SELL_FORM" bundle="${r}"/></div>

	<div id="inputsDiv">
		<div id="leftPart">
			<fieldset id="itemPicturesFieldset">
				<legend><fmt:message key="PICTURE" bundle="${r}"/></legend>
				<div id="picturesDiv">

				</div>
			</fieldset>
		</div>

		<div id="rightPart">
			<fieldset id="itemPropertiesFieldset">
				<legend><fmt:message key="ITEM" bundle="${r}"/></legend>

				<div>
					<div class="labelInputDiv">
						<label for="name"><fmt:message key="ITEM" bundle="${r}"/></label>
						<input id="name" name="name" type="text" value="${requestScope.name}"/>
					</div>
					<div class="errorSmall">
						<c:if test="${requestScope.errorName != null}">${requestScope.errorName}</c:if>
						<c:if test="${requestScope.errorName == null}">&nbsp;</c:if>
					</div>
				</div>

				<div>
					<div class="labelInputDiv topLabel">
						<label for="description"><fmt:message key="DESCRIPTION" bundle="${r}"/></label>
						<textarea id="description" name="description" cols="38" rows="4">${requestScope.description}</textarea>
					</div>
					<div class="errorSmall">
						<c:if test="${requestScope.errorDescription != null}">${requestScope.errorDescription}</c:if>
						<c:if test="${requestScope.errorDescription == null}">&nbsp;</c:if>
					</div>
				</div>

				<div>
					<div class="labelInputDiv">
						<label for="category"><fmt:message key="CATEGORY" bundle="${r}"/></label>
						<select id="category" name="category">
							<c:forEach var="category" items="${requestScope.categories}">
								<option value="${category.label}" <c:if test="${category == requestScope.category}"> selected</c:if>>${category.label}</option>
							</c:forEach>
						</select>
					</div>
					<div class="errorSmall">
						<c:if test="${requestScope.errorCategory != null}">${requestScope.errorCategory}</c:if>
						<c:if test="${requestScope.errorCategory == null}">&nbsp;</c:if>
					</div>
				</div>
			</fieldset>

			<fieldset id="salePropertiesFieldset">
				<legend><fmt:message key="SALE" bundle="${r}"/></legend>

				<div>
					<div class="labelInputDiv">
						<label for="initialPrice"><fmt:message key="INITIAL_PRICE" bundle="${r}"/></label>
						<input id="initialPrice" name="initialPrice" type="number" value="${requestScope.initialPrice}"/>
					</div>
					<div class="errorSmall">
						<c:if test="${requestScope.errorInitialPrice != null}">${requestScope.errorInitialPrice}</c:if>
						<c:if test="${requestScope.errorCategory == null}">&nbsp;</c:if>
					</div>
				</div>

				<div>
					<div class="labelInputDiv">
						<label for="biddingStart"><fmt:message key="BID_START" bundle="${r}"/></label>
						<input id="biddingStart" name="biddingStart" type="datetime-local" value="${requestScope.biddingStart}"/>
					</div>
					<div class="errorSmall">
						<c:if test="${requestScope.errorCategory != null}">${requestScope.errorCategory}</c:if>
						<c:if test="${requestScope.errorCategory == null}">&nbsp;</c:if>
					</div>
				</div>

				<div>
					<div class="labelInputDiv">
						<label for="biddingEnd"><fmt:message key="BID_END" bundle="${r}"/></label>
						<input id="biddingEnd" name="biddingEnd" type="datetime-local" value="${requestScope.biddingEnd}"/>
					</div>
					<div class="errorSmall">
						<c:if test="${requestScope.errorBiddingEnd != null}">${requestScope.errorBiddingEnd}</c:if>
						<c:if test="${requestScope.errorBiddingEnd == null}">&nbsp;</c:if>
					</div>
				</div>
			</fieldset>

			<fieldset id="pickupPlacePropertiesFieldset">
				<legend><fmt:message key="PICKUP_PLACE" bundle="${r}"/></legend>

				<div>
					<div class="labelInputDiv">
						<label for="street"><fmt:message key="STREET" bundle="${r}"/></label>
						<input id="street" name="street" type="text" value="${requestScope.street}"/>
					</div>
					<div class="errorSmall">
						<c:if test="${requestScope.errorStreet != null}">${requestScope.errorStreet}</c:if>
						<c:if test="${requestScope.errorStreet == null}">&nbsp;</c:if>
					</div>
				</div>

				<div>
					<div class="labelInputDiv">
						<label for="zipCode"><fmt:message key="ZIP_CODE" bundle="${r}"/></label>
						<input id="zipCode" name="zipCode" type="text" value="${requestScope.zipCode}"/>
					</div>
					<div class="errorSmall">
						<c:if test="${requestScope.errorZipCode != null}">${requestScope.errorZipCode}</c:if>
						<c:if test="${requestScope.errorZipCode == null}">&nbsp;</c:if>
					</div>
				</div>

				<div>
					<div class="labelInputDiv">
						<label for="city"><fmt:message key="CITY" bundle="${r}"/></label>
						<input id="city" name="city" type="text" value="${requestScope.city}"/>
					</div>
					<div class="errorSmall">
						<c:if test="${requestScope.errorCity != null}">${requestScope.errorCity}</c:if>
						<c:if test="${requestScope.errorCity == null}">&nbsp;</c:if>
					</div>
				</div>
			</fieldset>
		</div>
	</div>

	<div id="genericErrorDiv">
		<c:if test="${requestScope.errorGenericError != null}">${requestScope.errorGenericError}</c:if>
		<c:if test="${requestScope.errorGenericError == null}">&nbsp;</c:if>
	</div>

	<div id="buttonsDiv">
		<input id="save" name="save" type="submit" value="<fmt:message key="SAVE" bundle="${r}"/>">
		<input id="cancel" name="cancel" type="submit" value="<fmt:message key="CANCEL" bundle="${r}"/>">
	</div>

</form>

<jsp:include page="footer.jsp"/>
