<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setBundle basename="i18n/resource"/>
<fmt:message key="ui.text.Welcome"/>${user.username}<fmt:message key="ui.text.Comma"/>
<fmt:message key="ui.text.YourEmailIs"/><fmt:message key="ui.text.Colon"/>${user.email}<fmt:message key="ui.text.FullStop"/>