package middleware;

import model.User;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Authentication and Authorization Guard Filter.
 * Intercepts incoming web traffic to ensure non-authenticated users cannot
 * access restricted dashboard environments.
 */
@WebFilter("/*") // Intercepts all incoming application routes
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if required
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // 1. Extract the current request path relative to the context root
        String contextPath = httpRequest.getContextPath();
        String requestURI = httpRequest.getRequestURI();
        String path = requestURI.substring(contextPath.length());

        // 2. Define public assets and routes that DO NOT require authentication
        boolean isLoginPage = path.equals("/Login.jsp") || path.equals("/");
        boolean isRegisterPage = path.equals("/Register.jsp");
        boolean isAuthAction = path.equals("/auth"); // Our central AuthController endpoint
        
        // Allow public static resources like CSS, JavaScript, or images to load freely
        boolean isStaticResource = path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/images/") || path.endsWith(".css");

        // 3. Inspect the session state
        HttpSession session = httpRequest.getSession(false);
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        // 4. ROUTING GUARD RULES LAYER
        if (isLoggedIn) {
            // User is authenticated!
            if (isLoginPage || isRegisterPage) {
                // Prevent logged-in users from viewing login/register pages; redirect to their dashboard
                User u = (User) session.getAttribute("user");
                redirectToDashboard(u.getRole(), httpResponse, contextPath);
            } else {
                // Optional: Basic Role-Based Access Control (RBAC) URL Guardrails
                if (path.contains("lecturer-dashboard") && !checkRole(session, "lecturer")) {
                    httpResponse.sendRedirect(contextPath + "/Login.jsp?error=unauthorized");
                    return;
                }
                if (path.contains("student-dashboard") && !checkRole(session, "student")) {
                    httpResponse.sendRedirect(contextPath + "/Login.jsp?error=unauthorized");
                    return;
                }
                if (path.contains("course-list") && !checkRole(session, "admin")) {
                    httpResponse.sendRedirect(contextPath + "/Login.jsp?error=unauthorized");
                    return;
                }

                // Authorized request. Let it pass through to the target servlet/JSP
                chain.doFilter(request, response);
            }
        } else {
            // User is a guest (Not logged in)
            if (isLoginPage || isRegisterPage || isAuthAction || isStaticResource) {
                // Allow them to visit public endpoints or attempt log in
                chain.doFilter(request, response);
            } else {
                // Caught trying to sneak into a dashboard! Force redirect back to login screen
                httpResponse.sendRedirect(contextPath + "/Login.jsp?error=loginRequired");
            }
        }
    }

    // Helper method to safely verify role scopes
    private boolean checkRole(HttpSession session, String expectedRole) {
        String userRole = (String) session.getAttribute("role");
        return userRole != null && userRole.equalsIgnoreCase(expectedRole);
    }

    // Helper method to route users to their respective home panels
    private void redirectToDashboard(String role, HttpServletResponse response, String contextPath) throws IOException {
        switch (role.toLowerCase()) {
            case "lecturer":
                response.sendRedirect(contextPath + "/lecturer-dashboard.jsp");
                break;
            case "admin":
                response.sendRedirect(contextPath + "/course-list.jsp");
                break;
            case "student":
                response.sendRedirect(contextPath + "/student-dashboard.jsp");
                break;
            default:
                response.sendRedirect(contextPath + "/Login.jsp");
                break;
        }
    }

    @Override
    public void destroy() {
        // Cleanup resources if required
    }
}