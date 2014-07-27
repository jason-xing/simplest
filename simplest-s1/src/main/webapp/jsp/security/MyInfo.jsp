<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setBundle basename="i18n/resource"/>
<script>
$(function() {
    $("#save-button").click(
        function() {
            var tipsMessage = $(".tips-message");
            var password = $('input[name="password"]').val().trim();
            if (password != "" && password != $('input[name="passwordAgain"]').val().trim()) {
                tipsMessage.text($("#msg1").val());
                return;
            }
            document.forms[0].submit();
        }
    );
});
</script>
<form action="/MyInfoModify.action" method="post">
    <table class="form-table" style="margin:100px auto">
        <tr>
            <th><fmt:message key="ui.security.text.Username"/>:</th>
            <td><input type="text" name="username" class="text-input" style="width:180px" value="${sessionScope.user.username}" readonly/></td>
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
            <td><input type="text" name="email" class="text-input" style="width:180px" value="${param.email}"/></td>
        </tr>
        <tr>
            <th style="height:30px;"></th>
            <td>
                <input type="button" id="save-button" value="<fmt:message key="ui.button.Save"/>" class="button"/>
            </td>
        </tr>
    </table>
</form>
<input type="hidden" id="msg1" value="<fmt:message key="msg.security.PasswordAgainNotCorrect"/>">