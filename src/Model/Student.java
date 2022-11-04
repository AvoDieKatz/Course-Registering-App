package Model;

import java.sql.*;
import java.util.ArrayList;

public class Student {

    private String username;

    public String getUsername() {
        return username;
    }

    public Student() {

    }

    public Student(String username) {
        this.username = username;
    }


    public static boolean register(String fullName, String username, String password) {
        Database db = new Database();
        String query = "INSERT INTO tbl_user " +
                "(username, user_password, full_name)" +
                "VALUES (?, ?, ?)";
        try (Connection conn = db.getConn()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, fullName);
                stmt.executeUpdate();
                return true;
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return false;
        }
    }


    public static int login(String in_username, String in_password) {
        Database db = new Database();
        String query = "SELECT username, user_password, is_admin from tbl_user WHERE username = ?;";
        try (Connection conn = db.getConn()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, in_username);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String username = rs.getString(1);
                        String password = rs.getString(2);
                        if (username.equals(in_username) && password.equals(in_password)) {
                            if (rs.getInt(3) == 1)
                                return 1;
                            else if (rs.getInt(3) == 0)
                                return 0;
                        } else
                            return -1;
                    }
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return -1;
    }


    public ArrayList<Course> getEnrolledCourse() throws SQLException {
        Database db = new Database();
        String query = "SELECT c.id, c.course_name, c.total_hour \n" +
                "FROM tbl_course c \n" +
                "LEFT JOIN tbl_student_course uc \n" +
                "ON c.id = uc.course_id \n" +
                "INNER JOIN tbl_user u \n" +
                "ON uc.user_id = u.id \n" +
                "WHERE u.username = ?;";
        ArrayList<Course> courseList = new ArrayList<>();
        System.out.println(getUsername());
        try (Connection conn = db.getConn()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, getUsername());
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String courseId = rs.getString(1);
                        String courseName = rs.getString(2);
                        int totalHours = rs.getInt(3);
                        Course course = new Course(courseId, courseName, totalHours);
                        courseList.add(course);
                    }
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return courseList;
    }


    public ArrayList<Course> getAvailableCourse() {
        Database db = new Database();
        String query = "SELECT c.*, uc.user_id\n" +
                "FROM tbl_course c\n" +
                "LEFT JOIN tbl_student_course uc\n" +
                "ON c.id = uc.course_id\n" +
                "WHERE (c.is_disabled = 0) AND (uc.user_id IS NULL OR NOT EXISTS \n" +
                "(SELECT uc.course_id\n" +
                "FROM tbl_student_course uc\n" +
                "INNER JOIN tbl_user u\n" +
                "ON uc.user_id = u.id\n" +
                "WHERE u.username = ?)\n" +
                ");";
        ArrayList<Course> courseList = new ArrayList<>();
        try (Connection conn = db.getConn()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, getUsername());
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String courseId = rs.getString(1);
                        String courseName = rs.getString(2);
                        int totalHours = rs.getInt(3);
                        Course course = new Course(courseId, courseName, totalHours);
                        courseList.add(course);
                    }
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return courseList;
    }


    public boolean enrollCourse(String courseId) {
        Database db = new Database();
        String query = "INSERT INTO tbl_student_course VALUES " +
                "((SELECT id FROM tbl_user WHERE username = ?), ?)";
        try (Connection conn = db.getConn()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, getUsername());
                stmt.setString(2, courseId);
                stmt.executeUpdate();
                return true;
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return false;
        }
    }


    public boolean quitCourse(String courseId) {
        Database db = new Database();
        String query = "DELETE FROM tbl_student_course " +
                "WHERE user_id = (SELECT id FROM tbl_user WHERE username = ?) AND course_id = ?";
        try (Connection conn = db.getConn()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, getUsername());
                stmt.setString(2, courseId);
                stmt.executeUpdate();
                return true;
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return false;
        }
    }

}
