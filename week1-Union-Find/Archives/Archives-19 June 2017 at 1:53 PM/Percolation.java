/*************************************************************************************
  * Compilation: javac-algs4 Percolation.java
  * Execution:   java-algs4 Persolation
  * Dependency:  None
  * @author  Kevin James  05202017
  * @Email   kevinsocial@outlook.com
  * 
  * 
  *  This program is a application of WeightedQuickUnionUF algorithm.
  *  Write a program to estimate the value of the percolation threshold via 
  *  Monte Carlo simulation.
  *  
  *  Percolation. Given a composite systems comprised of randomly distributed
  *  insulating and metallic materials: what fraction of the materials need to 
  *  be metallic so that the composite system is an electrical conductor? Given 
  *  a porous landscape with water on the surface (or oil below), under what 
  *  conditions will the water be able to drain through to the bottom (or the 
  *  oil to gush through to the surface)? Scientists have defined an abstract 
  *  process known as percolation to model such situations.
  *  
  *  The model. We model a percolation system using an n-by-n grid of sites. 
  *  Each site is either open or blocked. A full site is an open site that can
  *  be connected to an open site in the top row via a chain of neighboring (
  *  left, right, up, down) open sites. We say the system percolates if there
  *  is a full site in the bottom row. In other words, a system percolates if 
  *  we fill all open sites connected to the top row and that process fills 
  *  some open site on the bottom row. (For the insulating/metallic materials 
  *  example, the open sites correspond to metallic materials, so that a system
  *  that percolates has a metallic path from top to bottom, with full sites 
  *  conducting. For the porous substance example, the open sites correspond to
  *  empty space through which water might flow, so that a system that 
  *  percolates lets water fill open sites, flowing from top to bottom.)
  * 
  *  Corner cases.  By convention, the row and column indices are integers 
  *  between 1 and n, where (1, 1) is the upper-left site: Throw a 
  *  java.lang.IndexOutOfBoundsException if any argument to open(), isOpen(), 
  *  or isFull() is outside its prescribed range. The constructor should throw 
  *  a java.lang.IllegalArgumentException if n ≤ 0.
  * 
  *  Performance requirements.  The constructor should take time proportional 
  *  to n2; all methods should take constant time plus a constant number of 
  *  calls to the union–find methods union(), find(), connected(), and count().
  * 
  *   Percolation data type. To model a percolation system, create a data type 
  *   Percolation with the following API:
  * 
  *   public class Percolation
  * ------------------------------------------------------------------------------------
  *   void      open(int row, int col)  || open site (row, col) if it is not open already
  *   boolean isOpen(int row, int col)  || is site (row, col) open?
  *   boolean isFull(int row, int col)  || is site (row, col) full?
  *   int          numberOfOpenSites()  || number of open sites
  *   boolean             percolates()  || does the system percolate?  
  * ************************************************************************************/
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * To model a percolation system 
 */
public class Percolation {
    private byte[] status;        // is this site open?
    private int opencount;        // number of opensites
    private int n;                // size of grid 
    private boolean percolateValue;
    private WeightedQuickUnionUF uf; // mainly for percolated? 
    
    /**
     *  Create n-by-n grid, with all sites blocked 
     */
    public Percolation(int n) { 
        if (n <= 0) { 
            throw new IllegalArgumentException("n must be positive.");
        }
        this.n = n;
        opencount = 0;
        percolateValue = false;
        status = new byte[n*n];
        // opensite = new boolean[n*n];
        uf = new WeightedQuickUnionUF(n*n);
        
        for (int i = 0; i < n; i++) {
            /**
             * byte value: 0010 
             * 0 --> [not percolate] 
             * 0 --> [not open] 
             * 1 --> [connect to top] 
             * 0 --> [not connect to bottom]
             */
            status[i] = 2; 
        }
        for (int i = (n-1)*n; i < n*n; i++) {
            /**
             * byte value: 0001 
             * 0 --> [not percolate] 
             * 0 --> [not open] 
             * 0 --> [not connect to top] 
             * 1 --> [connect to bottom]
             */
            status[i] = 1;
        }
    }  
    
    /**
     * a scheme for uniquely mapping 2D coordinates to 1D coordinates.
     */
    private int xyTo1D(int row, int col) {
        if (row <= 0 || row > n) {
            throw new IndexOutOfBoundsException("row index i out of bounds");
        }
        if (col <= 0 || col > n) {
            throw new IndexOutOfBoundsException("col index i out of bounds");
        }
        /**
         * n*n grid, if  row = 1, col = 1
         * 2D (1, 1), 
         * then 1D (0) 
         */
        return (row - 1) * n + col -1;
    }
    
    /**
     * open site (row, col) if it is not open already
     * 
     * @ row
     * @ col
     */
    public void open(int row, int col) { 
        if (row <= 0 || row > n) {
            throw new IndexOutOfBoundsException("row index i out of bounds");
        }
        if (col <= 0 || col > n) {
            throw new IndexOutOfBoundsException("col index i out of bounds");
        }
        int sequence = xyTo1D(row, col);
        if (isOpen(row, col)) {
            return; // only open site which is blokced
        }
        if (row == 1) {
            status[sequence] = 6; // byte value: 0110
        }
        if (row == n) {
            status[sequence] = 5; // byte value: 0101
        }
        if (row != 1 && row != n) {
            status[sequence] = 4; // byte value: 0100
        }
        opencount++;
        
        byte valueUpRoot = 0;
        byte valueDownRoot = 0;
        byte valueLeftRoot = 0;
        byte valueRightRoot = 0;
        int  bitOperation = 0;
        if (row > 1) {
            if (isOpen(row-1, col)) {   // adjacent up site
                valueUpRoot = status[uf.find(xyTo1D(row-1, col))];
                uf.union(sequence, xyTo1D(row-1, col));
            }
        }
        if (row < n) {
            if (isOpen(row+1, col)) {   // adjacent down site
                valueDownRoot = status[uf.find(xyTo1D(row+1, col))];
                uf.union(sequence, xyTo1D(row+1, col));
            }
        }
        if (col > 1) {
            if (isOpen(row, col-1)) {   // adjacent left site
                valueLeftRoot = status[uf.find(xyTo1D(row, col-1))];
                uf.union(sequence, xyTo1D(row, col-1));
            }
        }
        if (col < n) {
            if (isOpen(row, col+1)) {   // adjacent right site
                valueRightRoot = status[uf.find(xyTo1D(row, col+1))];
                uf.union(sequence, xyTo1D(row, col+1));
            }
        }
        bitOperation = valueUpRoot      |
                       valueDownRoot    | 
                       valueLeftRoot    | 
                       valueRightRoot   | 
                       status[sequence] |
                       4; // byte value: 0100
        /**
         * byte value: 0111 
         * 0 --> [not percolate] 
         * 1 --> [open] 
         * 1 --> [connect to top] 
         * 1 --> [connect to bottom] 
         * if status of root of this site is open && connect to top && connect to bottom
         * n*n grid must be percolated. [not percolate] need revalue to [percoalte]
         */
        if ((byte) (bitOperation) == 7) {   
            status[uf.find(xyTo1D(row, col))] = 15; // byte value: 1111
            percolateValue = true; 
        }
        else {
            // update status of root of this site
            status[uf.find(xyTo1D(row, col))] = (byte) (bitOperation);
        }
    }
    
    /**
     * is site (row, col) open? 
     * 
     * @ row 
     * @ col
     * @ return : open or not
     */
    public boolean isOpen(int row, int col) { 
        if (row <= 0 || row > n) {
            throw new IndexOutOfBoundsException("row index i out of bounds");
        }
        if (col <= 0 || col > n) {
            throw new IndexOutOfBoundsException("col index i out of bounds");
        }
        // return opensite[xyTo1D(row, col)];
        return (status[xyTo1D(row, col)] & 4) == 4; // **1* or **0**
    }
    
    /**
     * is site (row, col) full? 
     * 
     * @
     * @
     * @
     */
    public boolean isFull(int row, int col) { 
        if (row <= 0 || row > n) {
            throw new IndexOutOfBoundsException("row index i out of bounds");
        }
        if (col <= 0 || col > n) {
            throw new IndexOutOfBoundsException("col index i out of bounds");
        }
        if (n == 1) { // for 1*1 grid, deal with it specially.
            return opencount == 1;
        }
        else {
            /**
             * byte value: *11*
             *  * --> [percolate?] not sure
             *  1 --> [open]
             *  1 --> [connect to top]
             *  * --> [connect to bottom?] not sure
             *  As long as status of root of this site is open && connect to bottom,
             *  then all sites relate to this root site must be full.
             */
            return status[uf.find(xyTo1D(row, col))] >= 6; // byte value: *11*
        }
    }
    
    /**
     * number of open sites
     * 
     * @return : number of open sites
     */
    public int numberOfOpenSites() { 
        return opencount;
    }
    
    /**
     * does the system percolate?
     * 
     * @return system percolate or not.
     */
    public boolean percolates() { 
        if (n == 1 && opencount == 1) {
            return true;
        }
        return percolateValue;
    }   
}
