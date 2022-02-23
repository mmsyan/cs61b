package hw2.hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] items;
    private WeightedQuickUnionUF wuf;
    private WeightedQuickUnionUF wufWithoutBW;
    private int numberOfOpenSites;
    private final int N;

    // 判断节点坐标是否合理
    private void verify(int r, int c) {
        if (r >= N || c >= N)
            throw new IndexOutOfBoundsException();
    }

    // 计算节点坐标
    private int coordinate(int r, int c) {
        return r*N+c;
    }

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0)
            throw new IllegalArgumentException("N <= 0!");
        items = new boolean[N][N];
        wuf = new WeightedQuickUnionUF(N*N+2);
        wufWithoutBW = new WeightedQuickUnionUF(N*N+2);
        numberOfOpenSites = 0;
        this.N = N;
    }

    // is the site (row, col) open?
    boolean isOpen(int row, int col) {
        verify(row, col);
        return items[row][col];
    }

    private boolean connectVerify(int r, int c) {
        return  (r < N && c < N && r >=0 && c >= 0) && items[r][c] ;
    }

    private void connect(int r, int c) {
        if (connectVerify(r+1, c)) {
            wuf.union(coordinate(r+1,c), coordinate(r,c));
            wufWithoutBW.union(coordinate(r+1,c), coordinate(r,c));
        }
        if (connectVerify(r-1, c)) {
            wuf.union(coordinate(r-1,c), coordinate(r,c));
            wufWithoutBW.union(coordinate(r-1,c), coordinate(r,c));
        }
        if (connectVerify(r, c+1)) {
            wuf.union(coordinate(r,c+1), coordinate(r,c));
            wufWithoutBW.union(coordinate(r,c+1), coordinate(r,c));
        }
        if (connectVerify(r, c-1)) {
            wuf.union(coordinate(r,c-1), coordinate(r,c));
            wufWithoutBW.union(coordinate(r,c-1), coordinate(r,c));
        }

    }


    // open the site (row, col) if it is not open already
    void open(int row, int col) {
        verify(row, col);
        if (isOpen(row, col))
            return;
        items[row][col] = true;
        numberOfOpenSites++;

        // 判断和虚拟节点的连接情况，非回流并查集不考虑底部虚拟节点
        if (row == 0) {
            wuf.union(N*N, coordinate(row, col));
            wufWithoutBW.union(N*N, coordinate(row, col));
        }
        if (row == N-1) {
            wuf.union(N*N+1, coordinate(row, col));
        }

        // 判断周围四个节点的情况并考虑是否进行连接
        connect(row, col);
    }

    // is the site (row, col) full?
    boolean isFull(int row, int col) {
        return wufWithoutBW.connected(N*N, coordinate(row, col));
    }

    // does the system percolate?
    boolean percolates() {
        return wuf.connected(N*N, N*N+1);
    }

    // number of open sites
    int numberOfOpenSites() {
        return this.numberOfOpenSites;
    }

    public static void main(String[] args) {

    }

}
