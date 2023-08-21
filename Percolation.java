/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] percolation2dArray;
    private int size;
    private int numOfOpenSites;
    private WeightedQuickUnionUF weightedQuickUnionUF;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException();
        weightedQuickUnionUF = new WeightedQuickUnionUF(n * n + 2);
        size = n;
        percolation2dArray = new boolean[n][n];
        for (int i = 0; i < n; i++) {
            weightedQuickUnionUF.union(0, i + 1);
            for (int j = 0; j < n; j++) {
                percolation2dArray[i][j] = false;
                // if (i == n - 1) {
                //     weightedQuickUnionUF.union(size * size + 1, (n - 1) * n + j + 1);
                // }
            }
        }
        // StdOut.println(String.format("Initializing %d * %d percolation Array", n, n));
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || col < 1 || row > size || col > size)
            throw new IllegalArgumentException("illegal row: " + row + ", col: " + col);
        if (!isOpen(row, col)) {
            percolation2dArray[row - 1][col - 1] = true;
            numOfOpenSites++;
            // StdOut.println(String.format("open percolation[%d, %d]", row, col));
            isFull(row, col);
        }
        makeConnetion(row, col);
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        isValidParams(row, col);
        // StdOut.println(String.format("percolation[%d, %d] is Open: %s", row, col,
        //                              percolation2dArray[row - 1][col - 1]));
        return percolation2dArray[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        isValidParams(row, col);
        boolean isOpen = isOpen(row, col);
        boolean sameAsRoot = weightedQuickUnionUF.find((row - 1) * size + col)
                == weightedQuickUnionUF.find(0);
        boolean isFull = sameAsRoot && isOpen;
        return isFull;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        // StdOut.println("numberOfOpenSites: " + numOfOpenSites);
        return numOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        if (size == 1) return percolation2dArray[0][0];
        return weightedQuickUnionUF.find(size * size + 1) == weightedQuickUnionUF.find(0);
    }

    private void isValidParams(int row, int col) {
        if (row < 1 || col < 1 || row > size || col > size)
            throw new IllegalArgumentException("illegal row: " + row + ", col: " + col);
    }

    private void makeConnetion(int row, int col) {
        int ufPosition = (row - 1) * size + col;
        // System.out.println(weightedQuickUnionUF.find(ufPosition));
        int top = (row - 2) * size + col;
        int bottom = (row) * size + col;
        int left = (row - 1) * size + col - 1;
        int right = (row - 1) * size + col + 1;
        if (col > 1 && isOpen(row, col - 1)) {
            weightedQuickUnionUF.union(left, ufPosition);
            // System.out.println(weightedQuickUnionUF.find(left));
        }
        if (col < size && isOpen(row, col + 1)) {
            weightedQuickUnionUF.union(right, ufPosition);
            // System.out.println(weightedQuickUnionUF.find(right));
        }
        if (row > 1 && isOpen(row - 1, col)) {
            weightedQuickUnionUF.union(top, ufPosition);
            // System.out.println(weightedQuickUnionUF.find(top));
        }
        if (row < size && isOpen(row + 1, col)) {
            weightedQuickUnionUF.union(bottom, ufPosition);
            // System.out.println(weightedQuickUnionUF.find(bottom));
        }
        if (row == size) {
            weightedQuickUnionUF.union(ufPosition, size * size + 1);
        }
    }

    // test client (optional)
    public static void main(String[] args) {
        int size = Integer.parseInt(StdIn.readLine());
        Percolation p = new Percolation(size);
        int matrixSize = size * size;
        StdOut.println("size: " + size);
        StdOut.println("matrixSize: " + matrixSize);
        System.out.println(size);
        p.open(1, 6);
        p.open(2, 6);
        p.open(3, 6);
        p.open(4, 6);
        p.open(5, 6);
        p.open(5, 5);
        p.open(4, 4);
        p.open(3, 4);
        p.open(2, 4);
        p.open(2, 3);
        p.open(2, 2);
        p.open(2, 1);
        p.open(3, 1);
        p.open(4, 1);
        p.open(5, 1);
        p.open(5, 2);
        p.open(6, 2);
        p.open(5, 4);

        // int sites = 38;
        // int checkRow = 3;
        // int checkCol = 9;
        // while (/*sites > 0 || */!StdIn.isEmpty()) {
        //     // int openSite = StdRandom.uniformInt(1, size * size + 1);
        //     // int row = openSite % size == 0 ? openSite / size : openSite / size + 1;
        //     // int col = openSite % size == 0 ? size : openSite % size;
        //     int row = StdIn.readInt();
        //     int col = StdIn.readInt();
        //     System.out.println("open:" + row + ", " + col);
        //     p.open(row, col);
        //     if (p.percolates()) break;
        //     // sites--;
        // }
        // System.out.println(p.isFull(checkRow, checkCol));
        System.out.println();
        StdOut.println("percolates: " + p.percolates());
        for (boolean[] layer1 : p.percolation2dArray) {
            for (boolean j : layer1) {
                if (j) {
                    System.out.print("@");
                }
                else {
                    System.out.print("*");
                }
            }
            System.out.println();
        }
    }
}
