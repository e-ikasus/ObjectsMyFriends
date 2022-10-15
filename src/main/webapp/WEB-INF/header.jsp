<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setBundle basename="locale" var="r"/>

<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">

	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta http-equiv="Content-Language" content="fr, en"/>

	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="description" content="Site d'enchères d'objets d'occasion">
	<meta name="author" content="E-ikasus">

	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/theme.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/footer.css">

	<% for (String cssFile: request.getParameter("cssFiles").split(","))
		out.println("\t<link rel=\"stylesheet\" href=\"" + request.getContextPath() + "/css/" + cssFile + ".css\">");%>

	<% for (String jsFile: request.getParameter("jsFiles").split(","))
		out.println("\t<script src=\"" + request.getContextPath() + "/javascript/" + jsFile + ".js\"></script>");%>

	<title><fmt:message key="TITLE" bundle="${r}"/></title>
</head>

<body>
	<div id="main">

		<div id="header">
			<div id="title">
				<fmt:message key="TITLE" bundle="${r}"/>
			</div>
			<div id="board">
				<nav id="menu">
					<ul>
						<c:if test="${sessionScope.user == null}">
							<li><a href="${pageContext.request.contextPath}/subscribe"><fmt:message key="SUBSCRIBE" bundle="${r}"/></a></li><!--
							--><li><a href="${pageContext.request.contextPath}/login"><fmt:message key="LOG_IN" bundle="${r}"/></a></li>
						</c:if>

						<c:if test="${sessionScope.user != null && !sessionScope.user.admin}">
							<li><a href="${pageContext.request.contextPath}/show_profile"><fmt:message key="PROFILE" bundle="${r}"/></a></li><!--
					 --><li><a href="${pageContext.request.contextPath}/item_sell"><fmt:message key="ITEM_SELL" bundle="${r}"/></a></li><!--
					 --><li><a href="${pageContext.request.contextPath}/logout"><fmt:message key="LOG_OUT" bundle="${r}"/></a></li>
						</c:if>

						<c:if test="${sessionScope.user != null && sessionScope.user.admin}">
							<li><a href="${pageContext.request.contextPath}/show_profile"><fmt:message key="PROFILE" bundle="${r}"/></a></li><!--
					 --><li><a href="${pageContext.request.contextPath}/item_sell"><fmt:message key="ITEM_SELL" bundle="${r}"/></a></li><!--
					 --><li><a href="${pageContext.request.contextPath}/logout"><fmt:message key="LOG_OUT" bundle="${r}"/></a></li><!--
					 --><li><a href="${pageContext.request.contextPath}/admin"><fmt:message key="ADMIN" bundle="${r}"/></a></li>
						</c:if>
					</ul>
				</nav>
				<div id="identity">
					<c:if test="${sessionScope.user != null}"><fmt:message key="CONNECTED_AS" bundle="${r}"/> ${sessionScope.user.username}</c:if>
					<c:if test="${sessionScope.user == null}"><fmt:message key="DISCONNECTED" bundle="${r}"/></c:if>
				</div>
			</div>
		</div>

		<div id="content">
