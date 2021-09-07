package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] universe;
    private WeightedQuickUnionUF wuf;
    private int N;
    private int numberOfOpenSites;

    /** create N-by-N grid, with all sites initially blocked */
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N <= 0!");
        }
        universe = new boolean[N][N];
        wuf = new WeightedQuickUnionUF(N * N);
        this.N = N;
        numberOfOpenSites = 0;
    }

    private void verify(int row, int col) {
        if (row >= N || col >= N) {
            throw new IndexOutOfBoundsException("row or col >= N!");
        }
    }

    private boolean connectVerify(int x, int y) {
        if ((x < 0) || (y < 0) || (x >= N) || (y >= N)) {
            return false;
        }
        return universe[x][y];
    }

    private void connect(int row, int col) {
        int target = xyTo1D(row, col);
        if (connectVerify(row - 1, col)) {
            wuf.union(target, xyTo1D((row - 1), col));
        }
        if (connectVerify(row + 1, col)) {
            wuf.union(target, xyTo1D((row + 1), col));
        }
        if (connectVerify(row, col - 1)) {
            wuf.union(target, xyTo1D(row, (col - 1)));
        }
        if (connectVerify(row, col + 1)) {
            wuf.union(target, xyTo1D(row, (col + 1)));
        }
        return;
    }

    private int xyTo1D(int r, int c) {
        return r * N + c;
    }

    /** open the site (row, col) if it is not open already */
    public void open(int row, int col) {
        verify(row, col);
        if (universe[row][col]) {
            return;
        }
        universe[row][col] = true;
        numberOfOpenSites++;
        connect(row, col);
    }

    /** is the site (row, col) open? */
    public boolean isOpen(int row, int col) {
        verify(row, col);
        return universe[row][col];
    }

    /** is the site (row, col) full? */
    public boolean isFull(int row, int col) {
        verify(row, col);
        int target = xyTo1D(row, col);
        for (int i = 0; i < N; i++) {
            if (wuf.connected(target, i)) {
                return universe[row][col];
            }
        }
        return false;
    }

    /** number of open sites */
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    /** does the system percolate? */
    public boolean percolates() {
        for (int i = 0; i < N; i++) {
            if (isFull(N - 1, i)) {
                return true;
            }
        }
        return false;
    }

    /** use for unit testing (not required) */
    public static void main(String[] args) {
        return;
    }

}
