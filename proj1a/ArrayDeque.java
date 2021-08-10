public class ArrayDeque<T> {

    private T[] items;
    private int size;
    private int firstPointer;
    private int lastPointer;

    /** Creates an empty array deque. **/
    public ArrayDeque(){
        size = 0;
        items = (T[]) new Object[8];
        firstPointer = 0;
        lastPointer = 0;
    }

    private void resizing(int capacity){
        T[] newItems = (T[]) new Object[capacity];
        for(int i = 0; i < size; i++){
            newItems[i] = items[(firstPointer + i) % items.length];
        }
        this.items = newItems;
        this.firstPointer = 0;
        this.lastPointer = size;
    }

    /** Adds an item of type T to the front of the deque. **/
    public void addFirst(T item){
        if(size == items.length){
            resizing(2 * size);
        }
        size++;
        firstPointer = (firstPointer - 1 + items.length) % items.length;
        items[firstPointer] = item;
    }

    /** Adds an item of type T to the back of the deque. **/
    public void addLast(T item){
        if(size == items.length){
            resizing(2 * size);
        }
        size++;
        items[lastPointer] = item;
        lastPointer = (lastPointer + 1) % items.length;
    }

    /** Returns true if deque is empty, false otherwise. **/
    public boolean isEmpty(){
        return size == 0;
    }

    /** Returns the number of items in the deque. **/
    public int size(){
        return size;
    }

    /** Prints the items in the deque from first to last, separated by a space. **/
    public void printDeque(){
        System.out.print("[ ");
        for(int i = firstPointer; i < size; i++){
            System.out.print(items[i] + " ");
        }
        System.out.print("]");
        System.out.println();
    }

    /** Removes and returns the item at the front of the deque. If no such item exists, returns null. **/
    public T removeFirst(){
        if(size == 0){
            return null;
        }
        size--;
        /**
        if(items.length > 16 && ((double)size / items.length) <= 0.25){
            resizing(items.length / 2);
        }**/
        T result = items[firstPointer];
        firstPointer = (firstPointer + 1) % items.length;
        return result;
    }

    /** Removes and returns the item at the back of the deque. If no such item exists, returns null. **/
    public T removeLast(){
        if(size == 0){
            return null;
        }
        size--;
        /**
        if(items.length > 16 && ((double)size / items.length) <= 0.25){
            resizing(items.length / 2);
        }**/
        lastPointer = (lastPointer - 1 + items.length) % items.length;
        T result = items[lastPointer];
        return result;
    }

    /**
     * Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth.
     * If no such item exists, returns null. Must not alter the deque!
     */
    public T get(int index){
        if(index < 0 || index >= size){
            return null;
        }
        return items[(firstPointer + index) % items.length];
    }


}
