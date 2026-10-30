import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCExample {

    public static void main(String[] args) {

        // Database credentials
        String url = getEnvironmentValue(
            "MYSQL_URL",
            "jdbc:mysql://localhost:3306/mydatabase?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
        );

        String username = getEnvironmentValue("MYSQL_USERNAME", "root");
        String password = getEnvironmentValue("MYSQL_PASSWORD", "");

        String query = "SELECT id, name FROM students";

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            System.out.println("Connecting to database: " + url);

            try (Connection connection =
                    DriverManager.getConnection(url, username, password);
                 Statement statement = connection.createStatement()) {

                System.out.println("Database connected successfully!\n");

                statement.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS students (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(100) NOT NULL" +
                    ")"
                );

                try (ResultSet countRs =
                        statement.executeQuery("SELECT COUNT(*) AS total FROM students")) {

                    if (countRs.next() && countRs.getInt("total") == 0) {

                        System.out.println("Table was empty. Populating demo student records...");

                        statement.executeUpdate(
                            "INSERT INTO students (name) VALUES " +
                            "('Alice'), ('Bob'), ('Charlie'), ('Diana'), ('Ethan')"
                        );
                    }
                }

                System.out.println("=========================================");
                System.out.println(" STUDENT RECORDS ");
                System.out.println("=========================================");

                System.out.printf("%-8s | %-25s%n", "ID", "Name");
                System.out.println("-----------------------------------------");

                try (ResultSet resultSet = statement.executeQuery(query)) {

                    boolean hasRecords = false;

                    while (resultSet.next()) {

                        hasRecords = true;

                        int id = resultSet.getInt("id");
                        String name = resultSet.getString("name");

                        System.out.printf("%-8d | %-25s%n", id, name);
                    }

                    if (!hasRecords) {
                        System.out.println("No records found in 'students' table.");
                    }
                }

                System.out.println("=========================================\n");

            }

        } catch (ClassNotFoundException e) {

            System.err.println("Error: MySQL JDBC Driver not found!");
            System.err.println("Make sure 'mysql-connector-j.jar' is added to your classpath.");

        } catch (SQLException e) {

            System.err.println("Database error occurred: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());

            if (e.getErrorCode() == 1045) {

                System.err.println("\nHint: Authentication failed. Please verify your MySQL username and password.");
                System.err.println("You can set them via environment variables: MYSQL_USERNAME and MYSQL_PASSWORD.");

            } else if ("08S01".equals(e.getSQLState()) ||
                    (e.getMessage() != null &&
                    e.getMessage().contains("Communications link failure"))) {

                System.err.println("\nHint: MySQL server is not running or connection was lost.");
                System.err.println("Start MySQL server using start_mysql.ps1.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getEnvironmentValue(String name, String defaultValue) {

        String value = System.getenv(name);

        return value == null ? defaultValue : value;
    }
}
