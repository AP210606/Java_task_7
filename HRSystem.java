import java.sql.*;
import java.util.Scanner;

public class HRSystem {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/company";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "AP210066"; 

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load JDBC driver
        } catch (Exception e) {
            System.out.println("⚠️ Driver load error: " + e.getMessage());
        }

        Scanner sc = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\n=== HR Portal ===");
            System.out.println("1. Add Employee");
            System.out.println("2. Show Employees");
            System.out.println("3. Modify Record");
            System.out.println("4. Delete Entry");
            System.out.println("5. Close");
            System.out.print("Enter your choice: ");

            String option = sc.nextLine();

            switch (option) {
                case "1" -> addEmployee(sc);
                case "2" -> displayEmployees();
                case "3" -> updateEmployee(sc);
                case "4" -> deleteEmployee(sc);
                case "5" -> {
                    isRunning = false;
                    System.out.println("Session terminated.");
                }
                default -> System.out.println("❌ Invalid input, try again.");
            }
        }

        sc.close();
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }

    private static void addEmployee(Scanner sc) {
        try (Connection con = getConnection()) {
            System.out.print("Full Name: ");
            String fullName = sc.nextLine();
            System.out.print("Dept: ");
            String dept = sc.nextLine();
            System.out.print("Pay: ");
            double pay = Double.parseDouble(sc.nextLine());

            String sql = "INSERT INTO employees (name, department, salary) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, fullName);
            ps.setString(2, dept);
            ps.setDouble(3, pay);

            int rows = ps.executeUpdate();
            System.out.println("✅ " + rows + " employee(s) added.");
        } catch (Exception e) {
            System.out.println("❌ Failed to add: " + e.getMessage());
        }
    }

    private static void displayEmployees() {
        try (Connection con = getConnection()) {
            String sql = "SELECT * FROM employees";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql);

            System.out.printf("%-5s %-20s %-15s %-10s%n", "ID", "Name", "Department", "Salary");
            while (rs.next()) {
                System.out.printf("%-5d %-20s %-15s ₹%.2f%n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getDouble("salary"));
            }
        } catch (Exception e) {
            System.out.println("❌ Failed to fetch: " + e.getMessage());
        }
    }

    private static void updateEmployee(Scanner sc) {
        try (Connection con = getConnection()) {
            System.out.print("Enter ID to update: ");
            int empId = Integer.parseInt(sc.nextLine());

            System.out.print("New Name: ");
            String name = sc.nextLine();
            System.out.print("New Dept: ");
            String dept = sc.nextLine();
            System.out.print("New Salary: ");
            double salary = Double.parseDouble(sc.nextLine());

            String sql = "UPDATE employees SET name=?, department=?, salary=? WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, dept);
            ps.setDouble(3, salary);
            ps.setInt(4, empId);

            int rows = ps.executeUpdate();
            System.out.println("✅ " + rows + " record(s) updated.");
        } catch (Exception e) {
            System.out.println("❌ Failed to update: " + e.getMessage());
        }
    }

    private static void deleteEmployee(Scanner sc) {
        try (Connection con = getConnection()) {
            System.out.print("Enter ID to delete: ");
            int empId = Integer.parseInt(sc.nextLine());

            String sql = "DELETE FROM employees WHERE id=?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, empId);

            int rows = ps.executeUpdate();
            System.out.println("✅ " + rows + " employee(s) deleted.");
        } catch (Exception e) {
            System.out.println("❌ Failed to delete: " + e.getMessage());
        }
    }
}
