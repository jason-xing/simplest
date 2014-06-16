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
        <fmt:message key="ui.ProjectName"/>
    </div>
    <div class="smart-box-body">
        <form action="/UserLogin.action" method="post">
            <table border="0" class="form-table">
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
                    <th style="height:30px;"></th>
                    <td>
                        <input type="submit" value="<fmt:message key="ui.security.button.Login"/>" class="submit-button"/>
                        <a href="/UserRegisterEnter.action" style="margin-left:50px;"><fmt:message key="ui.security.button.Register"/></a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
</body>
</html>