package billing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BillingSystemGUI extends JFrame {
    private JTextField customerNameField, amountField;
    private JButton generateBillButton, clearButton;

    public BillingSystemGUI() {
        setTitle("Billing System");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout(10, 10));

        // 🟦 Panel for Inputs (Symmetrical Layout)
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(200, 230, 250)); // Light Blue Background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Padding

        // 📝 Labels & Fields
        JLabel customerNameLabel = new JLabel("Customer Name:");
        JLabel amountLabel = new JLabel("Amount ($):");

        customerNameField = new JTextField(15);
        amountField = new JTextField(15);

        customerNameField.setPreferredSize(new Dimension(200, 25));
        amountField.setPreferredSize(new Dimension(200, 25));

        // 🎨 Buttons
        generateBillButton = new JButton("Generate Bill");
        clearButton = new JButton("Clear");

        // Button Styling
        styleButton(generateBillButton, new Color(0, 150, 0), new Color(0, 180, 0));
        styleButton(clearButton, new Color(200, 0, 0), new Color(230, 0, 0));

        // 🔳 Adding Components with GridBagLayout
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(customerNameLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 0;
        panel.add(customerNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(amountLabel, gbc);
        
        gbc.gridx = 1; gbc.gridy = 1;
        panel.add(amountField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(generateBillButton);
        buttonPanel.add(clearButton);
        buttonPanel.setBackground(new Color(200, 230, 250));

        gbc.gridy = 2;
        panel.add(buttonPanel, gbc);

        add(panel, BorderLayout.CENTER);

        // 🎯 Button Actions
        generateBillButton.addActionListener(e -> 
            JOptionPane.showMessageDialog(null, "Bill Generated for " + customerNameField.getText() + " with amount $" + amountField.getText())
        );

        clearButton.addActionListener(e -> {
            customerNameField.setText("");
            amountField.setText("");
        });

        setVisible(true);
    }

    // 🎨 Button Styling Method
    private void styleButton(JButton button, Color normalColor, Color hoverColor) {
        button.setBackground(normalColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(normalColor);
            }
        });
    }

    public static void main(String[] args) {
        new BillingSystemGUI();
    }
}
