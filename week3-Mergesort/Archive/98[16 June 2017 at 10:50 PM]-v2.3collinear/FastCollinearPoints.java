import java.util.*;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class FastCollinearPoints {
   private List<LineSegment> alistSegment = new ArrayList<LineSegment>();
    
    public FastCollinearPoints(Point[] points) {
        
        
        Arrays.sort(points);
        
        Point[] pointsCopy = points.clone();

        
        for (Point startPoint : points) {
            List<Point> pointsList = new ArrayList<Point>();
            pointsList.add(startPoint);
            Arrays.sort(pointsCopy, startPoint.slopeOrder());
            
            double slope = 0;
            double preSlope = Double.NEGATIVE_INFINITY;
            for (int i = 1; i < pointsCopy.length; i++) {
                slope = startPoint.slopeTo(pointsCopy[i]);
                if (slope == preSlope) {
                    pointsList.add(pointsCopy[i]);
                    preSlope = slope;
                }
                else if (pointsList.size() >= 4) {
                    Point[] arrayPointsList = pointsList.toArray(new Point[pointsList.size()]);
                    Arrays.sort(arrayPointsList);
                    if (arrayPointsList[0] == startPoint) {
                        alistSegment.add(new LineSegment(startPoint, arrayPointsList[pointsList.size()-1]));
                    //addIfNew(pointsList);
                    }
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
                Point[] arrayPointsList = pointsList.toArray(new Point[pointsList.size()]);
                Arrays.sort(arrayPointsList);
                //addIfNew(pointsList);
                if (arrayPointsList[0] == startPoint) {
                    alistSegment.add(new LineSegment(startPoint, arrayPointsList[pointsList.size()-1]));
                }
               // pointsList.clear();
                //preSlope = Double.NEGATIVE_INFINITY;
            }
            

        }
        
    }
    /*
    private void addIfNew(List<Point> pointsList) {
        Point[] arrayPoints = pointsList.toArray(new Point[pointsList.size()]);
        Point[] arrayPointsCopy = arrayPints.clone();
        Arrays.sort(arrayPointsCopy);
        if (arrayPointsCopy == arrayPoints) {
            alistSegment.add(new LineSegment(pointsList.get[0], pointsList.get[-1]));
        }
        else {
            return;
        }
        
    }*/

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