import java.util.Random;

public class PriorityTest implements Runnable{
    final PriorityQueue gate;
    final static int SIZE = 2000;
    PriorityTest(PriorityQueue gate){
        this.gate = gate;
    }
    public void run(){
        for (int i = 0; i < 1; i++) {
            Random ran = new Random();
            int p = ran.nextInt(9 + 1);
            System.out.println(Thread.currentThread().getName() + " Adding, Priority is " + p);
            int j = gate.add(Thread.currentThread().getName(), p);
            //System.out.println(Thread.currentThread().getName() + " Index is " + j);
            String name = gate.getFirst();
            System.out.println(Thread.currentThread().getName() + " first one is " + name);
        }
    }
    public static void main(String args[]){
        PriorityQueue que = new PriorityQueue(SIZE);
        Thread[] t = new Thread[SIZE];
        for (int i = 0; i < SIZE; ++i) {
            t[i] = new Thread(new PriorityTest(que));
        }
        for(int i = 0; i < SIZE; i++){
            t[i].start();
        }
        //que.printQueue();
    }
}
