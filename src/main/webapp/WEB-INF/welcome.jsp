<%@ page import="fr.eikasus.objectsmyfriends.misc.ControllerSupport" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="locale" var="r"/>

<c:set var="urlImages" value="<%=ControllerSupport.getInstance().getUrlImageHandler(request)%>" scope="page"/>

<jsp:include page="header.jsp">
	<jsp:param name="cssFiles" value="forms,welcome"/>
	<jsp:param name="jsFiles" value="jquery,welcome"/>
	<jsp:param name="jsInit" value="welcome,\"${pageScope.urlImages}\""/>
</jsp:include>

<c:choose>
	<c:when test="${requestScope.searchType == 'purchases'}">
		<c:set var="purchasesRadioButton" value="checked"/>
		<c:set var="salesRadioButton" value=""/>
		<c:set var="purchasesInputs" value=""/>
		<c:set var="salesInputs" value="disabled"/>
		<c:set var="purchasesLabels" value=""/>
		<c:set var="salesLabels" value="labelDisable"/>
	</c:when>
	<c:otherwise>
		<c:set var="purchasesRadioButton" value=""/>
		<c:set var="salesRadioButton" value="checked"/>
		<c:set var="purchasesInputs" value="disabled"/>
		<c:set var="salesInputs" value=""/>
		<c:set var="purchasesLabels" value="labelDisable"/>
		<c:set var="salesLabels" value=""/>
	</c:otherwise>
</c:choose>

<div id="searchOptionsDiv">

	<form id="searchOptions" method="get">

		<fieldset id="itemFieldset">
			<legend><fmt:message key="ITEM_ATTRIBUTES" bundle="${r}"/></legend>

			<div class="labelInputDiv">
				<label for="keywords" class="leftLabel"><fmt:message key="ITEM" bundle="${r}"/></label>
				<input id="keywords" name="keywords" type="text" value="${requestScope.keywords}"/>
			</div>

			<div class="labelInputDiv">
				<label for="category" class="leftLabel"><fmt:message key="CATEGORY" bundle="${r}"/></label>
				<select id="category" name="category">
					<option value="all"<c:if test="${requestScope.category == 'all'}"> selected</c:if>>
						<fmt:message key="ALL_CATEGORIES" bundle="${r}"/></option>
					<c:forEach var="category" items="${requestScope.categories}">
						<option value="${category.label}"
										<c:if test="${category.label == requestScope.category}"> selected</c:if>>${category.label}</option>
					</c:forEach>
				</select>
			</div>
		</fieldset>

		<fieldset id="purchasesFieldset">
			<legend>
				<input type="radio" name="searchType" id="purchases" value="purchases" ${purchasesRadioButton}><label for="purchases"><fmt:message key="PURCHASES" bundle="${r}"/></label>
			</legend>

			<div class="labelInputDiv">
				<input id="openedBids" name="openedBids" type="checkbox" value="1" ${purchasesInputs}
				       <c:if test="${not empty requestScope.openedBids}">checked</c:if>/>
				<label for="openedBids" class="${purchasesLabels}"><fmt:message key="OPENED_BIDS" bundle="${r}"/></label>
			</div>

			<div class="labelInputDiv">
				<input id="currentBids" name="currentBids" type="checkbox" value="1" ${purchasesInputs}
				       <c:if test="${not empty requestScope.currentBids}">checked</c:if>/>
				<label for="currentBids" class="${purchasesLabels}"><fmt:message key="MY_CURRENT_BIDS" bundle="${r}"/></label>
			</div>

			<div class="labelInputDiv">
				<input id="wonBids" name="wonBids" type="checkbox" value="1" ${purchasesInputs}
				       <c:if test="${not empty requestScope.wonBids}">checked</c:if>/>
				<label for="wonBids" class="${purchasesLabels}"><fmt:message key="MY_WON_BIDS" bundle="${r}"/></label>
			</div>

		</fieldset>

		<fieldset id="salesFieldset">
			<legend>
				<input type="radio" name="searchType" id="sales" value="sales" ${salesRadioButton}><label for="sales"><fmt:message key="MY_SALES" bundle="${r}"/></label>
			</legend>

			<div class="labelInputDiv">
				<input id="myCurrentSales" name="myCurrentSales" type="checkbox" value="1" ${salesInputs}
				       <c:if test="${not empty requestScope.myCurrentSales}">checked</c:if>/>
				<label for="myCurrentSales" class="${salesLabels}"><fmt:message key="MY_CURRENT_SALES" bundle="${r}"/></label>
			</div>

			<div class="labelInputDiv">
				<input id="myPendingSales" name="myPendingSales" type="checkbox" value="1" ${salesInputs}
				       <c:if test="${not empty requestScope.myPendingSales}">checked</c:if>/>
				<label for="myPendingSales" class="${salesLabels}"><fmt:message key="MY_PENDING_SALES" bundle="${r}"/></label>
			</div>

			<div class="labelInputDiv">
				<input id="myEndedSales" name="myEndedSales" type="checkbox" value="1" ${salesInputs}
				       <c:if test="${not empty requestScope.myEndedSales}">checked</c:if>/>
				<label for="myEndedSales" class="${salesLabels}"><fmt:message key="MY_ENDED_SALES" bundle="${r}"/></label>
			</div>

		</fieldset>

		<div id="buttonsDiv">
			<input id="search" name="search" type="submit" value="<fmt:message key="SEARCH" bundle="${r}"/>">
		</div>

	</form>

</div>

<div id="itemsViewDiv">

	<c:forEach items="${requestScope.items}" var="item">

		<div class="itemCardShadowDiv">
			<div class="itemCardDiv" id="i${item.identifier}">
				<div class="itemCardImageDiv">

				</div>
				<div class="itemCardPropertiesDiv">

					<div class="itemCardPropertyDiv">
						<p class="propertyName"><fmt:message key="INITIAL_PRICE" bundle="${r}"/></p>
						<p class="propertyValue" id="initialPrice">${item.initialPrice}</p>
					</div>

					<div class="itemCardPropertyDiv">
						<p class="propertyName"><fmt:message key="BID_END" bundle="${r}"/></p>
						<p class="propertyValue" id="biddingEnd"><fmt:formatDate type="BOTH" value="${item.biddingEnd}" timeStyle="MEDIUM"/></p>
					</div>

					<div class="itemCardPropertyDiv">
						<p class="propertyName"><fmt:message key="SELLER" bundle="${r}"/></p>
						<p class="propertyValue" id="seller">${item.seller.username}</p>
					</div>
				</div>

			</div>
		</div>

	</c:forEach>

</div>

<jsp:include page="footer.jsp"/>
