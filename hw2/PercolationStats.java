package hw2.hw2;

import java.util.ArrayList;
import java.util.List;
import edu.princeton.cs.introcs.StdRandom;

public class PercolationStats {

    private List<Double> result;
    private int T;
    private double mean;
    private double stddev;


    private double test(int N, Percolation p) {
        while (!p.percolates()) {
            int x = StdRandom.uniform(0, N);
            int y = StdRandom.uniform(0, N);
            p.open(x, y);
        }
        return (double) p.numberOfOpenSites() / N;
    }

    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0)
            throw new IllegalArgumentException();
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
        this.mean = m/T;

        // 计算方差
        double s = 0;
        for (double d : result) {
            s += (d-mean)*(d-mean);
        }
        this.stddev = s/(T-1);
    }

    public double mean() {
        return this.mean;
    }

    public double stddev() {
        return this.stddev;
    }

    public double confidenceLow() {
        return this.mean - 1.96*this.stddev/Math.sqrt(T);
    }

    public double confidenceHigh() {
        return this.mean + 1.96*this.stddev/Math.sqrt(T);
    }
}
