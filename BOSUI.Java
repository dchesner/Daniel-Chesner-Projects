import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BankingUI {
    public static void main(String[] args) {
        // Create main frame
        JFrame frame = new JFrame(); // This creates a frame
        frame.setTitle("Dans Banking System"); // Frame title
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // exits on closing the x
        frame.setResizable(true); // Prevents user from changing dimensions on close
        frame.size(400, 200); // sets x-dimension, y dimension
        frame.setVisible(true); // make frame visible

        // Set app icon
        ImageIcon image = new ImageIcon("image.png"); // Ensure "image.png" exists
        frame.setIconImage(image.getImage());

        // Header panel with title
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(30, 144, 255)); // Dodger blue color
        JLabel titleLabel = new JLabel("Dans Banking System", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerPanel.add(titleLabel);

        // Create input fields
        JLabel nameLabel = new JLabel("Account Holder:");
        JLabel idLabel = new JLabel("Account ID:");
        JLabel amountLabel = new JLabel("Amount:");

        JTextField nameField = new JTextField();
        JTextField idField = new JTextField();
        JTextField amountField = new JTextField();

        // Styling labels
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        idLabel.setFont(new Font("Arial", Font.BOLD, 14));
        amountLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // Styling text fields
        nameField.setPreferredSize(new Dimension(150, 25));
        idField.setPreferredSize(new Dimension(150, 25));
        amountField.setPreferredSize(new Dimension(150, 25));

        // Panel for input fields
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(idLabel);
        inputPanel.add(idField);
        inputPanel.add(amountLabel);
        inputPanel.add(amountField);

        // Create buttons with improved design
        JButton createAccountButton = createStyledButton("Create Account");
        JButton depositButton = createStyledButton("Deposit Money");
        JButton withdrawButton = createStyledButton("Withdraw Money");
        JButton checkBalanceButton = createStyledButton("Check Balance");
        JButton exitButton = createStyledButton("Exit");

        // Status label for real-time updates
        JLabel statusLabel = new JLabel("Welcome to Dans Banking System", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(new Color(34, 139, 34)); // Forest green color

        // Button functionalities
        createAccountButton.addActionListener(e -> {
            String name = nameField.getText();
            statusLabel.setText("Account Created for: " + name);
            JOptionPane.showMessageDialog(frame, "Account created successfully for " + name);
        });

        depositButton.addActionListener(e -> {
            String accountId = idField.getText();
            String amount = amountField.getText();
            statusLabel.setText("Deposited $" + amount + " to Account ID: " + accountId);
            JOptionPane.showMessageDialog(frame, "Deposited $" + amount + " to Account ID: " + accountId);
        });

        withdrawButton.addActionListener(e -> {
            String accountId = idField.getText();
            String amount = amountField.getText();
            statusLabel.setText("Withdrew $" + amount + " from Account ID: " + accountId);
            JOptionPane.showMessageDialog(frame, "Withdrew $" + amount + " from Account ID: " + accountId);
        });

        checkBalanceButton.addActionListener(e -> {
            String accountId = idField.getText();
            statusLabel.setText("Checking balance for Account ID: " + accountId);
            JOptionPane.showMessageDialog(frame, "Checking balance for Account ID: " + accountId);
        });

        exitButton.addActionListener(e -> System.exit(0));

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new GridLayout(5, 1, 5, 5));
        buttonPanel.add(createAccountButton);
        buttonPanel.add(depositButton);
        buttonPanel.add(withdrawButton);
        buttonPanel.add(checkBalanceButton);
        buttonPanel.add(exitButton);

        // Main panel to organize components
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.add(inputPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add components to frame
        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(statusLabel, BorderLayout.SOUTH);

        // Set frame visibility
        frame.setVisible(true);
    }

    // Method to create styled buttons
    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(100, 149, 237)); // Cornflower blue color
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setFocusPainted(false);
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(72, 118, 255)); // Royal blue on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(100, 149, 237)); // Revert to original color
            }
        });

    // Make the frame visible
    frame.setVisible(true); // Sets frame to visible

        return button;
    }
}
