package View;

import Model.Course;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminPage extends JFrame {
    private JPanel mainPanel;
    private JTable courseTbl;
    private JTextField idTxt;
    private JTextField courseNameTxt;
    private JTextField totalHoursTxt;
    private JTextField totalStudentsTxt;
    private JButton updateBtn;
    private JButton showAddPanelBtn;
    private JTextField searchTxt;
    private JLabel usernameLabel;
    private JButton signOutBtn;
    private JPanel cardPanel;
    private JPanel homePanel;
    private JPanel addPanel;
    private JTextField add_hoursTxt;
    private JTextField add_idTxt;
    private JTextField add_nameTxt;
    private JButton createNewCourseBtn;
    private JButton showAdminPanelBtn;
    private JButton deleteBtn;
    private JLabel hourWarningLbl;
    private JLabel nameWarningLbl;
    private JLabel newHourWarningLbl;
    private JLabel newNameWarningLbl;

    public AdminPage() {

        super("Course Registering Application - Admin");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        DefaultTableModel tableModel = (DefaultTableModel) courseTbl.getModel();
        String[] headers = {"Course ID", "Course Name", "Total Hours", "Total Students"};
        tableModel.setColumnIdentifiers(headers);
        Course courseModel = new Course();

        try {
            ArrayList<Course> allCourses = courseModel.getAllCourse();
            for (Course course : allCourses) {
                tableModel.addRow(new Object[]{
                        course.getCourseID(),
                        course.getCourseName(),
                        course.getTotalHours(),
                        course.getTotalStudents()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Can't get data");
            e.printStackTrace();
        }

        courseTbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                courseTbl.setRowSelectionAllowed(true);
                String tblCourseId = tableModel.getValueAt(courseTbl.getSelectedRow(), 0).toString();
                String tblCourseName = tableModel.getValueAt(courseTbl.getSelectedRow(), 1).toString();
                String tblCourseHours = tableModel.getValueAt(courseTbl.getSelectedRow(), 2).toString();
                String tblCourseStudents = tableModel.getValueAt(courseTbl.getSelectedRow(), 3).toString();
                idTxt.setText(tblCourseId);
                courseNameTxt.setText(tblCourseName);
                totalHoursTxt.setText(tblCourseHours);
                totalStudentsTxt.setText(tblCourseStudents);
                deleteBtn.setEnabled(true);

            }
        });

        showAddPanelBtn.addActionListener(e -> showAddCoursePanel());

        showAdminPanelBtn.addActionListener(e -> showAdminPanel());

        add_nameTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                String courseId = Course.createCourseID(add_nameTxt.getText());
                add_idTxt.setText(courseId);
            }
        });

        createNewCourseBtn.addActionListener(e -> {
            String courseId = add_idTxt.getText();
            String courseName = add_nameTxt.getText();
            String totalHours = add_hoursTxt.getText();
            boolean valid = true;
            if (!Regex.checkStringOnly(courseName)) {
                newNameWarningLbl.setText("<html>This is not a valid course name<br>");
                valid = false;
            }
            if (!Regex.checkNumberOnly(totalHours)) {
                newHourWarningLbl.setText("<html>Numbers only<br>");
                valid = false;
            }
            if (valid) {
                Course courseModel13 = new Course(courseId, courseName, Integer.parseInt(totalHours));
                if (courseModel13.addCourse()) {
                    showAdminPanel();
                    tableModel.addRow(new Object[]{
                            courseId, courseName, totalHours, 0
                    });
                    JOptionPane.showMessageDialog(null, "Course added successfully");
                } else
                    JOptionPane.showMessageDialog(null, "Failed to add course", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        FocusListener listener = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                newNameWarningLbl.setText("");
                newHourWarningLbl.setText("");
            }

            @Override
            public void focusLost(FocusEvent e) {

            }
        };
        add_nameTxt.addFocusListener(listener);
        add_hoursTxt.addFocusListener(listener);


        updateBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Updating a course", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                String courseId = idTxt.getText();
                String courseName = courseNameTxt.getText();
                String totalHours = totalHoursTxt.getText();
                boolean valid = true;
                System.out.println("In admin Page" + courseName);
                if (!Regex.checkStringOnly(courseName)) {
                    nameWarningLbl.setText("<html>This is not a valid course name<br>");
                    valid = false;
                }
                if (!Regex.checkNumberOnly(totalHours)) {
                    hourWarningLbl.setText("<html>Numbers only<br>");
                    valid = false;
                }

                if (valid) {
                    Course courseModel12 = new Course(courseId, courseName, Integer.parseInt(totalHours));
                    if (courseModel12.editCourse()) {
                        tableModel.setValueAt(courseNameTxt.getText(), courseTbl.getSelectedRow(), 1);
                        tableModel.setValueAt(totalHoursTxt.getText(), courseTbl.getSelectedRow(), 2);
                        JOptionPane.showMessageDialog(null, "Course updated successfully");
                    } else
                        JOptionPane.showMessageDialog(null, "Update failed", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(null, "Are you sure?", "Deleting a course", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                int totalStudent = (int) tableModel.getValueAt(courseTbl.getSelectedRow(), 3);
                if (totalStudent == 0) {
                    String courseId = tableModel.getValueAt(courseTbl.getSelectedRow(), 0).toString();
                    Course courseModel1 = new Course(courseId);
                    if (courseModel1.deleteCourse()) {
                        tableModel.removeRow(courseTbl.getSelectedRow());
                        JOptionPane.showMessageDialog(null, "Delete successful");
                    } else
                        JOptionPane.showMessageDialog(null, "Delete failed", "Error", JOptionPane.ERROR_MESSAGE);
                } else
                    JOptionPane.showMessageDialog(null, "Can't delete this course because there are students studying.",
                                                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        totalHoursTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateBtn.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateBtn.setEnabled(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateBtn.setEnabled(true);
            }
        });

        courseNameTxt.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateBtn.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateBtn.setEnabled(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateBtn.setEnabled(true);
            }
        });

        signOutBtn.addActionListener(e -> {
            AdminPage.this.dispose();
            new LoginPage();
        });

    }


    private void showAdminPanel() {
        cardPanel.removeAll();
        cardPanel.add(homePanel);
        cardPanel.repaint();
        cardPanel.revalidate();
    }

    private void showAddCoursePanel() {
        cardPanel.removeAll();
        cardPanel.add(addPanel);
        cardPanel.repaint();
        cardPanel.revalidate();
    }

}
