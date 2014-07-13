<%@ page contentType="text/html; charset=utf8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!-- Process the problem that IE cann't forward automatically -->
<% response.setStatus(200); %>
<fmt:setBundle basename="i18n/resource"/>
<html>
<head>
<title><fmt:message key="ui.text.Exception"/></title>
</head>
<body>
<fmt:message key="ui.security.NoPermission"/>
</body>
</html>