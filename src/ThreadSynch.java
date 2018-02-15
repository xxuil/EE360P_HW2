/*
 * EE 360P HW 2
 * Name 1: Xiangxing Liu
 * EID 1: xl5587
 * Name 2: Kravis Cho
 * EID 2: kyc375
 * 02/14/2018
 */

import java.util.concurrent.Semaphore;


public class ThreadSynch {
    private int count;
    private Semaphore waitLock;
    private Semaphore mutexLock;
    private Semaphore resetLock;
    private final int parties;

    public ThreadSynch(int parties) {
        waitLock = new Semaphore(1);
        this.parties = parties;
        this.mutexLock = new Semaphore(1);
        this.resetLock = new Semaphore(1);
        count = 0;
    }

    public int await() throws InterruptedException {
        /* Mutex ensures that only one thread will enter */
        mutexLock.acquire();

        /* Critical Section for mutexLock */
        resetLock.acquire();
        resetLock.release();

        /*
         * Count keeps track of the amount of threads for this round
         * Index is the index value ready to return
         */
        count++;
        int index = parties - count;

        /* If this is the first of this round, acquire twice */
        if(index == parties - 1){
            waitLock.acquire();
        }

        /*
         * If this is the last of this round, release
         * Release ensures that no other threads will enter next
         * round's release section before the current round get finished
         */
        if(index == 0){
            resetLock.acquire();
            count--;
            waitLock.release();
            mutexLock.release();
            return index;
        }

        /* Release mutex and let the thread wait for the last one */
        mutexLock.release();
        waitLock.acquire();

        /* Decrement count means that this thread is exiting this round */
        count--;

        /* If this is the last that gets released, release resetLock */
        if(count == 0){
            resetLock.release();
        }

        /* Release from waiting */
        waitLock.release();
        return index;
    }
}