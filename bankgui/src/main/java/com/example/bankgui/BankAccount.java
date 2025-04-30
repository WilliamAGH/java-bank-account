package com.example.bankgui;

/**
 * Lab 5: Java Bank Account Class with GUI Support
 * Description: Defines a BankAccount class to manage balance, transactions,
 *              unique account numbers, and exception handling via a GUI application
 * Author: William Callahan
 * @since April 29, 2025
 */

import java.util.Calendar;
import java.text.SimpleDateFormat;

/**
 * Provides a basic bank account with deposit, withdrawal, and statement functionalities
 */
public class BankAccount {

    private final long accountNumber;
    private double balance;
    private final StringBuilder transactions;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static long nextAccountNumber = 100001;

    /**
     * Default constructor, initializes the balance to zero and assigns a default account number
     */
    public BankAccount() {
        this.accountNumber = generateAccountNumber();
        this.balance = 0.0;
        this.transactions = new StringBuilder("Account Statement for Account #" + this.accountNumber + ":\n");
        recordTransaction("Account opened", 0.0);
    }

    /**
     * Overloaded constructor. Initializes the balance with the given amount and assigns a unique account number.
     * Throws InvalidAmountException if the initial balance is negative.
     *
     * @param initialBalance The initial balance for the account. Must be non-negative.
     * @throws BankAccountException if initialBalance is negative.
     */
    public BankAccount(double initialBalance) throws BankAccountException {
        if (initialBalance < 0) {
            throw new BankAccountException("Initial balance cannot be negative.");
        }
        this.accountNumber = generateAccountNumber();
        this.transactions = new StringBuilder("Account Statement for Account #" + this.accountNumber + ":\n");
        this.balance = initialBalance;
        recordTransaction("Account opened with initial deposit", initialBalance);
    }

    // Generate unique account numbers incrementally
    private static synchronized long generateAccountNumber() {
        return nextAccountNumber++;
    }

    /**
     * Deposits a specified amount into the account (must be positive)
     *
     * @param amount The amount to deposit
     * @throws BankAccountException if the amount is not positive
     */
    public void deposit(double amount) throws BankAccountException {
        if (amount <= 0) {
            throw new BankAccountException("Deposit amount must be positive.");
        }
        balance += amount;
        recordTransaction("Deposit", amount);
    }

    /**
     * Withdraws a specified amount from the account
     * The amount must be positive and cannot exceed the current balance
     *
     * @param amount The amount to withdraw
     * @throws BankAccountException if the amount is not positive or if funds are insufficient
     */
    public void withdraw(double amount) throws BankAccountException {
        if (amount <= 0) {
            throw new BankAccountException("Withdrawal amount must be positive.");
        }
        if (balance < amount) {
            throw new BankAccountException("Insufficient funds. Current balance: " + String.format("%.2f", balance));
        }
        balance -= amount;
        recordTransaction("Withdrawal", amount);
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

    /**
     * Provides a string of the BankAccount, primarily showing the account number for display in UI components
     *
     * @return A string representation of the account (e.g., "Account #100001")
     */
    @Override
    public String toString() {
        return "Account #" + accountNumber;
    }
} 