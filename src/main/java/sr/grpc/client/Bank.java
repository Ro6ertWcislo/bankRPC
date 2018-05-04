package sr.grpc.client;


import BankClient.AccountFactory;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import sr.grpc.client.util.AccountFactoryI;
import sr.grpc.gen.Currency;
import sr.grpc.gen.CurrencyProviderGrpc;
import sr.grpc.gen.CurrencyType;
import sr.grpc.gen.ExchangeRate;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class Bank {
    private static final Logger logger = Logger.getLogger(Bank.class.getName());

    private final ManagedChannel channel;

    private final CurrencyProviderGrpc.CurrencyProviderStub currencyProviderStub;
    private final CurrencyProviderGrpc.CurrencyProviderBlockingStub currencyProviderBlockingStub;

    private final HashMap<CurrencyType,Double> currencyRateMap = new HashMap<>();
    private StreamObserver<Currency> requestObserver;


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
        currencyRateMap.put(CurrencyType.CHF,1.0);



    }

    public static void main(String[] args) throws Exception {
        Bank client = new Bank("localhost", 50051);
        client.start();
    }
    private void printRates(){
        currencyRateMap.entrySet().forEach(System.out::println);
        System.out.println("==================");
    }

    private void start() throws InterruptedException {
        try {
            getExchangeRates();
            int status = 0;
            Communicator communicator = null;

            try
            {
                communicator = Util.initialize();
                ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("Adapter1", "tcp -h localhost -p 10000:udp -h localhost -p 10000");

                // 3. Stworzenie serwanta/serwant�w
//                CalcI calcServant1 = new CalcI();
//                CalcI calcServant2 = new CalcI();
//
//                // 4. Dodanie wpis�w do tablicy ASM
//                adapter.add(calcServant1, new Identity("calc11", "calc"));
//                adapter.add(calcServant2, new Identity("calc22", "calc"));
                AccountFactoryI accountFactoryI = new AccountFactoryI(adapter);

                adapter.add(accountFactoryI,new Identity("calc11","calc"));

                adapter.activate();

                System.out.println("Entering event processing loop...");

                communicator.waitForShutdown();

            }
            catch (Exception e)
            {
                System.err.println(e);
                status = 1;
            }
            if (communicator != null)
            {
                // Clean up
                //
                try
                {
                    communicator.destroy();
                }
                catch (Exception e)
                {
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
                currencyRateMap.put(exchangeRate.getCurrency(),exchangeRate.getRate());
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

        requestObserver =currencyProviderStub.getExchangeRates(responseObserver);
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


    }
}
