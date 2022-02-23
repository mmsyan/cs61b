package hw2;

import java.util.ArrayList;
import java.util.List;
import edu.princeton.cs.introcs.StdRandom;

public class PercolationStats {

    private List<Double> result;
    private int T;
    private double mean;
    private double stddev;

    /**
     * result:记录每次发生渗透时的临界点（已开启的节点数/总结点数）
     * T:用于记录试验次数
     * mean:计算样本均值
     * stddev:计算样本标准差
     * */


    private double test(int N, Percolation p) {
        while (!p.percolates()) {
            int x = StdRandom.uniform(0, N);
            int y = StdRandom.uniform(0, N);
            p.open(x, y);
        }
        return (double) p.numberOfOpenSites() / (N * N);
    }

    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }
        this.T = T;
        result = new ArrayList<>();
        for (int i = 0; i < T; i++) {
            result.add(test(N, pf.make(N)));
        }

        // 计算平均数
        double m = 0;
        for (double d : result) {
            m += d;
        }
        this.mean = m / T;

        // 计算方差
        double s = 0;
        for (double d : result) {
            s += (d - mean) * (d - mean);
        }
        this.stddev = Math.sqrt(s / (T - 1));
    }

    public double mean() {
        return this.mean;
    }

    public double stddev() {
        return this.stddev;
    }

    public double confidenceLow() {
        double confidence = 1.96 * stddev() / ((double) Math.sqrt(T));
        return mean() - confidence;
    }

    public double confidenceHigh() {
        double confidence = 1.96 * stddev() / ((double) Math.sqrt(T));
        return mean() + confidence;
    }
}
