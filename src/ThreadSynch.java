import java.util.concurrent.Semaphore;


public class ThreadSynch {
    private int count;
    private Semaphore example;
    private Semaphore mutex;
    private Semaphore reset;
    private final int parties;

    public ThreadSynch(int parties) {
        example = new Semaphore(1);
        this.parties = parties;
        this.mutex = new Semaphore(1);
        this.reset = new Semaphore(1);
        count = 0;
    }

    public int await() throws InterruptedException {
        mutex.acquire();
        reset.acquire();
        reset.release();

        count++;
        int index = parties - count;

        if(index == parties - 1){
            example.acquire();
        }

        if(index == 0){
            reset.acquire();
            count--;
            example.release();
            mutex.release();
            return index;
        }

        mutex.release();
        example.acquire();

        count--;

        if(count == 0){
            reset.release();
        }
        example.release();
        // you need to write this code
        return index;
    }
}