package nl.food4bees.backend.user;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req,
                         ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;
        HttpSession session = request.getSession(false);

        String servletPath = ((HttpServletRequest)request).getRequestURI();
        String contextPath = request.getContextPath();
        if (servletPath.equals(contextPath + "/") ||
            servletPath.startsWith(contextPath + "/images/") ||
            servletPath.startsWith(contextPath + "/css/") ||
            servletPath.startsWith(contextPath + "/index.jsp") ||
            servletPath.startsWith(contextPath + "/login.jsp") ||
            servletPath.startsWith(contextPath + "/message.jsp") ||
            servletPath.startsWith(contextPath + "/reset_password.jsp") ||
            servletPath.startsWith(contextPath + "/new_password.jsp") ||
            servletPath.startsWith(contextPath + "/register.jsp") ||
            servletPath.startsWith(contextPath + "/verify-registration") ||
            servletPath.startsWith(contextPath + "/authenticate") ||
            servletPath.startsWith(contextPath + "/reset-password") ||
            servletPath.startsWith(contextPath + "/new-password") ||
            servletPath.startsWith(contextPath + "/register") ||
            servletPath.startsWith(contextPath + "/sync-plants") ||
            servletPath.startsWith(contextPath + "/add-vegetation")) {
            chain.doFilter(req, res);

            return;
        }

        if (session == null || session.getAttribute("uid") == null) {
            response.sendRedirect(contextPath + "/login.jsp"); // No logged-in user found, so redirect to login page.
        } else {
            chain.doFilter(req, res); // Logged-in user found, so just continue request.
        }
    }

    @Override
    public void destroy() {
    }
}
