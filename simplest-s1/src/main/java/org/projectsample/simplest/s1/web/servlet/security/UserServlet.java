package org.projectsample.simplest.s1.web.servlet.security;

import java.io.IOException;
import java.util.Locale;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.projectsample.simplest.s1.security.bean.Role;
import org.projectsample.simplest.s1.security.bean.User;
import org.projectsample.simplest.s1.security.exception.EmailRegisteredException;
import org.projectsample.simplest.s1.security.exception.PasswordNotCorrectException;
import org.projectsample.simplest.s1.security.exception.UsernameExistsException;
import org.projectsample.simplest.s1.security.exception.UsernameNotExistException;
import org.projectsample.simplest.s1.security.service.ResourceService;
import org.projectsample.simplest.s1.security.service.RoleService;
import org.projectsample.simplest.s1.security.service.UserService;
import org.projectsample.simplest.s1.web.utils.Resource;
import org.projectsample.simplest.s1.web.utils.StringUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns="/User")
public class UserServlet extends HttpServlet {

    private final static String MAIN_PAGE = "/jsp/Main.jsp";
    private final static String INDEX_PAGE = "/jsp/Index.jsp";
    private UserService userService = new UserService();
    private RoleService roleService = new RoleService();
    private ResourceService resourceService = new ResourceService();
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest req, HttpServletResponse resp)
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        doPost(req, resp);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest req, HttpServletResponse resp)
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        String urlCp = req.getParameter("urlCp");
        if (urlCp == null) {
        } else if (urlCp.equals("RegisterEnter")) {
            registerEnter(req, resp);
        } else if (urlCp.equals("Register")) {
            register(req, resp);
        } else if (urlCp.equals("Login")) {
            login(req, resp);
        } else if (urlCp.equals("Logout")) {
            logout(req, resp);
        } else if (urlCp.equals("MyInfoEnter")) {
            myInfoEnter(req, resp);
        } else if (urlCp.equals("MyInfoSave")) {
            myInfoSave(req, resp);
        }
    }
    
    private void registerEnter(HttpServletRequest req, HttpServletResponse resp) 
            throws IOException, ServletException {
        forwardTo("/jsp/security/UserRegister.jsp", req, resp);
    }

    private void register(HttpServletRequest req, HttpServletResponse resp) 
            throws IOException, ServletException {
        Locale locale = req.getLocale();
        String reqFromPage = "/jsp/security/UserRegister.jsp";
        String username = StringUtils.trim(req.getParameter("username"));
        if (username.equals("")) {
            req.setAttribute("message", Resource.get(locale, "msg.security.UsernameEmpty"));
            forwardTo(reqFromPage, req, resp);
            return;
        }
        String password = StringUtils.trim(req.getParameter("password"));
        if (password.equals("")) {
            req.setAttribute("message", Resource.get(locale, "msg.security.PasswordEmpty"));
            forwardTo(reqFromPage, req, resp);
            return;
        }
        String passwordAgain = StringUtils.trim(req.getParameter("passwordAgain"));
        if (passwordAgain.equals("")) {
            req.setAttribute("message", Resource.get(locale, "msg.security.PasswordAgainEmpty"));
            forwardTo(reqFromPage, req, resp);
            return;
        }
        if (!passwordAgain.equals(password)) {
            req.setAttribute("message", Resource.get(locale, "msg.security.PasswordAgainNotCorrect"));
            forwardTo(reqFromPage, req, resp);
            return;
        }        
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(req.getParameter("email"));
        try {
            userService.add(user);
            req.setAttribute("message", Resource.get(locale, "msg.SubmitSuccess"));
            req.setAttribute("registerSuccess", true);
        } catch (UsernameExistsException e) {
            req.setAttribute("message", Resource.get(locale, "msg.security.UsernameExists"));
        } catch (EmailRegisteredException e) {
            req.setAttribute("message", Resource.get(locale, "msg.security.EmailRegistered"));
        }
        forwardTo(reqFromPage, req, resp);
    }

    private void login(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Locale locale = req.getLocale();
        String username = StringUtils.trim(req.getParameter("username"));
        if (username.equals("")) {
            req.setAttribute("message", Resource.get(locale, "msg.security.UsernameEmpty"));
            forwardTo(INDEX_PAGE, req, resp);
            return;
        }
        String password = StringUtils.trim(req.getParameter("password"));
        if (password.equals("")) {
            req.setAttribute("message", Resource.get(locale, "msg.security.PasswordEmpty"));
            forwardTo(INDEX_PAGE, req, resp);
            return;
        }
        try {
            User user = userService.authenticate(username, password);
            HttpSession session = req.getSession(true);
            // Set the attributes for the session. Those attributes are: user, roles,
            // isAdministrator, menus, urls.
            session.setAttribute("user", user);
            Set<Role> roles = userService.getRoles(user.getId());
            session.setAttribute("roles", roles);
            // Is an administrator?
            // Once one of roles of the user is Administrator, the user will have Administrator's permissions.
            if (roles.contains(roleService.getAdministrator())) {
                session.setAttribute("isAdministrator", true);
                // All the menus
                session.setAttribute("menus", resourceService.getAllMenus());
                // All the URLs
                session.setAttribute("urls", resourceService.getAllUrls());
            } else {
                session.setAttribute("isAdministrator", false);
                session.setAttribute("menus", roleService.getMenus(roles));
                session.setAttribute("urls", roleService.getUrls(roles));
            }
            req.setAttribute("CONTENT_PAGE", "/jsp/Welcome.jsp");
            forwardTo(MAIN_PAGE, req, resp);
        } catch (UsernameNotExistException e) {
            req.setAttribute("message", Resource.get(locale, "msg.security.UsernameNotExist"));
            forwardTo(INDEX_PAGE, req, resp);
        } catch (PasswordNotCorrectException e) {
            req.setAttribute("message", Resource.get(locale, "msg.security.PasswordNotCorrect"));
            forwardTo(INDEX_PAGE, req, resp);
        } 
    }
    
    private void logout(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        session.invalidate();
        forwardTo(INDEX_PAGE, req, resp);
    }
    
    private void myInfoEnter(HttpServletRequest req, HttpServletResponse resp) 
            throws IOException, ServletException {
        req.setAttribute("CONTENT_PAGE", "/jsp/security/MyInfo.jsp");
        forwardTo(MAIN_PAGE, req, resp);
    }

    private void myInfoSave(HttpServletRequest req, HttpServletResponse resp) 
            throws IOException, ServletException {
        req.setAttribute("CONTENT_PAGE", "/jsp/security/MyInfo.jsp");
        forwardTo(MAIN_PAGE, req, resp);
    }
    
    
    private void forwardTo(String path, HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, resp);
    }
    
}
