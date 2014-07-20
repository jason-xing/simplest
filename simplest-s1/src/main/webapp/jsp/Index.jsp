<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setBundle basename="i18n/resource"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title><fmt:message key="ui.ProjectName"/></title>
<script src="jquery/jquery-2.1.1.js"></script>
<link rel="stylesheet" type="text/css" href="css/global.css"/>
<link rel="stylesheet" type="text/css" href="css/smart-box.css"/>
<script>
$(function() {
    $(".submit-button").click(
        function() {
            var tipsMessage = $(".tips-message");
            if ($("#username").val().trim() == "") {
                tipsMessage.text($("#msg1").val());
                return;
            }
            if ($("#password").val().trim() == "") {
            	tipsMessage.text($("#msg2").val());
                return;
            }
            document.forms[0].submit();
        }
    );
});
</script>
</head>
<body>
<div class="smart-box-layout">
    <div class="smart-box-header">
        <fmt:message key="ui.ProjectName"/>
    </div>
    <div class="smart-box-body">
        <form action="/UserLogin.action" method="post">
            <table class="form-table">
                <tr>
                    <th style="width:100px; height:20px;"></th>
                    <td class="tips-message">${message}</td>
                </tr>
                <tr>
                    <th><fmt:message key="ui.security.text.Username"/>:</th>
                    <td><input type="text" id="username" name="username" class="text-input" style="width:180px"/></td>
                </tr>
                <tr>
                    <th><fmt:message key="ui.security.text.Password"/>:</th>
                    <td><input type="password" id="password" name="password" class="text-input" style="width:180px"/></td>
                </tr>
                <tr>
                    <th style="height:30px;"></th>
                    <td>
                        <input type="button" value="<fmt:message key="ui.security.button.Login"/>" class="submit-button"/>
                        <a href="/UserRegisterEnter.action" style="margin-left:50px;"><fmt:message key="ui.security.button.Register"/></a>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</div>
<input type="hidden" id="msg1" value="<fmt:message key="msg.security.UsernameEmpty"/>">
<input type="hidden" id="msg2" value="<fmt:message key="msg.security.PasswordEmpty"/>">
</body>
</html>