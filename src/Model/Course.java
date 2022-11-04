package Model;

import java.sql.*;
import java.util.ArrayList;

public class Course {

    private String courseID;
    private String courseName;
    private int totalHours;
    private int totalStudents;

    public Course() {

    }

    public Course(String courseID) {
        this.courseID = courseID;
    }

    public Course(String courseID, String courseName, int totalHours) {
        this.courseID = courseID;
        this.courseName = courseName;
        this.totalHours = totalHours;
    }

    public Course(String courseId, String courseName, int totalHours, int totalStudents) {
        this.courseID = courseId;
        this.courseName = courseName;
        this.totalHours = totalHours;
        this.totalStudents = totalStudents;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getTotalHours() {
        return totalHours;
    }

    public void setTotalHours(int totalHours) {
        this.totalHours = totalHours;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }


    public static String createCourseID(String courseName) {
        String idChar = courseName.substring(0, 4).toUpperCase();
        int idNum = 101 + getNumberOfSameNameCourse(courseName);
        return idChar + idNum;
    }

    private static int getNumberOfSameNameCourse(String checkCourseName) {
        int numCourse = 0;
        Database db = new Database();
        String query = "SELECT COUNT(c.id) FROM tbl_course c WHERE c.course_name = ?;";
        try (Connection conn = db.getConn()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, checkCourseName);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        numCourse = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return numCourse;
    }


    public ArrayList<Course> getAllCourse() throws SQLException {
        ArrayList<Course> list = new ArrayList<>();
        Database db = new Database();
        String query = "SELECT c.id, c.course_name, c.total_hour, COUNT(uc.user_id)\n" +
                "FROM tbl_course c \n" +
                "LEFT JOIN tbl_student_course uc \n" +
                "ON c.id = uc.course_id \n" +
                "WHERE c.is_disabled = 0 \n" +
                "GROUP BY c.id;";
        try (Connection conn = db.getConn()) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(query)) {
                    while (rs.next()) {
                        Course course = new Course();
                        course.setCourseID(rs.getString(1));
                        course.setCourseName(rs.getString(2));
                        course.setTotalHours(rs.getInt(3));
                        course.setTotalStudents(rs.getInt(4));
                        list.add(course);
                    }
                }
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        return list;
    }


    public boolean addCourse() {
        Database db = new Database();
        String query = "INSERT INTO tbl_course (id, course_name, total_hour) VALUES (?, ?, ?)";
        try (Connection conn = db.getConn()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, getCourseID());
                stmt.setString(2, getCourseName());
                stmt.setInt(3, getTotalHours());
                stmt.executeUpdate();
                return true;
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return false;
        }
    }

    public boolean editCourse() {
        Database db = new Database();
        String query = "UPDATE tbl_course SET course_name = ?, total_hour = ? WHERE id = ?";
        try (Connection conn = db.getConn()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, getCourseName());
                stmt.setInt(2, getTotalHours());
                stmt.setString(3, getCourseID());
                stmt.executeUpdate();
                return true;
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return false;
        }
    }

    public boolean deleteCourse() {
        Database db = new Database();
        String query = "UPDATE tbl_course SET is_disabled = 1 WHERE id = ?";
        try (Connection conn = db.getConn()) {
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, getCourseID());
                stmt.executeUpdate();
                return true;
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
            return false;
        }
    }


}
