package controller;

import dao.CourseDAO;
import model.Course;
import model.User;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/CourseServlet")
public class CourseServlet extends HttpServlet {

    private CourseDAO dao;

    @Override
    public void init() {
        dao = new CourseDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {

            String action = request.getParameter("action");

            // 🔥 IMPORTANT: DO NOT force new session
            HttpSession session = request.getSession(false);

            if (session == null) {
                response.sendRedirect("Login.jsp");
                return;
            }

            User user = (User) session.getAttribute("user");

            if (user == null) {
                response.sendRedirect("Login.jsp");
                return;
            }

            String role = user.getRole();

            // ================= CREATE =================
            if ("create".equalsIgnoreCase(action)) {

                Course c = new Course();
                c.setTitle(request.getParameter("title"));
                c.setDescription(request.getParameter("description"));
                c.setCourseCode(request.getParameter("courseCode"));
                c.setLecturerId(user.getUserId());

                dao.addCourse(c);

                response.sendRedirect(getRedirect(role));
                return;
            }

            // ================= UPDATE =================
            if ("update".equalsIgnoreCase(action)) {

                int courseId = Integer.parseInt(request.getParameter("courseId"));

                Course c = new Course();
                c.setCourseId(courseId);
                c.setTitle(request.getParameter("title"));
                c.setDescription(request.getParameter("description"));
                c.setCourseCode(request.getParameter("courseCode"));
                c.setLecturerId(user.getUserId());

                dao.updateCourse(c);

                response.sendRedirect(getRedirect(role));
                return;
            }

            // ================= DELETE =================
            if ("delete".equalsIgnoreCase(action)) {

                int courseId = Integer.parseInt(request.getParameter("courseId"));

                // lecturer only delete own, admin delete all
                if ("admin".equalsIgnoreCase(role)) {
                    dao.deleteCourseByAdmin(courseId);
                } else {
                    dao.deleteCourse(courseId, user.getUserId());
                }

                response.sendRedirect(getRedirect(role));
                return;
            }

            response.sendRedirect(getRedirect(role));

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("Login.jsp");
        }
    }

    private String getRedirect(String role) {

        if ("admin".equalsIgnoreCase(role)) {
            return "course-list.jsp";
        } else {
            return "lecturer-dashboard.jsp";
        }
    }
}