package com.example.bankgui;

/**
 * Custom exception handling for bank account operations
 * Used for errors like insufficient funds or invalid transaction amounts
 * @author William Callahan
 * @since April 29, 2025
 */
public class BankAccountException extends Exception {
    public BankAccountException(String message) {
        super(message);
    }
} 