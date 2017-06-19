/****************************************************************************
  *  Compilation: javac-algs4 RandomizedQueue.java
  *  Execution:   java-algs4  RandomizedQueue
  *  Dependency:  None
  *  @author  Kevin James 05282017
  *  @Email   kevinsocial@outlook.com
  * 
  *   A randomized queue is similar to a stack or queue, except that the item 
  *   removed is chosen uniformly at random from items in the data structure.
  * 
  *   Corner cases. The order of two or more iterators to the same randomized 
  *   queue must be mutually independent; each iterator must maintain its own 
  *   random order. Throw a java.lang.NullPointerException if the client 
  *   attempts to add a null item; throw a java.util.NoSuchElementException if
  *   the client attempts to sample or dequeue an item from an empty randomized 
  *   queue; throw a java.lang.UnsupportedOperationException if the client calls 
  *   the remove() method in the iterator;
  *   throw a java.util.NoSuchElementException if the client calls the next()
  *   method in the iterator and there are no 
  *   more items to return.
  * 
  *   Performance requirements. Your randomized queue implementation must 
  *   support each randomized queue operation (besides creating an iterator)
  *   in constant amortized time. That is, any sequence of m randomized queue 
  *   operations (starting from an empty queue) should take at most cm steps 
  *   in the worst case, for some constant c. A randomized queue containing n 
  *   items must use at most 48n + 192 bytes of memory. Additionally, your 
  *   iterator implementation must support operations next() and hasNext() in
  *   constant worst-case time; and construction in linear time; you may (and 
  *   will need to) use a linear amount of extra memory per iterator.
  * 
  *-------------------------------------------------------------------------
  * public RandomizedQueue()                 
  * // construct an empty randomized queue
  * 
  * public boolean isEmpty()                 
  * // is the queue empty?
  * 
  * public int size()                        
  * // return the number of items on the queue
  * 
  * public void enqueue(Item item)           
  * // add the item
  * 
  * public Item dequeue()
  * // remove and return a random item
  * 
  * public Item sample()                     
  * // return (but do not remove) a random item
  * 
  * public Iterator<Item> iterator()         
  * // return an independent iterator over items in random order
  * 
  * public static void main(String[] args)   
  * // unit testing (optional)
  **************************************************************************/
import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

/**
 * A randomized queue is similar to a stack or queue, except that the item 
 * removed is chosen uniformly at random from items in the data structure 
 */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] itemsValue;
    private int size;
    
    /**
     * construct an empty randomized queue 
     */
    public RandomizedQueue() {
        size = 0;
        updateItemsValue(1);
    }
    
    /**
     * is the queue empty? 
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * return the number of items on the queue 
     */
    public int size() {
        return size;
    }
    
    /**
     * add the item 
     */
    public void enqueue(Item item) {
        if (item == null) {
            throw new NullPointerException("item is null");
        }
        if (size == itemsValue.length) {
            updateItemsValue(2*itemsValue.length);
        }
        itemsValue[size++] = item;
    }
    
    /**
     * remove and return a random item 
     */
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("there is no more items");
        }
        if (size > 0 && size == itemsValue.length/4) {
            updateItemsValue(itemsValue.length/2);
        }
        int random = StdRandom.uniform(size);
        Item item = itemsValue[random];
        itemsValue[random] = itemsValue[--size];
        itemsValue[size] = null;
        return item;
    }
    
    /**
     * return (but do not remove) a random item 
     */ 
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("there is no more items");
        }
        return itemsValue[StdRandom.uniform(size)];
    }
    
    /**
     * return an independent iterator over items in random order 
     */
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }
    private class RandomizedQueueIterator implements Iterator<Item> {
        private int[] random;
        private int current;
         
        public RandomizedQueueIterator() {
            random = new int[size];
            for (int i = 0; i < size; i++) random[i] = i;
            StdRandom.shuffle(random);
            current = 0;
        }
        public boolean hasNext() {
            return current != random.length;
        }
        public void remove() {
            throw new UnsupportedOperationException("remove operation unsupported");
        }
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("no more items");
            }
            return itemsValue[random[current++]];
        }
    }
    
    /**
     * change array size 
     */
    private void updateItemsValue(int capacity) {
        assert capacity >= size;
        
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i++) {
            temp[i] = itemsValue[i];
        }
        for (int i = size; i < capacity; i++) {
            temp[i] = null;
        }
        itemsValue = temp;
    }
    
    /** 
     * unit testing(optional)
     */
    public static void main(String[] args) {
        RandomizedQueue<Integer> randomque = new RandomizedQueue<Integer>();
        for (int i = 0; i < 10; i++) randomque.enqueue(i);
        Iterator<Integer> testiterator = randomque.iterator();
        while (testiterator.hasNext()) StdOut.print(testiterator.next() + " ");
        StdOut.println("random queue size: " + randomque.size());
        
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) randomque.dequeue();
            testiterator = randomque.iterator();
            while (testiterator.hasNext()) StdOut.print(testiterator.next() + " ");
            StdOut.println("random queue size: " + randomque.size());
            
        }
    }
}