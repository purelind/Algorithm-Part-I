import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

import java.util.List;
import java.util.ArrayList;

public class KdTree {
    private Node root;
   // private List<Point2D> pointsInsideList = new ArrayList<Point2D>();
    private double currentMinDistance;
    private Point2D nearestPoint;
    private int size;
    
    private static class Node {
        private final Point2D p;
        private final RectHV rect;
        private Node lb;
        private Node rt;
        
        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
            //this.lb = lb;
            //this.rt = rt
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
            root = put(root, p, new RectHV(0.0, 0.0, 1.0, 1.0), 1);  
        }
    }
    private Node put(Node parentPoint, Point2D newPoint, RectHV rect, int oritation) {
        if (parentPoint == null) {
            return new Node(newPoint, rect);
        }
        if (oritation % 2 == 0) {
            double cmp = newPoint.y() - (parentPoint.p.y());
            if (cmp < 0) {
                RectHV currentRect = new RectHV(parentPoint.rect.xmin(),
                                                parentPoint.rect.ymin(),
                                                parentPoint.rect.xmax(),
                                                parentPoint.p.y());
                parentPoint.lb = put(parentPoint.lb, newPoint, currentRect, ++oritation);
            }
            if (cmp >= 0) {
                RectHV currentRect = new RectHV(parentPoint.rect.xmin(),
                                                parentPoint.p.y(),
                                                parentPoint.rect.xmax(),
                                                parentPoint.rect.ymax());                
                parentPoint.rt = put(parentPoint.rt, newPoint, currentRect, ++oritation);
            }
        }
        else {
            double cmp = newPoint.x() - (parentPoint.p.x());
            if (cmp < 0) {
                RectHV currentRect = new RectHV(parentPoint.rect.xmin(),
                                                parentPoint.rect.ymin(),
                                                parentPoint.p.x(),
                                                parentPoint.rect.ymax());                
                parentPoint.lb = put(parentPoint.lb, newPoint, currentRect, ++oritation);
            }
            if (cmp >= 0) {
                RectHV currentRect = new RectHV(parentPoint.p.x(),
                                                parentPoint.rect.ymin(),
                                                parentPoint.rect.xmax(),
                                                parentPoint.rect.ymax());                
                parentPoint.rt = put(parentPoint.rt, newPoint, currentRect, ++oritation);
            }
        }
        return parentPoint;
    }
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("");
        }        
        return contains(root, p, 0);
    }
    private boolean contains(Node parentNode, Point2D searchPoint, int oritation) {
        if (parentNode == null) {
            return false;
        }
        if (oritation % 2 == 0) {
            if (parentNode.p.equals(searchPoint)) {
                return true;
            }
            double cmp = searchPoint.x() - (parentNode.p.x());
            
            if (cmp < 0) {
                return contains(parentNode.lb, searchPoint, ++oritation);
            }
            else  {
                return contains(parentNode.rt, searchPoint, ++oritation);
            }
        } 
        else {
            if (parentNode.p.equals(searchPoint)) {
                return true;
            }
            double cmp = searchPoint.y() - (parentNode.p.y());
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
        
        if (parentNode != null){
            if (oritation % 2 == 0) {
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(.01);
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
            if (oritation % 2 != 0) {
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(.01);
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
            throw new IllegalArgumentException("");
        }
        List<Point2D> pointsInsideList = new ArrayList<Point2D>();

        findPointsInside(root, rect, pointsInsideList);

        return pointsInsideList;
    }
    private void findPointsInside(Node currentNode, RectHV rangeRect, List<Point2D> pointsInsideList) {
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
            throw new IllegalArgumentException("");
        }  
        // double currentMinDistance = Double.POSITIVE_INFINITY;
        // nearestPoint = new Point2D();
        // Point2D nearestPoint;
        // nearestPoint = new Point2D(0.0, 0.0);
        currentMinDistance = Double.POSITIVE_INFINITY;
        nearestPoint = root.p;
        findNearestPoint(root, p);
        return nearestPoint;
    }
    private void findNearestPoint(Node currentNode, Point2D quaryPoint) {
        if (currentNode != null && currentNode.rect.distanceSquaredTo(quaryPoint) < currentMinDistance) {
            double tempSquareDistance = currentNode.p.distanceSquaredTo(quaryPoint);
            if (tempSquareDistance < currentMinDistance) {
                currentMinDistance = tempSquareDistance;
                nearestPoint = currentNode.p;
            }
            if (currentNode.lb != null ) { // && currentNode.lb.rect.contains(quaryPoint
                
                findNearestPoint(currentNode.lb, quaryPoint);
                findNearestPoint(currentNode.rt, quaryPoint);
            }
            if (currentNode.rt != null) {
                findNearestPoint(currentNode.rt, quaryPoint);
                findNearestPoint(currentNode.lb, quaryPoint);
            }
        }
     }

    
    
    public static void main(String[] args) {

        KdTree kd = new KdTree();
        kd.insert(new Point2D(0.5, 0.1));
        kd.insert(new Point2D(0.5, 0.1));
        kd.insert(new Point2D(0.5, 0.2)); 
        kd.insert(new Point2D(0.2, 0.8));
        kd.insert(new Point2D(0.8, 0.8));
        StdOut.println(kd.nearest(new Point2D(0.5, 0.8)));
        StdOut.println(kd.size());
        StdOut.println(kd.contains(new Point2D(0.5, 0.2)));
        //StdOut.println(kd.contains(new Point2D(0.5, 0.1)));
        //StdOut.println(kd.contains(new Point2D(0.2, 0.8)));
        //StdOut.println(kd.contains(new Point2D(0.8, 0.8)));
        for (Point2D each : kd.range(new RectHV(0.0, 0.0, 1.0, 1.0))) {
            StdOut.println(each);
        }
        StdOut.println(kd.size());
    }
}