/****************************************************************************
  *  Compilation: javac-algs4 Deque.java
  *  Execution:   java-algs4  Deque
  *  Dependency:  None
  *  @author  Kevin James 06182017
  *  @Email   kevinsocial@outlook.com
  * 
  *   A double-ended queue or deque (pronounced "deck") is a generalization of 
  *   a stack and a queue that supports adding and removing items from either 
  *   the front or the back of the data structure.
  * 
  *   Corner cases. Throw a java.lang.NullPointerException if the client 
  *   attempts to add a null item; throw a java.util.NoSuchElementException 
  *   if the client attempts to remove an item from an empty deque; throw a 
  *   java.lang.UnsupportedOperationException if the client calls the remove()
  *   method in the iterator; throw a java.util.NoSuchElementException if the
  *   client calls the next() method in the iterator and there are no more 
  *   items to return.
  * 
  *   Performance requirements.   Your deque implementation must support each 
  *   deque operation (including construction) in constant worst-case time. 
  *   A deque containing n items must use at most 48n + 192 bytes of memory 
  *   and use space proportional to the number of items currently in the deque.
  *   Additionally, your iterator implementation must support each operation 
  *   (including construction) in constant worst-case time.
  *   API  
  * -------------------------------------------------------------------------
  *  public Deque()                           
  * // construct an empty deque
  * 
  *  public boolean isEmpty()                 
  * // is the deque empty?
  * 
  *  public int size()               
  * // return the number of items on the deque
  * 
  *  public void addFirst(Item item)          
  * // add the item to the front
  * 
  *  public void addLast(Item item)           
  * // add the item to the end
  * 
  *  public Item removeFirst()      
  * // remove and return the item from the front
  * 
  *  public Item removeLast()       
  * // remove and return the item from the end
  * 
  *  public Iterator<Item> iterator()         
  * // return an iterator over items in order from front to end
  * 
  *  public static void main(String[] args)   // unit testing (optional)
  *************************************************************************/
import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdOut;

/**
 * a generalization of a stack and a queue that supports adding and removing 
 * items from either the front or the back of the data structure  
 */
public class Deque<Item> implements Iterable<Item> {

    private Node headsentinel;
    private int size;
    
    
    /**
     * a nested class to define data type of node.
     * a nestclass can have a constructor 
     */
    private class Node {
        private Item item;
        private Node next;
        private Node previous;
        
        public Node(Item item, Node previous, Node next) {
            this.item = item;
            this.next = next;
            this.previous = previous;
        }
    }
    
    /**
     * construct an empty deque 
     */
    public Deque() {
        headsentinel = new Node(null, null, null);
        headsentinel.previous = headsentinel;
        headsentinel.next = headsentinel;
        size = 0;
    }
    
    /**
     * is the deque empty? 
     */
    public boolean isEmpty() {
        return headsentinel.previous == headsentinel &&
               headsentinel.next == headsentinel;
    }
    
    /**
     * return the number of items on the deque 
     */
    public int size() {
        return size;
    }
    
    /**
     * add the item to the front 
     */
    public void addFirst(Item item) {
        if (item == null) {
            throw new NullPointerException("item is null");
        }
        
        if (isEmpty()) {
            Node newfirst = new Node(item, headsentinel, headsentinel);
            headsentinel.next = newfirst;
            headsentinel.previous = newfirst;
        }
        else {
            Node newfirst = new Node(item, headsentinel, headsentinel.next);
            headsentinel.next.previous = newfirst;
            headsentinel.next = newfirst;
        }
        size++;
    }
    
    /**
     * add the item to the front 
     */
    public void addLast(Item item) {
        if (item == null) throw new NullPointerException("item is null");
        
        if (isEmpty()) {
            Node newlast = new Node(item, headsentinel, headsentinel);
            headsentinel.previous = newlast;
            headsentinel.next = newlast;
        }
        else {
            Node newlast = new Node(item, headsentinel.previous, headsentinel);
            headsentinel.previous.next = newlast;
            headsentinel.previous = newlast;
        }
        size++;
    }
    
    /**
     * remove and return the item from the front 
     */
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("remove an item from an empty deque.");
        }
        Item item = headsentinel.next.item;
        if (headsentinel.next.next == headsentinel) {
            
            headsentinel.next = headsentinel;
            headsentinel.previous = headsentinel;
        }
        else {
            
            headsentinel.next.next.previous = headsentinel;
            headsentinel.next = headsentinel.next.next;
        }
        size--;       
        return item;
    }
    
    /**
     * remove and return the item from the end 
     * 
     */
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("remove an item from an empty deque.");
        }
        Item item = headsentinel.previous.item;
        if (headsentinel.next.next == headsentinel) {
            
            headsentinel.next = headsentinel;
            headsentinel.previous = headsentinel;
        }
        else {
            
            headsentinel.previous.previous.next = headsentinel;
            headsentinel.previous = headsentinel.previous.previous;
        }
        size--;       
        return item;       
    }
    
    /**
     * return an iterator over items in order from front to end 
     */
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }
    
    private class DequeIterator implements Iterator<Item> {
        private Node current = headsentinel.next;
        
        public boolean hasNext() {
            return current != headsentinel;
        }
        public void remove() {
            throw new UnsupportedOperationException("remove operation unsupported");
        }
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("there are no more items");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }
    
    
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        for (int i = 0; i < 10; i++) {
            deque.addFirst(i);
            deque.addLast(9-i);
        }
        
        Iterator<Integer> testIterator = deque.iterator();
        while (testIterator.hasNext()) {
            StdOut.print(testIterator.next() + " ");
        }
        StdOut.println("deque size: " + deque.size());
        
        for (int i = 0; i < 10; i++) {
            StdOut.print(deque.removeLast() + " ");
            StdOut.print(deque.removeFirst() + " ");
        }
        StdOut.println("size: " + deque.size());
        
    }      
}