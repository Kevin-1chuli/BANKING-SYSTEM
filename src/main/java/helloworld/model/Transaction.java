package helloworld.model;

import helloworld.model.TransactionType;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final  DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yy:MM:dd HH:mm:ss");

    private TransactionType type;
    private double balance;
    private double resultingBalance;
    private LocalDateTime  timeStamp;

    public Transaction(TransactionType type, double balance, double resultingBalance){

        this.type = type;
        this.balance = balance;
        this.resultingBalance = resultingBalance;
        this.timeStamp = LocalDateTime.now();
    }
    public TransactionType getTransactionType(){
        return type;
    }
    public double getBalance(){
        return balance;
    }
    public double getResultingBalance(){
        return resultingBalance;
    }
    public LocalDateTime getTimeStamp(){
        return timeStamp;
    }

    @Override
    public String toString(){
        String action = (type == TransactionType.DEPOSIT) ? "Deposit":"Withdraw";
        return String.format("[%s] %s : UGX%,.0f | Balance : UGX%,.0f",
              timeStamp.format(FORMATTER),
              action,
              balance,
              resultingBalance
        );

    }

}
