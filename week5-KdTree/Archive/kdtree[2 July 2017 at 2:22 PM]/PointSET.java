/******************************************************************************
 *  Compilation:  javac-algs4 PointSET.java
 *  Execution:    java-algs4 PointSET (siple unit test)
 *  Dependencies: none
 *  @author  Kevin James 07022017
 *  @Email   kevinsocial@outlook.com
 * 
 *  Brute-force implementation. Write a mutable data type PointSET.java that 
 *  represents a set of points in the unit square. Implement the following 
 *  API by using a red–black BST (using either SET from algs4.jar or
 *  java.util.TreeSet).
 * 
 *  Corner cases.  Throw a java.lang.IllegalArgumentException if any argument
 *  is null. Performance requirements.  Your implementation should support 
 *  insert() and contains() in time proportional to the logarithm of the number 
 *  of points in the set in the worst case; it should support nearest() and
 *  range() in time proportional to the number of points in the set.
 * 
 * API:
 * ---------------------------------------------------------------------------
 * public PointSET()                 // construct an empty set of points 
 * public boolean isEmpty()          // is the set empty? 
 * public int size()                 // number of points in the set 
 * public void insert(Point2D p)     // add the point to the set (if it is not already in the set)
 * public boolean contains(Point2D p)            // does the set contain point p? 
 * public void draw()                            // draw all points to standard draw 
 * public Iterable<Point2D> range(RectHV rect)   // all points that are inside the rectangle 
 * public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty 
 * 
 * public static void main(String[] args)        // unit testing of the methods (optional)
 * ----------------------------------------------------------------------------
 * ***************************************************************************/


import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.RectHV;

import java.util.List;
import java.util.ArrayList;

public class PointSET {
    
    private final SET<Point2D> set = new SET<Point2D>(); // use ET to store all points
    private int size; // number of points in the set
  
    public PointSET() {
        size = 0;       
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int size() {
        return size;
    }
    
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("unsupport to insert null into set");
        }
        if (!set.contains(p)) { // point in set is unique
            set.add(p);
            size++;
        }
    }
    
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("upsupport to search null point in a set");
        }        
        return set.contains(p);
    }
    
    public void draw() {
        StdDraw.setXscale();
        StdDraw.setYscale();
        for (Point2D point : set) {
            point.draw();
        }           
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Illegal null rect");
        } 
        
        List<Point2D> pointsInsideList = new ArrayList<Point2D>();
        for (Point2D point : set) {
            if (rect.contains(point)) {
                pointsInsideList.add(point);
            }
        }
        return pointsInsideList;
    }

    
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Illegal null quary point  ");
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