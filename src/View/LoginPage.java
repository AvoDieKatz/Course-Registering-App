package View;

import Model.Student;

import javax.swing.*;
import java.awt.event.*;

public class LoginPage extends JFrame {
    private JPanel mainPanel;
    private JPanel cardPanel;
    private JPanel signUpPanel;
    private JPanel loginPanel;
    private JTextField su_fullNameTxt;
    private JTextField su_userNameTxt;
    private JPasswordField su_passwordTxt;
    private JPasswordField su_confPasswordTxt;
    private JCheckBox su_showPassCbx1;
    private JCheckBox su_showPassCbx2;
    private JButton signUpBtn;
    private JButton showLoginPanelBtn;
    private JTextField li_usernameTxt;
    private JPasswordField li_passwordTxt;
    private JCheckBox li_showPassCbx;
    private JButton loginBtn;
    private JButton showSignUpPanelBtn;
    private JLabel warningTxt;
    private JLabel errorTxt;

    public LoginPage() {

        super("Course Registering Application");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        li_passwordTxt.setEchoChar('\u2022');
        su_passwordTxt.setEchoChar('\u2022');
        su_confPasswordTxt.setEchoChar('\u2022');

        li_showPassCbx.addActionListener(e -> {
            if (li_showPassCbx.isSelected()) li_passwordTxt.setEchoChar('\u0000');
            else li_passwordTxt.setEchoChar('\u2022');
        });

        loginBtn.addActionListener(e -> {
            String inputUsername = li_usernameTxt.getText();
            String inputPassword = String.valueOf(li_passwordTxt.getPassword());
            int validationValue = -2;
            if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
                errorTxt.setText("Please enter your username and password");
            } else {
                validationValue = Student.login(inputUsername, inputPassword);
            }
            switch (validationValue) {
                case 1 -> {
                    dispose();
                    new AdminPage();
                }
                case 0 -> {
                    dispose();
                    new StudentPage(inputUsername);
                }
                case -1 -> {
                    JOptionPane.showMessageDialog(null, "Incorrect password, try again",
                                                "Alert", JOptionPane.ERROR_MESSAGE);
                    li_passwordTxt.setText("");
                }
            }
        });

        showSignUpPanelBtn.addActionListener(e -> showSignUpPanel());

        su_showPassCbx1.addActionListener(e -> {
            if (su_showPassCbx1.isSelected()) su_passwordTxt.setEchoChar('\u0000');
            else su_passwordTxt.setEchoChar('\u2022');
        });

        su_showPassCbx2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (su_showPassCbx2.isSelected()) su_confPasswordTxt.setEchoChar('\u0000');
                else su_confPasswordTxt.setEchoChar('\u2022');
            }
        });


        signUpBtn.addActionListener(e -> {
            String input_fullName = su_fullNameTxt.getText();
            String input_username = su_userNameTxt.getText();
            String input_password = String.valueOf(su_passwordTxt.getPassword());
            String input_confPassword = String.valueOf(su_confPasswordTxt.getPassword());
            boolean valid = true;
            if (!Regex.checkUsername(input_username)) {
                warningTxt.setText(warningTxt.getText() + "<html>Username should follow format: abc123<br>");
                valid = false;
            }
            if (Regex.checkPassword(input_password)) {
                if (!input_confPassword.equals(input_password)) {
                    warningTxt.setText(warningTxt.getText() + "<html>Password does not match<br>");
                    valid = false;
                }
            } else {
                warningTxt.setText(warningTxt.getText() + "<html>Password too weak<br>");
                valid = false;
            }
            if (valid) {
                if (Student.register(input_fullName, input_username, input_password)) {
                    JOptionPane.showMessageDialog(null, "Successfully registered, please log in");
                    showLoginPanel();
                } else {
                    JOptionPane.showMessageDialog(null, "Something went wrong on our side");
                }
            }
        });

        showLoginPanelBtn.addActionListener(e -> showLoginPanel());

        su_confPasswordTxt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                warningTxt.setText("");
            }
        });

        FocusListener listener = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) { }

            @Override
            public void focusLost(FocusEvent e) {
                errorTxt.setText("");
            }
        };
        loginBtn.addFocusListener(listener);
        showSignUpPanelBtn.addFocusListener(listener);

    }

    private void showLoginPanel() {
        cardPanel.removeAll();
        cardPanel.add(loginPanel);
        cardPanel.repaint();
        cardPanel.revalidate();
    }

    private void showSignUpPanel() {
        cardPanel.removeAll();
        cardPanel.add(signUpPanel);
        cardPanel.repaint();
        cardPanel.revalidate();
    }

}
