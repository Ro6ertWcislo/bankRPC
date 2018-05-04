package sr.grpc.client.util;

import BankClient.*;
import BankClient.CurrencyType;
import com.zeroc.Ice.Current;
import sr.grpc.gen.*;

import java.util.HashMap;

public class PremiumAccountI extends AccountI implements PremiumAccount{
    HashMap<sr.grpc.gen.CurrencyType,Double> currencyRateMap;
    public PremiumAccountI(String firstName, String lastName, String pesel, double income, HashMap<sr.grpc.gen.CurrencyType,Double>currencyRateMap) {
        super(firstName, lastName, pesel, income);
        this.currencyRateMap = currencyRateMap;
    }

    @Override
    public CreditInfo applyForCredit(double value, CurrencyType currency, Date from, Date to, Current current) throws DateRangeError, NotAuthrorizedException {
        value *=1.3;
        return new CreditInfo(CurrencyType.PLN,value,currency,countForeignValue(value,currency));
    }

    private double countForeignValue(double value, CurrencyType currency) {
        return value / currencyRateMap.get(CurrencyAdapter.adapt(currency));
    }

    private static class CurrencyAdapter{
        static sr.grpc.gen.CurrencyType adapt(CurrencyType currencyType){
            switch (currencyType){
                case CHF:
                    return sr.grpc.gen.CurrencyType.CHF;
                case USD:
                    return sr.grpc.gen.CurrencyType.USD;
                case PLN:
                    return sr.grpc.gen.CurrencyType.PLN;
                case EUR:
                    return sr.grpc.gen.CurrencyType.EUR;
                default:
                    throw new IllegalArgumentException();
            }
        }
    }
}
