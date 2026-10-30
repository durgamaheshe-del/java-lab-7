import java.sql.*;

public class DeleteRecord {
    public static void main(String[] args) {

        String url = "jdbc:mysql://localhost:3306/mydatabase";
        String username = "root";
        String password = "root";

        String query = "DELETE FROM users WHERE id = ?";

        try (Connection con =
                 DriverManager.getConnection(url, username, password);
             PreparedStatement ps = con.prepareStatement(query)) {

            ps.setInt(1, 2);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("Row deleted successfully.");
            } else {
                System.out.println("No rows were deleted.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
