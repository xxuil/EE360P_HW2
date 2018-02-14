import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class FairReadWriteLock {
	boolean lockfor_reader = false;
	int numWriter = 0;
	final List<Timestamp> readstamp = new ArrayList<>();
	final List<Timestamp> writestamp = new ArrayList<>();
                        
	public synchronized void beginRead() {
		Timestamp for_read = new Timestamp(System.currentTimeMillis());
		readstamp.add(for_read);
		while(!lockfor_reader || !writestamp.isEmpty() && for_read.after(writestamp.get(0))){			//A reader thread that invokes beginRead() will be blocked until all preceding writer threads have acquired and released the lock.
			try {
				wait();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}

	public synchronized void endRead() {
		notifyAll();
	}

	public synchronized void beginWrite() {
		lockfor_reader = false;
		numWriter++;
		Timestamp for_write = new Timestamp(System.currentTimeMillis());
		writestamp.add(for_write);
		while(numWriter > 1 || lockfor_reader || !readstamp.isEmpty() && for_write.after(readstamp.get(0))){     //only one writer can do their stuff
			try{
				wait();
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
	}
	public synchronized void endWrite() {
		lockfor_reader = true;
		numWriter--;
		notifyAll();
	}
}
	
