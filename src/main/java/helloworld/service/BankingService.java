package helloworld.service;

import helloworld.model.Account;
import helloworld.model.Transaction;

import java.io.*;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BankingService {
    private static final String DATA_DIRECTORY = "data";
    private static final String ACCOUNTS_CSV = DATA_DIRECTORY + File.separator + "accounts.csv";
    private static final String ACCOUNTS_BALANCES_CSV = DATA_DIRECTORY + File.separator + "accounts_balances.csv";
    private static final String TRANSACTIONS_CSV = DATA_DIRECTORY +File.separator + "transactions.csv";

    private Map<String,Account> accounts;

    public Account createAccount(String accountNumber){
        if (accounts.containsKey(accountNumber)){
            throw new IllegalArgumentException("Account number already exists!");
        }
        Account account = new Account(accountNumber);
        accounts.put(accountNumber,account);
        saveAccounts();
        return account;
    }
    public Account createAccount(String accountNumber, String accountHolderName){
        if(accounts.containsKey(accountNumber)){
            throw new IllegalArgumentException("Account number already exists!");
        }
        Account account = new Account(accountNumber, accountHolderName);
        accounts.put(accountNumber, account);
        saveAccounts();
        return account;
    }
    public void updateAccountHolderName(String accountNumber ,String accountHolderName){
        Account account = getAccounts(accountNumber);
        account.setAccountHolderName(accountHolderName);
        saveAccounts();
    }
    public Account getAccounts(String accountNumber){
        Account account = accounts.get(accountNumber);
        if(account == null){
            throw new IllegalArgumentException("Account not found" + accountNumber);
        }
        return account;
    }
    public Map<String, Account> getAllAccounts() {
        return new HashMap<>(accounts);
    }
    public BankingService(){
      accounts = new HashMap<>();
      loadAccounts();
    }
    public List<Transaction> getTransactionHistory(String accountNumber){
        Account account = getAccounts(accountNumber);
        return account.getTransactionHistory();
    }
    public void deposit(String accountNumber, double amount){
        Account account = getAccounts(accountNumber);
        account.deposit(amount);
        saveAccounts();
    }
    public void withdraw(String accountNumber, double amount){
        Account account = getAccounts(accountNumber);
        account.withdraw(amount);
        saveAccounts();
    }
    private void saveAccounts(){
        File directory = new File(DATA_DIRECTORY);
        if(!directory.exists()){
            directory.mkdirs();
        }

        try(PrintWriter writer = new PrintWriter(new FileWriter(ACCOUNTS_BALANCES_CSV))){
             writer.println("accountNumber ,accountHolderName ,balance" );

            for (Account account : accounts.values()) {
                writer.println(account.getAccountNumber() + "," +
                        account.getAccountHolderName() + "," +
                        account.getBalance());
            }

            System.out.println("Saved "+accounts.size()+"accounts to" + ACCOUNTS_BALANCES_CSV);
        }
        catch(IOException e){
            System.err.println("Error saving account to CSV!"+e.getMessage());
        }
    }
    private void loadAccounts(){
        File accountsFile = new File(ACCOUNTS_CSV);
        File balancesFile = new File(ACCOUNTS_BALANCES_CSV);

        if(balancesFile.exists()){
            try{
                List<String> lines = Files.lines(Paths.get(ACCOUNTS_BALANCES_CSV))
                        .skip(1)
                        .collect(Collectors.toList());

                for(String line : lines){
                    String[] parts = line.split(",");
                    if(parts.length >=3){
                        String accountNumber = parts[0];
                        String accountHolderName = parts[1];
                        double balance = 0.0;
                        
                        try{
                           balance = Double.parseDouble(parts[2]);
                        }
                        catch(NumberFormatException e){
                            System.err.println("Invalid balance format for account" + accountNumber + ":"+ parts[2]);
                        }
                        Account account = new Account(accountNumber, accountHolderName);
                        if(balance > 0){
                            account.deposit(balance);
                        }

                        accounts.put(accountNumber,account);
                    }
                }
                System.out.println("Loaded"+accounts.size()+ "accounts from"+ACCOUNTS_BALANCES_CSV );
                return;
            }
            catch(IOException e){

                System.err.println("Error loading accounts  from"+ACCOUNTS_BALANCES_CSV + ":"+e.getMessage());

            }
        }
        if(accountsFile.exists()){
            try{
                List<String> lines = Files.lines(Paths.get(ACCOUNTS_CSV))
                        .skip(1)
                        .collect(Collectors.toList());
                for(String line : lines){
                    String[] parts = line.split(",");
                    if(parts.length >= 2){
                        String accountNumber = parts[0];
                        String accountHolderName = parts[1];
                        double balance = 0.0;

                        if(parts.length >= 3 && !parts[2].isEmpty()){
                            try{
                                balance = Double.parseDouble(parts[2]);
                            }
                            catch(NumberFormatException e){
                                System.err.println("Invalid balance Format!"+e.getMessage());
                            }
                        }
                        Account account = new Account(accountNumber, accountHolderName);
                        if(balance > 0 ){
                            account.deposit(balance);
                        }

                        accounts.put(accountNumber,account);
                    }
                }
                System.out.println("Loaded"+accounts.size()+"accounts from"+ACCOUNTS_CSV);
                saveAccounts();

            }
            catch(IOException e){
                System.err.println("Error loading accounts from " + ACCOUNTS_CSV + ": " + e.getMessage());
            }
        }


    }
}
