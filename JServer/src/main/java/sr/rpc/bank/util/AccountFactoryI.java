package sr.rpc.bank.util;

import BankClient.AccountFactory;
import BankClient.AccountPrx;
import BankClient.NoIncomeException;
import BankClient.PremiumAccountPrx;
import com.zeroc.Ice.Current;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import sr.rpc.gen.CurrencyType;

import java.util.HashMap;
import java.util.logging.Logger;

public class AccountFactoryI implements AccountFactory {
    private static final Logger logger = Logger.getLogger(AccountFactoryI.class.getName());

    private ObjectAdapter adapter;
    private HashMap<CurrencyType, Double> currencyRateMap;
    private int minPremiumIncome = 5000;

    public AccountFactoryI(ObjectAdapter adapter, HashMap<CurrencyType, Double> currencyRateMap) {
        this.adapter = adapter;
        this.currencyRateMap = currencyRateMap;
    }

    @Override
    public AccountPrx create(String firstName, String lastName, String pesel, double income, Current current) {
        if (income > minPremiumIncome) {
            logger.info("creating premium account for pesel: " + pesel);
            return PremiumAccountPrx.uncheckedCast(adapter.add(new PremiumAccountI(firstName, lastName, pesel, income, currencyRateMap), new Identity(pesel, "account")));
        } else {
            logger.info("creating standard account for pesel: "+pesel);
            return AccountPrx.uncheckedCast(adapter.add(new AccountI(firstName, lastName, pesel, income), new Identity(pesel, "account")));
        }
    }
}
