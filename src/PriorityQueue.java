import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PriorityQueue {
	private ReentrantLock foradd, forget;
	private Condition isFull, isEmpty;
	private int size;
	private pNode head = null;
	private pNode tail = null;

	private class pNode{

	    private int priority;
	    private String name;
	    private pNode next;

	    private pNode(int priority, String name){
            this.p = priority;
            this.name = name;
        }
    }

	public PriorityQueue(int maxSize) {
		foradd = new ReentrantLock();
		forget = new ReentrantLock();
		isFull = foradd.newCondition();
		isEmpty = forget.newCondition();
        // Creates a Priority queue with maximum allowed size as capacity
        this.size = maxSize;
	}

	public int add(String name, int priority) {
		while(size == 0){} //wait();
		int index = 0;
		pNode new_node = new pNode();
		pNode prev = null;
		new_node.name = name;
		new_node.priority = priority;
		pNode head_temp = head;
		if(head_temp == null){
			head = new_node;
			size--;
			return 0;
		}else{
			while(head_temp != null){
				if(head_temp.name.equals(new_node.name)){
					return -1;
				}
				if(head_temp.priority > new_node.priority){
					head_temp = head_temp.next;
				}else{
					if(prev != null) {
						prev.next = new_node;
					}
					new_node.next = head_temp;
					size--;
					return index;
				}
				prev = head_temp;
				head_temp = head_temp.next;
				index++;
			}
		}
        // Adds the name with its priority to this queue.
        // Returns the current position in the list where the name was inserted;
        // otherwise, returns -1 if the name is already present in the list.
        // This method blocks when the list is full.

        return 0;
	}

	public int search(String name) {
        // Returns the position of the name in the list;
        // otherwise, returns -1 if the name is not found.

        return 0;
	}

	public String getFirst() {
        // Retrieves and removes the name with the highest priority in the list,
        // or blocks the thread if the list is empty.

        return null;
	}
}

