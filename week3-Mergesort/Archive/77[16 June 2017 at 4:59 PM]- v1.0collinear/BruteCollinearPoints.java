/**
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
import java.util.Arrays;
import java.util.ArrayList;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class BruteCollinearPoints {
    private LineSegment[] arrayLineSegment;

    public BruteCollinearPoints(Point[] points) {
        checkPoints(points);
        ArrayList<LineSegment> alistLineSegment = new ArrayList<LineSegment>();
        Point[] pointsCopy = points.clone();
        Point firstPoint;
        int N = pointsCopy.length;
        
        Arrays.sort(pointsCopy);
        
        for (int i = 0; i < N; i++) {
            for (int j = i+1; j < N; j++) {
                for (int k = j+1; k < N; k++) {
                    for (int m = k+1; m < N; m++) {                   
                        firstPoint = pointsCopy[i];
                        if (firstPoint.slopeTo(pointsCopy[j]) == firstPoint.slopeTo(pointsCopy[k]) &&
                            firstPoint.slopeTo(pointsCopy[j]) == firstPoint.slopeTo(pointsCopy[m]))
                        {
                            alistLineSegment.add(new LineSegment(pointsCopy[i], pointsCopy[m]));
                        }
                    }
                }
            }
        }
        arrayLineSegment = alistLineSegment.toArray(new LineSegment[alistLineSegment.size()]);
                        
    }
    
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
    
    public int numberOfSegments() {

        return arrayLineSegment.length;
    }
    
    public LineSegment[] segments() {
        LineSegment[] lineSegCopy = arrayLineSegment.clone();
        return lineSegCopy;
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
