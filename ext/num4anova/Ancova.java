import org.apache.commons.math3.distribution.FDistribution;

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
    /*********************************/
    /* interface define              */
    /*********************************/
    private interface HypothesisTest {
        double calcTestStatistic(double[][][] xi);
        boolean executeTest(double statistic, double a);
    }
    /*********************************/
    /* class define                  */
    /*********************************/
    // 回帰直線モデルの平行性の検定
    private class Parallettest implements HypothesisTest {
        private int n = 0;
        private int m = 0;
        public double calcTestStatistic(double[][][] xi) {
            int sumn = calcSumn(xi);
            double sumx = calcSumx(xi);
            double sumy = calcSumy(xi);
            n = xi.length - 1;
            m = sumn - 2 * xi.length;

        // 全変動
            double sumx2 = calcSumx2(xi);
            double sumy2 = calcSumy2(xi);
            double sumyx = calcSumyx(xi);

            double sumtx = sumx2 - sumx*sumx / sumn;
            double sumty = sumy2 - sumy*sumy / sumn;
            double sumtyx = sumyx - sumy*sumx / sumn;

        // 水準間変動
            double sumax = calcSumax(xi) - sumx*sumx / sumn;
            double sumay = calcSumay(xi) - sumy*sumy / sumn;
            double sumayx = calcSumayx(xi) - sumy*sumx / sumn;

        // 水準内変動
            double sumex = sumtx - sumax;
            double sumey = sumty - sumay;
            double sumeyx = sumtyx - sumayx;

            double sumbx = calcbx(xi);
            double sumnp = sumbx - sumeyx * sumeyx / sumex;
            double sume2 = sumey - sumbx;

       // 
            double vnp = sumnp / n;
            double ve2 = sume2 / m;
            return vnp / ve2;
        }
        public boolean executeTest(double statistic, double a) {
            FDistribution fDist = new FDistribution(n, m);
            double f = fDist.inverseCumulativeProbability(1.0 - a);

            return (statistic >= f) ? true : false;
        }
        private int calcSumn(double[][][] xi) {
            int sum = 0;

            for(int i = 0; i < xi.length; i++) {
                sum += xi[i].length;
            }
            return sum;
        }
        // 全変動
        private double calcSumx(double[][][] xi) {
            double sum = 0.0;

            for (int i = 0; i < xi.length; i++) {
                for (int j = 0; j < xi[i].length; j++) {
                    sum += xi[i][j][1];
                }
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
        private double calcSumx2(double[][][] xi) {
            double sum = 0.0;

            for (int i = 0; i < xi.length; i++) {
                for (int j = 0; j < xi[i].length; j++) {
                    sum += xi[i][j][1] * xi[i][j][1];
                }
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
        private double calcSumyx(double[][][] xi) {
            double sum = 0.0;

            for (int i = 0; i < xi.length; i++) {
                for (int j = 0; j < xi[i].length; j++) {
                    sum += xi[i][j][0] * xi[i][j][1];
                }
            }
            return sum;
        }
        // 水準間変動
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
        // 平行性の検定
        private double calcbx(double[][][] xi) {
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
    }
}

