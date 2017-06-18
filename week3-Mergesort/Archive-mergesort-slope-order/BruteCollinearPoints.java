/*************************************************************************
 *  Compilation:  javac-algs4 BruteCollinearPoints.java
 *  Execution:    java-algs4 BruteCollinearPoints input8.txt(or others)
 *  Dependencies: Point.java  LineSegment.java
 *  @author   Kevin James  06172017
 *  @Email    kevinsocial@outlook.com
 * 
 *  Write a program BruteCollinearPoints.java that examines 4 points at a 
 *  time and checks whether they all lie on the same line segment, returning 
 *  all such line segments. To check whether the 4 points p, q, r, and s are 
 *  collinear, check whether the three slopes between p and q, between p and r,
 *  and between p and s are all equal.
 * 
 *  The method segments() should include each line segment containing 4 points
 *  exactly once. If 4 points appear on a line segment in the order p→q→r→s, 
 *  then you should include either the line segment p→s or s→p (but not both)
 *  and you should not include subsegments such as p→r or q→r. For simplicity,
 *  we will not supply any input to BruteCollinearPoints that has 5 or more 
 *  collinear points.
 * 
 *  Corner cases. Throw a java.lang.NullPointerException either the argument to
 *  the constructor is null or if any point in the array is null. Throw a 
 *  java.lang.IllegalArgumentException if the argument to the constructor 
 *  contains a repeated point.
 * 
 *  Performance requirement. The order of growth of the running time of your
 *  program should be n4 in the worst case and it should use space proportional 
 *  to n plus the number of line segments returned.
 *  
 *-------------------------------------------------------------------------
 *   API
 *   public BruteCollinearPoints(Point[] points)
 *   // finds all line segments containing 4 points   
 * 
 *   public int numberOfSegments() // the number of line segments
 * 
 *   public LineSegment[] segments() // the line segments
 *-------------------------------------------------------------------------
 *************************************************************************/
import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

/**
 *examines 4 points at a time and checks whether they all lie on the same 
 *line segment, returning all such line segments. 
 * 
 */
public class BruteCollinearPoints {
    // ArraryList to store all lineSegments
    private LineSegment[] arrayLineSegment;

    /**
     * finds all line segments containing 4 or more points
     * 
     * @ para points : all distinct points in the plane
     * 
     */
    public BruteCollinearPoints(Point[] points) {
        checkPoints(points);
        ArrayList<LineSegment> alistLineSegment = new ArrayList<LineSegment>();
        Point[] pointsCopy = points.clone(); // do not change points
        Point firstPoint;
        int n = pointsCopy.length;
        
        Arrays.sort(pointsCopy); // sort pointsCopy to natural order, compareTo()
        
        for (int i = 0; i < n; i++) {
            for (int j = i+1; j < n; j++) {
                for (int k = j+1; k < n; k++) { 
                   /* don't need to consider whether 4 points are collinear if 
                    you already know that the first 3 are not collinear;*/
                    if (pointsCopy[i].slopeOrder().compare(pointsCopy[j], pointsCopy[k]) != 0) {
                        continue; 
                    }
                    for (int m = k+1; m < n; m++) {                   
                        firstPoint = pointsCopy[i];
                        if (firstPoint.slopeOrder().compare(pointsCopy[j], pointsCopy[k]) == 0 &&
                            firstPoint.slopeOrder().compare(pointsCopy[j], pointsCopy[m]) == 0) {
                            
                            alistLineSegment.add(new LineSegment(pointsCopy[i], pointsCopy[m]));
                        }
                    }
                }
            }
        }
        arrayLineSegment = alistLineSegment.toArray(new LineSegment[alistLineSegment.size()]);
                        
    }
    
    /**
     * Corner cases. Throw Exception
     * Throw a java.lang.NullPointerException either the argument to the
     * constructor is null or if any point in the array is null. Throw a 
     * java.lang.IllegalArgumentException if the argument to the constructor 
     * contains a repeated point.
     * 
     * @ para points: points on the plane need to be checked.
     */
    private void checkPoints(Point[] points) { 
        if (points == null) {
            throw new NullPointerException();
        }
        int n = points.length;
        for (int i = 0; i < n; i++) {
            if (points[i] == null) {
                throw new NullPointerException();
            }
        }
        for (int i = 0; i < n-1; i++) {
            for (int j = i+1; j < n; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }
    
    /**
     * the number of line segments 
     */
    public int numberOfSegments() {

        return arrayLineSegment.length;
    }
    
    /**
     * the line segments 
     * 
     * @return : array that store all the line segments
     */
    public LineSegment[] segments() {
        LineSegment[] lineSegCopy = arrayLineSegment.clone();
        return lineSegCopy;
    }
    
    /**
     * Sample client. This client program takes the name of an input file as a 
     * command-line argument; read the input file (in the format specified 
     * below); prints to standard output the line segments that your program 
     * discovers, one per line; and draws to standard draw the line segments. 
     * 
     */
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
