/*
 * EE 360P HW 2
 * Name 1: Xiangxing Liu
 * EID 1: xl5587
 * Name 2: Kravis Cho
 * EID 2: kyc375
 * 02/14/2018
 */

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PriorityQueue {
    private final static boolean ADEBUG = false;
    private final static boolean GDEBUG = true;
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
        if(ADEBUG) {System.out.println(Thread.currentThread().getName() + " is locking addlock");}
        addLock.lock();
        int index = 0;
        if(ADEBUG) {System.out.println(Thread.currentThread().getName() + " locks addlock");}

        try{
            pNode newN = new pNode(priority, name);
            if(ADEBUG) {System.out.println(Thread.currentThread().getName() + " locks new node");}
            newN.lock();

            //2. Check if queue is full or not
            while(size.intValue() == capacity){
                if(ADEBUG) {System.out.println(Thread.currentThread().getName() + " cap full, waiting");}
                try{ addCon.await();}
                catch (InterruptedException ee){}
            }

            if(size.intValue() == 0){
                this.head = newN;
                this.tail = newN;
                size.incrementAndGet();
                if(ADEBUG) {System.out.println(Thread.currentThread().getName() + " unlocks new node");}
                newN.unlock();
            }

            else {
                pNode temp = this.head;
                temp.lock();

                if(temp.equals(newN)){
                    if(ADEBUG) {System.out.println(Thread.currentThread().getName() + " unlocks new node");}
                    temp.unlock();
                    newN.unlock();
                    index = -1;
                }

                else if(newN.getPriority() > temp.getPriority()){
                    newN.setNext(temp);
                    head = newN;
                    size.incrementAndGet();
                    temp.unlock();
                    if(ADEBUG) {System.out.println(Thread.currentThread().getName() + " unlocks new node");}
                    newN.unlock();
                }

                else {
                    boolean flag = true;
                    while(!temp.getNext().equals(temp)){
                        if(temp.equals(newN)){
                            if(ADEBUG) {System.out.println(Thread.currentThread().getName() + " unlocks new node");}
                            temp.unlock();
                            newN.unlock();
                            index = -1;
                            flag = false;
                            break;
                        }

                        if(newN.getPriority() > temp.getNext().getPriority()){
                            newN.setNext(temp.getNext());
                            temp.setNext(newN);
                            size.incrementAndGet();
                            index ++;
                            temp.unlock();
                            newN.unlock();
                            flag = false;
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
                        if(temp.equals(newN)){
                            if(ADEBUG) {System.out.println(Thread.currentThread().getName() + " unlocks new node");}
                            temp.unlock();
                            newN.unlock();
                            index = -1;
                        }else {
                            temp.setNext(newN);
                            if (ADEBUG) {
                                System.out.println(Thread.currentThread().getName() + " unlocks new node");
                            }
                            temp.unlock();
                            newN.unlock();
                            size.incrementAndGet();
                            index++;
                        }
                    }
                }
            }
        } finally {
            if(ADEBUG) {System.out.println(Thread.currentThread().getName() + " releases addlock");}
            addLock.unlock();
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
                if(GDEBUG) {System.out.println(Thread.currentThread().getName() + "  waiting");}
                try {getCon.await();}
                catch (InterruptedException ee){}
            }
            pNode delete = head;

            //if(head == null) { throw new NullPointerException(); }

            head.lock();
            try{
                first = head.getName();
                if(head.getNext().equals(head))
                    head = null;
                else {
                    head = head.getNext();
                }
            } finally {
                delete.unlock();
            }

            size.decrementAndGet();

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
	    if(temp == null) {
	        System.out.println("Empty");
	        return;
	    }

        System.out.println("[" + i + "] name: " + temp.getName() + " p: " + temp.getPriority());

	    while(!temp.getNext().equals(temp)){
            i++;
            temp = temp.getNext();
            System.out.println("[" + i + "] name: " + temp.getName() + " p: " + temp.getPriority());
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

        int getPriority(){ return this.p; }

        pNode getNext(){
            return this.next;
        }

        void setNext(pNode node){
            this.next = node;
        }

        boolean equals(pNode that){
            return this.name.equals(that.getName());
        }

        void lock(){
            lock.lock();
        }

        void unlock(){
            lock.unlock();
        }
    }
}

