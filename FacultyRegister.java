import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class FacultyRegister extends JFrame {

    JTextField name, email, dept;
    JPasswordField pass;

    public FacultyRegister(){

        setTitle("Faculty Registration");
        setSize(400,350);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel l1 = new JLabel("Name");
        l1.setBounds(40,40,100,25);
        add(l1);

        name = new JTextField();
        name.setBounds(150,40,180,25);
        add(name);

        JLabel l2 = new JLabel("email");
        l2.setBounds(40,80,100,25);
        add(l2);

        email = new JTextField();
        email.setBounds(150,80,180,25);
        add(email);

        JLabel l3 = new JLabel("Department");
        l3.setBounds(40,120,100,25);
        add(l3);

        dept = new JTextField();
        dept.setBounds(150,120,180,25);
        add(dept);

        JLabel l4 = new JLabel("Password");
        l4.setBounds(40,160,100,25);
        add(l4);

        pass = new JPasswordField();
        pass.setBounds(150,160,180,25);
        add(pass);

        JButton save = new JButton("Register");
        save.setBounds(120,220,120,30);
        add(save);

        save.addActionListener(e -> registerFaculty());
    }

    void registerFaculty(){

        try{

            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO faculty(name,email,department,password) VALUES(?,?,?,?)"
            );

            ps.setString(1,name.getText());
            ps.setString(2,email.getText());
            ps.setString(3,dept.getText());
            ps.setString(4,new String(pass.getPassword()));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,"Faculty Registered");

            dispose();

        }catch(Exception ex){ex.printStackTrace();}

    }
}