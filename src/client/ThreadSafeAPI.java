package client;


import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class ThreadSafeAPI implements Iterator<String> {

    private String lastID;
    private final int step;
    private boolean hasMore = true;
    private Stack<String> items;

    public ThreadSafeAPI(int step, String lastID) {
        this.step = step;
        this.lastID = lastID;
        this.items = new Stack<>();
        this.loadNext();
    }

    @Override
    public synchronized boolean hasNext() {
        return this.hasMore || !this.items.isEmpty();
    }

    @Override
    public synchronized String next(){
        if(!this.hasNext()){
            System.out.println("No next");
            return null;
        }

        if(this.items.isEmpty() && this.hasMore){
            this.loadNext();
        }

        return this.items.pop();
    }

    private void loadNext() {
        System.out.println("Retrieving next ID: " + lastID + ", limit: " + this.step);
        try {
            Thread.sleep(10000);
            this.items.addAll(ServerAPI.getLevels(0, this.step, lastID));
            this.hasMore = this.items.size() == this.step; //Assumes that if the full set is returned, there is more
            this.lastID = ServerAPI.transformLevelID(this.items.lastElement());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
