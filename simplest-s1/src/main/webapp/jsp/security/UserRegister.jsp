<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setBundle basename="i18n/resource"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title><fmt:message key="ui.ProjectName"/></title>
<link rel="stylesheet" type="text/css" href="css/global.css"/>
</head>
<body>
<div class="smart-box-layout">
    <div class="smart-box-header">
        <fmt:message key="ui.security.text.Register"/>
    </div>
    <div class="smart-box-body">
        <form action="/UserRegister.action" method="post">
            <table class="form-table">
                <tr>
                    <th style="width:100px; height:20px;"></th>
                    <td class="prompt-message">${message}</td>
                </tr>
                <tr>
                    <th><fmt:message key="ui.security.text.Username"/>:</th>
                    <td><input type="text" name="username" class="text-input" style="width:180px"/></td>
                </tr>
                <tr>
                    <th><fmt:message key="ui.security.text.Password"/>:</th>
                    <td><input type="password" name="password" class="text-input" style="width:180px"/></td>
                </tr>
                <tr>
                    <th><fmt:message key="ui.security.text.PasswordAgain"/>:</th>
                    <td><input type="password" name="passwordAgain" class="text-input" style="width:180px"/></td>
                </tr>
                <tr>
                    <th><fmt:message key="ui.security.text.Email"/>:</th>
                    <td><input type="text" name="email" class="text-input" style="width:180px"/></td>
                </tr>
                <tr>
                    <th style="height:30px;"></th>
                    <td>
                        <input type="submit" value="<fmt:message key="ui.security.button.Register"/>" class="submit-button"/>
                        <c:if test="${registerSuccess}">
                            <a href="/" style="margin-left:53px;"><fmt:message key="ui.security.button.ForwardToLogin"/></a>
                        </c:if>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
</body>
</html>