/******************************************************************************
 *  Compilation:  javac-algs4 Point.java
 *  Execution:    java-algs4 Point
 *  Dependencies: none
 *  @author  Kevin James 06/17/2017
 *  @Email   kevinsocial@outlook.com
 * 
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 * 
 *  Point data type. Create an immutable data type Point that represents a point
 *  in the plane.
 * 
 *  Corner cases. To avoid potential complications with integer overflow or 
 *  floating-point precision, you may assume that the constructor arguments x 
 *  and y are each between 0 and 32,767.
 *  
 *  As a part of week3 collinear homework of Algorithm Part I in coursera,
 *  student need to implement following API:
 * 
 *  public int compareTo(Point that)
 *  public double slopeTo(Point that)  
 *  public Comparator<Point> slopeOrder()
 * 
 *  --------------------------------------------------------------------------
 *  API: 
 *  public Point(int x, int y)       // constructs the point (x, y)
 *
 *  public void draw()               // draws this point
 *  
 *  public void drawTo(Point that)   
 *  // draws the line segment from this point to that point
 *  
 *  public String toString()   // string representation
 *
 *  public int compareTo(Point that)     
 *  // compare two points by y-coordinates, breaking ties by x-coordinates
 *  
 *  public double slopeTo(Point that)       
 *  // the slope between this point and that point
 *  
 *  public Comparator<Point> slopeOrder() 
 *  // compare two points by slopes they make with this point
 *----------------------------------------------------------------------------
 ******************************************************************************/

import java.util.Comparator;
import edu.princeton.cs.algs4.StdDraw;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param  x the <em>x</em>-coordinate of the point
     * @param  y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param  that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        if (that.y == this.y && that.x != this.x) {
            return +0.0;
        }
        /*else if (that.y == this.y && that.x < this.x) {
            return -0.0;
        }*/
        else if (that.x == this.x && that.y != this.y) {
            return Double.POSITIVE_INFINITY;
        }
        else if (that.x == this.x && that.y == this.y) {
            return Double.NEGATIVE_INFINITY;
        }
        return (that.y - this.y) / (double) (that.x - this.x);
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param  that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     *         point (x0 = x1 and y0 = y1);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Point that) {
        if (this.y < that.y) {
            return -1;
        }
        if (this.y == that.y && this.x < that.x) {
            return -1;
        }
        if (this.y == that.y && this.x == that.x) {
            return 0;
        }
        return 1;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        return new SlopeOrder();
    }
    private class SlopeOrder implements Comparator<Point> {
        public int compare(Point point1, Point point2) {
                double diffSlope = slopeTo(point1) - slopeTo(point2);
                if (diffSlope < 0) {
                    return -1;
                }
                if (diffSlope > 0) {
                    return 1;
                }
                return 0;
        }
    }

    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
    }
    
    
}
