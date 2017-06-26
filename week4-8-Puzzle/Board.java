/******************************************************************************
 *  Compilation:  javac-algs4 Board.java
 *  Execution:    java-algs4 Board puzzle04.txt (or others)
 *  Dependencies: none
 *  @author  Kevin James 06262017
 *  @Email   kevinsocial@outlook.com
 * 
 *  The problem. The 8-puzzle problem is a puzzle invented and popularized by 
 *  Noyes Palmer Chapman in the 1870s. It is played on a 3-by-3 grid with 8 
 *  square blocks labeled 1 through 8 and a blank square. Your goal is to 
 *  rearrange the blocks so that they are in order, using as few moves as
 *  possible. You are permitted to slide blocks horizontally or vertically 
 *  into the blank square.
 * 
 *  Organize your program by creating an immutable data type Board.
 * 
 *  Corner cases.  You may assume that the constructor receives an n-by-n array
 *  containing the n2 integers between 0 and n2 − 1, where 0 represents the 
 *  blank square.
 *  
 *  Performance requirements.  Your implementation should support all Board 
 *  methods in time proportional to n2 (or better) in the worst case.
 * ---------------------------------------------------------------------------
 * API:
 * public Board(int[][] blocks) // construct a board from an n-by-n array of blocks
 *                              // (where blocks[i][j] = block in row i, column j)
 * public int dimension()       // board dimension n
 * public int hamming()         // number of blocks out of place
 * public int manhattan()       // sum of Manhattan distances between blocks and goal
 * public boolean isGoal()      // is this board the goal board?
 * public Board twin()          // a board that is obtained by exchanging any pair of blocks
 * public boolean equals(Object y)        // does this board equal y?
 * public Iterable<Board> neighbors()     // all neighboring boards
 * public String toString()               // string representation of this board (in the output format specified below)
 * 
 * public static void main(String[] args) // unit tests (not graded)
 * ---------------------------------------------------------------------------
 *****************************************************************************/
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class Board {

    private int n;        // length of a n*n blocks;
    private char[] grid;  // 1D array store value of blocks[][], save much memory
    private List<Board> neighborsList = new ArrayList<Board>();
  
    public Board(int[][] blocks) {
        n = blocks.length;
        grid = new char[n*n];    // mot modify grid in any other function
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i*n+j] = (char) blocks[i][j];
            }
        }
        
    }
    /**
     * board dimension n
     */
    public int dimension() {
        return n;
    }
    
    /**
     *  number of blocks out of place
     */
    public int hamming() {
        int count = -1; // why -1? blank square: gird[i] = 0 
        for (int i = 0; i < grid.length; i++) {
            if (grid[i] != i+1) {
                count++;
            } 
        }
        return count;
    }
    
    /**
     * sum of Manhattan distances between blocks and goal 
     */
    public int manhattan() {
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            if (grid[i] == 0) {
                continue;  // do not count the blank square
            }
            if (grid[i] != i+1) {
                int value = grid[i];
                // sum of the vertical and horizontal distance
                count += Math.abs(i / n - (value-1) / n) + Math.abs(i % n - (value-1) % n);
            }
        }
        return count;   
    }
    
    
    /**
     * is this board the goal board? 
     */
    public boolean isGoal() {
        return hamming() == 0;
    }
    
    
    /**
     * a board that is obtained by exchanging any pair of blocks 
     */
    public Board twin() {
        int[][] blocksCopy = new int[n][n];
        for (int i = 0; i < n*n; i++) {
            blocksCopy[i / n][i % n] = grid[i];
        }
        while (true) {
            int block1Random = StdRandom.uniform(n*n);
            int block2Random = StdRandom.uniform(n*n);
            if (block1Random != block2Random) {
                if (grid[block1Random] != 0 && grid[block2Random] != 0) {
                    blocksCopy[block1Random / n][block1Random % n] = grid[block2Random];
                    blocksCopy[block2Random / n][block2Random % n] = grid[block1Random]; 
                    break;
                }
            }
        }
        return new Board(blocksCopy);
    }
    
    /**
     * does this board equal y? 
     */
    public boolean equals(Object y) {
        if (this == y) {
            return true;
        }
        if (y == null || getClass() != y.getClass()) {
            return false;
        }
        Board that = (Board) y;
        if (this.grid.length != that.grid.length) {
            return false;
        }
        for (int i = 0; i < n*n; i++) {
            if (grid[i] != that.grid[i]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * all neighboring boards
     */
    public Iterable<Board> neighbors() {
        // update ArrayList neighborsList only if this is first update it
        if (neighborsList.isEmpty()) { 
            findNeighbors(); 
        }
        return neighborsList;
    }
    /**
     * 1D array char[] to 2D array int[][] 
     */
    private int[][] to2DArray(char[] array1D) {
        int[][] array2D = new int[n][n];
        for (int i = 0; i < array1D.length; i++) {
            array2D[i / n][i % n] = (int) array1D[i];
        }
        return array2D;
    }
    
    /**
     * find all neighboring boards, 
     * 
     */
    private void findNeighbors() {
        int rowBlank; // row of blank square in 2D array int[][]
        int colBlank; // col of blank square in 2D array int[][]
        int indexOfBlank = 0;
        for (int i = 0; i < n*n; i++) { // find index of blank square in 1D array grid[]
            if (grid[i] == 0) {
                indexOfBlank = i;
                break;
            }
        }
        rowBlank = indexOfBlank / n;
        colBlank = indexOfBlank % n;
        // find the adjact square of blank square
        // pay atttention to corner cases
        if (rowBlank > 0) { // switch blank square with up squre
            int[][] blocksTemp = to2DArray(grid);
            blocksTemp[rowBlank][colBlank] = blocksTemp[rowBlank-1][colBlank];
            blocksTemp[rowBlank-1][colBlank] = 0;
            neighborsList.add(new Board(blocksTemp));
        }
        if (rowBlank < n-1) { // switch blank squrqe with down square
            int[][] blocksTemp = to2DArray(grid);
            blocksTemp[rowBlank][colBlank] = blocksTemp[rowBlank+1][colBlank];
            blocksTemp[rowBlank+1][colBlank] = 0;
            neighborsList.add(new Board(blocksTemp));
        }
        if (colBlank > 0) { // switch blank square with left square
            int[][] blocksTemp = to2DArray(grid);
            blocksTemp[rowBlank][colBlank] = blocksTemp[rowBlank][colBlank-1];
            blocksTemp[rowBlank][colBlank-1] = 0;
            neighborsList.add(new Board(blocksTemp));
        }
        if (colBlank < n-1) { // switch blank square with right squre
            int[][] blocksTemp = to2DArray(grid);
            blocksTemp[rowBlank][colBlank] = blocksTemp[rowBlank][colBlank+1];
            blocksTemp[rowBlank][colBlank+1] = 0;
            neighborsList.add(new Board(blocksTemp));
        }

    }
    
    /**
     * string representation of this board 
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");  // show dimension and enter a newline
        for (int i = 0; i < n*n; i++) {
            s.append(String.format("%4d ", (int) grid[i])); 
            if ((i+1) % n == 0) { // show board row by row
                s.append("\n");
            } 
            
        }
        return s.toString();
    }
    
    /**
     * unit tests (not graded)
     */
    public static void main(String[] args) {
        In in = new In(args[0]);
        int size = in.readInt();
        int[][] blocks = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                blocks[i][j] = in.readInt();
            }
        }
        Board initial = new Board(blocks);
        StdOut.println(initial);
        StdOut.println("board initial's dimension is: " + initial.dimension());
        StdOut.println("number of blocks out of place in board initial: " + initial.hamming());
        StdOut.println("manhattan distances: " + initial.manhattan());
        StdOut.println("is board initial the goal board: " + initial.isGoal() + "\n");
        StdOut.println("neighbors of board initial: ");
        for (Board neighbor : initial.neighbors()) {
            StdOut.println(neighbor);
        }
        StdOut.println("\n" + "three twin board of initial: ");
        for (int i = 0; i < 3; i++) {
            StdOut.println(initial.twin());
        }
    }
    
}