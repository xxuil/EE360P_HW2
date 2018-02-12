/*
 * EID's of group members
 * 
 */
import java.util.concurrent.Semaphore;


public class ThreadSynch {
	private Semaphore example;
	private int parties;
	private int count = 0;
	public ThreadSynch(int parties) {
		example = new Semaphore(0);
		this.parties = parties;
	}
	
	public int await() throws InterruptedException {
		int index = parties - count;
		if(example.getQueueLength() == parties - 1){
			example.release(parties);
			example = new Semaphore(0);
		}else{
			example.acquireUninterruptibly();
		}
		count++;
          // you need to write this code
	    return index;
	}
}
