<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<spring:url value="/resources" var="resourceUrl"/>

<script src="${resourceUrl }/js/menu.js" type="text/javascript"></script>
<link type="text/css" href="${resourceUrl }/css/menu" rel="stylesheet" media="screen"/>

<div id="sse1">
  <div id="sses1">
    <ul>
      <li><a href="/web/records/index">View Data</a></li>
      <li><a href="/web/">Import Errors</a></li>
      <li><a href="/web/admin/index">Admin</a></li>
    </ul>
  </div>
</div>
