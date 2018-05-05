package sr.rpc.bank.util;

import BankClient.Account;
import com.zeroc.Ice.Current;

public class AccountI implements Account {
    private String firstName, lastName, pesel;
    private double income, balance;

    public AccountI(String firstName, String lastName, String pesel, double income) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.pesel = pesel;
        this.income = income;
        this.balance = 0;
    }

    public double accountBalance(Current current) {
        return balance;
    }

    public void deposit(double value, Current current) {
        balance += value;
    }
}
