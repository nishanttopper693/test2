//package assi8.java;

import java.util.*;
import java.sql.*;

public class StudentDB {

    // Database connection details
    static final String URL = "jdbc:mysql://10.10.8.119:3306/se21144_db";
    static final String USER = "se21144";
    static final String PASS = "se21144";

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        int choice;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {

            System.out.println("Connected to MySQL Database!");

            do {
                System.out.println("\n===== MENU =====");
                System.out.println("1. Add Student");
                System.out.println("2. Delete Student");
                System.out.println("3. Edit Student");
                System.out.println("4. Display Students");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = sc.nextInt();

                switch (choice) {

                    case 1:
                        addStudent(conn, sc);
                        break;

                    case 2:
                        deleteStudent(conn, sc);
                        break;

                    case 3:
                        editStudent(conn, sc);
                        break;

                    case 4:
                        displayStudents(conn);
                        break;

                    case 5:
                        System.out.println("Exiting...");
                        break;

                    default:
                        System.out.println("Invalid Choice!");
                }

            } while (choice != 5);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ---------------- ADD STUDENT ----------------
    public static void addStudent(Connection conn, Scanner sc) throws SQLException {

        sc.nextLine();
        
        System.out.print("Enter Name: ");
        String name = sc.nextLine();

        System.out.print("Enter Age: ");
        int age = sc.nextInt();

        System.out.print("Enter Marks: ");
        float marks = sc.nextFloat();

        String sql = "INSERT INTO student_data(name, age, marks) VALUES (?, ?, ?)";

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, name);
        ps.setInt(2, age);
        ps.setFloat(3, marks);

        int rows = ps.executeUpdate();

        System.out.println(rows + " Student Added Successfully.");
    }

    // ---------------- DELETE STUDENT ----------------
    public static void deleteStudent(Connection conn, Scanner sc) throws SQLException {

        System.out.print("Enter Student ID to Delete: ");
        int id = sc.nextInt();

        String check = "SELECT * FROM student_data WHERE id = ?";
        PreparedStatement ps1 = conn.prepareStatement(check);
        ps1.setInt(1, id);

        ResultSet rs = ps1.executeQuery();

        if (rs.next()) {

            String sql = "DELETE FROM student_data WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            int rows = ps.executeUpdate();
            System.out.println(rows + " Student Deleted Successfully.");

        } else {
            System.out.println("ID Not Found.");
        }
    }

    // ---------------- EDIT STUDENT ----------------
    public static void editStudent(Connection conn, Scanner sc) throws SQLException {

        System.out.print("Enter Student ID to Update: ");
        int id = sc.nextInt();

        System.out.println("What do you want to update?");
        System.out.println("1. Name");
        System.out.println("2. Age");
        System.out.println("3. Marks");
        System.out.print("Enter Choice: ");
        int ch = sc.nextInt();

        String sql = "";
        PreparedStatement ps;

        switch (ch) {

            case 1:
                sc.nextLine();
                System.out.print("Enter New Name: ");
                String name = sc.nextLine();

                sql = "UPDATE student_data SET name=? WHERE id=?";
                ps = conn.prepareStatement(sql);
                ps.setString(1, name);
                ps.setInt(2, id);
                break;

            case 2:
                System.out.print("Enter New Age: ");
                int age = sc.nextInt();

                sql = "UPDATE student_data SET age=? WHERE id=?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, age);
                ps.setInt(2, id);
                break;

            case 3:
                System.out.print("Enter New Marks: ");
                float marks = sc.nextFloat();

                sql = "UPDATE student_data SET marks=? WHERE id=?";
                ps = conn.prepareStatement(sql);
                ps.setFloat(1, marks);
                ps.setInt(2, id);
                break;

            default:
                System.out.println("Invalid Choice");
                return;
        }

        int rows = ps.executeUpdate();
        System.out.println(rows + " Student Updated Successfully.");
    }

    // ---------------- DISPLAY STUDENTS ----------------
    public static void displayStudents(Connection conn) throws SQLException {

        String sql = "SELECT * FROM student_data";

        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        System.out.println("\n----- STUDENT RECORDS -----");

        while (rs.next()) {

            System.out.println(
                "ID: " + rs.getInt(1) +
                " | Name: " + rs.getString(2) +
                " | Age: " + rs.getInt(3) +
                " | Marks: " + rs.getFloat(4));
        }
    }
}
