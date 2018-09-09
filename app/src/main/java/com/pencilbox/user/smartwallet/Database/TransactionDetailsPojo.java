package com.pencilbox.user.smartwallet.Database;

public class TransactionDetailsPojo {
    private int transactionId;
    private int accountId;
    private String transaction_date;
    private String transaction_action;
    private String bank_name;
    private String account_no;
    private double transaction_amount;
    private double current_balance;

    public TransactionDetailsPojo(int transactionId, int accountId, String transaction_date, String transaction_action, String bank_name, String account_no, double transaction_amount, double current_balance) {
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.transaction_date = transaction_date;
        this.transaction_action = transaction_action;
        this.bank_name = bank_name;
        this.account_no = account_no;
        this.transaction_amount = transaction_amount;
        this.current_balance = current_balance;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getAccountId() {
        return accountId;
    }

    public String getTransaction_date() {
        return transaction_date;
    }

    public String getTransaction_action() {
        return transaction_action;
    }

    public String getBank_name() {
        return bank_name;
    }

    public String getAccount_no() {
        return account_no;
    }

    public double getTransaction_amount() {
        return transaction_amount;
    }

    public double getCurrent_balance() {
        return current_balance;
    }

    @Override
    public String toString() {
        return "TransactionDetailsPojo{" +
                "transactionId=" + transactionId +
                ", accountId=" + accountId +
                ", transaction_date='" + transaction_date + '\'' +
                ", transaction_action='" + transaction_action + '\'' +
                ", bank_name='" + bank_name + '\'' +
                ", account_no='" + account_no + '\'' +
                ", transaction_amount=" + transaction_amount +
                ", current_balance=" + current_balance +
                '}';
    }
}
