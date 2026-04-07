import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.*;

public class FacultyDashboard extends JFrame {

    int facultyId;

    JTable table;
    DefaultTableModel model;

    JComboBox<String> sectionBox;
    JComboBox<String> subjectBox;

    JTextField dateField;

    JLabel welcomeLabel;

    JPanel mainPanel;

    public FacultyDashboard(int id){

        facultyId=id;

        setTitle("Faculty Dashboard");
        setSize(1000,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        createSidebar();
        createMainPanel();
        loadFacultyName();
        loadSections();
        loadSubjects();

        setVisible(true);
    }

    // SIDEBAR

    private void createSidebar(){

        JPanel sidebar=new JPanel();
        sidebar.setBackground(new Color(30,40,80));
        sidebar.setPreferredSize(new Dimension(200,0));
        sidebar.setLayout(new GridLayout(8,1,10,10));

        JButton profileBtn=createBtn("Profile");
        JButton markBtn=createBtn("Mark Attendance");
        JButton updateBtn=createBtn("Update Attendance");
        JButton notifBtn=createBtn("Notifications");
        JButton lowBtn=createBtn("<75% Students");
        JButton logoutBtn=createBtn("Logout");

        sidebar.add(new JLabel());
        sidebar.add(profileBtn);
        sidebar.add(markBtn);
        sidebar.add(updateBtn);
        sidebar.add(notifBtn);
        sidebar.add(lowBtn);
        sidebar.add(logoutBtn);

        add(sidebar,BorderLayout.WEST);

        profileBtn.addActionListener(e->showProfile());
        markBtn.addActionListener(e->showMarkAttendance());
        updateBtn.addActionListener(e->showUpdateAttendance());
        notifBtn.addActionListener(e->showNotifications());
        lowBtn.addActionListener(e->showLowAttendance());
        logoutBtn.addActionListener(e->logout());
    }

    private JButton createBtn(String text){

        JButton b=new JButton(text);
        b.setBackground(new Color(0,123,255));
        b.setForeground(Color.WHITE);
        b.setFont(new Font("Segoe UI",Font.BOLD,14));
        b.setFocusPainted(false);

        return b;
    }

    // MAIN PANEL

    private void createMainPanel(){

        mainPanel=new JPanel(new BorderLayout());

        welcomeLabel=new JLabel("Welcome");
        welcomeLabel.setFont(new Font("Segoe UI",Font.BOLD,22));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10,20,10,10));

        mainPanel.add(welcomeLabel,BorderLayout.NORTH);

        model=new DefaultTableModel(
                new String[]{"SNo","Name","Rollno","Present"},0){

            public Class getColumnClass(int c){
                if(c==3) return Boolean.class;
                return String.class;
            }
        };

        table=new JTable(model);

        mainPanel.add(new JScrollPane(table),BorderLayout.CENTER);

        add(mainPanel,BorderLayout.CENTER);
    }

    // LOAD FACULTY NAME

    private void loadFacultyName(){

        try{

            Connection con=DBConnection.getConnection();

            PreparedStatement ps=
                    con.prepareStatement("select name from faculty where faculty_id=?");

            ps.setInt(1,facultyId);

            ResultSet rs=ps.executeQuery();

            if(rs.next())
                welcomeLabel.setText("Welcome "+rs.getString(1));

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // LOAD SECTIONS

    private void loadSections(){

    sectionBox = new JComboBox<>();

    for(int i=1;i<=10;i++){
        sectionBox.addItem(String.valueOf(i));
    }
}

    // LOAD SUBJECTS

    private void loadSubjects(){

    subjectBox = new JComboBox<>();

    subjectBox.addItem("JAVA");
    subjectBox.addItem("ADSA");
    subjectBox.addItem("AI");
    subjectBox.addItem("OS");
    subjectBox.addItem("P&S");
    subjectBox.addItem("AMSD");
}

    // PROFILE

    private void showProfile(){

        mainPanel.removeAll();

        JPanel p=new JPanel(new GridLayout(4,1));

        try{

            Connection con=DBConnection.getConnection();

            PreparedStatement ps=
                    con.prepareStatement("select name,email,department from faculty where faculty_id=?");

            ps.setInt(1,facultyId);

            ResultSet rs=ps.executeQuery();

            if(rs.next()){

                p.add(new JLabel("Name : "+rs.getString("name")));
                p.add(new JLabel("Email : "+rs.getString("email")));

                p.add(new JLabel("Department : "+rs.getString("department")));
                p.add(new JLabel("Faculty ID : "+facultyId));
                p.add(new JLabel("Role : Faculty"));
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        mainPanel.add(p,BorderLayout.CENTER);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // MARK ATTENDANCE

    private void showMarkAttendance(){

        mainPanel.removeAll();

        JPanel top=new JPanel();

        JButton loadBtn=createBtn("Load Students");
        JButton saveBtn=createBtn("Save Attendance");

        JCheckBox selectAll=new JCheckBox("Select All");

        top.add(new JLabel("Section"));
        top.add(sectionBox);

        top.add(new JLabel("Subject"));
        top.add(subjectBox);

        top.add(selectAll);
        top.add(loadBtn);
        top.add(saveBtn);

        mainPanel.add(top,BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(table),BorderLayout.CENTER);

        loadBtn.addActionListener(e->loadStudents());
        saveBtn.addActionListener(e->saveAttendance());

        selectAll.addActionListener(e->{

            boolean val=selectAll.isSelected();

            for(int i=0;i<table.getRowCount();i++)
                model.setValueAt(val,i,3);
        });

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // UPDATE ATTENDANCE PANEL

    private void showUpdateAttendance(){

        mainPanel.removeAll();

        JPanel top = new JPanel();

        dateField = new JTextField(10);

        JButton loadBtn = createBtn("Load");
        JButton updateBtn = createBtn("Update");

        top.add(new JLabel("Date"));
        top.add(dateField);

        top.add(new JLabel("Section"));
        top.add(sectionBox);

        top.add(new JLabel("Subject"));
        top.add(subjectBox);

        top.add(loadBtn);
        top.add(updateBtn);

        mainPanel.add(top,BorderLayout.NORTH);
        mainPanel.add(new JScrollPane(table),BorderLayout.CENTER);

        loadBtn.addActionListener(e -> loadAttendanceByDate());
        updateBtn.addActionListener(e -> updateAttendance());

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // LOAD STUDENTS

    private void loadStudents(){

        model.setRowCount(0);

        try{

            Connection con=DBConnection.getConnection();

            PreparedStatement ps=
                    con.prepareStatement("select name,rollno from students where section=? order by rollno");

            ps.setString(1,sectionBox.getSelectedItem().toString());

            ResultSet rs=ps.executeQuery();

            int i=1;

            while(rs.next()){

                model.addRow(new Object[]{
                        i++,
                        rs.getString("name"),
                        rs.getString("rollno"),
                        true
                });
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // LOAD ATTENDANCE BY DATE

    private void loadAttendanceByDate(){

        model.setRowCount(0);

        try{

            Connection con = DBConnection.getConnection();

            String date = dateField.getText();

            int subjectId = getSubjectId(subjectBox.getSelectedItem().toString());

            String section = sectionBox.getSelectedItem().toString();

            PreparedStatement ps = con.prepareStatement(

            "SELECT s.name,s.rollNo,a.attended " +
            "FROM students s " +
            "LEFT JOIN attendance a ON s.student_id=a.student_id AND a.subject_id=? AND a.date=? " +
            "WHERE s.section=? ORDER BY s.rollNo");

            ps.setInt(1,subjectId);
            ps.setString(2,date);
            ps.setString(3,section);

            ResultSet rs = ps.executeQuery();

            int i=1;

            while(rs.next()){

                boolean present = rs.getInt("attended")==1;

                model.addRow(new Object[]{
                        i++,
                        rs.getString("name"),
                        rs.getString("rollno"),
                        present
                });
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // SAVE ATTENDANCE

    private void saveAttendance(){

        try{

            Connection con=DBConnection.getConnection();

            int subjectId=getSubjectId(subjectBox.getSelectedItem().toString());

            String section=sectionBox.getSelectedItem().toString();

            String date=java.time.LocalDate.now().toString();

            for(int i=0;i<table.getRowCount();i++){

                String roll=(String)table.getValueAt(i,2);

                int studentId=getStudentId(roll);

                if(studentId==0) continue;

                boolean present=(boolean)table.getValueAt(i,3);

                int attended=present?1:0;

                PreparedStatement check=
                        con.prepareStatement("select * from attendance where student_id=? and subject_id=? and date=?");

                check.setInt(1,studentId);
                check.setInt(2,subjectId);
                check.setString(3,date);

                ResultSet rs=check.executeQuery();

                if(rs.next()){

                    PreparedStatement update=
                            con.prepareStatement("update attendance set attended=? where student_id=? and subject_id=? and date=?");

                    update.setInt(1,attended);
                    update.setInt(2,studentId);
                    update.setInt(3,subjectId);
                    update.setString(4,date);

                    update.executeUpdate();

                }else{

                    PreparedStatement insert=
                            con.prepareStatement("insert into attendance(student_id,subject_id,section,attended,date) values(?,?,?,?,?)");

                    insert.setInt(1,studentId);
                    insert.setInt(2,subjectId);
                    insert.setString(3,section);
                    insert.setInt(4,attended);
                    insert.setString(5,date);

                    insert.executeUpdate();
                }
            }

            JOptionPane.showMessageDialog(this,"Attendance Saved");

            showNotifications();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // UPDATE ATTENDANCE

    private void updateAttendance(){

        try{

            Connection con = DBConnection.getConnection();

            String date = dateField.getText();

            int subjectId = getSubjectId(subjectBox.getSelectedItem().toString());

            for(int i=0;i<table.getRowCount();i++){

                String rollno=(String)table.getValueAt(i,2);

                int studentId=getStudentId(rollno);

                boolean present=(boolean)table.getValueAt(i,3);

                int attended=present?1:0;

                PreparedStatement ps=con.prepareStatement(

                "UPDATE attendance SET attended=? WHERE student_id=? AND subject_id=? AND date=?");

                ps.setInt(1,attended);
                ps.setInt(2,studentId);
                ps.setInt(3,subjectId);
                ps.setString(4,date);

                ps.executeUpdate();
            }

            JOptionPane.showMessageDialog(this,"Attendance Updated");

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // NOTIFICATIONS

    private void showNotifications(){

        JTextArea area=new JTextArea();

        String today=java.time.LocalDate.now().toString();

        area.append("Date : "+today+"\n\n");

        try{

            Connection con=DBConnection.getConnection();

            PreparedStatement ps=
                    con.prepareStatement(
                            "select s.name,s.rollno,s.section,sub.subject_name "+
                                    "from students s join attendance a on s.student_id=a.student_id "+
                                    "join subjects sub on a.subject_id=sub.subject_id "+
                                    "where a.attended=0 and a.date=? order by s.section,s.rollno");

            ps.setString(1,today);

            ResultSet rs=ps.executeQuery();

            while(rs.next()){

                area.append(rs.getString("name")+"("+rs.getString("rollno")+
                        ") is absent for "+rs.getString("subject_name")+"\n");
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(this,new JScrollPane(area));
    }

    // <75%

    private void showLowAttendance(){

        JTextArea area=new JTextArea();

        try{

            Connection con=DBConnection.getConnection();

            String q=
                    "select s.name,s.rollno,s.section, "+
                            "round((sum(a.attended)/count(a.attended))*100,2) as percent "+
                            "from students s join attendance a on s.student_id=a.student_id "+
                            "group by s.student_id having percent<75 order by s.section,s.rollNo";

            Statement st=con.createStatement();

            ResultSet rs=st.executeQuery(q);

            while(rs.next()){

                area.append(rs.getString("name")+"("+rs.getString("rollno")+
                        ") - "+rs.getDouble("percent")+"%\n");
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(this,new JScrollPane(area));
    }

    // HELPERS

    private int getStudentId(String roll){

        try{

            Connection con=DBConnection.getConnection();

            PreparedStatement ps=
                    con.prepareStatement("select student_id from students where rollno=?");

            ps.setString(1,roll);

            ResultSet rs=ps.executeQuery();

            if(rs.next())
                return rs.getInt(1);

        }catch(Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    private int getSubjectId(String subject){

        try{

            Connection con=DBConnection.getConnection();

            PreparedStatement ps=
                    con.prepareStatement("select subject_id from subjects where subject_name=?");

            ps.setString(1,subject);

            ResultSet rs=ps.executeQuery();

            if(rs.next())
                return rs.getInt(1);

        }catch(Exception e){
            e.printStackTrace();
        }

        return 0;
    }

    // LOGOUT

    private void logout(){

        dispose();

        new LoginRegisterFrame();
    }
}