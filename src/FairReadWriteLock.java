
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class FairReadWriteLock {
	AtomicInteger count = new AtomicInteger(0);
	int writerQ = 0;
	int readerQ = 0;
	final LinkedList<Integer> readstamp = new LinkedList<>();
	final LinkedList<Integer> writestamp = new LinkedList<>();

	public synchronized void beginRead() {
		System.out.println(Thread.currentThread().getId() + " Attempt to Read");
		int task = count.incrementAndGet();
		readstamp.add(task);
		while(writerQ > 0 || readerHasLess(task)){
			try{
				System.out.println(Thread.currentThread().getId() + " Reader waiting");
				wait();
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		System.out.println(Thread.currentThread().getId() + " Start Reading");
		readerQ++;
		readstamp.remove(new Integer(task));
	}

	public synchronized void endRead() {
		System.out.println(Thread.currentThread().getId() + " End Reading");
		readerQ--;
		notifyAll();
	}

	public synchronized void beginWrite() {
		System.out.println(Thread.currentThread().getId() + " Attempt to Write");
		int task = count.incrementAndGet();
		writestamp.add(task);
		while((writerQ > 0 || readerQ > 0) || (writerHasLess(task))){
			try{
				System.out.println(Thread.currentThread().getId() + " Writer waiting");
				wait();
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		System.out.println(Thread.currentThread().getId() + " Start Writing");
		writerQ++;
		writestamp.remove(new Integer(task));
	}
	public synchronized void endWrite() {
		System.out.println(Thread.currentThread().getId() + " End Writing");
		writerQ--;
		notifyAll();
	}

	private boolean readerHasLess(int taskNum){
		for(Integer i : writestamp){
			if(i < taskNum){
				return true;
			}
		}
		return false;
	}
	private boolean writerHasLess(int taskNum){
		for(Integer i : writestamp){
			if(i < taskNum){
				return true;
			}
		}
		for(Integer i : readstamp){
			if(i < taskNum){
				return true;
			}
		}
		return false;
	}
}
	
