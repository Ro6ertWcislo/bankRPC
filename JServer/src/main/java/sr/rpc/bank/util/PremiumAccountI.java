package sr.rpc.bank.util;

import BankClient.*;
import BankClient.CurrencyType;
import com.zeroc.Ice.Current;

import java.util.HashMap;

public class PremiumAccountI extends AccountI implements PremiumAccount {
    private HashMap<sr.rpc.gen.CurrencyType, Double> currencyRateMap;

    public PremiumAccountI(String firstName, String lastName, String pesel, double income, HashMap<sr.rpc.gen.CurrencyType, Double> currencyRateMap) {
        super(firstName, lastName, pesel, income);
        this.currencyRateMap = currencyRateMap;
    }

    @Override
    public CreditInfo applyForCredit(double value, CurrencyType currency, Date from, Date to, Current current) throws DateRangeError {
        if(from.year>to.year || (from.year==to.year && from.month>to.month) ||(from.year==to.year && from.month==to.month && from.day > to.day))
            throw new DateRangeError("'from' date is after 'to' date");
        value *= 1.3;
        return new CreditInfo(CurrencyType.PLN, countForeignValue(value, currency), currency, value);
    }

    private double countForeignValue(double value, CurrencyType currency) {
        return value * currencyRateMap.get(CurrencyAdapter.adapt(currency));
    }

    private static class CurrencyAdapter {
        static sr.rpc.gen.CurrencyType adapt(CurrencyType currencyType) {
            switch (currencyType) {
                case CHF:
                    return sr.rpc.gen.CurrencyType.CHF;
                case USD:
                    return sr.rpc.gen.CurrencyType.USD;
                case PLN:
                    return sr.rpc.gen.CurrencyType.PLN;
                case EUR:
                    return sr.rpc.gen.CurrencyType.EUR;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}
