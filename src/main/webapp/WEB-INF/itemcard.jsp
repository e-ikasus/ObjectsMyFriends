<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setBundle basename="locale" var="r"/>

<div class="itemCardShadowDiv">
	<div class="itemCardTitleDiv">
		<p class="itemCardTitle">${requestScope.itemCard.name}</p>
	</div>
	<div class="itemCardDiv" id="i${requestScope.itemCard.identifier}">
		<div class="itemCardImageDiv">

		</div>
		<div class="itemCardPropertiesDiv">

			<div class="itemCardPropertyDiv">
				<p class="propertyName"><fmt:message key="INITIAL_PRICE" bundle="${r}"/></p>
				<p class="propertyValue" id="initialPrice">${requestScope.itemCard.initialPrice}</p>
			</div>

			<div class="itemCardPropertyDiv">
				<p class="propertyName"><fmt:message key="BID_END" bundle="${r}"/></p>
				<p class="propertyValue" id="biddingEnd"><fmt:formatDate type="BOTH" value="${requestScope.itemCard.biddingEnd}" timeStyle="MEDIUM"/></p>
			</div>

			<div class="itemCardPropertyDiv">
				<p class="propertyName"><fmt:message key="SELLER" bundle="${r}"/></p>
				<p class="propertyValue" id="seller">
					<c:if test="${sessionScope.user != null}"> <a class="itemCardSellerLink" href="${pageContext.request.contextPath}/show_profile?identifier=${requestScope.itemCard.seller.identifier}"></c:if>
					${requestScope.itemCard.seller.username}
					<c:if test="${sessionScope.user != null}"></a></c:if>
				</p>
			</div>
		</div>

	</div>
</div>
