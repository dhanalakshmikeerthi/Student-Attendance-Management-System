import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginRegisterFrame extends JFrame {

    JTextField emailField;
    JPasswordField passwordField;
    JRadioButton studentRB, facultyRB;

    public LoginRegisterFrame() {

        setTitle("Attendance Management System");
        setSize(450,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Background panel
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(70,130,180));
        panel.setBounds(0,0,450,400);
        add(panel);

        JLabel title = new JLabel("Attendance System");
        title.setFont(new Font("Arial",Font.BOLD,24));
        title.setForeground(Color.WHITE);
        title.setBounds(110,30,250,30);
        panel.add(title);

        JLabel userLabel = new JLabel("Email");
        userLabel.setBounds(60,110,100,25);
        userLabel.setForeground(Color.WHITE);
        panel.add(userLabel);

        emailField = new JTextField();
        emailField.setBounds(170,110,180,25);
        panel.add(emailField);

        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(60,160,100,25);
        passLabel.setForeground(Color.WHITE);
        panel.add(passLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(170,160,180,25);
        panel.add(passwordField);

        

        // Radio buttons
        studentRB = new JRadioButton("Student");
        facultyRB = new JRadioButton("Faculty");

        studentRB.setBounds(120,210,100,25);
        facultyRB.setBounds(230,210,100,25);

        studentRB.setSelected(true);

        studentRB.setBackground(new Color(70,130,180));
        facultyRB.setBackground(new Color(70,130,180));

        studentRB.setForeground(Color.WHITE);
        facultyRB.setForeground(Color.WHITE);

        ButtonGroup bg = new ButtonGroup();
        bg.add(studentRB);
        bg.add(facultyRB);

        panel.add(studentRB);
        panel.add(facultyRB);

        JButton loginBtn = new JButton("Login");
        JButton registerBtn = new JButton("Register");

        loginBtn.setBounds(90,270,120,35);
        registerBtn.setBounds(230,270,120,35);

        loginBtn.setBackground(new Color(34,139,34));
        registerBtn.setBackground(new Color(255,140,0));

        loginBtn.setForeground(Color.WHITE);
        registerBtn.setForeground(Color.WHITE);

        loginBtn.setFocusPainted(false);
        registerBtn.setFocusPainted(false);

        panel.add(loginBtn);
        panel.add(registerBtn);

        // LOGIN ACTION
        loginBtn.addActionListener(e -> loginAction());

        // REGISTER ACTION
        registerBtn.addActionListener(e -> {

            if(studentRB.isSelected()){
                new StudentRegister().setVisible(true);
            }
            else{
                new FacultyRegister().setVisible(true);
            }

        });

        setVisible(true);
    }

    void loginAction(){

        String username = emailField.getText();
        String password = new String(passwordField.getPassword());

        if(username.isEmpty() || password.isEmpty()){
            JOptionPane.showMessageDialog(this,"Enter username and password");
            return;
        }

        try{

            Connection con = DBConnection.getConnection();

            if(studentRB.isSelected()){

                PreparedStatement ps = con.prepareStatement(
                        "SELECT student_id FROM students WHERE rollno=? AND password=?"
                );

                ps.setString(1,username);
                ps.setString(2,password);

                ResultSet rs = ps.executeQuery();

                if(rs.next()){

                    int id = rs.getInt("student_id");

                    JOptionPane.showMessageDialog(this,"Student Login Successful");

                    dispose();

                    new StudentDashboard(id);

                }
                else{
                    JOptionPane.showMessageDialog(this,"Invalid Student Credentials");
                }

            }

            else{

                PreparedStatement ps = con.prepareStatement(
                        "SELECT faculty_id FROM faculty WHERE email=? AND password=?"
                );

                ps.setString(1,username);
                ps.setString(2,password);

                ResultSet rs = ps.executeQuery();

                if(rs.next()){

                    int id = rs.getInt("faculty_id");

                    JOptionPane.showMessageDialog(this,"Faculty Login Successful");

                    dispose();

                    new FacultyDashboard(id);

                }
                else{
                    JOptionPane.showMessageDialog(this,"Invalid Faculty Credentials");
                }

            }

        }
        catch(Exception ex){
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) {

        new LoginRegisterFrame();

    }

}