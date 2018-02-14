


public class PriorityQueue {
	private final int size;

	private class pNode{
	    private int p;
	    private String name;

	    private pNode(int priority, String name){
            this.p = priority;
            this.name = name;
        }
    }

	public PriorityQueue(int maxSize) {
        // Creates a Priority queue with maximum allowed size as capacity
        this.size = maxSize;
	}

	public int add(String name, int priority) {
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

