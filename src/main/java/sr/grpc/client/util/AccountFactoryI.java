package sr.grpc.client.util;

import BankClient.AccountFactory;
import BankClient.AccountPrx;
import BankClient.NoIncomeException;
import BankClient.PremiumAccountPrx;
import com.zeroc.Ice.Current;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import sr.grpc.gen.CurrencyType;

import java.util.HashMap;

public class AccountFactoryI implements AccountFactory{
    private ObjectAdapter adapter;
    private  HashMap<CurrencyType,Double> currencyRateMap;
    public AccountFactoryI(ObjectAdapter adapter, HashMap<CurrencyType,Double> currencyRateMap) {
        this.adapter = adapter;
        this.currencyRateMap =currencyRateMap;
    }

    @Override
    public AccountPrx create(String firstName, String lastName, String pesel, double income, Current current) throws NoIncomeException {
        if(income>5000)
            return PremiumAccountPrx.uncheckedCast(adapter.add(new PremiumAccountI(firstName,lastName,pesel,income,currencyRateMap), new Identity(pesel,"account")));
        else
            return AccountPrx.uncheckedCast(adapter.add(new AccountI(firstName,lastName,pesel,income), new Identity(pesel,"account")));
    }
}
