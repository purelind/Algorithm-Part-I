import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    
    private static class Node {
        private Point2D p;
        private RectHV rect;
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

    }
    public boolean isEmpty() {
        return size(root) == 0;
    }   
    public int size() {
        return size(root);
    }
    private int size(Node x) {
        if (x == null) {
            return 0;
        }
        else {
            return 1+size(x.rt)+size(x.lb);
        }
        
    }
    public void insert(Point2D p) {
       root = put(root, p, new RectHV(0.0, 0.0, 1.0, 1.0), 1);  
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
                                                parentPoint.rect.ymin(),
                                                parentPoint.rect.xmax(),
                                                parentPoint.p.y());                
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
        return contains(root, p, 1);
    }
    private boolean contains(Node parentNode, Point2D searchPoint, int oritation) {
        if (parentNode == null) {
            return false;
        }
        if (oritation % 2 == 0) {
            double cmp = searchPoint.x() - (parentNode.p.x());
            if (cmp < 0) {
                return contains(parentNode.lb, searchPoint, oritation++);
            }
            else if (cmp > 0) {
                return contains(parentNode.rt, searchPoint, oritation++);
            }
            else {
                return parentNode.p.equals(searchPoint);
            }
        }
        else {
            double cmp = searchPoint.y() - (parentNode.p.y());
            if (cmp < 0) {
                return contains(parentNode.lb, searchPoint, oritation++);
            }
            else if (cmp > 0) {
                return contains(parentNode.rt, searchPoint, oritation++);
            }
            else {
                return parentNode.p.equals(searchPoint);
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
    /*public Iterable<Point2D> range(RectHV rect) {
        
    }
    public Point2D nearest(Point2D p) {
        
    }*/
    
    public static void main(String[] args) {

        KdTree kd = new KdTree();
        kd.insert(new Point2D(0.5, 0.5));
        kd.insert(new Point2D(0.2, 0.2));
        kd.insert(new Point2D(0.6, 0.5));
        kd.insert(new Point2D(0.7, 0.4));
        // StdOut.println(kd.contains(new Point2D(0.5, 0.4)));
        kd.draw();
    }
}