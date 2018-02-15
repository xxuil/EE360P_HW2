/*
 * EE 360P HW 2
 * Name 1: Xiangxing Liu
 * EID 1: xl5587
 * Name 2: Kravis Cho
 * EID 2: kyc375
 * 02/14/2018
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
