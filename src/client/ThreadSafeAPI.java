package client;


import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class ThreadSafeAPI implements Iterator<String> {

    private int next;
    private final int step;
    private final int max;
    private Stack<String> items;

    public ThreadSafeAPI(int next, int step, int max) {
        this.step = step;
        this.max = max;
        this.next = next;
        this.items = new Stack<>();
        this.loadNext();
    }

    @Override
    public synchronized boolean hasNext() {
        return this.next < this.max || !this.items.isEmpty();
    }

    @Override
    public synchronized String next(){
        if(!this.hasNext()){
            System.out.println("No next");
            return null;
        }

        if(this.items.isEmpty() && this.next < this.max){
            this.loadNext();
        }

        return this.items.pop();
    }

    private void loadNext() {
        System.out.println("Retrieving next: " + next + ", limit: " + this.step + " with max: " + max);
        try {
            this.items.addAll(ServerAPI.getLevels(this.next, this.step));
            this.next += this.step;
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
}
