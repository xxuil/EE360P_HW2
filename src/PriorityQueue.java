

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PriorityQueue {
    private final static boolean DEBUG = true;
	private final int capacity;
	private pNode head;
	private pNode tail;
	private AtomicInteger size;

	private Lock addLock;
	private Lock getLock;
	private Condition addCon;
	private Condition getCon;

	public PriorityQueue(int maxSize) {
        // Creates a Priority queue with maximum allowed size as capacity
        this.capacity = maxSize;
        this.head = null;
        this.tail = head;
        this.size = new AtomicInteger(0);
        this.addLock = new ReentrantLock();
        this.getLock = new ReentrantLock();
        this.addCon = addLock.newCondition();
        this.getCon = getLock.newCondition();

	}

	public int add(String name, int priority) {
        // Adds the name with its priority to this queue.
        // Returns the current position in the list where the name was inserted;
        // otherwise, returns -1 if the name is already present in the list.
        // This method blocks when the list is full.
        int index = 0;
        if(DEBUG) {System.out.println(Thread.currentThread().getName() + " is locking addlock");}
        addLock.lock();
        if(DEBUG) {System.out.println(Thread.currentThread().getName() + " locks addlock");}

        try{
            pNode newN = new pNode(priority, name);
            newN.lock();

            //2. Check if queue is full or not
            while(size.intValue() == capacity){
                try{ addCon.await();}
                catch (InterruptedException ee){}
            }

            if(size.intValue() == 0){
                this.head = newN;
                this.tail = newN;
                size.incrementAndGet();
                newN.unlock();
            } else {
                pNode temp = this.head;
                temp.lock();

                if(newN.getPriority() > temp.getPriority()){
                    newN.setNext(temp);
                    head = newN;
                    size.incrementAndGet();
                    temp.unlock();
                    newN.unlock();
                } else {
                    boolean flag = false;
                    while(!temp.getNext().equals(temp)){
                        if(temp.equals(newN)){
                            temp.unlock();
                            newN.unlock();
                            addLock.unlock();
                            flag = true;
                            break;
                        }

                        if(newN.getPriority() > temp.getNext().getPriority()){
                            newN.setNext(temp.getNext());
                            temp.setNext(newN);
                            size.incrementAndGet();
                            index ++;
                            temp.unlock();
                            newN.unlock();
                            break;
                        }
                        else{
                            index++;
                            temp.next.lock();
                            temp.unlock();
                            temp = temp.getNext();
                        }
                    }
                    if(flag){
                        temp.setNext(newN);
                        temp.unlock();
                        newN.unlock();
                        size.incrementAndGet();
                        index++;
                    }
                }
            }
        } finally {
            addLock.unlock();
            if(DEBUG) {System.out.println(Thread.currentThread().getName() + " releases addlock");}
        }
        getLock.lock();
        try{
            getCon.signalAll();
        } finally{
            getLock.unlock();
        }

        return index;
	}

	public int search(String name) {
        // Returns the position of the name in the list;
        // otherwise, returns -1 if the name is not found.
        int index = 0;

        pNode tempN = head;
        String temp = tempN.getName();

        while(!temp.equals(name)){
            if(tempN.getNext().equals(tempN)){
                return -1;
            }
            index ++;
            tempN = tempN.getNext();
            temp = tempN.getName();
        }

        return index;
	}

	public String getFirst() {
        // Retrieves and removes the name with the highest priority in the list,
        // or blocks the thread if the list is empty.

        getLock.lock();
        String first = null;
        try{
            while(size.intValue() == 0){
                try {getCon.await();}
                catch (InterruptedException ee){}
            }

            head.lock();
            pNode delete = head;
            try{
                first = head.getName();
                head = head.getNext();
            } finally {
                delete.unlock();
            }

        }
        finally{
            getLock.unlock();
        }

        addLock.lock();
        try{
            addCon.signalAll();
        }
        finally{
            addLock.unlock();
        }
        return first;
	}

	public void printQueue(){
        System.out.println();
        System.out.println("Printing Queue: ");
	    pNode temp = head;
	    int i = 0;
	    while(!temp.getNext().equals(temp)){
            System.out.println("[" + i + "] name: " + temp.getName() + " p: " + temp.getPriority());
            i++;
            temp = temp.getNext();
        }
	    System.out.println();
    }

    private class pNode{
        private int p;
        private String name;
        private pNode next;

        private Lock lock;

        pNode(int priority, String name){
            this.p = priority;
            this.name = name;
            this.next = this;
            this.lock = new ReentrantLock();
        }

        String getName(){
            return this.name;
        }

        int getPriority(){
            return this.p;
        }

        pNode getNext(){
            return this.next;
        }

        void setNext(pNode node){
            this.next = node;
        }

        boolean equals(pNode that){
            if(this.name.equals(that.getName())){
                return true;
            }
            return false;
        }

        Condition getCondition(){
            return this.lock.newCondition();
        }

        void lock(){
            lock.lock();
        }

        void unlock(){
            lock.unlock();
        }
    }
}

