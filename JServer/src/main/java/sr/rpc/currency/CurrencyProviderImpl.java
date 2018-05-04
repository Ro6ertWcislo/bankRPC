package sr.rpc.currency;

import io.grpc.stub.StreamObserver;
import sr.rpc.gen.Currency;
import sr.rpc.gen.CurrencyProviderGrpc;
import sr.rpc.gen.CurrencyType;
import sr.rpc.gen.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class CurrencyProviderImpl extends CurrencyProviderGrpc.CurrencyProviderImplBase {
    private static long period = 20;
    private HashMap<CurrencyType,Double> currencyMap = new HashMap<>();
    private HashMap<StreamObserver<ExchangeRate>,List<CurrencyType>> bankCurrenciesMap = new HashMap<>();

    public CurrencyProviderImpl() {
        currencyMap.put(CurrencyType.EUR,4.03);
        currencyMap.put(CurrencyType.USD,3.55);
        currencyMap.put(CurrencyType.CHF,2.01);
        currencyMap.put(CurrencyType.PLN,1.00);
        ScheduledExecutorService ses = Executors.newScheduledThreadPool(2);
        ses.scheduleAtFixedRate(this::updateCurrencies,0, period, TimeUnit.SECONDS);
        ses.scheduleAtFixedRate(this::notifyAllBanks,0, period, TimeUnit.SECONDS);
    }

    private void updateCurrencies() {
        currencyMap.keySet().forEach(this::updateExchangeRate);
    }

    private void updateExchangeRate(CurrencyType currencyType) {
        double newValue = currencyMap.get(currencyType) * ThreadLocalRandom.current().nextDouble(0.9,1.1);
        currencyMap.put(currencyType,round(newValue,2));
    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public StreamObserver<Currency> getExchangeRates(StreamObserver<ExchangeRate> responseObserver) {
        bankCurrenciesMap.putIfAbsent(responseObserver,new LinkedList<>());
        return new StreamObserver<Currency>() {

            @Override
            public void onNext(Currency currency) {
                bankCurrenciesMap.get(responseObserver).add(currency.getCurrency());
                singleCurrencyNotify(responseObserver,currency.getCurrency());
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                bankCurrenciesMap.remove(responseObserver);
                responseObserver.onCompleted();
            }
        };
    }


    private void singleCurrencyNotify(StreamObserver<ExchangeRate> bankObserver, CurrencyType currencyType) {
        ExchangeRate responseRate = ExchangeRate.newBuilder()
                .setCurrency(currencyType)
                .setRate(currencyMap.get(currencyType))
                .build();
        bankObserver.onNext(responseRate);
    }
    private void notifyAllBanks(){
        bankCurrenciesMap.entrySet().forEach(this::notifyBank);
    }

    private void notifyBank(Map.Entry<StreamObserver<ExchangeRate>, List<CurrencyType>> bankCurrencyStreamObserver) {
        StreamObserver<ExchangeRate> bankObserver  = bankCurrencyStreamObserver.getKey();
        bankCurrencyStreamObserver.getValue()
                .forEach(currencyType -> singleCurrencyNotify(bankObserver,currencyType));
    }
}
