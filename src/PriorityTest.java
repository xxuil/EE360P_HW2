public class PriorityTest implements Runnable{
    final PriorityQueue gate;
    final static int SIZE = 2;
    PriorityTest(PriorityQueue gate){
        this.gate = gate;
    }
    public void run(){
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " Adding, Priority is " + i);
            int j = gate.add(Thread.currentThread().getName(), i);
            //int j = gate.search(Thread.currentThread().getName());
            System.out.println(Thread.currentThread().getName() + " Index is " + j);
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
    }
}
