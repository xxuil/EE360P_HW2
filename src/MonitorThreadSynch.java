/*
 * EID's of group members
 * 
 */



public class MonitorThreadSynch {
	private int permits;
	private int parties;
	private int numofwait = 0;
	public MonitorThreadSynch(int parties) {
		permits = 0;
		this.parties = parties;
	}
	
	public int await() throws InterruptedException {
		int index = 0;
		if(numofwait == parties - 1){
			release();
		}else{
			acquire();
		}
          // you need to write this code
	    return index;
	}
	public synchronized void acquire(){
		numofwait++;
		try{
			wait();
		}catch(InterruptedException e) {
			e.printStackTrace();
		}
	}
	public synchronized void release(){
		numofwait = 0;
		notifyAll();
	}
}
