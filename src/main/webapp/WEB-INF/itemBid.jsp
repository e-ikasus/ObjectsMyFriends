<%@ page import="fr.eikasus.objectsmyfriends.misc.ControllerSupport" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="locale" var="r"/>

<c:set var="urlImages" value="<%=ControllerSupport.getInstance().getUrlImageHandler(request)%>" scope="page"/>

<jsp:include page="header.jsp">
	<jsp:param name="cssFiles" value="forms,item_sell,item_bid"/>
	<jsp:param name="jsFiles" value="jquery,item_bid"/>
	<jsp:param name="jsInit" value="ItemBid,\"${pageScope.urlImages}\""/>
</jsp:include>

<form id="itemBid" class="formApp" method="post" action="${pageContext.request.contextPath}/item_bid">

	<div id="formTitle"><fmt:message key="TITLE_ITEM_BID_FORM" bundle="${r}"/></div>

	<div id="inputsDiv">
		<div id="leftPart">
			<fieldset id="itemPicturesFieldset">
				<legend><fmt:message key="PICTURE_ITEM" bundle="${r}"/></legend>
				<div id="picturesDiv">

				</div>
			</fieldset>
		</div>

		<div id="rightPart">
			<fieldset id="itemPropertiesFieldset">
				<legend><fmt:message key="ITEM" bundle="${r}"/></legend>

				<div>
					<div class="labelInputDiv">
						<p class="propertyName"><fmt:message key="ITEM" bundle="${r}"/></p>
						<p id="name" class="propertyValue">${sessionScope.item.name}</p>
					</div>
				</div>

				<div>
					<div class="labelInputDiv topLabel">
						<label for="description"><fmt:message key="DESCRIPTION" bundle="${r}"/></label>
						<textarea id="description" name="description" cols="38" rows="4">${sessionScope.item.description}</textarea>
					</div>
				</div>

				<div>
					<div class="labelInputDiv">
						<p class="propertyName"><fmt:message key="CATEGORY" bundle="${r}"/></p>
						<p class="propertyValue" id="category">${sessionScope.item.category.label}</p>
					</div>
				</div>

				<div>
					<div class="labelInputDiv">
						<p class="propertyName"><fmt:message key="SELLER" bundle="${r}"/></p>
						<p class="propertyValue" id="seller">${sessionScope.item.seller.username}</p>
					</div>
				</div>
			</fieldset>

			<fieldset id="pickupPlacePropertiesFieldset">
				<legend><fmt:message key="PICKUP_PLACE" bundle="${r}"/></legend>

				<div>
					<div class="labelInputDiv">
						<p class="propertyName"><fmt:message key="STREET" bundle="${r}"/></p>
						<p class="propertyValue" id="street">
							<c:if test="${sessionScope.item.pickupPlace != null}">${sessionScope.item.pickupPlace.street}</c:if>
							<c:if test="${sessionScope.item.pickupPlace == null}">${sessionScope.user.street}</c:if>
						</p>
					</div>
				</div>

				<div>
					<div class="labelInputDiv">
						<p class="propertyName"><fmt:message key="ZIP_CODE" bundle="${r}"/></p>
						<p class="propertyValue" id="zipCode">
							<c:if test="${sessionScope.item.pickupPlace != null}">${sessionScope.item.pickupPlace.zipCode}</c:if>
							<c:if test="${sessionScope.item.pickupPlace == null}">${sessionScope.user.zipCode}</c:if>
						</p>
					</div>
				</div>

				<div>
					<div class="labelInputDiv">
						<p class="propertyName"><fmt:message key="CITY" bundle="${r}"/></p>
						<p class="propertyValue" id="city">
							<c:if test="${sessionScope.item.pickupPlace != null}">${sessionScope.item.pickupPlace.city}</c:if>
							<c:if test="${sessionScope.item.pickupPlace == null}">${sessionScope.user.city}</c:if>
						</p>
					</div>
				</div>
			</fieldset>

			<fieldset id="salePropertiesFieldset">
				<legend><fmt:message key="SALE" bundle="${r}"/></legend>

				<div>
					<div class="labelInputDiv">
						<p class="propertyName"><fmt:message key="INITIAL_PRICE" bundle="${r}"/></p>
						<p class="propertyValue" id="initialPrice">${sessionScope.item.initialPrice}</p>
					</div>
				</div>

				<div>
					<div class="labelInputDiv">
						<p class="propertyName"><fmt:message key="BID_END" bundle="${r}"/></p>
						<p class="propertyValue" id="biddingEnd"><fmt:formatDate type="BOTH" value="${sessionScope.item.biddingEnd}" timeStyle="MEDIUM"/></p>
					</div>
				</div>

				<div>
					<div class="labelInputDiv">
						<p class="propertyName"><fmt:message key="BEST_BUYER" bundle="${r}"/></p>
						<p class="propertyValue" id="bestBuyer">
							<c:if test="${sessionScope.bestBid != null}">${sessionScope.bestBid.user.username}</c:if>
							<c:if test="${sessionScope.bestBid == null}"><fmt:message key="NO_BIDDER" bundle="${r}"/></c:if>
						</p>
					</div>
				</div>

				<div>
					<div class="labelInputDiv">
						<p class="propertyName"><fmt:message key="BEST_OFFER" bundle="${r}"/></p>
						<p class="propertyValue" id="bestOffer">
							<c:if test="${sessionScope.bestBid != null}">${sessionScope.bestBid.price}</c:if>
							<c:if test="${sessionScope.bestBid == null}"><fmt:message key="NO_OFFER" bundle="${r}"/></c:if>
						</p>
					</div>
				</div>

				<div>
					<div class="labelInputDiv">
						<label for="yourOffer"><fmt:message key="YOUR_OFFER" bundle="${r}"/></label>
						<input id="yourOffer" name="yourOffer" type="number" value="${requestScope.yourOffer}"/>
					</div>
					<div class="errorSmall">
						<c:if test="${requestScope.errorYourOffer != null}">${requestScope.errorYourOffer}</c:if>
						<c:if test="${requestScope.errorYourOffer == null}">&nbsp;</c:if>
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
		<input id="makeOffer" name="makeOffer" type="submit" value="<fmt:message key="MAKE_OFFER" bundle="${r}"/>">
		<input id="cancel" name="cancel" type="submit" value="<fmt:message key="CANCEL" bundle="${r}"/>">
	</div>

</form>

<jsp:include page="footer.jsp"/>
