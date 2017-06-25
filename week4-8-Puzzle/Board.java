import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private char[] grid;
    private List<Board> neighborsList = new ArrayList<Board>();
    private int n;
    
    public Board(int[][] blocks) {
        n = blocks.length;
        // int n = blocks.length
        grid = new char[n*n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i*n+j] = (char) blocks[i][j];
            }
        }
        
    }
    
    public int dimension() {
        return n;
    }
    
    public int hamming() {
        int count = -1;
        for (int i = 0; i < grid.length; i++) {
            if (grid[i] != i+1) {
                count++;
            } 
        }
        return count;
    }
    
    public int manhattan() {
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            if (grid[i] == 0) {
                continue;
            }
            if (grid[i] != i+1) {
                int value = grid[i];
                count += Math.abs(i / n - (value-1) / n) + Math.abs(i % n - (value-1) % n);
            }
        }
        return count;   
    }
    
    public boolean isGoal() {
        return hamming() == 0;
    }
    
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
    public Iterable<Board> neighbors() {
        if (neighborsList.isEmpty()) {
            findNeighbors();
        }
        return neighborsList;
    }
    /* private class NeighborsIterator implements Iterator<Board> {
        private int index = 0;
        
        public boolean hasNext() {
            return index < neighbors.length;
        }
        public void remove() {
            throw new UnsupportedOperationException("remove operation unsupported");
        }
        public Board next() {
            if (!hasNext()) {
                throw new NoSuchElementException("there are no more Boards");
            }
            return neighbors[index++]; 
        }
    }*/
    /* private int[] findPositionOfBlank() {
        int indexOfBlank = 0;
        int[] positionOfBlank = new int[2];
        for (int i = 0; i < n*n; i++) {
            if (grid[i] == 0) {
                indexOfBlank = i;
                break;
            }
        }
        positionOfBlank[0] = indexOfBlank / n;
        positionOfBlank[1] = indexOfBlank % n;
        return positionOfBlank;
    }*/
    private int[][] to2DArray(char[] array1D) {
        int[][] array2D = new int[n][n];
        for (int i = 0; i < array1D.length; i++) {
            array2D[i / n][i % n] = (int) array1D[i];
        }
        return array2D;
    }
    private void findNeighbors() {
        int rowBlank;
        int colBlank;
        int indexOfBlank = 0;
        for (int i = 0; i < n*n; i++) {
            if (grid[i] == 0) {
                indexOfBlank = i;
                break;
            }
        }
        rowBlank = indexOfBlank / n;
        colBlank = indexOfBlank % n;
        
        if (rowBlank > 0) {
            int[][] blocksTemp = to2DArray(grid);
            blocksTemp[rowBlank][colBlank] = blocksTemp[rowBlank-1][colBlank];
            blocksTemp[rowBlank-1][colBlank] = 0;
            neighborsList.add(new Board(blocksTemp));
        }
        if (rowBlank < n-1) {
            int[][] blocksTemp = to2DArray(grid);
            blocksTemp[rowBlank][colBlank] = blocksTemp[rowBlank+1][colBlank];
            blocksTemp[rowBlank+1][colBlank] = 0;
            neighborsList.add(new Board(blocksTemp));
        }
        if (colBlank > 0) {
            int[][] blocksTemp = to2DArray(grid);
            blocksTemp[rowBlank][colBlank] = blocksTemp[rowBlank][colBlank-1];
            blocksTemp[rowBlank][colBlank-1] = 0;
            neighborsList.add(new Board(blocksTemp));
        }
        if (colBlank < n-1) {
            int[][] blocksTemp = to2DArray(grid);
            blocksTemp[rowBlank][colBlank] = blocksTemp[rowBlank][colBlank+1];
            blocksTemp[rowBlank][colBlank+1] = 0;
            neighborsList.add(new Board(blocksTemp));
        }

    }
    
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n*n; i++) {
            s.append(String.format("%4d ", (int) grid[i]));
            if ((i+1) % n == 0) {
                s.append("\n");
            } 
            
        }
        return s.toString();
    }
    
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

        // StdOut.println(initial.dimension());
        StdOut.println(initial.hamming());
        StdOut.println(initial.manhattan());
        StdOut.println(initial.isGoal());
        // StdOut.println(initial.neighbors());
        StdOut.println(initial);
        for (Board neighbor : initial.neighbors()) {
            StdOut.println(neighbor);
        }
        StdOut.println(1/3);
        // StdOut.println(initial.twin());
    }
    
}