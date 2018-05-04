package sr.grpc.client.util;

import BankClient.*;
import com.zeroc.Ice.Current;

public class PremiumAccountI extends AccountI implements PremiumAccount{
    public PremiumAccountI(String firstName, String lastName, String pesel, double income) {
        super(firstName, lastName, pesel, income);
    }

    @Override
    public CreditInfo applyForCredit(double value, CurrencyType currency, Date from, Date to, Current current) throws DateRangeError, NotAuthrorizedException {
        return null;
    }
}
