import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.TDistribution;

public class Ancova {
    private static Ancova ancova = new Ancova();
    public static Ancova getInstance() {
        return ancova;
    }
    public boolean parallelTest(double[][][] xi, double a) {
        HypothesisTest hypoth = new Parallettest();

        double statistic = hypoth.calcTestStatistic(xi);
        return hypoth.executeTest(statistic, a);
    }
    public boolean significanceTest(double[][][] xi, double a) {
        HypothesisTest hypoth = new SignificanceTest();

        double statistic = hypoth.calcTestStatistic(xi);
        return hypoth.executeTest(statistic, a);
    }
    public boolean differenceTest(double[][][] xi, double a) {
        HypothesisTest hypoth = new DifferenceTest();

        double statistic = hypoth.calcTestStatistic(xi);
        return hypoth.executeTest(statistic, a);
    }
    public Interval intervalEstim(double[][][] xi, double a) {
        Estim estim = new IntervalEstim();

        return estim.calcInterval(xi, a);
    }
    /*********************************/
    /* interface define              */
    /*********************************/
    private interface HypothesisTest {
        double calcTestStatistic(double[][][] xi);
        boolean executeTest(double statistic, double a);
    }
    private interface Estim {
        Interval calcInterval(double[][][] xi, double a);
    }
    /*********************************/
    /* class define                  */
    /*********************************/
    public class Interval {
        private double min;
        private double max;
        public Interval(double min, double max) {
            this.min = min;
            this.max = max;
        }
        public double getMin() { return this.min; }
        public double getMax() { return this.max; }
    }
    private class RegressionLine {
        protected int calcSumn(double[][][] xi) {
            int sum = 0;

            for(int i = 0; i < xi.length; i++) {
                sum += xi[i].length;
            }
            return sum;
        }
        // 全変動
        private double calcSty(double[][][] xi, int sumn) {
            double sumy2 = calcSumy2(xi);
            double sumy = calcSumy(xi);

            return sumy2 - sumy*sumy / sumn;
        }
        private double calcStx(double[][][] xi, int sumn) {
            double sumx2 = calcSumx2(xi);
            double sumx = calcSumx(xi);

            return sumx2 - sumx*sumx / sumn;
        }
        private double calcStyx(double[][][] xi, int sumn) {
            double sumx = calcSumx(xi);
            double sumy = calcSumy(xi);
            double sumyx = calcSumyx(xi);

            return sumyx - sumy*sumx / sumn;
        }
        // 水準間変動
        private double calcSay(double[][][] xi, int sumn) {
            double sumy = calcSumy(xi);

            return calcSumay(xi) - sumy*sumy / sumn;
        }
        private double calcSax(double[][][] xi, int sumn) {
            double sumx = calcSumx(xi);

            return calcSumax(xi) - sumx*sumx / sumn;
        }
        private double calcSayx(double[][][] xi, int sumn) {
            double sumx = calcSumx(xi);
            double sumy = calcSumy(xi);

            return calcSumayx(xi) - sumy*sumx / sumn;
        }
        // 水準内変動
        protected double calcSex(double[][][] xi, int sumn) {
            return calcStx(xi, sumn) - calcSax(xi, sumn);
        }
        protected double calcSey(double[][][] xi, int sumn) {
            return calcSty(xi, sumn) - calcSay(xi, sumn);
        }
        protected double calcSeyx(double[][][] xi, int sumn) {
            return calcStyx(xi, sumn) - calcSayx(xi, sumn);
        }

        // 平行性の検定
        protected double calcbx(double[][][] xi) {
            double sum = 0.0;

            for (int i = 0; i < xi.length; i++) {
                int n = xi[i].length;
                double sumx = 0.0;
                double sumy = 0.0;
                double sumyx = 0.0;
                double sumx2 = 0.0;
                for (int j = 0; j < n; j++) {
                    sumx +=  xi[i][j][1];
                    sumy +=  xi[i][j][0];
                    sumyx += xi[i][j][1] * xi[i][j][0];

                    sumx2 += xi[i][j][1] * xi[i][j][1];

                }
                double wki = n * sumyx - sumy * sumx;
                double wk = wki * wki / (n * (n * sumx2 - sumx * sumx));

                sum += wk;
            }
            return sum;
        }
        // 差の検定
        protected double calcSa(double[][][] xi, int sumn) {
            double sumty = calcSty(xi, sumn);
            double sumtyx = calcStyx(xi, sumn);
            double sumtx  = calcStx(xi, sumn);
            double sumey  = calcSey(xi, sumn);
            double sumeyx = calcSeyx(xi, sumn);
            double sumex  = calcSex(xi, sumn);

            return (sumty - sumtyx * sumtyx / sumtx)
                 - (sumey - sumeyx * sumeyx / sumex);
        }

        // ETC
        private double calcSumay(double[][][] xi) {
            double sum = 0.0;

            for (int i = 0; i < xi.length; i++) {
                double sumyi = 0.0;
                for (int j = 0; j < xi[i].length; j++) {
                    sumyi += xi[i][j][0];
                }
                sum += sumyi * sumyi / xi[i].length;
            }
            return sum;
        }
        private double calcSumy2(double[][][] xi) {
            double sum = 0.0;

            for (int i = 0; i < xi.length; i++) {
                for (int j = 0; j < xi[i].length; j++) {
                    sum += xi[i][j][0] * xi[i][j][0];
                }
            }
            return sum;
        }
        private double calcSumx(double[][][] xi) {
            double sum = 0.0;

            for (int i = 0; i < xi.length; i++) {
                for (int j = 0; j < xi[i].length; j++) {
                    sum += xi[i][j][1];
                }
            }
            return sum;
        }
        protected double calcSumx2(double[][][] xi) {
            double sum = 0.0;

            for (int i = 0; i < xi.length; i++) {
                for (int j = 0; j < xi[i].length; j++) {
                    sum += xi[i][j][1] * xi[i][j][1];
                }
            }
            return sum;
        }
        protected double calcSumyx(double[][][] xi) {
            double sum = 0.0;

            for (int i = 0; i < xi.length; i++) {
                for (int j = 0; j < xi[i].length; j++) {
                    sum += xi[i][j][0] * xi[i][j][1];
                }
            }
            return sum;
        }
        private double calcSumayx(double[][][] xi) {
            double sum = 0.0;

            for (int i = 0; i < xi.length; i++) {
                double sumxi = 0.0;
                double sumyi = 0.0;
                for (int j = 0; j < xi[i].length; j++) {
                    sumxi += xi[i][j][1];
                    sumyi += xi[i][j][0];
                }
                sum += sumxi * sumyi / xi[i].length;
            }
            return sum;
        }
        private double calcSumax(double[][][] xi) {
            double sum = 0.0;

            for (int i = 0; i < xi.length; i++) {
                double sumxi = 0.0;
                for (int j = 0; j < xi[i].length; j++) {
                    sumxi += xi[i][j][1];
                }
                sum += sumxi * sumxi / xi[i].length;
            }
            return sum;
        }
        private double calcSumy(double[][][] xi) {
            double sum = 0.0;

            for (int i = 0; i < xi.length; i++) {
                for (int j = 0; j < xi[i].length; j++) {
                    sum += xi[i][j][0];
                }
            }
            return sum;
        }

    }
    // 回帰直線モデルの平行性の検定
    private class Parallettest extends RegressionLine implements HypothesisTest {
        private int n = 0;
        private int m = 0;
        public double calcTestStatistic(double[][][] xi) {
            int sumn = calcSumn(xi);
            n = xi.length - 1;
            m = sumn - 2 * xi.length;
 
            double vnp = calcVnp(xi, sumn);
            double ve2 = calcVe2(xi, sumn);

            return vnp / ve2;
        }
        private double calcVnp(double[][][] xi, int sumn){
            return calcSnp(xi, sumn) / n;
        }
        private double calcSnp(double[][][] xi, int sumn) {
            double sumbx = calcbx(xi);
            double sumeyx = calcSeyx(xi, sumn);
            double sumex = calcSex(xi, sumn);

            return sumbx - sumeyx * sumeyx / sumex;
        }
        private double calcVe2(double[][][] xi, int sumn) {
            return calcSe2(xi, sumn) / m;
        }
        private double calcSe2(double[][][] xi, int sumn) {
            double sumey = calcSey(xi, sumn);
            double sumbx = calcbx(xi);

            return sumey - sumbx;
        }
        public boolean executeTest(double statistic, double a) {
            FDistribution fDist = new FDistribution(n, m);
            double f = fDist.inverseCumulativeProbability(1.0 - a);

            return (statistic >= f) ? true : false;
        }
    }
    // 回帰直線モデルの平行性の検定
    private class SignificanceTest extends RegressionLine implements HypothesisTest {
        private int n = 0;
        private int m = 0;
        public double calcTestStatistic(double[][][] xi) {
            int sumn = calcSumn(xi);
            n = 1;
            m = sumn - xi.length - 1;

            double vr = calcVr(xi, sumn);
            double ve = calcVe(xi, sumn);

            return vr / ve;
        }
        public boolean executeTest(double statistic, double a) {
            FDistribution fDist = new FDistribution(n, m);
            double f = fDist.inverseCumulativeProbability(1.0 - a);

            return (statistic >= f) ? true : false;
        }
        private double calcVr(double[][][] xi, int sumn) {
            double sumeyx = calcSeyx(xi, sumn);
            double sumex = calcSex(xi, sumn);

            return (sumeyx * sumeyx) / sumex;
        }
        private double calcVe(double[][][] xi, int sumn) {
            double sumey = calcSey(xi, sumn);
            double sumex = calcSex(xi, sumn);
            double sumeyx = calcSeyx(xi, sumn);

            return (sumey * sumex - sumeyx * sumeyx) / (m * sumex);
        }
    }
    // 水準間の差の検定
    private class DifferenceTest extends RegressionLine implements HypothesisTest {
        private int n = 0;
        private int m = 0;
        public double calcTestStatistic(double[][][] xi) {
            int sumn = calcSumn(xi);
            n = xi.length - 1;
            m = sumn - xi.length - 1;

            double va = calcSa(xi, sumn) / n;
            double ve = calcVe(xi, sumn);

            return va / ve;
        }
        public boolean executeTest(double statistic, double a) {
            FDistribution fDist = new FDistribution(n, m);
            double f = fDist.inverseCumulativeProbability(1.0 - a);

            return (statistic >= f) ? true : false;
        }
        private double calcVe(double[][][] xi, int sumn) {
            double sumey = calcSey(xi, sumn);
            double sumex = calcSex(xi, sumn);
            double sumeyx = calcSeyx(xi, sumn);

            return (sumey * sumex - sumeyx * sumeyx) / (m * sumex);
        }
    }
    //
    private class IntervalEstim extends RegressionLine 
                                implements Estim {
        private int n = 0;
        private int[] ni = null;
        private double sumex = 0.0;
        public Interval calcInterval(double[][][] xi, double a) {
            ni = calcNi(xi);
            int sumn = calcSumn(xi);
            n = sumn - xi.length - 1;
            sumex = calcSex(xi, sumn);
            double ve = calcVe(xi, sumn);
            double b = calcB(xi, sumn);

            double[] meanyi = calcMeanyi(xi);
            double[] meanxi = calcMeanxi(xi);
            double meanx = calcMeanx(xi);

            TDistribution tDist = new TDistribution(n);
            double t = tDist.inverseCumulativeProbability(1.0 - a / 2.0);
            double wk = (meanxi[0] - meanx);
            double wk2 = t * Math.sqrt((1/ni[0] + wk * wk / sumex) * ve);
            double min = meanyi[0] - b * wk - wk2;
            double max = meanyi[0] - b * wk + wk2;

            return new Interval(min, max);
        }
        private int[] calcNi(double[][][] xi) {
            int[] ni = new int[xi.length];

            for(int i = 0; i < xi.length; i++) {
                ni[i] = xi[i].length;
            }
            return ni;
        }
        private double calcVe(double[][][] xi, int sumn) {
            double sumey = calcSey(xi, sumn);
            double sumeyx = calcSeyx(xi, sumn);

            return (sumey * sumex - sumeyx * sumeyx) / (n * sumex);
        }
        private double calcB(double[][][] xi, int sumn) {
            double sumeyx = calcSeyx(xi, sumn);
            double sex = calcSex(xi, sumn);
            
            return sumeyx / sex;
        }
        private double[] calcMeanyi(double[][][] xi) {
            double[] meanyi = new double[xi.length];

            for(int i = 0; i < xi.length; i++) {
                double sum = 0.0;
                for(int j = 0; j < xi[i].length; j++) {
                    sum += xi[i][j][0];
                }
                meanyi[i] = sum / xi[i].length;
            }
            return meanyi;
        }
        private double[] calcMeanxi(double[][][] xi) {
            double[] meanxi = new double[xi.length];

            for(int i = 0; i < xi.length; i++) {
                double sum = 0.0;
                for(int j = 0; j < xi[i].length; j++) {
                    sum += xi[i][j][1];
                }
                meanxi[i] = sum / xi[i].length;
            }
            return meanxi;
        }
        private double calcMeanx(double[][][] xi) {
            double sum = 0.0;
            double n = 0;
            for(int i = 0; i < xi.length; i++) {
                for(int j = 0; j < xi[i].length; j++) {
                    sum += xi[i][j][1];
                    n++;
                }
            }
            return sum / n;
        }
    }
}

