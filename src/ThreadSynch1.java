/*
 * EID's of group members
 * 
 */
import java.util.concurrent.Semaphore;


public class ThreadSynch1 {
    private Semaphore mutex;
    private Semaphore everyone;
    private Semaphore resetting;
    private int parties;
    private int index;

    public ThreadSynch1(int parties) {
        this.parties = parties;
        this.index = parties;
        this.mutex = new Semaphore(1); //serves as mutex tool
        this.everyone = new Semaphore(1); //waits for everyone to be done
        this.resetting = new Semaphore(1); //blocks if in the process of resetting

    }

    public int await() throws InterruptedException {

        mutex.acquire();

        resetting.acquire(); //just to handle edge case that new set of threads enter while
        resetting.release(); //still releasing previous set of threads

        if(index == parties) { //first to arrive
            everyone.acquire(); //note, first one will try to acquire everyone TWICE, this is intended
        }

        int myIndex = --index;

        if(myIndex == 0) { //last one has arrived
            resetting.acquire(); //we're resetting everything now, release when all threads are done
            index++; //index will now serve as a count for the number of threads released
            everyone.release(); //release "everyone" that first visitor acquired
            mutex.release(); //release mutex b/c returning early
            return 0;
        }

        mutex.release();

        everyone.acquire(); //wait for everyone before exiting
        index++; //increment
        if(index == parties) { //that is, we're the last one to exit
            resetting.release();
        }
        everyone.release();

        return myIndex;
    }
}
