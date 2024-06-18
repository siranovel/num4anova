import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.TDistribution;

public class Ancova {
    private static Ancova ancova = new Ancova();
    public static Ancova getInstance() {
        return ancova;
    }
    public boolean parallelTest(double yi[][], double[][] xi, double a) {
        HypothesisTest hypoth = new Parallettest();

        double statistic = hypoth.calcTestStatistic(yi, xi);
        return hypoth.executeTest(statistic, a);
    }
    public boolean significanceTest(double yi[][], double[][] xi, double a) {
        HypothesisTest hypoth = new SignificanceTest();

        double statistic = hypoth.calcTestStatistic(yi, xi);
        return hypoth.executeTest(statistic, a);
    }
    public boolean differenceTest(double yi[][], double[][] xi, double a) {
        HypothesisTest hypoth = new DifferenceTest();

        double statistic = hypoth.calcTestStatistic(yi, xi);
        return hypoth.executeTest(statistic, a);
    }
    public Interval[] intervalEstim(double yi[][], double[][] xi, double a) {
        Estim estim = new IntervalEstim();

        return estim.calcInterval(yi, xi, a);
    }
    /*********************************/
    /* interface define              */
    /*********************************/
    private interface HypothesisTest {
        double calcTestStatistic(double yi[][], double[][] xi);
        boolean executeTest(double statistic, double a);
    }
    private interface Estim {
        Interval[] calcInterval(double yi[][], double[][] xi, double a);
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
        protected int calcSumn(double[][] xi) {
            int sum = 0;

            for(int i = 0; i < xi.length; i++) {
                sum += xi[i].length;
            }
            return sum;
        }
        // 全変動
        private double calcSty(double yi[][], int sumn) {
            double sumy2 = calcSumy2(yi);
            double sumy = calcSumy(yi);

            return sumy2 - sumy*sumy / sumn;
        }
        private double calcStx(double[][] xi, int sumn) {
            double sumx2 = calcSumx2(xi);
            double sumx = calcSumx(xi);

            return sumx2 - sumx*sumx / sumn;
        }
        private double calcStyx(double yi[][], double[][] xi, int sumn) {
            double sumx = calcSumx(xi);
            double sumy = calcSumy(yi);
            double sumyx = calcSumyx(yi, xi);

            return sumyx - sumy*sumx / sumn;
        }
        // 水準間変動
        private double calcSay(double yi[][], int sumn) {
            double sumy = calcSumy(yi);

            return calcSumay(yi) - sumy*sumy / sumn;
        }
        private double calcSax(double[][] xi, int sumn) {
            double sumx = calcSumx(xi);

            return calcSumax(xi) - sumx*sumx / sumn;
        }
        private double calcSayx(double yi[][], double[][] xi, int sumn) {
            double sumx = calcSumx(xi);
            double sumy = calcSumy(yi);

            return calcSumayx(yi, xi) - sumy*sumx / sumn;
        }
        // 水準内変動
        protected double calcSex(double[][] xi, int sumn) {
            return calcStx(xi, sumn) - calcSax(xi, sumn);
        }
        protected double calcSey(double yi[][], int sumn) {
            return calcSty(yi, sumn) - calcSay(yi, sumn);
        }
        protected double calcSeyx(double yi[][], double[][] xi, int sumn) {
            return calcStyx(yi, xi, sumn) - calcSayx(yi, xi, sumn);
        }

        // 平行性の検定
        protected double calcbx(double yi[][], double[][] xi) {
            double sum = 0.0;

            for (int i = 0; i < yi.length; i++) {
                int n = yi[i].length;
                double sumx = 0.0;
                double sumy = 0.0;
                double sumyx = 0.0;
                double sumx2 = 0.0;
                for (int j = 0; j < n; j++) {
                    sumx +=  xi[i][j];
                    sumy +=  yi[i][j];
                    sumyx += xi[i][j] * yi[i][j];

                    sumx2 += xi[i][j] * xi[i][j];

                }
                double wki = n * sumyx - sumy * sumx;
                double wk = wki * wki / (n * (n * sumx2 - sumx * sumx));

                sum += wk;
            }
            return sum;
        }
        // 差の検定
        protected double calcSa(double yi[][], double[][] xi, int sumn) {
            double sumty = calcSty(yi, sumn);
            double sumtyx = calcStyx(yi, xi, sumn);
            double sumtx  = calcStx(xi, sumn);
            double sumey  = calcSey(yi, sumn);
            double sumeyx = calcSeyx(yi, xi, sumn);
            double sumex  = calcSex(xi, sumn);

            return (sumty - sumtyx * sumtyx / sumtx)
                 - (sumey - sumeyx * sumeyx / sumex);
        }

        // ETC
        private double calcSumay(double yi[][]) {
            double sum = 0.0;

            for (int i = 0; i < yi.length; i++) {
                double sumyi = 0.0;
                for (int j = 0; j < yi[i].length; j++) {
                    sumyi += yi[i][j];
                }
                sum += sumyi * sumyi / yi[i].length;
            }
            return sum;
        }
        private double calcSumy2(double yi[][]) {
            double sum = 0.0;

            for (int i = 0; i < yi.length; i++) {
                for (int j = 0; j < yi[i].length; j++) {
                    sum += yi[i][j] * yi[i][j];
                }
            }
            return sum;
        }
        private double calcSumx(double[][] xi) {
            double sum = 0.0;

            for (int i = 0; i < xi.length; i++) {
                for (int j = 0; j < xi[i].length; j++) {
                    sum += xi[i][j];
                }
            }
            return sum;
        }
        protected double calcSumx2(double[][] xi) {
            double sum = 0.0;

            for (int i = 0; i < xi.length; i++) {
                for (int j = 0; j < xi[i].length; j++) {
                    sum += xi[i][j] * xi[i][j];
                }
            }
            return sum;
        }
        protected double calcSumyx(double yi[][], double[][] xi) {
            double sum = 0.0;

            for (int i = 0; i < yi.length; i++) {
                for (int j = 0; j < yi[i].length; j++) {
                    sum += yi[i][j] * xi[i][j];
                }
            }
            return sum;
        }
        private double calcSumayx(double yi[][], double[][] xi) {
            double sum = 0.0;

            for (int i = 0; i < yi.length; i++) {
                double sumxi = 0.0;
                double sumyi = 0.0;
                for (int j = 0; j < yi[i].length; j++) {
                    sumxi += xi[i][j];
                    sumyi += yi[i][j];
                }
                sum += sumxi * sumyi / xi[i].length;
            }
            return sum;
        }
        private double calcSumax(double[][] xi) {
            double sum = 0.0;

            for (int i = 0; i < xi.length; i++) {
                double sumxi = 0.0;
                for (int j = 0; j < xi[i].length; j++) {
                    sumxi += xi[i][j];
                }
                sum += sumxi * sumxi / xi[i].length;
            }
            return sum;
        }
        private double calcSumy(double yi[][]) {
            double sum = 0.0;

            for (int i = 0; i < yi.length; i++) {
                for (int j = 0; j < yi[i].length; j++) {
                    sum += yi[i][j];
                }
            }
            return sum;
        }

    }
    // 回帰直線モデルの平行性の検定
    private class Parallettest extends RegressionLine implements HypothesisTest {
        private int n = 0;
        private int m = 0;
        public double calcTestStatistic(double yi[][], double[][] xi) {
            int sumn = calcSumn(xi);
            n = yi.length - 1;
            m = sumn - 2 * yi.length;
 
            double vnp = calcVnp(yi, xi, sumn);
            double ve2 = calcVe2(yi, xi, sumn);

            return vnp / ve2;
        }
        private double calcVnp(double yi[][], double[][] xi, int sumn){
            return calcSnp(yi, xi, sumn) / n;
        }
        private double calcSnp(double yi[][], double[][] xi, int sumn) {
            double sumbx = calcbx(yi, xi);
            double sumeyx = calcSeyx(yi, xi, sumn);
            double sumex = calcSex(xi, sumn);

            return sumbx - sumeyx * sumeyx / sumex;
        }
        private double calcVe2(double yi[][], double[][] xi, int sumn) {
            return calcSe2(yi, xi, sumn) / m;
        }
        private double calcSe2(double yi[][], double[][] xi, int sumn) {
            double sumey = calcSey(yi, sumn);
            double sumbx = calcbx(yi, xi);

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
        public double calcTestStatistic(double yi[][], double[][] xi) {
            int sumn = calcSumn(xi);
            n = 1;
            m = sumn - xi.length - 1;

            double vr = calcVr(yi, xi, sumn);
            double ve = calcVe(yi, xi, sumn);

            return vr / ve;
        }
        public boolean executeTest(double statistic, double a) {
            FDistribution fDist = new FDistribution(n, m);
            double f = fDist.inverseCumulativeProbability(1.0 - a);

            return (statistic >= f) ? true : false;
        }
        private double calcVr(double yi[][], double[][] xi, int sumn) {
            double sumeyx = calcSeyx(yi, xi, sumn);
            double sumex = calcSex(xi, sumn);

            return (sumeyx * sumeyx) / sumex;
        }
        private double calcVe(double yi[][], double[][] xi, int sumn) {
            double sumey = calcSey(yi, sumn);
            double sumex = calcSex(xi, sumn);
            double sumeyx = calcSeyx(yi, xi, sumn);

            return (sumey * sumex - sumeyx * sumeyx) / (m * sumex);
        }
    }
    // 水準間の差の検定
    private class DifferenceTest extends RegressionLine implements HypothesisTest {
        private int n = 0;
        private int m = 0;
        public double calcTestStatistic(double yi[][], double[][] xi) {
            int sumn = calcSumn(xi);
            n = yi.length - 1;
            m = sumn - yi.length - 1;

            double va = calcSa(yi, xi, sumn) / n;
            double ve = calcVe(yi, xi, sumn);

            return va / ve;
        }
        public boolean executeTest(double statistic, double a) {
            FDistribution fDist = new FDistribution(n, m);
            double f = fDist.inverseCumulativeProbability(1.0 - a);

            return (statistic >= f) ? true : false;
        }
        private double calcVe(double yi[][], double[][] xi, int sumn) {
            double sumey = calcSey(yi, sumn);
            double sumex = calcSex(xi, sumn);
            double sumeyx = calcSeyx(yi, xi, sumn);

            return (sumey * sumex - sumeyx * sumeyx) / (m * sumex);
        }
    }
    //
    private class IntervalEstim extends RegressionLine 
                                implements Estim {
        private int n = 0;
        private int[] ni = null;
        private double sumex = 0.0;
        public Interval[] calcInterval(double yi[][], double[][] xi, double a) {
            Interval[] ret = new Interval[xi.length];
            ni = calcNi(yi);
            int sumn = calcSumn(xi);
            n = sumn - xi.length - 1;
            sumex = calcSex(xi, sumn);
            double ve = calcVe(yi, xi, sumn);
            double b = calcB(yi, xi, sumn);

            double[] meanyi = calcMeanyi(yi);
            double[] meanxi = calcMeanxi(xi);
            double meanx = calcMeanx(xi);

            TDistribution tDist = new TDistribution(n);
            double t = tDist.inverseCumulativeProbability(1.0 - a / 2.0);
            for(int i = 0; i < ret.length; i++) {
                double wk = (meanxi[i] - meanx);
                double wk2 = t * Math.sqrt((1/ni[i] + wk * wk / sumex) * ve);
                double min = meanyi[i] - b * wk - wk2;
                double max = meanyi[i] - b * wk + wk2;

                ret[i] = new Interval(min, max);
            }
            
            return ret;
        }
        private int[] calcNi(double[][] yi) {
            int[] ni = new int[yi.length];

            for(int i = 0; i < yi.length; i++) {
                ni[i] = yi[i].length;
            }
            return ni;
        }
        private double calcVe(double[][] yi, double[][] xi, int sumn) {
            double sumey = calcSey(yi, sumn);
            double sumeyx = calcSeyx(yi, xi, sumn);

            return (sumey * sumex - sumeyx * sumeyx) / (n * sumex);
        }
        private double calcB(double[][] yi, double[][] xi, int sumn) {
            double sumeyx = calcSeyx(yi, xi, sumn);
            double sex = calcSex(xi, sumn);
            
            return sumeyx / sex;
        }
        private double[] calcMeanyi(double[][] yi) {
            double[] meanyi = new double[yi.length];

            for(int i = 0; i < yi.length; i++) {
                double sum = 0.0;
                for(int j = 0; j < yi[i].length; j++) {
                    sum += yi[i][j];
                }
                meanyi[i] = sum / yi[i].length;
            }
            return meanyi;
        }
        private double[] calcMeanxi(double[][] xi) {
            double[] meanxi = new double[xi.length];

            for(int i = 0; i < xi.length; i++) {
                double sum = 0.0;
                for(int j = 0; j < xi[i].length; j++) {
                    sum += xi[i][j];
                }
                meanxi[i] = sum / xi[i].length;
            }
            return meanxi;
        }
        private double calcMeanx(double[][] xi) {
            double sum = 0.0;
            double n = 0;
            for(int i = 0; i < xi.length; i++) {
                for(int j = 0; j < xi[i].length; j++) {
                    sum += xi[i][j];
                    n++;
                }
            }
            return sum / n;
        }
    }
}

