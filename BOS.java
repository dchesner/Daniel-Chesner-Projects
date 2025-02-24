// Basic Banking Operating System

import java.sql.*;
import java.util.Scanner;

public class BankingApp {
    static final String URL = "jdbc:mysql://localhost:3306/BankDB";
    static final String USER = "root"; // Change if needed
    static final String PASS = "your_password"; // Change to your MySQL password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            System.out.println("\nBanking System:");
            System.out.println("1. Create Account");
            System.out.println("2. Deposit Money");
            System.out.println("3. Withdraw Money");
            System.out.println("4. Check Balance");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> createAccount();
                case 2 -> depositMoney();
                case 3 -> withdrawMoney();
                case 4 -> checkBalance();
                case 5 -> {
                    System.out.println("Thank you for using our banking system!");
                    scanner.close();
                    return;
                }
                default -> System.out.println("Invalid choice, try again.");
            }
        }
    }

    public static void createAccount() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Scanner scanner = new Scanner(System.in)) {
            
            System.out.print("Enter account holder's name: ");
            String name = scanner.nextLine();

            String query = "INSERT INTO accounts (account_holder, balance) VALUES (?, 0.00)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.executeUpdate();

            System.out.println("Account created successfully for " + name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void depositMoney() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
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
            e.printStackTrace();
        }
    }

    public static void withdrawMoney() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Scanner scanner = new Scanner(System.in)) {
            
            System.out.print("Enter account ID: ");
            int accountId = scanner.nextInt();
            System.out.print("Enter withdrawal amount: ");
            double amount = scanner.nextDouble();

            String checkBalanceQuery = "SELECT balance FROM accounts WHERE account_id = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkBalanceQuery);
            checkStmt.setInt(1, accountId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void checkBalance() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
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
            e.printStackTrace();
        }
    }
}
