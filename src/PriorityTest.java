import java.util.Random;
import java.util.concurrent.TimeUnit;

public class PriorityTest {

    public static PriorityQueue lock = new PriorityQueue(5000);
    private static AddThread[] writers;
    private static GetThread[] readers;
    private static int attempts;

    private static class AddThread extends Thread {
        public void run() {
            Random rand = new Random();
            int number = rand.nextInt(10);
            for(int i = 0; i < attempts; i++){
                System.out.println(Thread.currentThread().getName() + " Adding Priority is " + number);
                lock.add(Thread.currentThread().getName(), number);
                try {
                    TimeUnit.MILLISECONDS.sleep(5);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private static class GetThread extends Thread {
        public void run() {
            for(int i = 0; i < attempts; i++){
                System.out.println("First one is " + lock.getFirst());
                try {
                    TimeUnit.MILLISECONDS.sleep(50);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static void test(int numWriters, int numReaders, int numAttempts){

        writers = new AddThread[numWriters];
        readers = new GetThread[numReaders];
        attempts = numAttempts;

        for(int i = 0; i < writers.length; i++) {
            writers[i] = new AddThread();
        }

        for(int i = 0; i < readers.length; i++) {
            readers[i] = new GetThread();
        }

        int limit = writers.length;
        if(readers.length > writers.length) {
            limit = readers.length;
        }

        for(int i = 0; i < limit; i++) {
            if(i < readers.length) {
                readers[i].start();
            }
            if(i < writers.length) {
                writers[i].start();
            }
        }
    }
    public static void main(String args[]){
        test(10, 10, 5);
    }
}
