<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ page session="false" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Home</title>
	<meta http-equiv="X-UA-Compatible" content="IE=edge"></meta>
	<title><tiles:insertAttribute name="title" ignore="true"/> - <tiles:insertAttribute name="heading"/></title>
	
	<spring:url value="/resources" var="resourceUrl"/>
	
	<link type="text/css" href="${resourceUrl }/css/jquery-ui.min.css" rel="stylesheet" media="screen"/>
	<link type="text/css" href="${resourceUrl }/css/jquery-ui.structure.min.css" rel="stylesheet" media="screen"/>
	<link type="text/css" href="${resourceUrl }/css/jquery-ui.theme.min.css" rel="stylesheet" media="screen"/>
	<link type="text/css" href="${resourceUrl }/css/jquery.mobile.custom.theme.min.css" rel="stylesheet" media="screen"/>
	<link type="text/css" href="${resourceUrl }/css/jquery.mobile.custom.structure.min.css" rel="stylesheet" media="screen"/>
	<link type="text/css" href="${resourceUrl }/css/menu.css" rel="stylesheet" media="screen"/>
	<link type="text/css" href="${resourceUrl }/css/dataTables.jqueryui.css" rel="stylesheet" media="screen"/>
	<link type="text/css" href="${resourceUrl }/css/ncp.css" rel="stylesheet" media="screen"/>
	<link type="text/css" href="${resourceUrl }/css/jquery.dataTables.css" rel="stylesheet" media="screen"/>
	<link type="text/css" href="${resourceUrl }/css/dataTables.colVis.css" rel="stylesheet" media="screen"/>
	
	<script type="text/javascript" src="${resourceUrl }/js/jquery.mobile.custom.min.js"></script>
	<script type="text/javascript" src="${resourceUrl }/js/menu.js"></script>
	<script type="text/javascript" src="${resourceUrl }/js/jquery.form.js"></script>
	<script type="text/javascript" src="${resourceUrl }/js/jquery-1.11.2.min.js"></script>
	<script type="text/javascript" src="${resourceUrl }/js/jquery-ui.min.js"></script>
	<script type="text/javascript" src="${resourceUrl }/js/jquery.dataTables.min.js"></script>
	<script type="text/javascript" src="${resourceUrl }/js/dataTables.tableTools.min.js"></script>
	<script type="text/javascript" src="${resourceUrl }/js/dataTables.colVis.min.js"></script>
</head>

<body>
	<div class="container_home">
		<div id="masthead">
			<tiles:insertAttribute name="header"/>
		</div>
		
		<tiles:insertAttribute name="navigation"/>
		
		<div class="landingPageHeader">
			<tiles:insertAttribute name="heading"/>
		</div>
		
		<div id="newContainer">
			<div id="ncpPageContent" style="display:none;">
				<tiles:insertAttribute name="body"/>
			</div>
		</div>
		
		<div class="footer">
			<tiles:insertAttribute name="footer"/>
		</div>
	</div>
</body>

<script>
	$("document").ready(function(){
		$("#ncpPageContent").show();
	});
</script>


</html>
