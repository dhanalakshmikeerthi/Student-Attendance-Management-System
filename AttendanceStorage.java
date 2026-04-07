import java.util.ArrayList;

public class AttendanceStorage {

    // Store all registered users
    public static ArrayList<User> users = new ArrayList<>();


    // Add new user
    public static void addUser(User user) {
        users.add(user);
    }


    // Login verification
    public static User login(String username, String password, String role) {

        for(User u : users){

            if(u.getUsername().equals(username)
            && u.getPassword().equals(password)
            && u.getRole().equals(role)){

                return u;
            }

        }

        return null;
    }


    // Get only students (used in faculty attendance)
    public static ArrayList<User> getStudents(){

        ArrayList<User> students = new ArrayList<>();

        for(User u : users){

            if(u.getRole().equals("Student")){
                students.add(u);
            }

        }

        return students;
    }

}