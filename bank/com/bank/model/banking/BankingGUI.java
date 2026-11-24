package banking;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class BankingGUI extends JFrame {

    BankingController controller = new BankingController();

    Customer customer;

    // GUI components
    private JTextField firstName;
    private JTextField surname;
    private JTextField address;
    private JTextField phone;
    private JTextField amount;

    private JComboBox<String> accountType;

    private JTextArea output;

    public BankingGUI() {

        setTitle("Banking System");
        setSize(450, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(0, 1, 10, 10));

        // Title
        JLabel title = new JLabel("BANKING SYSTEM");
        title.setFont(new Font("Arial", Font.BOLD, 22));

        // Customer details
        firstName = new JTextField();
        firstName.setBorder(BorderFactory.createTitledBorder("First Name"));

        surname = new JTextField();
        surname.setBorder(BorderFactory.createTitledBorder("Surname"));

        address = new JTextField();
        address.setBorder(BorderFactory.createTitledBorder("Address"));

        phone = new JTextField();
        phone.setBorder(BorderFactory.createTitledBorder("Phone Number"));

        // Register button
        JButton register = new JButton("Register Customer");

        // Account selection
        accountType = new JComboBox<>();

        accountType.addItem("Savings");
        accountType.addItem("Investment");
        accountType.addItem("Cheque");

        JButton openAccount = new JButton("Open Account");

        // Transaction amount
        amount = new JTextField();
        amount.setBorder(BorderFactory.createTitledBorder("Amount"));

        JButton deposit = new JButton("Deposit");

        JButton withdraw = new JButton("Withdraw");

        // Output area
        output = new JTextArea();
        output.setEditable(false);
        output.setLineWrap(true);

        JScrollPane scrollPane = new JScrollPane(output);

        // REGISTER CUSTOMER
        register.addActionListener(e -> {

            try {

                if (firstName.getText().trim().isEmpty()
                        || surname.getText().trim().isEmpty()
                        || address.getText().trim().isEmpty()
                        || phone.getText().trim().isEmpty()) {

                    throw new IllegalArgumentException(
                        "Please complete all customer details."
                    );
                }

                customer = new Customer(
                    1,
                    firstName.getText(),
                    surname.getText(),
                    address.getText(),
                    phone.getText()
                );

                controller.registerCustomer(customer);

                output.setText("Customer registered successfully.");

            } catch (Exception ex) {

                output.setText(ex.getMessage());
            }
        });

        // OPEN ACCOUNT
        openAccount.addActionListener(e -> {

            try {

                if (customer == null) {
                    throw new IllegalArgumentException(
                        "Register a customer first."
                    );
                }

                String type = (String) accountType.getSelectedItem();

                Account account;

                String number = "ACC" + System.currentTimeMillis();

                switch (type) {

                    case "Savings":

                        account = new SavingsAccount(
                            number,
                            BigDecimal.ZERO,
                            "Gaborone",
                            customer
                        );

                        break;

                    case "Investment":

                        account = new InvestmentAccount(
                            number,
                            new BigDecimal("500"),
                            "Gaborone",
                            customer
                        );

                        break;

                    case "Cheque":

                        output.setText(
                            "Cheque accounts require employment details."
                        );

                        return;

                    default:

                        throw new IllegalArgumentException(
                            "Invalid account type."
                        );
                }

                controller.openAccount(customer, account);

                output.setText(
                    "Account opened successfully.\n"
                    + "Account Number: " + number
                );

            } catch (Exception ex) {

                output.setText(ex.getMessage());
            }
        });

        // DEPOSIT
        deposit.addActionListener(e -> {

            try {

                if (customer == null) {
                    throw new IllegalArgumentException(
                        "Register a customer first."
                    );
                }

                if (customer.getAccounts().isEmpty()) {
                    throw new IllegalArgumentException(
                        "Open an account first."
                    );
                }

                BigDecimal value = new BigDecimal(amount.getText());

                Account account = customer.getAccounts().get(0);

                controller.deposit(account, value);

                output.setText("Deposit successful.");

            } catch (Exception ex) {

                output.setText(ex.getMessage());
            }
        });

        // WITHDRAW
        withdraw.addActionListener(e -> {

            try {

                if (customer == null
                        || customer.getAccounts().isEmpty()) {

                    throw new IllegalArgumentException(
                        "Open an account first."
                    );
                }

                BigDecimal value = new BigDecimal(amount.getText());

                Account account = customer.getAccounts().get(0);

                controller.withdraw(account, value);

                output.setText("Withdrawal successful.");

            } catch (Exception ex) {

                output.setText(ex.getMessage());
            }
        });

        // ADD COMPONENTS
        panel.add(title);
        panel.add(firstName);
        panel.add(surname);
        panel.add(address);
        panel.add(phone);
        panel.add(register);
        panel.add(accountType);
        panel.add(openAccount);
        panel.add(amount);
        panel.add(deposit);
        panel.add(withdraw);
        panel.add(scrollPane);

        add(panel);

        setVisible(true);
    }

    // MAIN METHOD
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new BankingGUI();
        });
    }
}