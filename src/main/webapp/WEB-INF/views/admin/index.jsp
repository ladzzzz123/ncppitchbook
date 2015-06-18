<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ page session="false" %>

<spring:url value="/resources" var="resourceUrl"/>

<script>
	
	
$("document").ready(function(){

	
});
	
	
</script>
<style>
a {
    color: #0060B6;
    text-decoration: none;
}

a:hover 
{
     color:#00A0C6; 
     text-decoration:none; 
     cursor:pointer;  
}
</style>

<div style="font-size:1.3em;margin-top:50px;margin-bottom:50px;font-weight:bold;text-align:center;">
	<table id="adminDataTable" width="100%" >
		<thead>
		</thead>
		<tbody class="ajaxDataTable">
			<tr>
				<td><a href="user/index">Users</a></td>
				<td><a href="category/index">Categories</a></td>
				<td><a href="industry/index">Industry</a></td>
				<td><a href="company/index">Company</a></td>
				<td><a href="companyRecordType/index">Company Types</a></td>
			</tr>
		</tbody>
	</table>
</div>
