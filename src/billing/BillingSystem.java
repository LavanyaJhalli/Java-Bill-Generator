package billing;
import java.sql.*;
import java.util.Scanner;

public class BillingSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String url = "jdbc:mysql://localhost:3306/billing_system";
        String user = "root";
        String password = "your_mysql_password";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            System.out.println("Connected to database!");
            while (true) {
                System.out.println("\n1. Add Customer");
                System.out.println("2. Add Product");
                System.out.println("3. Generate Invoice");
                System.out.println("4. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1:
                        addCustomer(conn, scanner);
                        break;
                    case 2:
                        addProduct(conn, scanner);
                        break;
                    case 3:
                        generateInvoice(conn, scanner);
                        break;
                    case 4:
                        System.out.println("Exiting...");
                        scanner.close();
                        return;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addCustomer(Connection conn, Scanner scanner) throws SQLException {
        scanner.nextLine(); // consume leftover newline
        System.out.print("Enter customer name: ");
        String name = scanner.nextLine();
        System.out.print("Enter customer email: ");
        String email = scanner.nextLine();
        System.out.print("Enter customer phone: ");
        String phone = scanner.nextLine();

        String sql = "INSERT INTO customers (name, email, phone) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.executeUpdate();
            System.out.println("Customer added successfully!");
        }
    }

    private static void addProduct(Connection conn, Scanner scanner) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();
        System.out.print("Enter product price: ");
        double price = scanner.nextDouble();

        String sql = "INSERT INTO products (name, price) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setDouble(2, price);
            pstmt.executeUpdate();
            System.out.println("Product added successfully!");
        }
    }

    private static void generateInvoice(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter customer ID: ");
        int customerId = scanner.nextInt();
        System.out.print("Enter product ID: ");
        int productId = scanner.nextInt();
        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();

        String priceQuery = "SELECT price FROM products WHERE id = ?";
        double price = 0;
        try (PreparedStatement pstmt = conn.prepareStatement(priceQuery)) {
            pstmt.setInt(1, productId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                price = rs.getDouble("price");
            }
        }

        double totalAmount = price * quantity;

        String invoiceSQL = "INSERT INTO invoices (customer_id, total_amount) VALUES (?, ?)";
        int invoiceId = -1;
        try (PreparedStatement pstmt = conn.prepareStatement(invoiceSQL, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, customerId);
            pstmt.setDouble(2, totalAmount);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                invoiceId = rs.getInt(1);
            }
        }

        if (invoiceId != -1) {
            String invoiceItemSQL = "INSERT INTO invoice_items (invoice_id, product_id, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(invoiceItemSQL)) {
                pstmt.setInt(1, invoiceId);
                pstmt.setInt(2, productId);
                pstmt.setInt(3, quantity);
                pstmt.executeUpdate();
                System.out.println("Invoice generated successfully! Invoice ID: " + invoiceId);
            }
        } else {
            System.out.println("Failed to generate invoice.");
        }
    }
}
