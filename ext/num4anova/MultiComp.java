import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
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
        public boolean scheffe_test(double[][] xi, double a) {
            return false;
        }

        public boolean[][] twosideTest(double[][] xi, double a) {
            HypothesisTest hypoth = new TwoSideTest();
            double[][] statistic = hypoth.calcTestStatistic(xi);

            return hypoth.executeTest(statistic, a / 2.0);
        }
        public boolean[][] rightsideTest(double[][] xi, double a) {
            HypothesisTest hypoth = new RightSideTest();
            double[][] statistic = hypoth.calcTestStatistic(xi);

            return hypoth.executeTest(statistic, a);
        }
        public boolean[][] leftsideTest(double[][] xi, double a) {
            HypothesisTest hypoth = new LeftSideTest();
            double[][] statistic = hypoth.calcTestStatistic(xi);

            return hypoth.executeTest(statistic, a);
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
            // スチューデント化された範囲のa点
            // v=120
            //   k=10  q= 4.5595 p2/a=0.016282   1/61.417516276
            //   k=8   q= 4.3630 p2/a=0.025263   1/39.583580731
            //   k=6   q= 4.0960 p2/a=0.044883   1/22.280150614
            //   k=5   q= 3.9169 p2/a=0.065040   1/15.375153752
            //   k=4   q= 3.6846 p2/a=0.103377   1/9.673331592
            //   k=3   q= 3.3561 p2/a=0.192270   1/5.2010194
            //   k=2   q= 2.8000 p2/a=0.500036   1/2
            // v=14
            //   k=10  q= 5.2534 p2/a=0.023091   1/43.306916115
            //   k=8   q= 4.9903 p2/a=0.033394   1/29.945499191   
            //   k=6   q= 4.6385 p2/a=0.054768   1/18.258837277   
            //   k=5   q= 4.4066 p2/a=0.075883   1/13.178182202   
            //   k=4   q= 4.1105 p2/a=0.114920   1/8.701705534
            //   k=3   q= 3.7014 p2/a=0.202830   1/4.930237144
            //   k=2   q= 3.0332 p2/a=0.499991   1/2.000036001   
            // v=12
            //   k=10  q= 5.3946 p2/a=0.024637   1/40.58935747
            //   k=8   q= 5.1187 p2/a=0.035180   1/28.425241615   
            //   k=6   q= 4.7502 p2/a=0.056858   1/17.587674558 
            //   k=5   q= 4.5077 p2/a=0.078128   1/12.799508499
            //   k=4   q= 4.1987 p2/a=0.117245   1/8.529148365 
            //   k=3   q= 3.7729 p2/a=0.204923   1/4.879881712   
            //   k=2   q= 3.0813 p2/a=0.500004   1/2
            // v=10
            //   k=10  q= 5.5984 p2/a=0.026922   1/37.144342917
            //   k=8   q= 5.3042 p2/a=0.037799   1/26.455726342
            //   k=6   q= 4.9128 p2/a=0.059822   1/16.716258233
            //   k=5   q= 4.6543 p2/a=0.081351   1/12.292411894
            //   k=4   q= 4.3266 p2/a=0.120575   1/8.293593199   
            //   k=3   q= 3.8768 p2/a=0.207859   1/4.810953579
            //   k=2   q= 3.1511 p2/a=0.499979   1/2
            // v=9
            //   k=10  q= 5.7384 p2/a=0.028519   1/35.06434307
            //   k=8   q= 5.4312 p2/a=0.039639   1/25.22767981
            //   k=6   q= 5.0235 p2/a=0.061950   1/16.14205004
            //   k=5   q= 4.7554 p2/a=0.083549   1/11.969024165
            //   k=4   q= 4.4149 p2/a=0.122828   1/8.141466115   
            //   k=3   q= 3.9485 p2/a=0.209848   1/4.76535397
            //   k=2   q= 3.1992 p2/a=0.499985   1/2
            private double qvalue(int k, int v, double a) {
                double den = (k-1) * (2 + k) / 2;
                double p = 1.0 - a / den;
                TDistribution tDist = new TDistribution(v);
                double t = 
                    tDist.inverseCumulativeProbability(p);
                return Math.sqrt(2) * t;
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
        // ダネット法
        private class DunnetTest{
            private int k = 0;
            private int v = 0;
            private double[] mean = null;
            private double[] n = null;
            protected int getK() { return k;}
            protected int getV() { return v;}
            public double[][] calcTestStatistic(double[][] xi) {
                k = xi.length;
                mean = new double[k];
                n = new double[k];
                double[][] statistic = new double[k][k];
                double ve = calcVe(xi);

                for(int i = 0; i < k; i++) {
                    for(int j = 0; j < k; j++) {
                        statistic[i][j] = (mean[j] - mean[i]) 
                                        / Math.sqrt(ve * (1.0 / n[j] + 1.0 / n[i]));
                    }
                }                
                return statistic;
            }
            private double calcVe(double[][] xi) {
                double sumSq = 0.0;
                int sumN = 0;
                for(int i = 0; i < k; i++) {
                    DescriptiveStatistics stat = new DescriptiveStatistics();
                    Arrays.stream(xi[i]).forEach(stat::addValue);
                    mean[i] = stat.getMean();
                    n[i] = stat.getN();
                    sumSq += (n[i] - 1) * stat.getVariance();
                    sumN += n[i];
                    stat.clear();
                }
                v = sumN - k;
                return sumSq / v;
            }
        }        
        private class TwoSideTest extends DunnetTest 
                                  implements HypothesisTest {
            public boolean[][] executeTest(double[][] statistic, double a) {
                int v = super.getV();
                int k = super.getK();
                double den = k - 1;
                double p = 1.0 - a / den;
                TDistribution tDist = new TDistribution(v);
                double l_val = tDist.inverseCumulativeProbability(a  / den);
                double r_val = tDist.inverseCumulativeProbability(1.0 - a / den);
                boolean[][] ret = new boolean[k][k];

                for(int i = 0; i < k; i++) {
                    for(int j = 0; j < k; j++) {
                       ret[i][j] = evaluation(statistic[i][j], l_val, r_val ); 
                    }
                }
                return ret;
            }
            private boolean evaluation(double statistic, double l_val, double r_val) {
                boolean ret = true;

                if ((l_val < statistic) && (statistic < r_val)) {
                    ret = false;
                }
                return ret;
            }
        }
        private class RightSideTest extends DunnetTest 
                                  implements HypothesisTest {
            public boolean[][] executeTest(double[][] statistic, double a) {
                int v = super.getV();
                int k = super.getK();
                double den = k - 1;
                double p = 1.0 - a / den;
                TDistribution tDist = new TDistribution(v);
                double r_val = tDist.inverseCumulativeProbability(1.0 - a);
                boolean[][] ret = new boolean[k][k];

                for(int i = 0; i < k; i++) {
                    for(int j = 0; j < k; j++) {
                       ret[i][j] = evaluation(statistic[i][j], r_val ); 
                    }
                }
                return ret;
            }
            private boolean evaluation(double statistic, double r_val) {
                boolean ret = true;

                if (statistic < r_val) {
                    ret = false;
                }
                return ret;
            }
        }
        private class LeftSideTest extends DunnetTest 
                                  implements HypothesisTest {
            public boolean[][] executeTest(double[][] statistic, double a) {
                int v = super.getV();
                int k = super.getK();
                double den = k - 1;
                double p = a / den;
                TDistribution tDist = new TDistribution(v);
                double l_val = tDist.inverseCumulativeProbability(a);
                boolean[][] ret = new boolean[k][k];

                for(int i = 0; i < k; i++) {
                    for(int j = 0; j < k; j++) {
                       ret[i][j] = evaluation(statistic[i][j], l_val ); 
                    }
                }
                return ret;
            }
            private boolean evaluation(double statistic, double l_val) {
                boolean ret = true;

                if (l_val < statistic) {
                    ret = false;
                }
                return ret;
            }
        }
    }
}

