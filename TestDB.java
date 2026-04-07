import java.sql.Connection;

public class TestDB {
    public static void main(String[] args) {
        Connection con = DBConnection.getConnection();

        if (con != null) {
            System.out.println("SUCCESS: Connection is done");
        } else {
            System.out.println("FAILED: Connection is null");
        }
    }
}
