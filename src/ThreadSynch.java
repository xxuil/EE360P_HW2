/*
 * EID's of group members
 * 
 */
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;


public class ThreadSynch {
    private AtomicInteger count;
	private Semaphore example;
	private int parties;

	public ThreadSynch(int parties) {
		example = new Semaphore(0);
		this.parties = parties;
		count = new AtomicInteger(0);
	}

	public int await() throws InterruptedException {
        count.incrementAndGet();
		int index = parties - count.intValue();

		if(index == 0){
		    count.set(0);
			example.release(parties);
			example = new Semaphore(0);
		} else {
			example.acquire();
		}

		// you need to write this code
		return index;
	}
}
