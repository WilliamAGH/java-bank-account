/**
 * Lab 4, Group 3: Java Bank Account Class
 * Description: Defines a BankAccount class to manage balance and transactions
 * Author: William Callahan & Roman Labonte
 * Date: 4/8/2025
 */

import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * Provides a basic bank account with deposit, withdrawal, and statement functionalities
 */
public class BankAccount {

    private long accountNumber;
    private double balance;
    private StringBuilder transactions;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Default constructor, initializes the balance to zero and assigns a default account number (0)
     */
    public BankAccount() {
        this.accountNumber = 0; // Default account number
        this.balance = 0.0;
        this.transactions = new StringBuilder("Account Statement:\n");
        recordTransaction("Account opened", 0.0);
    }

    /**
     * Overloaded constructor, initializes the balance with the given amount and assigns a default account number (0)
     * If the initial amount is negative, the balance is set to zero
     *
     * @param initialBalance The initial balance for the account
     */
    public BankAccount(double initialBalance) {
        this.accountNumber = 0; // Default account number
        this.transactions = new StringBuilder("Account Statement:\n");
        if (initialBalance >= 0) {
            this.balance = initialBalance;
            recordTransaction("Account opened with initial deposit", initialBalance);
        } else {
            this.balance = 0.0;
            System.out.println("Initial balance cannot be negative. Setting balance to 0.");
            recordTransaction("Account opened", 0.0);
        }
    }

    /**
     * Deposits a specified amount into the account
     * The amount must be positive
     *
     * @param amount The amount to deposit, must be positive
     */
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            recordTransaction("Deposit", amount);
        } else {
            System.out.println("Deposit amount must be positive.");
            // Optionally record the failed attempt
            // recordTransaction("Failed deposit attempt (negative amount)", amount);
        }
    }

    /**
     * Withdraws a specified amount from the account
     * The amount must be positive and cannot exceed the current balance
     *
     * @param amount The amount to withdraw, must be positive and not exceed balance
     */
    public void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("Withdrawal amount must be positive.");
            // Optionally record the failed attempt
            // recordTransaction("Failed withdrawal attempt (non-positive amount)", amount);
        } else if (balance >= amount) {
            balance -= amount;
            recordTransaction("Withdrawal", amount);
        } else {
            System.out.println("Insufficient funds for withdrawal.");
             // Optionally record the failed attempt
            // recordTransaction("Failed withdrawal attempt (insufficient funds)", amount);
        }
    }

    /**
     * Returns the current balance of the account
     *
     * @return The current balance
     */
    public double getBalance() {
        return balance;
    }

    /**
     * Returns the transaction statement for the account
     *
     * @return A string containing all recorded transactions
     */
    public String getStatement() {
        return transactions.toString();
    }

     /**
     * Returns the account number
     *
     * @return The account number
     */
    public long getAccountNumber() {
        return accountNumber;
    }

    /**
     * Records a transaction by appending its details to the transactions log
     * Includes the timestamp, transaction type, amount, and resulting balance
     *
     * @param type   The type of transaction (e.g., "Deposit", "Withdrawal")
     * @param amount The amount involved in the transaction
     */
    private void recordTransaction(String type, double amount) {
        Calendar cal = Calendar.getInstance();
        String timestamp = DATE_FORMAT.format(cal.getTime());
        transactions.append(timestamp)
                    .append(" | Type: ").append(String.format("%-10s", type))
                    .append(" | Amount: ").append(String.format("%10.2f", amount))
                    .append(" | Balance: ").append(String.format("%10.2f", balance))
                    .append("\n");
    }
} 