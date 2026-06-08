package dao;

import db.DBConnection;
import model.Course;

import java.sql.*;
import java.util.*;

public class CourseDAO {

    Connection con = DBConnection.getConnection();

    // ================= CREATE =================
    public void addCourse(Course c) throws Exception {

        String sql =
            "INSERT INTO courses(title, description, courseCode, lecturerId) VALUES(?,?,?,?)";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setString(1, c.getTitle());
        ps.setString(2, c.getDescription());
        ps.setString(3, c.getCourseCode());
        ps.setInt(4, c.getLecturerId());

        ps.executeUpdate();
    }

    // ================= UPDATE (UTK LECTURER & ADMIN) =================
    // FIX: Ditukar supaya kalau lecturerId > 0 (Lecturer), dia semak owner. 
    // Kalau lecturerId <= 0 (Admin), Admin boleh terus update semua benda!
    public void updateCourse(Course c) throws Exception {
        String sql;
        PreparedStatement ps;

        if (c.getLecturerId() > 0) {
            // Jika dikemaskini oleh LECTURER (Mesti check hak milik)
            sql = "UPDATE courses SET title=?, description=?, courseCode=? WHERE courseId=? AND lecturerId=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, c.getTitle());
            ps.setString(2, c.getDescription());
            ps.setString(3, c.getCourseCode());
            ps.setInt(4, c.getCourseId());
            ps.setInt(5, c.getLecturerId());
        } else {
            // Jika dikemaskini oleh ADMIN (Boleh kemaskini apa sahaja tanpa sekat lecturerId)
            sql = "UPDATE courses SET title=?, description=?, courseCode=? WHERE courseId=?";
            ps = con.prepareStatement(sql);
            ps.setString(1, c.getTitle());
            ps.setString(2, c.getDescription());
            ps.setString(3, c.getCourseCode());
            ps.setInt(4, c.getCourseId());
        }

        ps.executeUpdate();
    }

    // ================= DELETE (LECTURER ONLY) =================
    public void deleteCourse(int courseId, int lecturerId) throws Exception {

        String sql =
            "DELETE FROM courses WHERE courseId=? AND lecturerId=?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, courseId);
        ps.setInt(2, lecturerId);

        ps.executeUpdate();
    }

    // ================= DELETE (ADMIN FULL ACCESS) =================
    public void deleteCourseByAdmin(int courseId) throws Exception {

        String sql =
            "DELETE FROM courses WHERE courseId=?";

        PreparedStatement ps = con.prepareStatement(sql);

        ps.setInt(1, courseId);

        ps.executeUpdate();
    }

    // ================= GET ALL =================
    public List<Course> getAllCourses() throws Exception {

        List<Course> list = new ArrayList<>();

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery("SELECT * FROM courses");

        while (rs.next()) {

            Course c = new Course();

            c.setCourseId(rs.getInt("courseId"));
            c.setTitle(rs.getString("title"));
            c.setDescription(rs.getString("description"));
            c.setCourseCode(rs.getString("courseCode"));
            c.setLecturerId(rs.getInt("lecturerId"));

            list.add(c);
        }

        return list;
    }

    // ================= BY LECTURER =================
    public List<Course> getCoursesByLecturer(int lecturerId) throws Exception {

        List<Course> list = new ArrayList<>();

        PreparedStatement ps = con.prepareStatement(
            "SELECT * FROM courses WHERE lecturerId=?"
        );

        ps.setInt(1, lecturerId);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            Course c = new Course();

            c.setCourseId(rs.getInt("courseId"));
            c.setTitle(rs.getString("title"));
            c.setDescription(rs.getString("description"));
            c.setCourseCode(rs.getString("courseCode"));
            c.setLecturerId(rs.getInt("lecturerId"));

            list.add(c);
        }

        return list;
    }

    public int findCourseByCode(String code) throws Exception {

        String sql = "SELECT courseId FROM courses WHERE courseCode=?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, code);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("courseId");
        }

        return -1;
    }

    public List<Course> getCoursesByStudent(int studentId) throws Exception {

        List<Course> list = new ArrayList<>();

        PreparedStatement ps = con.prepareStatement(
            "SELECT c.* FROM courses c " +
            "JOIN enrollment e ON c.courseId = e.courseId " +
            "WHERE e.studentId=?"
        );

        ps.setInt(1, studentId);

        ResultSet rs = ps.executeQuery();

        while(rs.next()){
            Course c = new Course();
            c.setCourseId(rs.getInt("courseId"));
            c.setTitle(rs.getString("title"));
            c.setCourseCode(rs.getString("courseCode"));
            list.add(c);
        }

        return list;
    }

    // ================= GET COURSE BY ID =================
    public Course getCourseById(int courseId) throws Exception {
        
        String sql = "SELECT * FROM courses WHERE courseId=?";
        
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, courseId);
        
        ResultSet rs = ps.executeQuery();
        
        if (rs.next()) {
            Course c = new Course();
            c.setCourseId(rs.getInt("courseId"));
            c.setTitle(rs.getString("title"));
            c.setDescription(rs.getString("description"));
            c.setCourseCode(rs.getString("courseCode"));
            c.setLecturerId(rs.getInt("lecturerId"));
            return c; 
        }
        
        return null; 
    }
}