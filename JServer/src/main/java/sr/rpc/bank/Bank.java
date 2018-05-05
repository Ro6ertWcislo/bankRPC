package sr.rpc.bank;


import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import sr.rpc.bank.util.AccountFactoryI;
import sr.rpc.gen.Currency;
import sr.rpc.gen.CurrencyProviderGrpc;
import sr.rpc.gen.CurrencyType;
import sr.rpc.gen.ExchangeRate;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Bank {
    private static final Logger logger = Logger.getLogger(Bank.class.getName());

    private final ManagedChannel channel;

    private final CurrencyProviderGrpc.CurrencyProviderStub currencyProviderStub;

    private final HashMap<CurrencyType, Double> currencyRateMap = new HashMap<>();
    private StreamObserver<Currency> requestObserver;
    private String bankName;


    /**
     * Construct client connecting to HelloWorld server at {@code host:port}.
     */
    public Bank(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid needing certificates.
                .usePlaintext(true)
                .build();

        currencyProviderStub = CurrencyProviderGrpc.newStub(channel);
    }

    public static void main(String[] args) throws Exception {
        Bank client = new Bank("localhost", 50051);
        client.start();
    }

    private void printRates() {
        currencyRateMap.entrySet().forEach(System.out::println);
        System.out.println("==================");
    }

    private void start() throws InterruptedException {
        try {

            int status = 0;
            Communicator communicator = null;
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
            try {
                System.out.println("type bank name");
                bankName = in.readLine();
                System.out.println("type currencies devided by spaces, for example: USD PLN CHF");
                Stream.of(in.readLine().split(" "))
                        .forEach(currency -> currencyRateMap.put(CurrencyType.valueOf(currency), 1.0));
            } catch (IOException e) {
                System.out.print("something went wrong with reading from console. Try again later");
                System.exit(1);
            } catch (Exception ex) {
                System.out.print("something went wrong. Try again later");
                System.exit(1);
            }
            getExchangeRates();
            try {


                communicator = Util.initialize();
                ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("Adapter1", "tcp -h localhost -p 10000:udp -h localhost -p 10000");

                AccountFactoryI accountFactoryI = new AccountFactoryI(adapter, currencyRateMap);

                adapter.add(accountFactoryI, new Identity(bankName, "bank"));

                adapter.activate();

                System.out.println("Entering event processing loop...");

                communicator.waitForShutdown();
                System.out.println("Entering event processing loop...");

            } catch (Exception e) {
                System.err.println(e);
                status = 1;
            }
            if (communicator != null) {

                try {
                    communicator.destroy();
                } catch (Exception e) {
                    System.err.println(e);
                    status = 1;
                }
            }
            System.exit(status);
        } finally {
            // Mark the end of requests
            requestObserver.onCompleted();
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
                currencyRateMap.put(exchangeRate.getCurrency(), exchangeRate.getRate());
                printRates();
            }

            @Override
            public void onError(Throwable t) {
                t.printStackTrace();
            }

            @Override
            public void onCompleted() {
                System.out.println("przeslane wszystko");
            }
        };

        requestObserver = currencyProviderStub.getExchangeRates(responseObserver);
        try {
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


    }
}
