/*
 * EE 360P HW 2
 * Name 1: Xiangxing Liu
 * EID 1: xl5587
 * Name 2: Kravis Cho
 * EID 2: kyc375
 * 02/14/2018
 */

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class FairReadWriteLock {
    private static final boolean DEBUG = true;
    private AtomicInteger count = new AtomicInteger(0);
    private int writerQ = 0;
    private int readerQ = 0;
    private final LinkedList<Integer> readstamp = new LinkedList<>();
    private final LinkedList<Integer> writestamp = new LinkedList<>();

	public synchronized void beginRead() {
		if(DEBUG) System.out.println(Thread.currentThread().getId() + " Attempt to Read");
		int task = count.incrementAndGet();
		readstamp.add(task);

		while(writerQ > 0 || readerCheck(task)){
			try{
                if(DEBUG) System.out.println(Thread.currentThread().getId() + " Reader waiting");
				wait();
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}

        if(DEBUG) System.out.println(Thread.currentThread().getId() + " Start Reading");
		readerQ++;
		readstamp.remove(new Integer(task));
	}

	public synchronized void endRead() {
        if(DEBUG) System.out.println(Thread.currentThread().getId() + " End Reading");
		readerQ--;
		notifyAll();
	}

	public synchronized void beginWrite() {
        if(DEBUG) System.out.println(Thread.currentThread().getId() + " Attempt to Write");
		int task = count.incrementAndGet();
		writestamp.add(task);

		while((writerQ > 0 || readerQ > 0) || (writerCheck(task))){
			try{
                if(DEBUG) System.out.println(Thread.currentThread().getId() + " Writer waiting");
				wait();
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}

        if(DEBUG) System.out.println(Thread.currentThread().getId() + " Start Writing");
		writerQ++;
		writestamp.remove(new Integer(task));
	}
	public synchronized void endWrite() {
        if(DEBUG) System.out.println(Thread.currentThread().getId() + " End Writing");
		writerQ--;
		notifyAll();
	}

	private boolean readerCheck(int taskNum){
		for(Integer i : writestamp){
			if(i < taskNum){
				return true;
			}
		}
		return false;
	}

	private boolean writerCheck(int taskNum){
		if(readerCheck(taskNum))
		    return true;

		for(Integer i : readstamp){
			if(i < taskNum){
				return true;
			}
		}
		return false;
	}
}
	
