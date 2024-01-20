import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.TDistribution;

import org.apache.commons.math3.util.Combinations;
public class MultiComp {
    public static class ParametrixTest {
        private static ParametrixTest paramTest = new ParametrixTest();
        public static ParametrixTest getInstance() {
            return paramTest;
        }
        public boolean[][] turkeyTest(double[][] xi, double a) {
            HypothesisTest hypoth = new TurkeyTest();

            double[][] statistic = hypoth.calcTestStatistic(xi);
            return hypoth.executeTest(statistic, a);
        }
        public boolean[][] bonferronoTest(double[][] xi, double a) {
            HypothesisTest hypoth = new BonferroniTest();

            double[][] statistic = hypoth.calcTestStatistic(xi);
            return hypoth.executeTest(statistic, a * 0.5);
        }
        /*********************************/
        /* interface define              */
        /*********************************/
        private interface HypothesisTest {
            double[][] calcTestStatistic(double[][] xi);
            boolean[][] executeTest(double[][] statistic, double a);
        }
        /*********************************/
        /* Class define                  */
        /*********************************/
        // turkey法
        private class TurkeyTest implements HypothesisTest {
            private int m = 0;
            private int n = 0;
            public double[][] calcTestStatistic(double[][] xi) {
                n = xi.length;                  // a
                m = xi[0].length;               // n
                double[][] statistic = new double[n][n];
                double[] mean = calcStdMean(xi);
                double ve = calcVe(xi);

                for(int i = 0; i < n; i++) {
                    for(int j = i; j < n; j++) {
                        statistic[i][j] = 
                            Math.abs(mean[i] - mean[j]) / Math.sqrt(ve / m);
                    }
                }
  
                return statistic;

            }
            public boolean[][] executeTest(double[][] statistic, double a) {
                int v = n * (m - 1);
                double q = qvalue(n, v, a);
                boolean[][] ret = new boolean[n][n];

                for(int i = 0; i < n; i++) {
                    for(int j = i; j < n; j++) {
                        ret[i][j] = (statistic[i][j]  >= q) ? true : false;
                    }
                }
                return ret;
            }

            private double[] calcStdMean(double[][] xi) {
                int n = xi.length;
                double[] mean = new double[n];

                for(int i = 0; i < n; i++) {
                    DescriptiveStatistics stat = new DescriptiveStatistics();
                    Arrays.stream(xi[i]).forEach(stat::addValue);

                    mean[i] = stat.getMean();
                    stat.clear();
                }
                return mean;
            } 
            private double calcVe(double[][] xi) {
                int n = xi.length;
                int m = xi[0].length;
                double na = n * (m - 1);
                double sumSq = 0.0;

                for(int i = 0; i < n; i++) {
                    DescriptiveStatistics stat = new DescriptiveStatistics();
                    Arrays.stream(xi[i]).forEach(stat::addValue);
                    double mean = stat.getMean();
                    for (int j = 0; j < m; j++) {
                        double diff = xi[i][j] - mean;
                        sumSq = sumSq + diff * diff;
                    }
                    stat.clear();
                }
                return sumSq / na;
            } 
            private double qvalue(int k, int v, double a) {
                TDistribution tDist = new TDistribution(v - 1);
                double t = 
                    tDist.inverseCumulativeProbability(1.0 - a / 2.0);

                return t * Math.sqrt(k- 1);
            }
        }
        // ボンフェロー法
        private class BonferroniTest implements HypothesisTest {
            private int k = 0;
            private int n = 0;
            private int m = 0;
            public double[][] calcTestStatistic(double[][] xi) {
                n = xi.length;
                m = xi[0].length;
                double[][] statistic = new double[n][n];
                double[] mean = calcStdMean(xi);
                Combinations c = new Combinations(n, 2);
                List<int[]> al = new ArrayList<>();
                for(int[] iterate : c) {
                    al.add(iterate);
                }
                k = al.size();
                double ve = calcVe(xi);
                for(int[] array : al) {
                    int i = array[0];
                    int j = array[1];
                    int n1 = xi[i].length;
                    int n2 = xi[j].length;

                    statistic[i][j] = Math.abs(mean[i] - mean[j])
                                    / Math.sqrt((1.0 / n1 + 1.0 / n2) * ve);
                }
                return statistic;
            }
            public boolean[][] executeTest(double[][] statistic, double a) {
                boolean[][] ret = new boolean[n][n];
                double na = n * (m - 1);
                TDistribution tDist = new TDistribution(na);

                double t = tDist.inverseCumulativeProbability(1.0 - a / k);

                for(int i = 0; i < n; i++) {
                    for(int j = i; j < n; j++) {
                        ret[i][j] = (statistic[i][j] >= t) ? true : false;
                    }
                }
                return ret;
            }
            private double[] calcStdMean(double[][] xi) {
                double[] mean = new double[n];

                for(int i = 0; i < n; i++) {
                    DescriptiveStatistics stat = new DescriptiveStatistics();
                    Arrays.stream(xi[i]).forEach(stat::addValue);

                    mean[i] = stat.getMean();
                    stat.clear();
                }
                return mean;
            }
            private double calcVe(double[][] xi) {
                double na = n * (m - 1);
                double sumSq = 0.0;

                for(int i = 0; i < n; i++) {
                    DescriptiveStatistics stat = new DescriptiveStatistics();
                    Arrays.stream(xi[i]).forEach(stat::addValue);
                    double mean = stat.getMean();
                    for (int j = 0; j < m; j++) {
                        double diff = xi[i][j] - mean;
                        sumSq = sumSq + diff * diff;
                    }
                    stat.clear();
                }
                return sumSq / na;
            } 
        }
    }
}

