import javax.swing.*;
import java.sql.*;

public class StudentRegister extends JFrame {

    JTextField name, rollno,email;
    JComboBox  branch, section;
    JPasswordField pass;

    public StudentRegister(){

        setTitle("Student Register");
        setSize(420,420);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel l1=new JLabel("Name");
        l1.setBounds(40,40,120,25);
        add(l1);

        name=new JTextField();
        name.setBounds(160,40,180,25);
        add(name);

        JLabel l2=new JLabel("Roll Number");
        l2.setBounds(40,80,120,25);
        add(l2);

        rollno=new JTextField();
        rollno.setBounds(160,80,180,25);
        add(rollno);

        JLabel l3=new JLabel("Email");
        l3.setBounds(40,120,120,25);
        add(l3);

        email=new JTextField();
        email.setBounds(160,120,180,25);
        add(email);



        

        JLabel l4=new JLabel("Branch");
        l4.setBounds(40,160,120,25);
        add(l4);

        String branches[]={"CSE","ECE","AI&ML"};
        branch=new JComboBox(branches);
        branch.setBounds(160,160,180,25);
        add(branch);

        JLabel l5=new JLabel("Section");
        l5.setBounds(40,200,120,25);
        add(l5);

        String sec[]={"1","2","3","4","5","6","7","8","9"};
        section=new JComboBox(sec);
        section.setBounds(160,200,180,25);
        add(section);

        JLabel l6=new JLabel("Password");
        l6.setBounds(40,240,120,25);
        add(l6);

        pass=new JPasswordField();
        pass.setBounds(160,240,180,25);
        add(pass);

        JButton save=new JButton("Register");
        save.setBounds(130,300,130,30);
        add(save);

        save.addActionListener(e->registerStudent());

    }

    void registerStudent(){

        try{

            Connection con=DBConnection.getConnection();

            PreparedStatement ps=con.prepareStatement(
                    "INSERT INTO students(name,rollno,email,branch,section,password) VALUES(?,?,?,?,?,?)"
            );

            ps.setString(1,name.getText());
            ps.setString(2,rollno.getText());
            ps.setString(3,email.getText());
        

           
            ps.setString(4,branch.getSelectedItem().toString());
            ps.setString(5,section.getSelectedItem().toString());
            ps.setString(6,new String(pass.getPassword()));

            ps.executeUpdate();

            JOptionPane.showMessageDialog(this,"Student Registered");

            dispose();

        }catch(Exception ex){ex.printStackTrace();}
    }

}