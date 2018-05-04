package sr.grpc.server;

import io.grpc.stub.StreamObserver;
import sr.grpc.gen.Currency;
import sr.grpc.gen.CurrencyProviderGrpc;
import sr.grpc.gen.CurrencyType;
import sr.grpc.gen.ExchangeRate;

import java.util.HashMap;

public class CurrencyProviderImpl extends CurrencyProviderGrpc.CurrencyProviderImplBase {
    HashMap<CurrencyType,Double> currencyMap = new HashMap<>();

    public CurrencyProviderImpl() {
        currencyMap.put(CurrencyType.EUR,4.03);
        currencyMap.put(CurrencyType.USD,3.55);
        currencyMap.put(CurrencyType.CHF,2.01);
        currencyMap.put(CurrencyType.PLN,1.00);
    }

    @Override
    public StreamObserver<Currency> getExchangeRates(StreamObserver<ExchangeRate> responseObserver) {
        return new StreamObserver<Currency>() {

            @Override
            public void onNext(Currency currency) {
                ExchangeRate responseRate = ExchangeRate.newBuilder()
                        .setCurrency(currency.getCurrency())
                        .setRate(currencyMap.get(currency.getCurrency()))
                        .build();
                    responseObserver.onNext(responseRate);
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("lipa");
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
