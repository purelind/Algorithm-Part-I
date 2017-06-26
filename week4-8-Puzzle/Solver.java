/******************************************************************************
 *  Compilation:  javac-algs4 Board.java
 *  Execution:    java-algs4 Board puzzle04.txt (or others)
 *  Dependencies: none
 *  @author  Kevin James 06262017
 *  @Email   kevinsocial@outlook.com
 * 
 *  To implement the A* algorithm, you must use MinPQ from algs4.jar for the
 *  priority queue(s).
 * 
 *  Corner cases.  The constructor should throw a java.lang.IllegalArgumentException 
 *  if passed a null argument.
 *-----------------------------------------------------------------------------
 * API:
 * public Solver(Board initial)  // find a solution to the initial board (using the A* algorithm)
 * public boolean isSolvable()   // is the initial board solvable?
 * public int moves()            // min number of moves to solve initial board; -1 if unsolvable
 * public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
 * public static void main(String[] args) // solve a slider puzzle (given below)
 * ***************************************************************************/
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Solver {
    private List<Board> solutionBoards = new ArrayList<Board>(); // all boards from initial to the goal
    private boolean solvability; // is the board solvable?
    
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("pass a null initial board");
        }
        /* boards are divided into two equivalence classes with respect to reachability: 
         * (i) those that lead to the goal board and 
         * (ii) those that lead to the goal board if we modify the initial board 
         *  by swapping any pair of blocks (the blank square is not a block) 
         */
        MinPQ<SearchNode> searchNodePQ = new MinPQ<SearchNode>();
        MinPQ<SearchNode> searchNodeTwPQ = new MinPQ<SearchNode>();
        searchNodePQ.insert(new SearchNode(initial, 0, null));
        searchNodeTwPQ.insert(new SearchNode(initial.twin(), 0, null));
        
        // run the A* algorithm on two puzzle instances—one with the initial board 
        // and one with the initial board modified by swapping a pair of blocks—in 
        // lockstep (alternating back and forth between exploring search nodes in 
        // each of the two game trees). Exactly one of the two will lead to the goal board.
        while (!searchNodePQ.min().getBoard().isGoal() && !searchNodeTwPQ.min().getBoard().isGoal()) {
            SearchNode tempNode = searchNodePQ.delMin();
            for (Board neighbor : tempNode.getBoard().neighbors()) {
                // don't enqueue a neighbor if its board is the same as the board of the previous search
                if (isValid(tempNode, neighbor)) {
                    searchNodePQ.insert(new SearchNode(neighbor, tempNode.getNumOfMoves()+1, tempNode));
                }
            }
            
            SearchNode tempNodeTw = searchNodeTwPQ.delMin();
            for (Board neighbor : tempNodeTw.getBoard().neighbors()) {
                // don't enqueue a neighbor if its board is the same as the board of the previous search
                if (isValid(tempNodeTw, neighbor)) {
                    searchNodeTwPQ.insert(new SearchNode(neighbor, tempNodeTw.getNumOfMoves()+1, tempNodeTw));
                }
            }
             
        }
        SearchNode stepSearchNode = searchNodePQ.delMin(); // the last searchNode wo got, may be isGoal or not
        solvability = stepSearchNode.getBoard().isGoal();
        
        solutionBoards.add(stepSearchNode.getBoard()); // add boards to list in reverse order
        // while ((stepSearchNode = stepSearchNode.getPrevSearchNode()) != null) {
        while (true) {
            stepSearchNode = stepSearchNode.getPrevSearchNode();
            if (stepSearchNode == null) {
                break;
            }
            solutionBoards.add(0, stepSearchNode.getBoard());
        }
        
    }
    
    /**
     *  the board of a neighbors is the same as the board of the previous search node?
     *  if yes: don't enqueue a neighbor if its board
     */
    private boolean isValid(SearchNode lastNode, Board neighbor) {
        SearchNode prevSearchNode = lastNode;
        // while ((prevSearchNode = prevSearchNode.getPrevSearchNode()) != null)
        while (true) {
            prevSearchNode = prevSearchNode.getPrevSearchNode();
            if (prevSearchNode == null) {
                break;
            }
            if (prevSearchNode.getBoard().equals(neighbor)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * define a search node of the game to be a board, the number of moves made 
     * to reach the board, and the previous search node.
     */
    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int numOfMoves;
        private SearchNode prevSearchNode;
        private int numOfManhattan;
        
        public SearchNode(Board board, int numOfMoves, SearchNode prevSearchNode) {
            this.board = board;
            this.numOfMoves = numOfMoves;
            this.prevSearchNode = prevSearchNode;
            this.numOfManhattan = board.manhattan();
        }
        public Board getBoard() {
            return board;
        }
        public int getNumOfMoves() {
            return numOfMoves;
        }
        public SearchNode getPrevSearchNode() {
            return prevSearchNode;
        }
        public int getNumOfManhattan() {
            return numOfManhattan;
        }
        public int getPriority() {
            return getNumOfManhattan() + numOfMoves;
        }
        public int compareTo(SearchNode that) {
            if (this.getPriority() > that.getPriority()) {
                return +1;
            }
            if (this.getPriority() < that.getPriority()) {
                return -1;
            }
            return 0;
        }
        
        
    }
    
    /**
     * is the initial board solvable?
     */
    public boolean isSolvable() {
        return solvability;
    }
    
    /**
     * min number of moves to solve initial board; -1 if unsolvable
     */
    public int moves() {
        int numOfMoves;
        if (isSolvable()) {
            numOfMoves = solutionBoards.size() - 1;
        }
        else {
            numOfMoves = -1;
        }
        return numOfMoves;
    }
    
    /**
     *  sequence of boards in a shortest solution; null if unsolvable
     */
    public Iterable<Board> solution() {
        Iterable<Board> iterable;
        if (isSolvable()) {
            iterable = new Iterable<Board>() {
                public Iterator<Board> iterator() {
                    return new SolutionIterator();
                }
            };
        }
        else {
            iterable = null;
        }
        return iterable;
    }
    private class SolutionIterator implements Iterator<Board> {
        private int index = 0;
        public boolean hasNext() {
            return index < solutionBoards.size();
        }
        public Board next() {
            if (!hasNext()) {
                throw new NoSuchElementException("there are no more Boards");
            }
            return solutionBoards.get(index++);
        }
        public void remove() {
            throw new UnsupportedOperationException("remove a board unsupported");
        }
    }
    
    /**
     * solve a slider puzzle (given below)
     */
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
            blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        
        // solve the puzzle
        Solver solver = new Solver(initial);
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
   
}

