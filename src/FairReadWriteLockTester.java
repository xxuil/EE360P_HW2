import java.util.concurrent.TimeUnit;

public class FairReadWriteLockTester {

    public static FairReadWriteLock lock = new FairReadWriteLock();
    private static WriteThread[] writers;
    private static ReadThread[] readers;
    private static int attempts;

    private static class ReadThread extends Thread {
        public void run() {
            for(int i = 0; i < attempts; i++){
                lock.beginRead();
                try {
                    TimeUnit.MILLISECONDS.sleep(5);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                lock.endRead();
                try {
                    TimeUnit.MILLISECONDS.sleep(5);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    private static class WriteThread extends Thread {
        public void run() {
            for(int i = 0; i < attempts; i++){
                lock.beginWrite();
                try {
                    TimeUnit.MILLISECONDS.sleep(5);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                lock.endWrite();
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

        writers = new WriteThread[numWriters];
        readers = new ReadThread[numReaders];
        attempts = numAttempts;

        for(int i = 0; i < writers.length; i++) {
            writers[i] = new WriteThread();
        }

        for(int i = 0; i < readers.length; i++) {
            readers[i] = new ReadThread();
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
        test(5, 5, 5);
    }
}
