/******************************************************************************
 *  Compilation:  javac-algs4 KdTree.java
 *  Execution:    java-algs4 KdTree (simple unit test)
 *  Dependencies: none
 *  @author  Kevin James 07022017
 *  @Email   kevinsocial@outlook.com
 * 
 *  2d-tree implementation. Write a mutable data type KdTree.java that uses a 
 *  2d-tree to implement the same API (but replace PointSET with KdTree). A 
 *  2d-tree is a generalization of a BST to two-dimensional keys. The idea 
 *  is to build a BST with points in the nodes, using the x- and y-coordinates
 *  of the points as keys in strictly alternating sequence.
 * 
 * API:
 *----------------------------------------------------------------------------
 * public KdTree()                   // construct an empty set of points 
 * public boolean isEmpty()          // is the set empty? 
 * public int size()                 // number of points in the set 
 * public void insert(Point2D p)     // add the point to the set (if it is not already in the set)
 * public boolean contains(Point2D p)            // does the set contain point p? 
 * public void draw()                            // draw all points to standard draw 
 * public Iterable<Point2D> range(RectHV rect)   // all points that are inside the rectangle 
 * public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty 
 * 
 * public static void main(String[] args)        // unit testing of the methods (optional) 
 * ---------------------------------------------------------------------------
 * ***************************************************************************/

import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

import java.util.List;
import java.util.ArrayList;

public class KdTree {
    
    private Node root;
    private int size;
    private Point2D nearestPoint;
    private double currentMinDistance;
    
    private static class Node {
        private final Point2D p;
        private final RectHV rect;
        private Node lb;
        private Node rt;
        
        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }
    
    public KdTree() {
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
            throw new IllegalArgumentException("");
        }
        if (!contains(p)) {
            size++;
            if (root == null) {
                root = new Node(p, new RectHV(0.0, 0.0, 1.0, 1.0));
            }
            else {
                boolean lbORrt = false;
                root = put(root, p, new RectHV(0.0, 0.0, 1.0, 1.0), root.p, lbORrt, 0); 
            } 
        }
    }
    
    // oritation: current level the newPoint stay
    private Node put(Node parentPoint, Point2D newPoint, RectHV rect, Point2D fatherPoint, boolean lbORrt, int oritation) {
        if (oritation % 2 != 0 && parentPoint == null && lbORrt) { // vertial left
            return new Node(newPoint, new RectHV(rect.xmin(), rect.ymin(), fatherPoint.x(), rect.ymax()));
        }
        if (oritation % 2 != 0 && parentPoint == null && !lbORrt) { // vertial right
            return new Node(newPoint, new RectHV(fatherPoint.x(), rect.ymin(), rect.xmax(), rect.ymax()));
        } 
        if (oritation % 2 == 0 && parentPoint == null && lbORrt) { // horizontal bottom
            return new Node(newPoint, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), fatherPoint.y()));
        }
        if (oritation % 2 == 0 && parentPoint == null && !lbORrt) { // horizontal top
            return new Node(newPoint, new RectHV(rect.xmin(), fatherPoint.y(), rect.xmax(), rect.ymax()));
        }
        
        if (oritation % 2 != 0) {
            double cmp = newPoint.y() - (parentPoint.p.y());
            if (cmp < 0) {
                parentPoint.lb = put(parentPoint.lb, newPoint, parentPoint.rect, parentPoint.p, true, ++oritation);
            }
            else {            
                parentPoint.rt = put(parentPoint.rt, newPoint, parentPoint.rect, parentPoint.p, false, ++oritation);
            }
        }   
        else {
            double cmp = newPoint.x() - (parentPoint.p.x());
            if (cmp < 0) { 
                parentPoint.lb = put(parentPoint.lb, newPoint, parentPoint.rect, parentPoint.p, true, ++oritation);
            }
            else {             
                parentPoint.rt = put(parentPoint.rt, newPoint, parentPoint.rect, parentPoint.p, false, ++oritation);
            }
        }
        return parentPoint;
    }
    
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("unsupport to search null point.");
        }        
        return contains(root, p, 0);
    }
    
    private boolean contains(Node parentNode, Point2D searchPoint, int oritation) {
        if (parentNode == null) {
            return false;
        }
        if (oritation % 2 == 0) { // compare with point of Node on even level 
            if (parentNode.p.equals(searchPoint)) { // equal to point of root of this subtree?
                return true;
            }
            double cmp = searchPoint.x() - parentNode.p.x();
            
            if (cmp < 0) { // search euqal point among left/bottom subtree of this parent node
                return contains(parentNode.lb, searchPoint, ++oritation);
            }
            else  {        // search equal point among right/top subtree of this parent node
                return contains(parentNode.rt, searchPoint, ++oritation);
            }
        } 
        else { // compare with point of Node on odd level
            if (parentNode.p.equals(searchPoint)) {
                return true;
            }
            double cmp = searchPoint.y() - parentNode.p.y();
            if (cmp < 0) {
                return contains(parentNode.lb, searchPoint, ++oritation);
            }
            else  {
                return contains(parentNode.rt, searchPoint, ++oritation);
            }
        }
    }
    
    public void draw() {
        StdDraw.setXscale();
        StdDraw.setYscale();
        draw(root, 0);    
    }
    
    private void draw(Node parentNode, int oritation) {  
        if (parentNode != null) {
            if (oritation % 2 == 0) { // even level: vertial red splitting line
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(0.01);
                parentNode.p.draw();
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.setPenRadius();
                StdDraw.line(parentNode.p.x(), parentNode.rect.ymin(),
                             parentNode.p.x(), parentNode.rect.ymax());
                int lbtemp = oritation;
                draw(parentNode.lb, ++lbtemp);
                int rttemp = oritation;
                draw(parentNode.rt, ++rttemp); 
            }
            if (oritation % 2 != 0) { // odd level: horizontal blue splitting line
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(0.01);
                parentNode.p.draw();
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.setPenRadius();
                StdDraw.line(parentNode.rect.xmin(), parentNode.p.y(),
                             parentNode.rect.xmax(), parentNode.p.y());
                int lbtemp = oritation;
                int rttemp = oritation;
                draw(parentNode.lb, ++lbtemp);
                draw(parentNode.rt, ++rttemp);
            }
        }
    }
    
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException("Illegal null rect.");
        }
        List<Point2D> pointsInsideList = new ArrayList<Point2D>();
  
        findPointsInside(root, rect, pointsInsideList);
        return pointsInsideList;
    }
    
    private void findPointsInside(Node currentNode, RectHV rangeRect, List<Point2D> pointsInsideList) {
        // if the query rectangle does not intersect the rectangle 
        // corresponding to a node, there is no need to explore that
        // node (or its subtrees)
        if (currentNode != null && currentNode.rect.intersects(rangeRect)) {
            if (rangeRect.contains(currentNode.p)) {
                pointsInsideList.add(currentNode.p);
            }
            findPointsInside(currentNode.lb, rangeRect, pointsInsideList);
            findPointsInside(currentNode.rt, rangeRect, pointsInsideList);
        }
    }
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("Illegal null quary point.");
        }  
        if (root == null) {
            return null; // no point in the plane, no nearest point
        }
        currentMinDistance = Double.POSITIVE_INFINITY;
        nearestPoint = root.p;
        findNearestPoint(root, p);
        return nearestPoint;   
    }

    private void findNearestPoint(Node currentNode, Point2D quaryPoint) {
        // if the closest point discovered so far is closer than the distance
        // between the query point and the rectangle corresponding to a node, 
        // there is no need to explore that node (or its subtrees)
        if (currentNode != null && currentNode.rect.distanceSquaredTo(quaryPoint) < currentMinDistance) {
            double tempSquareDistance = currentNode.p.distanceSquaredTo(quaryPoint);
            if (tempSquareDistance < currentMinDistance) { // update currentMinDistance and nearestPoint
                currentMinDistance = tempSquareDistance;
                nearestPoint = currentNode.p;
            }
            // when there are two possible subtrees to go down
            if (currentNode.lb != null && currentNode.rt != null) { 
                // always choose the subtree that is on the same side of the 
                // splitting line as the query point as the first subtree to explore
                if (currentNode.lb.rect.contains(quaryPoint)) {
                    findNearestPoint(currentNode.lb, quaryPoint);
                    findNearestPoint(currentNode.rt, quaryPoint);
                }
                else {
                    findNearestPoint(currentNode.rt, quaryPoint);
                    findNearestPoint(currentNode.lb, quaryPoint);
                }
            }
            else {
                findNearestPoint(currentNode.lb, quaryPoint);
                findNearestPoint(currentNode.rt, quaryPoint);
            }
        }
    } 
    
    public static void main(String[] args) {
        KdTree kd = new KdTree();
        
        kd.insert(new Point2D(0.7, 0.2));
        kd.insert(new Point2D(0.5, 0.4));
        kd.insert(new Point2D(0.2, 0.3)); 
        kd.insert(new Point2D(0.4, 0.7));
        kd.insert(new Point2D(0.9, 0.6));
        
        kd.draw();
        StdOut.println(kd.nearest(new Point2D(0.5, 0.8)));
        StdOut.println(kd.size());
        StdOut.println(kd.contains(new Point2D(0.5, 0.2)));
        for (Point2D each : kd.range(new RectHV(0.0, 0.0, 1.0, 1.0))) {
            StdOut.println(each);
        }
        StdOut.println(kd.size());
        RectHV testRect = new RectHV(0.0, 0.0, 1.0, 1.0);
        StdOut.println(testRect.distanceSquaredTo(new Point2D(1.0, 1.0)));
    }
}