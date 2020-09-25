<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ page import="com.epam.project.constants.Constants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="${ sessionScope.get(\"locale\").language }" />
<%-- <fmt:setLocale value="ru_RU"/> --%>
<c:set value="${ sessionScope.get(\"user\") }" var="user" scope="page" />
<c:set value="${ sessionScope.get(\"locale\") }" var="locale"
	scope="page" />


<!DOCTYPE html>
<html>
<head>
	<link
		href="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css"
		rel="stylesheet" id="bootstrap-css">
	<script
		src="//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js"></script>
	<script
		src="//cdnjs.cloudflare.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
	
	<meta charset="ISO-8859-1">
	<title><c:out value="${ title }"></c:out></title>
	
	<link href="<c:url value="/css/style.css" />" rel="stylesheet">
</head>
<body>
	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-9 nopadding">