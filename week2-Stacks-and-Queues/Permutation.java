/****************************************************************************
  *  Compilation:   javac-alsg4 Permutation.java
  *  Execution:     java-algs4  Permutaion 3 < distinct.txt
  *  Dependencies:  RandomizedQueue.java
  *  @author  Kevin James 05282017
  *  @Email   kevinsocial@outlook.com
  * 
  *   Takes a command-line integer k; reads in a sequence of strings from 
  *   standard input using StdIn.readString(); and prints exactly k of them, 
  *   uniformly at random. Print each item from the sequence at most once. You 
  *   may assume that 0 ≤ k ≤ n, where n is the number of string on standard 
  *   input.
  *    
  *   You may assume that 0 ≤ k ≤ n, where n is the number of string on
  *   standard input.
  * 
  *   Performance requirements. The running time of Permutation must be linear 
  *   in the size of the input. You may use only a constant amount of memory 
  *   plus either one Deque or RandomizedQueue object of maximum size at most n.
  *   (For an extra challenge, use only one Deque or RandomizedQueue object of 
  *   maximum size at most k.)
  **************************************************************************/
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> randomque = new RandomizedQueue<String>();
        String[] str = StdIn.readAllStrings();
        /**
         *(For an extra challenge, use only one Deque or RandomizedQueue 
         * object of maximum size at most k. To achieve bonous, shffle the str 
         * string, then randomque just enqueue k items, the maximum size of 
         * RandomizedQueue object created is equal to k). 
         * Total: 3/2 tests passed!
         */
        StdRandom.shuffle(str); // randomsize str string
        for (int i = 0; i < k; i++) {
            randomque.enqueue(str[i]);
        }
        for (int i = 0; i < k; i++) {
            StdOut.println(randomque.dequeue());
        }
    }
}