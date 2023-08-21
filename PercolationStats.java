/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private double[] results;
    private double mean;
    private double stddev;
    private int n;
    private int trials;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();
        this.n = n;
        this.trials = trials;
        results = new double[trials];
        int i = 0;
        while (i < trials) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                int openSite = StdRandom.uniformInt(1, n * n + 1);
                int row = openSite % n == 0 ? openSite / n : openSite / n + 1;
                int col = openSite % n == 0 ? n : openSite % n;
                percolation.open(row, col);
                if (percolation.percolates()) {
                    results[i] = (double) percolation.numberOfOpenSites() / (n * n);
                    i++;
                }
            }
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        mean = StdStats.mean(results);
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        stddev = StdStats.stddev(results);
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean - CONFIDENCE_95 * stddev / Math.sqrt(trials);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean + CONFIDENCE_95 * stddev / Math.sqrt(trials);
    }

    // test client (see below)

    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(StdIn.readInt(),
                                                   StdIn.readInt());

        StdOut.println("mean = " + ps.mean());
        StdOut.println("stddev = " + ps.stddev());
        StdOut.println(
                "95% confidence interval = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}
