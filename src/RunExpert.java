import client.Client;
import client.ServerAPI;
import client.ThreadSafeAPI;
import com.sun.corba.se.spi.activation.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RunExpert {

    public static void main(String[] args) throws Exception {

        int limit = 1000;
        int poolSize = 500;
        ForkJoinPool threadPool = new ForkJoinPool(poolSize);
        int threadCount = 12;
        String lastID = ServerAPI.getLastID();
        ThreadSafeAPI api = new ThreadSafeAPI(limit, lastID);
        List<Integer> threads = IntStream.range(0, threadCount).boxed().collect(Collectors.toList());

        threadPool.submit(() -> runIterator(threads, api)).get();

        System.out.println("Completed");
    }

    private static void runIterator(List<Integer> threads, Iterator<String> iterator){
        threads.parallelStream().forEach(integer -> {
            System.out.println("Started thread " + integer);

            while (iterator.hasNext()){
                String next = iterator.next();

                if(next == null) {
                    return;
                }

                try {
                    Client.main(new String[]{next, integer + ""});
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Caught error in map : " + next);
                }
            }
        });
    }
}
