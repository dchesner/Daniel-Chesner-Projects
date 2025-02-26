import java.sql.*;
import java.util.Scanner;

public class SimpleBankingApp {
    static final String URL = "jdbc:mysql://localhost:3306/BankDB"; // Change when needed
    static final String USER = "root"; // Change when needed
    static final String PASS = "your_password"; // Change to your MySQL password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\nDans Banking System:");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Check Balance");
            System.out.println("5. Exit");
            System.out.println("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            // Outcome of selection 1 through 5
            if (choice == 1) {
                createAccount();
            } else if (choice == 2) {
                depositMoney();
            } else if (choice == 3) {
                withdrawMoney();
            } else if (choice == 4) {
                checkBalance();
            } else if (choice == 5) {
                System.out.println("Thank you for using our Dans banking system!");
                break;
            } else {
                System.out.println("Invalid choice. Try again."); // if option 1 through 6 is not selected an error will apear
            }
        }
        
        scanner.close(); // Scanner closed
    }
// classes from above selection based on the number selected
    public static void createAccount() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
            Scanner scanner = new Scanner(System.in)) {
            
            System.out.print("Enter account holder's name: ");
            String name = scanner.nextLine();

            String query = "INSERT INTO accounts (account_holder, balance) VALUES (x, 0.00)";
            PreparedStatement pstmt = conn.prepareStatement(query); // pstmt instead of statment to prevent interuption of SQL
            pstmt.setString(1, name);
            pstmt.executeUpdate();

            System.out.println("Account created successfully for " + name);
        } catch (SQLException e) {
            System.out.println("Error creating account.");
        }
    }

    public static void depositMoney() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS); // Connection to the database refers to lines 5-7
             Scanner scanner = new Scanner(System.in)) {
            
            System.out.print("Enter account ID: ");
            int accountId = scanner.nextInt();
            System.out.print("Enter deposit amount: ");
            double amount = scanner.nextDouble();

            String query = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, accountId);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Deposit successful!");
            } else {
                System.out.println("Account not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error depositing money.");
        }
    }

    public static void withdrawMoney() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS); // Connection to the database refers to lines 5-7
             Scanner scanner = new Scanner(System.in)) {
            
            System.out.print("Enter account ID: ");
            int accountId = scanner.nextInt();
            System.out.print("Enter withdrawal amount: ");
            double amount = scanner.nextDouble();

            String checkBalanceQuery = "SELECT balance FROM accounts WHERE account_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkBalanceQuery);
            checkStmt.setInt(1, accountId);
            ResultSet rs = checkStmt.executeQuery();

            // moves data from SQL if no data, account doesn't exist
            if (rs.next()) { // rs is result set
                double currentBalance = rs.getDouble("balance"); // using double more precise than float
                if (currentBalance >= amount) {
                    String query = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
                    PreparedStatement pstmt = conn.prepareStatement(query);
                    pstmt.setDouble(1, amount);
                    pstmt.setInt(2, accountId);
                    pstmt.executeUpdate();

                    System.out.println("Withdrawal successful!");
                } else {
                    System.out.println("Insufficient balance.");
                }
            } else {
                System.out.println("Account not found.");
            }
        } catch (SQLException e) { // If something goes wrong with the database, print an error message.
            System.out.println("Error withdrawing money. Please try again.");
        }
    }

    public static void checkBalance() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS); // Connection to the database refers to lines 5-7
             Scanner scanner = new Scanner(System.in)) {
            
            System.out.print("Enter account ID: ");
            int accountId = scanner.nextInt();

            String query = "SELECT balance FROM accounts WHERE account_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("Current Balance: $" + rs.getDouble("balance"));
            } else {
                System.out.println("Account not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error checking balance.");
        }
    }
}

