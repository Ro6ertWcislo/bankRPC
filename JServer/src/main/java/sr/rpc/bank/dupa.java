package sr.rpc.bank;



import java.util.concurrent.CompletableFuture;

import BankClient.AccountFactoryPrx;
import BankClient.AccountPrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Util;
import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.LocalException;

public class dupa
{
    public static void main(String[] args)
    {
        int status = 0;
        Communicator communicator = null;


        try {
            // 1. Inicjalizacja ICE
            communicator = Util.initialize(args);

            // 2. Uzyskanie referencji obiektu na podstawie linii w pliku konfiguracyjnym
            //Ice.ObjectPrx base = communicator.propertyToProxy("Calc1.Proxy");
            // 2. To samo
            ObjectPrx base = communicator.stringToProxy("calc/calc11:tcp -h localhost -p 10000:udp -h localhost -p 10000");

            // 3. Rzutowanie, zawanie
            AccountFactoryPrx obj = AccountFactoryPrx.checkedCast(base);
            if (obj == null) throw new Error("Invalid proxy");

            AccountFactoryPrx objOneway = (AccountFactoryPrx)obj.ice_oneway();


            // 4. Wywolanie zdalnych operacji

            String line = null;
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
            CompletableFuture<Long> cfl = null;
            do
            {
                try
                {
                    System.out.print("==> ");
                    System.out.flush();
                    line = in.readLine();
                    if (line == null)
                    {
                        break;
                    }
                    if (line.equals("add"))
                    {
                        AccountPrx r = obj.create("r","w","95092612131",43232);

                        r.deposit(100);

                        System.out.println("RESULT = " + r.accountBalance());
                    }
                    if(line.equals("conn")){
                        ObjectPrx bas1e = communicator.stringToProxy("premium/95092612130:tcp -h localhost -p 10000:udp -h localhost -p 10000");
                        AccountPrx obj1 = AccountPrx.checkedCast(bas1e);
                        System.out.println("RESULT = " + obj1.accountBalance());
                    }

	                /*else if(line.equals("o"))
	                {
	                    objOneway.add(7,8);
	                }
	                else if(line.equals("O"))
	                {
	                    objBatchOneway.add(7, 8);
	                }
	                else if(line.equals("d"))
	                {
	                    objDatagram.add(7, 8);
	                }
	                else if(line.equals("D"))
	                {
	                    objBatchDatagram.add(7, 8);
	                }*/
                    else if (line.equals("x"))
                    {
                        // Nothing to do
                    }
                }
                catch (java.io.IOException ex)
                {
                    System.err.println(ex);
                }
            }
            while (!line.equals("x"));


        } catch (LocalException e) {
            e.printStackTrace();
            status = 1;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            status = 1;
        }
        if (communicator != null) {
            // Clean up
            //
            try {
                communicator.destroy();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                status = 1;
            }
        }
        System.exit(status);
    }

}