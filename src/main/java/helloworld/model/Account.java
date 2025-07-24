package helloworld.model;

import helloworld.model.Transaction;
import helloworld.model.TransactionType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable {
    private static final long serialVersionUID= 1L;

    private String accountNumber;
    private String accountHolderName;
    private double balance;
    private List<Transaction> transactionHistory;

    public Account(String accountNumber){
        this.accountNumber = accountNumber;
        this.accountHolderName = "";
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();

    }

    public Account(String accountNumber, String accountHolderName){
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = 0.0;
        this.transactionHistory = new ArrayList<>();
    }

    public String getAccountNumber(){
        return accountNumber;
    }

    public String getAccountHolderName(){
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName){
        this.accountHolderName = accountHolderName;
    }

    public double getBalance(){
        return balance;
    }

    public List<Transaction> getTransactionHistory(){
        return transactionHistory;
    }

    public void deposit(double amount){

        if(amount <= 0){
            throw new IllegalArgumentException("Deposit amount must be positive!");
        }
        balance += amount;
        Transaction transaction = new Transaction(
                TransactionType.DEPOSIT,
                amount,
                balance
        );
         transactionHistory.add(transaction);
    }

    public void withdraw(double amount){
        if(amount <= 0){
            throw new IllegalArgumentException("Withdraw must be positive!");
        }
        if(amount > balance){
            throw new IllegalArgumentException("Insufficient Funds!");
        }
        balance -= amount;
        Transaction transaction = new Transaction(
                TransactionType.WITHDRAW,
                amount,
                balance
        );
        transactionHistory.add(transaction);
    }
    @Override
    public String toString(){
        String nameDisplay = (accountHolderName == null || accountHolderName.isEmpty()) ? "[No Name]" : accountHolderName;
        return "Account #" + accountNumber +"| Name:"+nameDisplay+"| BalanceUGX%"+ String.format("%,.0f",balance);
    }
}



