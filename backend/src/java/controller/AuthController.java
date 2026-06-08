package controller;

import dao.UserDAO;
import model.User;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/auth")
public class AuthController extends HttpServlet {

    // Base instantiation reference 
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if (action == null) {
            action = "loginForm";
        }

        try {
            switch (action.toLowerCase()) {
                case "login":
                    handleLogin(request, response);
                    break;
                case "register":
                    handleRegister(request, response);
                    break;
                case "logout":
                    handleLogout(request, response);
                    break;
                default:
                    response.sendRedirect("Login.jsp");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("Login.jsp?error=system");
        }
    }

    // ================= 1. LOGIN LIFECYCLE =================
    private void handleLogin(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String formRole = request.getParameter("role");

        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            response.sendRedirect("Login.jsp?error=invalid");
            return;
        }

        User u = userDAO.login(email.trim(), password);

        if (u == null) {
            response.sendRedirect("Login.jsp?error=invalid");
            return;
        }

        if (formRole != null && !formRole.equalsIgnoreCase(u.getRole())) {
            response.sendRedirect("Login.jsp?error=roleMismatch");
            return;
        }

        // Clean up session fixation targets
        HttpSession oldSession = request.getSession(false);
        if (oldSession != null) {
            oldSession.invalidate();
        }

        HttpSession session = request.getSession(true);
        session.setAttribute("user", u);
        session.setAttribute("role", u.getRole());

        switch (u.getRole().toLowerCase()) {
            case "lecturer":
                response.sendRedirect("lecturer-dashboard.jsp");
                break;
            case "admin":
                response.sendRedirect("course-list.jsp");
                break;
            case "student":
                response.sendRedirect("student-dashboard.jsp");
                break;
            default:
                response.sendRedirect("Login.jsp?error=role");
                break;
        }
    }

    // ================= 2. REGISTER LIFECYCLE =================
    private void handleRegister(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String matricNo = request.getParameter("matricNo");

        if (name == null || email == null || password == null || role == null ||
            name.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty() || role.trim().isEmpty()) {
            response.sendRedirect("Register.jsp?error=emptyFields");
            return;
        }

        role = role.trim().toLowerCase();

        if ("admin".equalsIgnoreCase(role)) {
            response.sendRedirect("Register.jsp?error=unauthorizedRole");
            return;
        }

        User u = new User();
        u.setName(name.trim());
        u.setEmail(email.trim());
        u.setPassword(password); 
        u.setRole(role);
        
        // Safe evaluation mapping 
        if ("student".equals(role) && matricNo != null) {
            u.setMatricNo(matricNo.trim());
        } else {
            u.setMatricNo(null);
        }

        // FIXED: Lowercase invocation name to perfectly align with regular DAO syntax
        boolean isRegistered = userDAO.register(u);

        if (isRegistered) {
            response.sendRedirect("Login.jsp?success=registered");
        } else {
            response.sendRedirect("Register.jsp?error=duplicateOrFailed");
        }
    }

    // ================= 3. LOGOUT LIFECYCLE =================
    private void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        response.sendRedirect("Login.jsp");
    }
}