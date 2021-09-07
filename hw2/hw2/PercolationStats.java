package hw2;

public class PercolationStats {

    private double[] fractions;
    private int T;



    private void test(Percolation p, int N) {
        while (true) {
            int x = edu.princeton.cs.introcs.StdRandom.uniform(N);
            int y = edu.princeton.cs.introcs.StdRandom.uniform(N);
            p.open(x, y);
            if (p.percolates()) {
                break;
            }
        }
    }

    /** perform T independent experiments on an N-by-N grid */
    public PercolationStats(int N, int T, PercolationFactory pf) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException("");
        }
        this.fractions = new double[T];
        this.T = T;
        for (int i = 0; i < T; i++) {
            Percolation p = pf.make(N);
            test(p, N);
            fractions[i] = (double) p.numberOfOpenSites() / (N * N);
        }
    }

    /** sample mean of percolation threshold */
    public double mean() {
        double sum = 0.0;
        for (int i = 0; i < T; i++) {
            sum += fractions[i];
        }
        return sum / T;
    }

    /** sample standard deviation of percolation threshold */
    public double stddev() {
        double mean = mean();
        double sum = 0;
        for (int i = 0; i < T; i++) {
            sum = sum + (fractions[i] - mean) * (fractions[i] - mean);
        }
        sum = sum / (T - 1);
        return Math.sqrt(sum);
    }

    /** low endpoint of 95% confidence interval */
    public double confidenceLow() {
        double confidence = 1.96 * stddev() / ((double) Math.sqrt(T));
        return mean() - confidence;
    }

    /** // high endpoint of 95% confidence interval */
    public double confidenceHigh() {
        double confidence = 1.96 * stddev() / ((double) Math.sqrt(T));
        return mean() + confidence;
    }

}
