package sr.rpc.bank.util;

import BankClient.Account;
import com.zeroc.Ice.Current;

import java.util.logging.Logger;

public class AccountI implements Account {
    private static final Logger logger = Logger.getLogger(AccountI.class.getName());

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
        logger.info("returning balance for" + current.id);
        return balance;
    }

    public void deposit(double value, Current current) {
        logger.info("transfering money for" + current.id);
        balance += value;
    }
}
