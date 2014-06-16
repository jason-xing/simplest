<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setBundle basename="i18n/resource"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><fmt:message key="ui.ProjectName"/></title>
<link rel="stylesheet" type="text/css" href="css/global.css"/>
<link rel="stylesheet" type="text/css" href="css/main.css"/>
</head>
<body>
<div id="header"><%@ include file="Header.jsp" %></div>
<div id="menu"><%@ include file="Menu.jsp" %></div>
<div id="prompt"><%@ include file="Prompt.jsp"%></div>
<div id="body"><jsp:include page="${CONTENT_PAGE}"/></div>
<div id="footer"><%@ include file="Footer.jsp" %></div>
</body>
</html>