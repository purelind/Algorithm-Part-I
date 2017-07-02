import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.In;
import java.util.TreeSet;
import java.util.Set;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class PointSET {
    private SET<Point2D> set = new SET<Point2D>();
    private int size;
    private Point2D nearestPoint;
    
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
        nearestPoint = p;

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
        String filename = args[0];
        In in = new In(filename);

        StdDraw.enableDoubleBuffering();

        // initialize the data structures with N points from standard input
        PointSET brute = new PointSET();
       // KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
         //   kdtree.insert(p);
            brute.insert(p);
        }

        double x0 = 0.0, y0 = 0.0;      // initial endpoint of rectangle
        double x1 = 0.0, y1 = 0.0;      // current location of mouse
        boolean isDragging = false;     // is the user dragging a rectangle

        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        brute.draw();
        StdDraw.show();

        while (true) {

            // user starts to drag a rectangle
            if (StdDraw.mousePressed() && !isDragging) {
                x0 = StdDraw.mouseX();
                y0 = StdDraw.mouseY();
                isDragging = true;
                continue;
            }

            // user is dragging a rectangle
            else if (StdDraw.mousePressed() && isDragging) {
                x1 = StdDraw.mouseX();
                y1 = StdDraw.mouseY();
                continue;
            }

            // mouse no longer pressed
            else if (!StdDraw.mousePressed() && isDragging) {
                isDragging = false;
            }


            RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1),
                                     Math.max(x0, x1), Math.max(y0, y1));
            // draw the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            brute.draw();

            // draw the rectangle
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius();
            rect.draw();

            // draw the range search results for brute-force data structure in red
            StdDraw.setPenRadius(0.03);
            StdDraw.setPenColor(StdDraw.RED);
            for (Point2D p : brute.range(rect))
                p.draw();

            // draw the range search results for kd-tree in blue
           // StdDraw.setPenRadius(.02);
           // StdDraw.setPenColor(StdDraw.BLUE);
            //for (Point2D p : kdtree.range(rect))
           //     p.draw();

            StdDraw.show();
            StdDraw.pause(40); 
        }
    }
    
}