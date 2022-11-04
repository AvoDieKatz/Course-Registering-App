package View;

import Model.Course;
import Model.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentPage extends JFrame {
    private JPanel mainPanel;
    private JTable studentCourseTbl;
    private JTable availableCourseTbl;
    private JButton showEnrollCourseBtn;
    private JButton quitBtn;
    private JButton signOutBtn;
    private JLabel usernameLabel;
    private JLabel courseIdLabel;
    private JLabel courseNameLabel;
    private JLabel totalHourLabel;
    private JPanel cardPanel;
    private JPanel homePanel;
    private JPanel enrollPanel;
    private JButton enrollNowBtn;
    private JButton backBtn;
    private JLabel enrollCourseIdLbl;
    private JLabel enrollCourseNameLbl;
    private JLabel enrollHourLbl;
    private JLabel pageLabel;

    public StudentPage(String username) {
        super("Course Registering Application");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.usernameLabel.setText(username);


        String[] headers = {"Course ID", "Course Name", "Total Hours"};
        DefaultTableModel studentCourseTableModel = (DefaultTableModel) studentCourseTbl.getModel();
        DefaultTableModel availableCourseTableModel = (DefaultTableModel) availableCourseTbl.getModel();
        studentCourseTableModel.setColumnIdentifiers(headers);
        availableCourseTableModel.setColumnIdentifiers(headers);
        Student studentModel = new Student(usernameLabel.getText());

        try {
            ArrayList<Course> enrolledCourseList = studentModel.getEnrolledCourse();
            for (Course course : enrolledCourseList) {
                studentCourseTableModel.addRow(new Object[]{
                        course.getCourseID(),
                        course.getCourseName(),
                        course.getTotalHours()
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Can't get data");
            e.printStackTrace();
        }

        studentCourseTbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                studentCourseTbl.setRowSelectionAllowed(true);
                String tblCourseId = studentCourseTableModel.getValueAt(studentCourseTbl.getSelectedRow(), 0).toString();
                String tblCourseName = studentCourseTableModel.getValueAt(studentCourseTbl.getSelectedRow(), 1).toString();
                String tblCourseHours = studentCourseTableModel.getValueAt(studentCourseTbl.getSelectedRow(), 2).toString();
                courseIdLabel.setText("Course ID: " + tblCourseId);
                courseNameLabel.setText("Course Name: " + tblCourseName);
                totalHourLabel.setText("Total Hour: " + tblCourseHours);
                quitBtn.setEnabled(true);
            }
        });

        availableCourseTbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                availableCourseTbl.setRowSelectionAllowed(true);
                String tblCourseId = availableCourseTableModel.getValueAt(availableCourseTbl.getSelectedRow(), 0).toString();
                String tblCourseName = availableCourseTableModel.getValueAt(availableCourseTbl.getSelectedRow(), 1).toString();
                String tblCourseHours = availableCourseTableModel.getValueAt(availableCourseTbl.getSelectedRow(), 2).toString();
                enrollCourseIdLbl.setText("Course ID: " + tblCourseId);
                enrollCourseNameLbl.setText("Course Name: " + tblCourseName);
                enrollHourLbl.setText("Total Hour: " + tblCourseHours);
                enrollNowBtn.setVisible(true);
            }
        });

        backBtn.addActionListener(e -> {
            pageLabel.setText("HOME");
            showHomePanel();
        });

        showEnrollCourseBtn.addActionListener(e -> {
            pageLabel.setText("ENROLL A COURSE");

            enrollNowBtn.setVisible(false);

            showEnrollPanel();
            ArrayList<Course> list = studentModel.getAvailableCourse();
            for (Course course : list) {
                availableCourseTableModel.addRow(new Object[]{
                        course.getCourseID(),
                        course.getCourseName(),
                        course.getTotalHours()
                });
            }
        });

        enrollNowBtn.addActionListener(e -> {
                String courseId = enrollCourseIdLbl.getText().substring(11);
                String courseName = enrollCourseNameLbl.getText().substring(11);
                if (studentModel.enrollCourse(courseId)) {
                    JOptionPane.showMessageDialog(null, "You joined " + courseName);
                } else
                    JOptionPane.showMessageDialog(null, "Can't join right now", "Error", JOptionPane.ERROR_MESSAGE);

        });

        signOutBtn.addActionListener(e -> {
            StudentPage.this.dispose();
            new LoginPage();
        });

        quitBtn.addActionListener(e -> {
            String message = "Quitting " + courseNameLabel.getText().substring(11);
            int choice = JOptionPane.showConfirmDialog(null, message, "Quiting course", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                String courseId = courseIdLabel.getText().substring(11);
                if (studentModel.quitCourse(courseId)) {
                    studentCourseTableModel.removeRow(studentCourseTbl.getSelectedRow());
                    JOptionPane.showMessageDialog(null, "You quited");
                } else
                    JOptionPane.showMessageDialog(null, "Can't quit right now", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

    }


    private void showHomePanel() {
        cardPanel.removeAll();
        cardPanel.add(homePanel);
        cardPanel.repaint();
        cardPanel.revalidate();
    }


    private void showEnrollPanel() {
        cardPanel.removeAll();
        cardPanel.add(enrollPanel);
        cardPanel.repaint();
        cardPanel.revalidate();
    }

}
