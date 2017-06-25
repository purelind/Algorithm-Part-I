import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Solver {
    private List<Board> solutionBoards = new ArrayList<Board>();
    private boolean solvability;
    
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("pass a null initial board");
        }
        MinPQ<SearchNode> searchNodePQ = new MinPQ<SearchNode>();
        MinPQ<SearchNode> searchNodeTwPQ = new MinPQ<SearchNode>();
        searchNodePQ.insert(new SearchNode(initial, 0, null));
        searchNodeTwPQ.insert(new SearchNode(initial.twin(), 0, null));
        
        
        while (!searchNodePQ.min().getBoard().isGoal() && !searchNodeTwPQ.min().getBoard().isGoal()) {
            SearchNode tempNode = searchNodePQ.delMin();
            // solutionBoards.add(tempNode.getBoard());
            for (Board neighbor : tempNode.getBoard().neighbors()) {
                if (isValid(tempNode, neighbor)) {
                    searchNodePQ.insert(new SearchNode(neighbor, tempNode.getNumOfMoves()+1, tempNode));
                }
            }
            
            SearchNode tempNodeTw = searchNodeTwPQ.delMin();
            for (Board neighbor : tempNodeTw.getBoard().neighbors()) {
                if (isValid(tempNodeTw, neighbor)) {
                    searchNodeTwPQ.insert(new SearchNode(neighbor, tempNodeTw.getNumOfMoves()+1, tempNodeTw));
                }
            }
            
        }
        SearchNode stepSearchNode = searchNodePQ.delMin();
        solvability = stepSearchNode.getBoard().isGoal();
        
        solutionBoards.add(stepSearchNode.getBoard());
        // while ((stepSearchNode = stepSearchNode.getPrevSearchNode()) != null) {
        while (true) {
            stepSearchNode = stepSearchNode.getPrevSearchNode();
            if (stepSearchNode == null) {
                break;
            }
            solutionBoards.add(0, stepSearchNode.getBoard());
        }
        
    }
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
    public boolean isSolvable() {
        return solvability;
    }
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

