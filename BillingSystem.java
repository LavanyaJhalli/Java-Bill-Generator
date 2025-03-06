import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class BillingSystem {
    private JFrame frame;
    private JTextArea billArea;
    private JTextField customerNameField, itemNameField, itemPriceField, quantityField;
    private JButton addItemButton, generateBillButton;
    private double totalAmount = 0.0;

    // Database Connection Details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/billing_system";
    private static final String USER = "root";
    private static final String PASSWORD = "password";  // Update this with your MySQL password

    public BillingSystem() {
        frame = new JFrame("Automated Billing System");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        frame.add(new JLabel("Customer Name:"));
        customerNameField = new JTextField(20);
        frame.add(customerNameField);

        frame.add(new JLabel("Item Name:"));
        itemNameField = new JTextField(15);
        frame.add(itemNameField);

        frame.add(new JLabel("Price:"));
        itemPriceField = new JTextField(7);
        frame.add(itemPriceField);

        frame.add(new JLabel("Quantity:"));
        quantityField = new JTextField(5);
        frame.add(quantityField);

        addItemButton = new JButton("Add Item");
        generateBillButton = new JButton("Generate Bill");
        frame.add(addItemButton);
        frame.add(generateBillButton);

        billArea = new JTextArea(20, 40);
        billArea.setEditable(false);
        frame.add(new JScrollPane(billArea));

        addItemButton.addActionListener(e -> addItem());
        generateBillButton.addActionListener(e -> generateBill());

        frame.setVisible(true);
    }

    private void addItem() {
        String itemName = itemNameField.getText();
        String priceText = itemPriceField.getText();
        String quantityText = quantityField.getText();

        if (itemName.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Please enter all item details!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int quantity = Integer.parseInt(quantityText);
            double itemTotal = price * quantity;
            totalAmount += itemTotal;

            billArea.append(itemName + " | Price: " + price + " | Qty: " + quantity + " | Total: " + itemTotal + "\n");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Invalid price or quantity!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generateBill() {
        String customerName = customerNameField.getText();
        if (customerName.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Enter customer name!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        billArea.append("\nTotal Amount: " + totalAmount + "\n");
        saveTransaction(customerName, totalAmount);
        JOptionPane.showMessageDialog(frame, "Bill Generated Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void saveTransaction(String customerName, double totalAmount) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO transactions (customer_name, total_amount) VALUES (?, ?)")) {
            stmt.setString(1, customerName);
            stmt.setDouble(2, totalAmount);
            stmt.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(frame, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BillingSystem::new);
    }
}
