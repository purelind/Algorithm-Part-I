import java.util.*;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class FastCollinearPoints {
    private List<LineSegment> alistSegment = new ArrayList<LineSegment>();
    
    public FastCollinearPoints(Point[] points) {
        checkPoints(points);
        Point[] points2 = points.clone();
        Arrays.sort(points2);
        
        Point[] pointsCopy = points.clone();

        
        for (Point startPoint : points2) {
            List<Point> pointsList = new ArrayList<Point>();
            double slope = 0;
            double preSlope = Double.NEGATIVE_INFINITY;
            //double testslope = Double.NEGATIVE_INFINITY;  
            
            pointsList.add(startPoint);
            Arrays.sort(pointsCopy, startPoint.slopeOrder());
            
            for (int i = 1; i < pointsCopy.length; i++) {
                /*if (startPoint.compareTo(pointsCopy[i]) > 0) {
                    testslope = startPoint.slopeTo(pointsCopy[i]);
                    preSlope = testslope;
                    pointsList.clear();
                    pointsList.add(startPoint);
                    continue;
                }*/
                slope = startPoint.slopeTo(pointsCopy[i]);
                /*if (slope == testslope) {
                    preSlope = testslope;
                    pointsList.clear();
                    pointsList.add(startPoint);                    
                    continue;}*/
                if (slope == preSlope) {
                    pointsList.add(pointsCopy[i]);
                    preSlope = slope;
                }
                else if (pointsList.size() >= 4) {
                    /*
                    Point[] arrayPointsList = pointsList.toArray(new Point[pointsList.size()]);
                    Arrays.sort(arrayPointsList);
                    if (arrayPointsList[0] == startPoint) {
                        alistSegment.add(new LineSegment(startPoint, arrayPointsList[pointsList.size()-1]));
                    //addIfNew(pointsList);
                    }
                    */
                    addIfNew(pointsList, startPoint);
                    pointsList.clear();
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
                /*Point[] arrayPointsList = pointsList.toArray(new Point[pointsList.size()]);
                Arrays.sort(arrayPointsList);
                //addIfNew(pointsList);
                if (arrayPointsList[0] == startPoint) {
                    alistSegment.add(new LineSegment(startPoint, arrayPointsList[pointsList.size()-1]));
                }*/
               // pointsList.clear();
                //preSlope = Double.NEGATIVE_INFINITY;
            }
        }     
    }
    
    private void addIfNew(List<Point> testPointsList, Point startPoint) {
         /*
         Point[] arrayPointsList = testPointsList.toArray(new Point[testPointsList.size()]);
         Arrays.sort(arrayPointsList);
         if (arrayPointsList[0] == startPoint) {
             alistSegment.add(new LineSegment(startPoint, arrayPointsList[testPointsList.size()-1]));
         }
         */
        for (int i = 0; i < testPointsList.size(); i++) {
            if (startPoint.compareTo(testPointsList.get(i)) > 0) {
                return;
            } 
        }
        Collections.sort(testPointsList);
        alistSegment.add(new LineSegment(startPoint, testPointsList.get(testPointsList.size()-1)));
        /*Collections.sort(testPointsList);
        if (testPointsList.get(0) == startPoint) {
            alistSegment.add(new LineSegment(startPoint, testPointsList.get(testPointsList.size()-1)));
        }*/
    }
    
    private void checkPoints(Point[] points) {
        /*int N = points.length;
        if (points == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < N; i++) {
            if (points[i] == null) {
                throw new NullPointerException();
            }
        }*/
        int N = points.length;
        for (int i = 0; i < N-1; i++) {
            for (int j = i+1; j < N; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
    }
    
    public int numberOfSegments() {
        return alistSegment.size();
    }
    
    public LineSegment[] segments() {
        return alistSegment.toArray(new LineSegment[alistSegment.size()]);
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