import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StudentDashboard extends JFrame {

    int studentId;
    String username;

    JPanel mainPanel;
    JTable attendanceTable;
    DefaultTableModel model;

    JLabel welcomeLabel;

    public StudentDashboard(int id) {

        studentId = id;

        setTitle("Student Dashboard");
        setSize(950,550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        createSidebar();
        createMainPanel();

        loadStudentName();
        loadAttendance();

        setVisible(true);
    }

    // ---------- SIDEBAR ----------
    private void createSidebar(){

        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(30,30,60));
        sidebar.setPreferredSize(new Dimension(180,0));
        sidebar.setLayout(new GridLayout(6,1,10,10));

        JButton dashBtn = createButton("Dashboard");
        JButton profileBtn = createButton("Profile");
        JButton notifBtn = createButton("🔔 Notifications");
        JButton logoutBtn = createButton("Logout");

        sidebar.add(new JLabel());
        sidebar.add(dashBtn);
        sidebar.add(profileBtn);
        sidebar.add(notifBtn);
        sidebar.add(logoutBtn);

        add(sidebar,BorderLayout.WEST);

        dashBtn.addActionListener(e->showDashboard());
        profileBtn.addActionListener(e->showProfile());
        notifBtn.addActionListener(e->showNotifications());
        logoutBtn.addActionListener(e->logout());
    }

    // ---------- BUTTON STYLE ----------
    private JButton createButton(String text){

        JButton btn = new JButton(text);

        btn.setFont(new Font("Segoe UI",Font.BOLD,14));
        btn.setBackground(new Color(0,120,215));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);

        return btn;
    }

    // ---------- MAIN PANEL ----------
    private void createMainPanel(){

        mainPanel = new JPanel(new BorderLayout());

        welcomeLabel = new JLabel("Welcome");
        welcomeLabel.setFont(new Font("Segoe UI",Font.BOLD,22));
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(10,20,10,10));

        mainPanel.add(welcomeLabel,BorderLayout.NORTH);

        String cols[]={"Subject","Total Classes","Attended","Percentage"};

        model = new DefaultTableModel(cols,0);
        attendanceTable = new JTable(model);

        JScrollPane scroll = new JScrollPane(attendanceTable);

        mainPanel.add(scroll,BorderLayout.CENTER);

        add(mainPanel,BorderLayout.CENTER);
    }

    // ---------- LOAD NAME ----------
    private void loadStudentName(){

        try{

            Connection con = DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(
                            "SELECT name FROM students WHERE student_id=?"
                    );

            ps.setInt(1,studentId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                username = rs.getString("name");
                welcomeLabel.setText("Welcome "+username);

            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    // ---------- LOAD ATTENDANCE ----------
    private void loadAttendance(){

        model.setRowCount(0);

        try{

            Connection con = DBConnection.getConnection();

            String query =

                    "SELECT s.subject_name, " +
                    "COUNT(a.attendance_id) AS total_classes, " +
                    "SUM(a.attended) AS attended_classes " +
                    "FROM subjects s " +
                    "LEFT JOIN attendance a " +
                    "ON s.subject_id = a.subject_id AND a.student_id=? " +
                    "GROUP BY s.subject_id";

            PreparedStatement ps = con.prepareStatement(query);

            ps.setInt(1,studentId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                String subject = rs.getString("subject_name");

                int total = rs.getInt("total_classes");

                int attended = rs.getInt("attended_classes");

                double percent = 0;

                if(total>0)
                    percent = (attended*100.0)/total;

                model.addRow(new Object[]{
                        subject,
                        total,
                        attended,
                        String.format("%.2f %%",percent)
                });

            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    // ---------- DASHBOARD ----------
    private void showDashboard(){

        mainPanel.removeAll();

        mainPanel.setLayout(new BorderLayout());

        mainPanel.add(welcomeLabel,BorderLayout.NORTH);

        mainPanel.add(new JScrollPane(attendanceTable),BorderLayout.CENTER);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // ---------- PROFILE ----------
    private void showProfile(){

        JPanel profile = new JPanel(new GridLayout(6,1,10,10));
        profile.setBorder(BorderFactory.createEmptyBorder(40,40,40,40));

        try{

            Connection con = DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(
                            "SELECT * FROM students WHERE student_id=?"
                    );

            ps.setInt(1,studentId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){

                profile.add(new JLabel("Name : "+rs.getString("name")));
                profile.add(new JLabel("Rollno : "+rs.getString("rollno")));
                profile.add(new JLabel("Email : "+rs.getString("email")));

                
                profile.add(new JLabel("Branch : "+rs.getString("branch")));
                profile.add(new JLabel("Section : "+rs.getString("section")));

            }

        }catch(Exception e){
            e.printStackTrace();
        }

        mainPanel.removeAll();
        mainPanel.add(profile);

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // ---------- NOTIFICATIONS ----------
    private void showNotifications(){

        JTextArea area = new JTextArea();
        area.setFont(new Font("Segoe UI",Font.PLAIN,14));

        try{

            Connection con = DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(
                            "SELECT date FROM attendance WHERE student_id=? AND attended=0"
                    );

            ps.setInt(1,studentId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){

                area.append("Absent on "+rs.getDate("date")+"\n");

            }

        }catch(Exception e){
            e.printStackTrace();
        }

        mainPanel.removeAll();
        mainPanel.add(new JScrollPane(area));

        mainPanel.revalidate();
        mainPanel.repaint();
    }

    // ---------- LOGOUT ----------
    private void logout(){

        dispose();
        new LoginRegisterFrame();

    }

}