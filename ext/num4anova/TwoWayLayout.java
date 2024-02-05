import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.distribution.FDistribution;
import java.util.Arrays;
public class TwoWayLayout {
    private static TwoWayLayout twoWay = new TwoWayLayout();
    public static TwoWayLayout getInstance() {
        return twoWay;
    }
    public boolean[] twowayAnova(double[][][] xij, double a) {
        TwoWayAnovaTest twoway = new TwoWayAnova();

        double[] statistic = twoway.calcTestStatistic(xij);
        return twoway.execute_test(statistic, a);
    }
    public boolean[] twoway2Anova(double[][] xij, double a) {
        TwoWay2AnovaTest twoway = new TwoWay2Anova();

        double[] statistic = twoway.calcTestStatistic(xij);
        return twoway.execute_test(statistic, a);
    }
    /*********************************/
    /* interface define              */
    /*********************************/
    private interface TwoWayAnovaTest {
        double[] calcTestStatistic(double[][][] xij);
        boolean[] execute_test(double statistic[], double a);
    }
    private interface TwoWay2AnovaTest {
        double[] calcTestStatistic(double[][] xij);
        boolean[] execute_test(double statistic[], double a);
    }
    /*********************************/
    /* class define                  */
    /*********************************/
    // 二元配置の分散分析(繰り返し数が等しい時)
    private class TwoWayAnova implements TwoWayAnovaTest {
        private int a = 0;
        private int b = 0;
        private int n = 0;
        private int an = 0;
        private int bn = 0;
        private int abn = 0;
        private int en = 0;
        public double[] calcTestStatistic(double[][][] xij) {
            double statistic[] = new double[3];

            a = xij.length;
            b = xij[0].length;
            n = xij[0][0].length;
            an = a- 1;
            bn = b - 1;
            abn = (a- 1) * (b - 1);
            en = a * b * (n - 1);

            double[][] meanXij = calcMeanXij(xij);
            double[] meanAn = calcMeanAn(meanXij);
            double[] meanBn = calcMeanBn(meanXij);
            double meanABn = calcMeanABn(meanAn);

            double anDrift  = calcAnDrift(meanAn, meanABn); // 水準Ai間変動
            double bnDrift  = calcBnDrift(meanBn, meanABn); // 水準Bj間変動
                                                            // 交互作用の変動
            double interaDrift = calcInteraDrift(meanXij, meanAn, meanBn, meanABn);
            double benchDrift  = calcBenchDrift(xij, meanXij);   // 水準内変動
            double va = b * n * anDrift / an;
            double vb = a * n * bnDrift / bn;
            double vab = n * interaDrift / abn;
            double ve = benchDrift / en;

            statistic[0] = va / ve;
            statistic[1] = vb / ve;
            statistic[2] = vab/ ve;
            return statistic;
        }
        private double[][] calcMeanXij(double[][][] xij) {
            DescriptiveStatistics stat = new DescriptiveStatistics();
            double[][] meanXij = new double[a][b];

            for(int i = 0; i < a; i++) {
                for(int j = 0; j < b; j++) {
                    Arrays.stream(xij[i][j]).forEach(stat::addValue);
                    meanXij[i][j] = stat.getMean();
                    stat.clear();
                }
            }
            return meanXij;
        }
        private double[] calcMeanAn(double[][] meanXij) {
            double[] an = new double[a];

            for(int i = 0; i < a; i++) {
                double sumSa = 0.0;
                for(int j = 0; j < b; j++) {
                    an[i] += meanXij[i][j] / b;
                }
            }
            return an;
        }
        private double[] calcMeanBn(double[][] meanXij) {
            double[] bn = new double[b];

            for(int i = 0; i < a; i++) {
                for(int j = 0; j < b; j++) {
                    bn[j] += meanXij[i][j] / a;
                }
            }
            return bn;
        }
        private double calcMeanABn(double[] meanAn) {
            DescriptiveStatistics stat = new DescriptiveStatistics();

            Arrays.stream(meanAn).forEach(stat::addValue);
            return stat.getMean();
        }
        // 水準Ai間変動
        private double calcAnDrift(double[] meanAn, double meanABn) {
            double sumDrift = 0.0;

            for(int i =0; i < meanAn.length; i++) {
                double diffXi = meanAn[i] - meanABn;

                sumDrift += diffXi * diffXi;
            }
            return sumDrift;      
        }
        // 水準Bj間変動
        private double calcBnDrift(double[] meanBn, double meanABn) {
            double sumDrift = 0.0;

            for(int j = 0; j < meanBn.length; j++) {
                double diffXj = meanBn[j] - meanABn;

                sumDrift += diffXj * diffXj;
            }
            return sumDrift;
        }
        // 交互作用の変動
        private double calcInteraDrift(double[][] meanXij, double[] meanAn, double[] meanBn, double meanABn) {
            double sumDrift = 0.0;

            for(int i = 0; i< a; i++) {
                for(int j = 0; j < b; j++) {
                    double diffXj = meanXij[i][j] - meanAn[i] - meanBn[j] + meanABn;

                    sumDrift += diffXj * diffXj;
                }
            }
            return sumDrift;
        }
        // 水準内変動
        private double calcBenchDrift(double[][][] xij, double[][] meanXij) {
            double sumDrift = 0.0;

            for(int i = 0; i < a; i++) {
                for(int j = 0; j < b; j++) {
                    for(int k = 0; k < xij[i][j].length; k++) {
                        double diffXj = xij[i][j][k] - meanXij[i][j];

                        sumDrift += diffXj * diffXj;
                    }
                }
            }
            return sumDrift;
        }
        public boolean[] execute_test(double statistic[], double a) {
            boolean[] ret = new boolean[3];

            ret[0] = evaluation(new FDistribution(an, en), statistic[0], a);
            ret[1] = evaluation(new FDistribution(bn, en), statistic[1], a);
            ret[2] = evaluation(new FDistribution(abn, en), statistic[2], a);
            return ret;
        }
        private boolean evaluation(FDistribution fDist, double statistic, double a) {
            double r_val = fDist.inverseCumulativeProbability(1.0 - a);

            return (statistic >= r_val) ? true : false;
        }
    }
    // 二元配置の分散分析(繰り返しのない時)
    private class TwoWay2Anova implements TwoWay2AnovaTest {
        private int a = 0;
        private int b = 0;
        private int an = 0;
        private int bn = 0;
        private int en = 0;
        public double[] calcTestStatistic(double[][] xij) {
            double statistic[] = new double[2];

            a = xij.length;
            b = xij[0].length;
            an = a- 1;
            bn = b - 1;
            en = (a- 1) * (b - 1);

            double[] meanAn = calcMeanAn(xij);
            double[] meanBn = calcMeanBn(xij);
            double meanAB = calcMeanAB(meanAn);

            double anDrift  = calcAnDrift(meanAn, meanAB); // 水準Ai間変動
            double bnDrift  = calcBnDrift(meanBn, meanAB); // 水準Bj間変動
            double benchDrift  = calcBenchDrift(xij, meanAn, meanBn, meanAB);   // 水準内変動
            double va = anDrift / an;
            double vb = bnDrift / bn;
            double ve = benchDrift / en;

            statistic[0] = va / ve;
            statistic[1] = vb / ve;
            return statistic;
        }
        private double[] calcMeanAn(double[][] xij) {
            double[] an = new double[a];

            for(int i = 0; i < a; i++) {
                for(int j = 0; j < b; j++) {
                    an[i] += xij[i][j] / b;
                }
            }
            return an;
        }
        private double[] calcMeanBn(double[][] xij) {
            double[] bn = new double[b];

            for(int i = 0; i < a; i++) {
                for(int j = 0; j < b; j++) {
                    bn[j] += xij[i][j] / a;
                }
            }
            return bn;
        }
        private double calcMeanAB(double[] meanAn) {
            DescriptiveStatistics stat = new DescriptiveStatistics();

            Arrays.stream(meanAn).forEach(stat::addValue);
            return stat.getMean();
        }
        // 水準Ai間変動
        private double calcAnDrift(double[] meanAn, double meanAB) {
            double sumDrift = 0.0;

            for(int i =0; i < meanAn.length; i++) {
                double diffXi = meanAn[i] - meanAB;

                sumDrift += diffXi * diffXi;
            }
            return b * sumDrift;      
        }
        // 水準Bj間変動
        private double calcBnDrift(double[] meanBn, double meanAB) {
            double sumDrift = 0.0;

            for(int j = 0; j < meanBn.length; j++) {
                double diffXj = meanBn[j] - meanAB;

                sumDrift += diffXj * diffXj;
            }
            return a * sumDrift;
        }
        // 水準内変動
        private double calcBenchDrift(double[][] xij, double[] meanAn, double[] meanBn, double meanAB) {
            double sumDrift = 0.0;

            for(int i = 0; i < a; i++) {
                for(int j = 0; j < b; j++) {
                    double diffXj = xij[i][j] - meanAn[i] - meanBn[j] + meanAB;

                    sumDrift += diffXj * diffXj;
                }
            }
            return sumDrift;
        }

        public boolean[] execute_test(double statistic[], double a) {
            boolean[] ret = new boolean[2];

            ret[0] = evaluation(new FDistribution(an, en), statistic[0], a);
            ret[1] = evaluation(new FDistribution(bn, en), statistic[1], a);
            return ret;
        }
        private boolean evaluation(FDistribution fDist, double statistic, double a) {
            double r_val = fDist.inverseCumulativeProbability(1.0 - a);

            return (statistic >= r_val) ? true : false;
        }
    }
}

