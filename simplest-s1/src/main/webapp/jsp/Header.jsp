<div id="logo">
    <fmt:message key="ui.ProjectName"/>
</div>
<div id="logout">
    <fmt:message key="ui.text.CurrentUser"/><fmt:message key="ui.text.Colon"/><a href="/MyInfoEnter.action" class="button" style="padding:0.3em 0.3em;">${user.username}</a>
    <a href="/UserLogout.action" class="button" style="padding:0.3em 0.3em; margin-left:0.5em;"><fmt:message key="ui.security.text.Logout"/></a>
</div>