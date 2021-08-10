public class LinkedListDeque<T> {

    private class Node<T> {
        Node prev;
        T value;
        Node next;

        Node(Node p, T v, Node n) {
            this.prev = p;
            this.value = v;
            this.next = n;
        }
    }

    private int size;
    private Node sentinel;

    /** Creates an empty LinkedListDeque **/
    public LinkedListDeque() {
        size = 0;
        sentinel = new Node(null, null, null);
        sentinel.prev = sentinel;
        sentinel.next = sentinel;
    }

    /** Adds an item of type T to the front of the deque. **/
    public void addFirst(T item) {
        size++;
        Node newFirst = new Node(sentinel, item, sentinel.next);
        sentinel.next.prev = newFirst;
        sentinel.next = newFirst;
    }

    /** Adds an item of type T to the back of the deque. **/
    public void addLast(T item) {
        size++;
        Node newLast = new Node(sentinel.prev, item, sentinel);
        sentinel.prev.next = newLast;
        sentinel.prev = newLast;
    }

    /** Returns true if deque is empty, false otherwise. **/
    public boolean isEmpty() {
        return size == 0;
    }

    /** Returns the number of items in the deque. **/
    public int size() {
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space. **/
    public void printDeque() {
        System.out.print("[ ");
        Node p = sentinel.next;
        for (int i = 0; i < size; i++) {
            System.out.print(p.value + " ");
            p = p.next;
        }
        System.out.print("]");
        System.out.println();
    }

    /** Removes and returns the item at the front of the deque.
     * If no such item exists, returns null. **/
    public T removeFirst() {
        if (size == 0){
            return null;
        }
        size--;
        T result = (T) sentinel.next.value;
        sentinel.next.next.prev = sentinel;
        sentinel.next = sentinel.next.next;
        return result;
    }

    /** Removes and returns the item at the back of the deque.
     * If no such item exists, returns null. **/
    public T removeLast() {
        if (size == 0){
            return null;
        }
        size--;
        T result = (T) sentinel.prev.value;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        return result;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        Node p = sentinel.next;
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return (T) p.value;
    }

    private T getRecursiveHelpMethod(Node p, int index) {
        if (index == 0) {
            return (T) p.value;
        }
        return (T) getRecursiveHelpMethod(p.next, index - 1);
    }

    /** Same as get, but uses recursion. **/
    public T getRecursive(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return (T) getRecursiveHelpMethod(sentinel.next, index);
    }

}
