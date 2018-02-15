
public class testMonitor implements Runnable {
    final static int SIZE = 3;
    final static int ROUND = 999;

    final MonitorThreadSynch gate;

    public testMonitor(MonitorThreadSynch gate) {
        this.gate = gate;
    }

    public void run() {
        int index = -1;

        for (int round = 0; round < ROUND; ++round) {
            System.out.println("Thread " + Thread.currentThread().getName() + " is WAITING round:" + round);
            try {
                index = gate.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Thread " + Thread.currentThread().getName() + " is leaving round:" + round);
        }
    }

    public static void main(String[] args) {
        MonitorThreadSynch gate = new MonitorThreadSynch(SIZE);
        Thread[] t = new Thread[SIZE];

        for (int i = 0; i < SIZE; ++i) {
            t[i] = new Thread(new testMonitor(gate));
        }

        for (int i = 0; i < SIZE; ++i) {
            t[i].start();
        }
    }
}
