/*************************************************************************
 *  Compilation:  javac-algs4 FastCollinearPoints.java
 *  Execution:    java-algs4 FastCollinearPoints input8.txt (or others)
 *  Dependencies: Point.java
 *  @author  Kevin James   06172017
 *  @Email   kevinsocial@outlook.com
 *
 *  A faster, sorting-based solution. Remarkably, it is possible to solve the 
 *  problem much faster than the brute-force solution described above. Given 
 *  a point p, the following method determines whether p participates in a set
 *  of 4 or more collinear points.
 *  --Think of p as the origin.
 *  --For each other point q, determine the slope it makes with p.
 *  --Sort the points according to the slopes they makes with p.
 *  --Check if any 3 (or more) adjacent points in the sorted order have equal 
 *    slopes with respect to p. If so, these points, together with p, are 
 *    collinear.
 * 
 *  Applying this method for each of the n points in turn yields an efficient 
 *  algorithm to the problem. The algorithm solves the problem because points 
 *  that have equal slopes with respect to p are collinear, and sorting brings 
 *  such points together. The algorithm is fast because the bottleneck operation 
 *  is sorting.
 * 
 *  The method segments() should include each maximal line segment containing 
 *  4 (or more) points exactly once. For example, if 5 points appear on a line 
 *  segment in the order p→q→r→s→t, then do not include the subsegments p→s or 
 *  q→t.
 *  
 *  Corner cases. Throw a java.lang.NullPointerException either the argument to 
 *  the constructor is null or if any point in the array is null. Throw a 
 *  java.lang.IllegalArgumentException if the argument to the constructor 
 *  contains a repeated point.
 *
 *  Performance requirement. The order of growth of the running time of your 
 *  program should be n2 log n in the worst case and it should use space 
 *  proportional to n plus the number of line segments returned. 
 *  FastCollinearPoints should work properly even if the input has 5 or more 
 *  collinear points.
 *  
 * --------------------------------------------------------------------------
 *  API
 *  public FastCollinearPoints(Point[] points)     
 *  // finds all line segments containing 4 or more points
 * 
 *  public int numberOfSegments()    // the number of line segments     
 *  
 *  public LineSegment[] segments()  // the line segments
 *  
 *  
 *
 *************************************************************************/

import java.util.*;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdDraw;

public class FastCollinearPoints {
    // ArraryList to store all lineSegments
    private List<LineSegment> alistSegment = new ArrayList<LineSegment>(); 
    
    /**
     * finds all line segments containing 4 or more points
     * 
     * @ para points : all distinct points in the plane
     * 
     */
    public FastCollinearPoints(Point[] points) {
        checkPoints(points); 
        Point[] points2 = points.clone(); // do not change points
        Arrays.sort(points2); // sort points2 to natural order, compareTo()
        
        // Point[] pointsCopy = points2.clone(); // copy of points to sort with slope order

        
        for (Point startPoint : points2) {
            // ArrayList to store point with the same slope to startPoint
            List<Point> pointsList = new ArrayList<Point>(); 
            double slope = 0;
            double preSlope = Double.NEGATIVE_INFINITY;
            Point[] pointsCopy = points2.clone();
            
            pointsList.add(startPoint);
            Merge.sort(pointsCopy, startPoint.slopeOrder());
            //Arrays.sort(pointsCopy, startPoint.slopeOrder()); 
            
            for (int i = 1; i < pointsCopy.length; i++) { // ignore i=0 first point: startPoint
                slope = startPoint.slopeTo(pointsCopy[i]);
                if (slope == preSlope) { 
                    pointsList.add(pointsCopy[i]);
                    preSlope = slope;
                }
                else if (pointsList.size() >= 4) { // 4 or more points
                    addIfNew(pointsList, startPoint);
                    pointsList.clear();  // init pointList
                    pointsList.add(startPoint);
                    pointsList.add(pointsCopy[i]);        
                }
                else {
                    pointsList.clear();
                    pointsList.add(startPoint);
                    pointsList.add(pointsCopy[i]);   
                }
                preSlope = slope;     
            }
            if ( pointsList.size() >= 4) {
                addIfNew(pointsList, startPoint);
            }
        }     
    }
    
    /**
     * the number of line segments 
     * 
     * @return alistSegment.size() : number of line segments 
     */
    public int numberOfSegments() {
        return alistSegment.size();
    }
    
    /**
     * the line segments
     * 
     * @return the line segments
     */
    public LineSegment[] segments() {
        return alistSegment.toArray(new LineSegment[alistSegment.size()]);
    }
    
    /**
     * segments() should include each maximal line segment containing 4 
     * (or more) points exactly once.
     * For example, if 5 points appear on a line segment in the order p→q→r→s→t,
     * then do not include the subsegments p→s or q→t.
     * add LineSegment only if line segment is maximal.
     * 
     * @para testPointsList : pointsList which contain points with same slope to startPoint
     * @para startPoint
     *
     */
    private void addIfNew(List<Point> testPointsList, Point startPoint) {
         /*
         Point[] arrayPointsList = testPointsList.toArray(new Point[testPointsList.size()]);
         Arrays.sort(arrayPointsList);
         if (arrayPointsList[0] == startPoint) {
             alistSegment.add(new LineSegment(startPoint, arrayPointsList[testPointsList.size()-1]));
         }
         */
        /** 
         *You don't need an array. Let's say you have 5 points in their natural
         * order a, b, c, d, e. When you have b as the anchor and sort the 
         * remaining points by slopeOrder, you want points with the same slope 
         * to appear in their natural order (i.e. ... a, c, d, e, ...). To avoid 
         * adding the subsegment (b, c, d, e), whenever you start seeing a new 
         * slope (i.e. at a), you just need to check if b is less than a in terms 
         * of natural order - if it is not, it means b is not the first point in
         * that segment, then you know you don't need to add it. 
         * statPoint is b, and we got pointsList containing "b a c d e", b is not
         * the first element in natural order(use compareTo()), then do nothing;
         * only when we got pointsList containg " b c d h f g", b is the first 
         * element in natural order, then sort the pointsList,"b c d f g h",so
         * add a LineSegment b->h;
         * 
         * ATTENTION: if not use for loop to find whether b is the first element
         * in natural order, you could use too much Collections.sort(), especially 
         * meet a pointsList n*1 grid(n is quite large), time test could be faild.
         * 
         * IMPROVEMENT: if we use a stable sort method to sort pointsCopy with 
         * slope order, startPoint.compateTo(tesPointsList.get(1)) will find
         * whether b is the first number in natural order.
         */
        /*
        for (int i = 0; i < testPointsList.size(); i++) {
            if (startPoint.compareTo(testPointsList.get(i)) > 0) {
                return;
            } 
        }
        Collections.sort(testPointsList);
        alistSegment.add(new LineSegment(startPoint, testPointsList.get(testPointsList.size()-1)));
        */
        if (startPoint.compareTo(testPointsList.get(1)) > 0) {
            return;
        }
        Collections.sort(testPointsList);
        alistSegment.add(new LineSegment(startPoint, testPointsList.get(testPointsList.size()-1)));
        
    }
    
    /**
     * Throw a java.lang.NullPointerException either the argument to the 
     * constructor is null or if any point in the array is null. Throw a 
     * java.lang.IllegalArgumentException if the argument to the constructor 
     * contains a repeated point. 
     * 
     * @para points
     * 
     */
    private void checkPoints(Point[] points) {
        int N = points.length;
        if (points == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < N; i++) {
            if (points[i] == null) {
                throw new NullPointerException();
            }
        }
        for (int i = 0; i < N-1; i++) {
            for (int j = i+1; j < N; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }
    
    private static class Merge {
        // This class should not be instantiated.
        private Merge() { }
        
        // stably merge a[lo .. mid] with a[mid+1 ..hi] using aux[lo .. hi]
        private static void merge(Object[] a, Object[] aux, Comparator comparator, int lo, int mid, int hi) {
            // precondition: a[lo .. mid] and a[mid+1 .. hi] are sorted subarrays
            assert isSorted(a, comparator, lo, mid);
            assert isSorted(a, comparator, mid+1, hi);
            
            // copy to aux[]
            for (int k = lo; k <= hi; k++) {
                aux[k] = a[k]; 
            }
            
            // merge back to a[]
            int i = lo, j = mid+1;
            for (int k = lo; k <= hi; k++) {
                if      (i > mid)              a[k] = aux[j++];
                else if (j > hi)               a[k] = aux[i++];
                else if (less(comparator, aux[j], aux[i])) a[k] = aux[j++];
                else                           a[k] = aux[i++];
            }
            
            // postcondition: a[lo .. hi] is sorted
            assert isSorted(a, comparator, lo, hi);
        }
        // mergesort a[lo..hi] using auxiliary array aux[lo..hi]
        private static void sort(Object[] a, Object[] aux, Comparator comparator, int lo, int hi) {
            if (hi <= lo) return;
            int mid = lo + (hi - lo) / 2;
            sort(a, aux, comparator, lo, mid);
            sort(a, aux, comparator, mid + 1, hi);
            merge(a, aux, comparator, lo, mid, hi);
        }
        
        /**
         * Rearranges the array in ascending order, using the natural order.
         * @param a the array to be sorted
         */
        public static void sort(Object[] a, Comparator comparator) {
            Object[] aux = new Object[a.length];
            sort(a, aux, comparator, 0, a.length-1);
            assert isSorted(a, comparator);
        }
        
        
        /***************************************************************************
          *  Helper sorting function.
          ***************************************************************************/
        
        // is v < w ?
        private static boolean less(Comparator c, Object v, Object w) {
            return c.compare(v, w) < 0;
        }
        
        /***************************************************************************
          *  Check if array is sorted - useful for debugging.
          ***************************************************************************/
        private static boolean isSorted(Object[] a, Comparator comparator) {
            return isSorted(a, comparator, 0, a.length - 1);
        }
        
        private static boolean isSorted(Object[] a, Comparator comparator, int lo, int hi) {
            for (int i = lo + 1; i <= hi; i++)
                if (less(comparator, a[i], a[i-1])) return false;
            return true;
        }        
    }
    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
             int y = in.readInt();
            points[i] = new Point(x, y);
        }
        
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}