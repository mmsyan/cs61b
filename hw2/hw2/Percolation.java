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
        wuf = new WeightedQuickUnionUF(N * N + 2);
        this.N = N;
        numberOfOpenSites = 0;
    }

    private void verify(int row, int col) {
        if (row >= N || col >= N) {
            throw new IndexOutOfBoundsException("row or col >= N!");
        }
    }

    private boolean connectVerify(int x, int y) {
        return  !(x < 0 || y < 0 || x >= N || y >= N) && universe[x][y];
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
        if (row == 0) {
            wuf.union(N * N, xyTo1D(row, col));
        }
        if (row == N - 1) {
            wuf.union(N * N + 1, xyTo1D(row, col));
        }
    }

    /** is the site (row, col) open? */
    public boolean isOpen(int row, int col) {
        verify(row, col);
        return universe[row][col];
    }

    /** is the site (row, col) full? */
    public boolean isFull(int row, int col) {
        verify(row, col);
        return wuf.connected(xyTo1D(row, col), N * N + 1);
    }

    /** number of open sites */
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    /** does the system percolate? */
    public boolean percolates() {
        return wuf.connected(N * N, N * N + 1);
    }

    /** use for unit testing (not required) */
    public static void main(String[] args) {
    }

}
