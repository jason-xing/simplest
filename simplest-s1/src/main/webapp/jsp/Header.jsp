<div id="logo">
    <fmt:message key="ui.ProjectName"/>
</div>
<div id="logout">
    <fmt:message key="ui.text.CurrentUser"/><fmt:message key="ui.text.Colon"/><a href="/MyInfoEnter.action">${user.username}</a>
    <a href="/UserLogout.action" style="margin-left:5px;"><fmt:message key="ui.security.text.Logout"/></a>
</div>