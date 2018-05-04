package sr.grpc.client;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import sr.grpc.gen.Currency;
import sr.grpc.gen.CurrencyProviderGrpc;
import sr.grpc.gen.CurrencyType;
import sr.grpc.gen.ExchangeRate;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Bank {
    private static final Logger logger = Logger.getLogger(Bank.class.getName());

    private final ManagedChannel channel;

    private final CurrencyProviderGrpc.CurrencyProviderStub currencyProviderStub;
    private final CurrencyProviderGrpc.CurrencyProviderBlockingStub currencyProviderBlockingStub;

    private HashMap<CurrencyType,Double> currencyRateMap = new HashMap<>();


    /**
     * Construct client connecting to HelloWorld server at {@code host:port}.
     */
    public Bank(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid needing certificates.
                .usePlaintext(true)
                .build();


        currencyProviderBlockingStub = CurrencyProviderGrpc.newBlockingStub(channel);
        currencyProviderStub = CurrencyProviderGrpc.newStub(channel);
        currencyRateMap.put(CurrencyType.USD,1.0);
        currencyRateMap.put(CurrencyType.EUR,1.0);

    }

    public static void main(String[] args) throws Exception {
        Bank client = new Bank("localhost", 50051);
        client.start();
    }
    private void printRates(){
        currencyRateMap.entrySet().forEach(System.out::println);
    }

    private void start() throws InterruptedException {
        try {
            String line = null;
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
            do {
                try {
                    System.out.print("==> ");
                    System.out.flush();
                    line = in.readLine();
                    if (line == null) {
                        break;
                    }
                    if (line.equals("add")) {
                        printRates();
                        getExchangeRates();
                    }
                } catch (java.io.IOException ex) {
                    System.err.println(ex);
                }
            }
            while (!line.equals("x"));
        } finally {
            shutdown();
        }
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public void getExchangeRates() {
        StreamObserver<ExchangeRate> responseObserver = new StreamObserver<ExchangeRate>() {
            @Override
            public void onNext(ExchangeRate exchangeRate) {
                currencyRateMap.put(exchangeRate.getCurrency(),exchangeRate.getRate());
                printRates();
            }

            @Override
            public void onError(Throwable t) {
                System.out.println("lipa");
            }

            @Override
            public void onCompleted() {
                System.out.println("przeslane wszystko");
                printRates();
            }
        };

        StreamObserver<Currency> requestObserver =currencyProviderStub.getExchangeRates(responseObserver);
        try{
            currencyRateMap.keySet().stream()
                    .map(currencyType -> Currency.newBuilder()
                            .setCurrency(currencyType)
                            .build())
                    .forEach(requestObserver::onNext);
        } catch (RuntimeException e) {
            // Cancel RPC
            requestObserver.onError(e);
            throw e;
        }
        // Mark the end of requests
        requestObserver.onCompleted();


    }
}
