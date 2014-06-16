<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<fmt:setBundle basename="i18n/resource"/>
<div class="layout">
    <form action="/MyInfoSave.action" method="post">
        <table border="0" class="form-table">
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
                <td><input type="text" name="email" class="text-input" style="width:180px" value="${sessionScope.user.email}"/></td>
            </tr>
            <tr>
                <th style="height:30px;"></th>
                <td>
                    <input type="submit" value="<fmt:message key="ui.button.Save"/>" class="submit-button"/>
                </td>
            </tr>
        </table>
    </form>
</div>