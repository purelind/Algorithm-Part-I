import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.In;

import java.util.List;
import java.util.ArrayList;

public class PointSET {
    private final SET<Point2D> set = new SET<Point2D>();
    private int size;

    
    public PointSET() {
        size = 0;
        // Iterator<Point2D> setIter = set.iterator();
        
    }
    public boolean isEmpty() {
        return size == 0;
    }
    public int size() {
        return size;
    }
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("");
        }
        if (!set.contains(p)) {
            set.add(p);
            size++;
        }
    }
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("");
        }        
        return set.contains(p);
    }
    public void draw() {
        StdDraw.setXscale();
        StdDraw.setYscale();
       // StdDraw.setPenColor(StdDraw.BLACK);
        // StdDraw.setPenRadius(.01);

        for (Point2D point : set) {
            point.draw();
        }
        
            
    }
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("");
        }        
        List<Point2D> pointsInsideList = new ArrayList<Point2D>();
        /*while (setIter.hasNext()) {
            Point2D tempPoint = new Point2D();
            tempPoint = setIter.next();
            if (isIn(tempPoint, rect)) {
                pointsInsideList.add(tempPoint);
            }
        }*/
        for (Point2D point : set) {
            if (rect.contains(point)) {
                pointsInsideList.add(point);
            }
        }
        return pointsInsideList;
    }

    
    public Point2D nearest(Point2D p) {
        /*Point2D tempPoint = new Point2D();
        Point2D nearestPoint = new Point2D();
        tempPoint = setIter.next();
        nearestPoint = tempPoint;
        double minSquareDistance = suqareDistance(p, tempPoint);
        double tempSquareDistance = 0.0;
        while (setIter.hasNext()) {
            tempPoint = setIter.next();
            tempSquareDistance = squareDistance(p, tempPoint);
            if (tempSquareDistance < minSquareDistance) {
                minSquareDistance = tempSquareDistance;
                nearestPoint = tempPoint;
            }
        }
        return nearestPoint;*/
        if (p == null) {
            throw new IllegalArgumentException("");
        }
        if (set.size() == 0) {
            return null;
        }
        
        double minSquaredDistance = Double.POSITIVE_INFINITY;
        double tempSquaredDistance;
        Point2D nearestPoint = p;

        for (Point2D point : set) {
            tempSquaredDistance = point.distanceSquaredTo(p);
            if (tempSquaredDistance < minSquaredDistance) {
                minSquaredDistance = tempSquaredDistance;
                nearestPoint = point;
            }
            
        }
        return nearestPoint;
        
    }

    public static void main(String[] args) {
    }
}