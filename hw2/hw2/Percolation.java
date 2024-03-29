package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] items;
    private WeightedQuickUnionUF wuf;
    private WeightedQuickUnionUF wufWithoutBW;
    private int numberOfOpenSites;
    private final int N;

    /**
     * items:用于判断(row, col)处的节点是否处于开启状态
     * wuf:用于连接已经开启的节点。
     *      wuf[N*N]为顶部虚拟节点，wuf[N*N+1]为底部虚拟节点
     * wufWithoutBW:用于解决回流问题
     *      和wuf不同，没有底部虚拟节点
     * numberOfOpenSites:用于计算已经开启的节点数量
     * */

    // 判断节点坐标是否合理
    private void verify(int r, int c) {
        if (r >= N || c >= N) {
            throw new IndexOutOfBoundsException();
        }
    }

    // 将二维的节点坐标转换为一维坐标
    private int coordinate(int r, int c) {
        return r * N + c;
    }

    // create N-by-N grid, with all sites initially blocked
    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException("N <= 0!");
        }
        items = new boolean[N][N];
        wuf = new WeightedQuickUnionUF(N * N + 2);
        wufWithoutBW = new WeightedQuickUnionUF(N * N + 2);
        numberOfOpenSites = 0;
        this.N = N;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        verify(row, col);
        return items[row][col];
    }

    // 用于辅助open()函数，确认节点坐标是否合理
    private boolean connectVerify(int r, int c) {
        return  (r < N && c < N && r >= 0 && c >= 0) && items[r][c];
    }

    // 尝试与上下左右四个方位的节点连接
    private void connect(int r, int c) {
        if (connectVerify(r + 1, c)) {
            wuf.union(coordinate(r + 1, c), coordinate(r, c));
            wufWithoutBW.union(coordinate(r + 1, c), coordinate(r, c));
        }
        if (connectVerify(r - 1, c)) {
            wuf.union(coordinate(r - 1, c), coordinate(r, c));
            wufWithoutBW.union(coordinate(r - 1, c), coordinate(r, c));
        }
        if (connectVerify(r, c + 1)) {
            wuf.union(coordinate(r, c + 1), coordinate(r, c));
            wufWithoutBW.union(coordinate(r, c + 1), coordinate(r, c));
        }
        if (connectVerify(r, c - 1)) {
            wuf.union(coordinate(r, c - 1), coordinate(r, c));
            wufWithoutBW.union(coordinate(r, c - 1), coordinate(r, c));
        }

    }

    // open the site (row, col) if it is not open already
    public void open(int row, int col) {
        verify(row, col);
        if (isOpen(row, col)) {
            return;
        }
        items[row][col] = true;
        numberOfOpenSites++;

        // 判断和虚拟节点的连接情况，非回流并查集不考虑底部虚拟节点
        if (row == 0) {
            wuf.union(N * N, coordinate(row, col));
            wufWithoutBW.union(N * N, coordinate(row, col));
        }
        if (row == N - 1) {
            wuf.union(N * N + 1, coordinate(row, col));
        }

        // 判断周围四个节点的情况并考虑是否进行连接
        connect(row, col);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return wufWithoutBW.connected(N * N, coordinate(row, col));
    }

    // does the system percolate?
    public boolean percolates() {
        return wuf.connected(N * N, N * N + 1);
    }

    // number of open sites
    public int numberOfOpenSites() {
        return this.numberOfOpenSites;
    }

    public static void main(String[] args) {

    }

}
