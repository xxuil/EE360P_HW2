/*
 * EID's of group members
 * 
 */



public class MonitorThreadSynch {
	private int parties;
	private int count = 0;
	public MonitorThreadSynch(int parties) {
		this.parties = parties;
	}
	
	public synchronized int await() throws InterruptedException {
		count++;
		int index = parties - count;
		if(index == 0){
			count = 0;
			notifyAll();
		}else{
			wait();
		}
          // you need to write this code
	    return index;
	}
}
