/******************************************************************************
  * Compilation: javac-algs4 PercolationStats.java
  * Execution:   java-algs4  PersolationStats
  * Dependency:  Percolation.java
  * @author:  Kevin James  05202017
  * @Email:   kevinsocial@outlook.com
  * 
  *  The problem. In a famous scientific problem, researchers are interested 
  *  in the following question: if sites are independently set to be open with 
  *  probability p (and therefore blocked with probability 1 − p), what is the
  *  probability that the system percolates? When p equals 0, the system does 
  *  not percolate; when p equals 1, the system percolates. The plots below 
  *  show the site vacancy probability p versus the percolation probability 
  *  for 20-by-20 random grid (left) and 100-by-100 random grid (right).
  * 
  *  When n is sufficiently large, there is a threshold value p* such that
  *  when p < p* a random n-by-n grid almost never percolates, and when p > p*,
  *  a random n-by-n grid almost always percolates. No mathematical solution 
  *  for determining the percolation threshold p* has yet been derived. Your 
  *  task is to write a computer program to estimate p*.
  * 
  *  Monte Carlo simulation. To estimate the percolation threshold, consider 
  *  the following computational experiment:
  *  * Initialize all sites to be blocked.
  *  * Repeat the following until the system percolates:
  *  * Choose a site uniformly at random among all blocked sites.
  *  * Open the site.
  *  The fraction of sites that are opened when the system percolates provides
  *  an estimate of the percolation threshold.
  * 
  *  To estimate the percolation threshold, Monte Carlo simulation
  *  created a data type PercolationStats with the following API:
  * 
  *  public class PercolationStats  || perform trials independent experiments on an n-by-n grid
  * ---------------------------------------------------------------------------
  *   double  mean()          ||  sample mean of percolation threshold
  *   dobule  stddev()        ||  sample standard deviation of percolation threshold
  *   double  confidenceLo()  ||  low endpoint of 95% confidence interval
  *   double  confidenceHi()  ||  high endpoint of 95% confidence interval
  *   public static void main(String[] args)  // test client (described below)
  *    
  * ***************************************************************************/
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private double[] threshold; // array strore every percolation threshold
    private int t;              // trials time
    
    /**
     * perform trials independent experiments on an n-by-n grid
     * The constructor should throw a java.lang.IllegalArgumentException 
     * if either n ≤ 0 or trials ≤ 0.
     * 
     * @ n
     * @ trials
     */
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("n and trials must positve.");
        }
        t = trials;
        Percolation perc;
        threshold = new double[t]; // need to strore "t" percoaltion threshold data
        for (int i = 0; i < trials; i++) {
            perc = new Percolation(n);
            // keep on opening sites randomly untill percolate
            int opensite = 0;
            while (!perc.percolates()) {
                int rowRandom = StdRandom.uniform(1, n+1); // random row 1---n
                int colRandom = StdRandom.uniform(1, n+1); // random col 1--n
                if (!perc.isOpen(rowRandom, colRandom)) {
                    perc.open(rowRandom, colRandom);
                    opensite++;
                }
            }
            threshold[i] = opensite / (n*n + 0.0); // store percoalte threshold
        }
    }
    
    /* *
     * sample mean of percolation threshold
     * 
     * @return 
     */
    public double mean() {
        return StdStats.mean(threshold);
    }
    
    /* *
     * sample standard deviation of percolation threshold
     * 
     * @return
     */
    public double stddev() { 
        return StdStats.stddev(threshold);
    }
    
    /** 
     * low  endpoint of 95% confidence interval
     * 
     * @return
     */
    public double confidenceLo() {
        return StdStats.mean(threshold) - 1.96 * StdStats.stddev(threshold) / Math.sqrt(t);
    }
    
    /**
     * high endpoint of 95% confidence interval
     * 
     * @return
     */
    public double confidenceHi() { 
        return StdStats.mean(threshold) + 1.96 * StdStats.stddev(threshold) / Math.sqrt(t);
    }
    
    /**
     * Also, include a main() method that takes two command-line arguments n 
     * and T, performs T independent computational experiments (discussed above)
     * on an n-by-n grid, and prints the sample mean, sample standard deviation,
     * and the 95% confidence interval for the percolation threshold. Use 
     * StdRandom to generate random numbers; use StdStats to compute the sample 
     * mean and sample standard deviation. 
     */
    public static void main(String[] args) {
        int n = 20;
        int trials = 30; 
        if (args.length == 2) {
            n = Integer.parseInt(args[0]);
            trials = Integer.parseInt(args[1]);
        }
        PercolationStats percStats = new PercolationStats(n, trials);

        StdOut.printf("%-24s%s%.16f\n", "mean", "= ", percStats.mean());
        StdOut.printf("%-24s%s%.16f\n", "stddev", "= ", percStats.stddev());
        StdOut.printf("%-24s%s%s%.16f%s%.16f%s\n", 
                      "95% confidence interval ", "= ", "[", 
                      percStats.confidenceLo(), ", ", percStats.confidenceHi(), "]");
    } 
}