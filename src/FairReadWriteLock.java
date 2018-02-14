import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FairReadWriteLock {
	boolean lockfor_reader = false;
	final LinkedList<Timestamp> readstamp = new LinkedList<>();
	final LinkedList<Timestamp> writestamp = new LinkedList<>();

	FairReadWriteLock(){
	}
	public synchronized void beginRead() {
		System.out.println(Thread.currentThread().getId() + " Reading");
		Timestamp for_read = new Timestamp(System.currentTimeMillis());
		readstamp.add(for_read);
		while(!lockfor_reader || !writestamp.isEmpty() && for_read.after(writestamp.get(0))){			//A reader thread that invokes beginRead() will be blocked until all preceding writer threads have acquired and released the lock.
			try {
				System.out.println(Thread.currentThread().getId() + " Waiting to Read");
				wait();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}

	public synchronized void endRead() {
		System.out.println(Thread.currentThread().getId() + " End Reading");
		readstamp.removeFirst();
		notifyAll();
	}

	public synchronized void beginWrite() {
		System.out.println(Thread.currentThread().getId() + " Writing");
		lockfor_reader = false;
		Timestamp for_write = new Timestamp(System.currentTimeMillis());
		writestamp.add(for_write);
		while(lockfor_reader || !readstamp.isEmpty() && for_write.after(readstamp.get(0))){     //only one writer can do their stuff
			try{
				System.out.println(Thread.currentThread().getId() + " Waiting to Write");
				wait();
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	public synchronized void endWrite() {
		System.out.println(Thread.currentThread().getId() + " End Writing");
		lockfor_reader = true;
		writestamp.removeFirst();
		notifyAll();
	}
}
	
