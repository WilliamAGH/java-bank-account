package com.example.bankgui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.application.Platform;
import javafx.scene.control.Tooltip;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;

import atlantafx.base.theme.PrimerDark;
import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class BankController {

    @FXML
    private ComboBox<BankAccount> accountSelector;
    @FXML
    private TextField amountField;
    @FXML
    private Label messageLabel;
    @FXML
    private ToggleButton themeToggle;

    // menu items
    @FXML private MenuItem createAccountMenuItem;
    @FXML private MenuItem exitMenuItem;
    @FXML private MenuItem aboutMenuItem;

    // toolbar buttons
    @FXML private Button depositButton;
    @FXML private Button withdrawButton;
    @FXML private Button balanceButton;
    @FXML private Button statementButton;


    private List<BankAccount> accounts;
    private ObservableList<BankAccount> observableAccounts;

    /**
     * Initializes the controller class -- this method is automatically called
     * after the fxml file has been loaded
     */
    @FXML
    public void initialize() {
        accounts = new ArrayList<>();
        observableAccounts = FXCollections.observableArrayList(accounts);
        accountSelector.setItems(observableAccounts);


        // clear the message label initially
        messageLabel.setText("");

        // add a listener to clear the message label when a new account is selected
        accountSelector.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                messageLabel.setText(""); // clear the message on selection change
                amountField.clear();
            }
        });

        // theme toggle initialization
        updateThemeToggleText();

        // setup the toolbar icons
        setupToolbarIcons();
    }

    private void setupToolbarIcons() {
        //  basic FontAwesomeSolid icons (https://github.com/FortAwesome/Font-Awesome)
        depositButton.setGraphic(new FontIcon(FontAwesomeSolid.HAND_HOLDING_USD));
        withdrawButton.setGraphic(new FontIcon(FontAwesomeSolid.MONEY_BILL_WAVE));
        balanceButton.setGraphic(new FontIcon(FontAwesomeSolid.BALANCE_SCALE));
        statementButton.setGraphic(new FontIcon(FontAwesomeSolid.LIST_ALT));
        themeToggle.setGraphic(new FontIcon(FontAwesomeSolid.ADJUST));

        // add tooltips
        depositButton.setTooltip(new Tooltip("Deposit Funds"));
        withdrawButton.setTooltip(new Tooltip("Withdraw Funds"));
        balanceButton.setTooltip(new Tooltip("Show Balance"));
        statementButton.setTooltip(new Tooltip("Show Statement"));
        themeToggle.setTooltip(new Tooltip("Toggle Dark/Light Theme"));
    }

    @FXML
    protected void handleThemeToggle() {
        if (themeToggle.isSelected()) {
            // switch to the dark theme
            Application.setUserAgentStylesheet(new PrimerDark().getUserAgentStylesheet());
        } else {
            // switch to the light theme
            Application.setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
        }
        updateThemeToggleText();
    }

    private void updateThemeToggleText() {
         // icon change could also happen here if using two different icons in the future?
         if (themeToggle.getTooltip() != null) {
             themeToggle.getTooltip().setText(themeToggle.isSelected() ? "Switch to Light Mode" : "Switch to Dark Mode");
         }
    }

    @FXML
    protected void handleCreateAccount() {
        try {
            BankAccount newAccount = new BankAccount();
            accounts.add(newAccount);
            observableAccounts.setAll(accounts);
            accountSelector.getSelectionModel().select(newAccount);
            messageLabel.setText("Created: " + newAccount.toString());
            amountField.clear();
        } catch (Exception e) {
             showAlertDialog(Alert.AlertType.ERROR, "Account Creation Error", "Could not create new account: " + e.getMessage());
        }
    }

    @FXML
    protected void handleDeposit() {
        BankAccount selectedAccount = getSelectedAccount();
        if (selectedAccount == null) return; // (when no account selected)

        try {
            double amount = parseAmount();
            selectedAccount.deposit(amount);
            messageLabel.setText(String.format("Deposited: %.2f", amount));
            amountField.clear();
        } catch (NumberFormatException e) {
            showAlertDialog(Alert.AlertType.ERROR, "Input Error", "Invalid amount entered. Please enter a number.");
        } catch (BankAccountException e) { // catch the consolidated exception
            showAlertDialog(Alert.AlertType.ERROR, "Deposit Error", e.getMessage());
        }
    }

    @FXML
    protected void handleWithdraw() {
        BankAccount selectedAccount = getSelectedAccount();
        if (selectedAccount == null) return;

        try {
            double amount = parseAmount();
            selectedAccount.withdraw(amount);
            messageLabel.setText(String.format("Withdrew: %.2f", amount));
            amountField.clear();
        } catch (NumberFormatException e) {
            showAlertDialog(Alert.AlertType.ERROR, "Input Error", "Invalid amount entered. Please enter a number.");
        } catch (BankAccountException e) { // catch the consolidated exception
            showAlertDialog(Alert.AlertType.ERROR, "Withdrawal Error", e.getMessage());
        }
    }

    @FXML
    protected void handleShowBalance() {
        BankAccount selectedAccount = getSelectedAccount();
        if (selectedAccount == null) return;

        double balance = selectedAccount.getBalance();
        showAlertDialog(Alert.AlertType.INFORMATION, "Account Balance",
                selectedAccount.toString() + "\nCurrent Balance: " + String.format("%.2f", balance));
        messageLabel.setText(""); // clear the message label after showing the popup
    }

    @FXML
    protected void handleShowStatement() {
        BankAccount selectedAccount = getSelectedAccount();
        if (selectedAccount == null) return;

        // get the transaction data from the selected account
        List<BankAccount.TransactionRecord> history = selectedAccount.getTransactionHistory();

        // show the data in a table dialog
        showStatementTableDialog("Account Statement", selectedAccount.getAccountNumber(), history);
        messageLabel.setText("");
    }

    /**
     * Retrieves the currently selected BankAccount from the ComboBox
     * Shows an error alert if no account is selected
     * @return the selected BankAccount, or null if none is selected
     */
    private BankAccount getSelectedAccount() {
        BankAccount selected = accountSelector.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlertDialog(Alert.AlertType.WARNING, "Selection Error", "Please select or create an account first.");
            return null;
        }
        return selected;
    }

    /**
     * Parses the amount from the amountField
     * @return the parsed amount as a double
     * @throws NumberFormatException if the input is not a valid number
     */
    private double parseAmount() throws NumberFormatException {
        String amountText = amountField.getText();
        if (amountText == null || amountText.trim().isEmpty()) {
             throw new NumberFormatException("Amount field cannot be empty.");
        }
        return Double.parseDouble(amountText.trim());
    }

    /**
     * Shows a standard JavaFX Alert dialog
     * @param alertType the type of native JavaFX alert (e.g., INFORMATION, ERROR, WARNING)
     * @param title the title of the dialog window
     * @param content the message content of the dialog
     */
    private void showAlertDialog(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

     /**
     * Shows a dialog specifically for displaying the account statement in a TableView
     * @param title the title of the dialog window
     * @param accountNumber the account number for the header
     * @param history the list of TransactionRecord objects
     */
    private void showStatementTableDialog(String title, long accountNumber, List<BankAccount.TransactionRecord> history) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText("Transaction History for Account #" + accountNumber);
        alert.getDialogPane().getButtonTypes().clear();
        alert.getDialogPane().getButtonTypes().add(javafx.scene.control.ButtonType.OK);

        TableView<BankAccount.TransactionRecord> tableView = new TableView<>();

        // create the columns using lambda expressions for CellValueFactory -- the data was't appearing until I started using these
        TableColumn<BankAccount.TransactionRecord, String> timestampCol = new TableColumn<>("Timestamp");
        timestampCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().timestamp()));
        timestampCol.setPrefWidth(160);
        timestampCol.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<BankAccount.TransactionRecord, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().type()));
        typeCol.setPrefWidth(120);
        typeCol.setStyle("-fx-alignment: CENTER-LEFT;");

        TableColumn<BankAccount.TransactionRecord, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().amount()).asObject());
        amountCol.setPrefWidth(100);
        amountCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        TableColumn<BankAccount.TransactionRecord, Double> balanceCol = new TableColumn<>("Balance");
        balanceCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().balance()).asObject());
        balanceCol.setPrefWidth(100);
        balanceCol.setStyle("-fx-alignment: CENTER-RIGHT;");

        tableView.getColumns().setAll(List.of(timestampCol, typeCol, amountCol, balanceCol));

        // add the data to the table
        tableView.setItems(FXCollections.observableArrayList(history));

        // set the TableView properties
        tableView.setPrefHeight(300);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS); // prevent horizontal scrollbar if possible, cus I really disalike them ;) 

        // set the content of the dialog pane
        alert.getDialogPane().setContent(tableView);
        alert.setResizable(true);

        alert.showAndWait();
    }

    // Menu Item Handlers

    @FXML
    private void handleExit() {
        Platform.exit();
    }

    @FXML
    private void handleAbout() {
        showAlertDialog(Alert.AlertType.INFORMATION, "About BankOS",
                "Copyright 2025 Bank of Java\nBankOS Version 1.0\nWritten by William Callahan\nPowered by JavaFX and Ikonli");
    }
}