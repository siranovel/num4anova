import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.ChartFactory;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.BoxAndWhiskerCalculator;
import org.jfree.data.statistics.BoxAndWhiskerItem;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.chart.labels.BoxAndWhiskerToolTipGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.labels.StandardCategoryToolTipGenerator;

import org.jfree.chart.ChartUtils;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.math3.stat.inference.OneWayAnova;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import java.util.Map;
public class OneWayLayout {
    private static OneWayLayout oneWay = new OneWayLayout();
    public static OneWayLayout getInstance() {
        return oneWay;
    }
    public void boxWhiskerPlot(String dname, Map<String, double[]> vals) {
        ChartPlot plot = new BoxWhiskerChartPlot();
        JFreeChart chart = plot.createChart("箱ひげ図", dname, vals);

        plot.writeJPEG("boxWhisker.jpeg", chart, 500, 300);
    }
    public void oneWayScatterPlot(String dname, Map<String, double[]> vals) {
        ChartPlot plot = new OneWayScatterChartPlot();
        JFreeChart chart = plot.createChart("一元散布図", dname, vals);

        plot.writeJPEG("scatter.jpeg", chart, 800, 500);
    }
    public boolean onewayanova(double[][] xi, double a) {
        List<double[]> data = new ArrayList<double[]>();
        OneWayAnova anova = new OneWayAnova();

        Arrays.stream(xi).forEach(data::add);
        return anova.anovaTest(data, a);
    }
    public boolean bartletTest(double[][] xi, double a) {
        OneWayAnovaTest oneway = new BartletTest();

        double statistic = oneway.calcTestStatistic(xi);
        return oneway.test(statistic, a);
    }
    /*********************************/
    /* interface define              */
    /*********************************/
    private interface ChartPlot {
        JFreeChart createChart(String title, String dname, Map<String, double[]> vals);
        default void writeJPEG(String fname, JFreeChart chart, int width, int height) {
            File file = new File(fname);
            try {
                ChartUtils.saveChartAsJPEG(file, chart, width, height);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private interface CreatePlot {
        CategoryPlot createPlot(String dname, Map<String, double[]> xi);
    }
    private interface OneWayAnovaTest {
        double calcTestStatistic(double[][] xi);
        boolean test(double statistic, double a);
    }
    /*********************************/
    /* class define                  */
    /*********************************/
    // 箱ひげ図
    private class BoxWhiskerChartPlot implements ChartPlot {
        public JFreeChart createChart(String title, String dname, Map<String, double[]> vals) {
            CategoryPlot plot = createPlot(dname, vals);
            ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
            JFreeChart chart = new JFreeChart(title, plot);

            ChartUtils.applyCurrentTheme(chart);
            return chart;
        }
        private CategoryPlot createPlot(String dname, Map<String, double[]> vals) {
            CreatePlot plotImpl = new BoxWhiskerPlot();

            return plotImpl.createPlot(dname, vals);
        }
        private class BoxWhiskerPlot implements CreatePlot {
            public CategoryPlot createPlot(String dname, Map<String, double[]> vals) {
                BoxAndWhiskerRenderer renderer0 = new BoxAndWhiskerRenderer();
                renderer0.setDefaultToolTipGenerator(new BoxAndWhiskerToolTipGenerator());
                renderer0.setMaximumBarWidth(0.2);

                CategoryPlot plot = new CategoryPlot();
                plot.setOrientation(PlotOrientation.VERTICAL);
                plot.mapDatasetToRangeAxis(0,0);
	        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

                /*--- 横軸 ---*/
                CategoryAxis categoryAxis = new CategoryAxis();
                plot.setDomainAxis(categoryAxis);

                /*--- 縦軸 ---*/
                NumberAxis valueAxis = new NumberAxis();
                valueAxis.setAutoRangeIncludesZero(false);
                plot.setRangeAxis(valueAxis);
                

                plot.setRenderer(0, renderer0);
                plot.setDataset(0, createDataset(dname, vals));

                return plot;
            }
            private BoxAndWhiskerCategoryDataset createDataset(String dname, Map<String, double[]> vals) {
                DefaultBoxAndWhiskerCategoryDataset data = 
                    new DefaultBoxAndWhiskerCategoryDataset();
                List<Double> values = new ArrayList<Double>();
                for(Map.Entry<String, double[]> entry : vals.entrySet()) {
                    double[] v = entry.getValue();

                    Arrays.stream(v).forEach(values::add);
                    data.add(
                        BoxAndWhiskerCalculator.calculateBoxAndWhiskerStatistics(values),
                        dname, entry.getKey()
                    );
                    values.clear();
                }
                return data;
            }
        }
    }
    // 一元配置グラフ
    private class OneWayScatterChartPlot implements ChartPlot {
        public JFreeChart createChart(String title, String dname, Map<String, double[]> vals) {
            CategoryPlot plot = createPlot(dname, vals);

            ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
            JFreeChart chart = new JFreeChart(title, plot);

            ChartUtils.applyCurrentTheme(chart);
            return chart;
        }
        private CategoryPlot createPlot(String dname, Map<String, double[]> vals) {
            CreatePlot plotImpl = new OneWayScatterPlot();

            return plotImpl.createPlot(dname, vals);
        }
        private class OneWayScatterPlot implements CreatePlot {
            public CategoryPlot createPlot(String dname, Map<String, double[]> vals) {
                LineAndShapeRenderer renderer = new LineAndShapeRenderer(false, true);
                renderer.setDefaultToolTipGenerator(
                    new StandardCategoryToolTipGenerator());

                CategoryPlot plot = new CategoryPlot();
                plot.setOrientation(PlotOrientation.VERTICAL);
                plot.mapDatasetToRangeAxis(0,0);
	        plot.setDatasetRenderingOrder(DatasetRenderingOrder.FORWARD);

                /*--- 横軸 ---*/
                CategoryAxis categoryAxis = new CategoryAxis();
                plot.setDomainAxis(categoryAxis);

                /*--- 縦軸 ---*/
                NumberAxis valueAxis0 = new NumberAxis();
                plot.setRangeAxis(valueAxis0);

                plot.setRenderer(0, renderer);
                plot.setDataset(0, createDataset(dname, vals));
                return plot;
            }

            private CategoryDataset createDataset(String dname, Map<String, double[]> vals) {
                DefaultCategoryDataset data = new DefaultCategoryDataset();
                for(Map.Entry<String, double[]> entry : vals.entrySet()) {
                    double[] v = entry.getValue();
                    for(int i = 0; i < v.length; i++) {
                        String rowKey = String.format("dt%02d", i);

                        data.addValue(v[i], rowKey, entry.getKey());
                    }
                }
                return data;
            }
        }
    }
    private class BartletTest implements OneWayAnovaTest {
        private int n = 0;
        public double calcTestStatistic(double[][] xi) {
            n = xi.length;
            double ln2L = logL(xi);

            return calcB(ln2L, xi);
        }
        private double logL(double[][] xi) {
            double[] si = new double[n];
            DescriptiveStatistics stat = new DescriptiveStatistics();
            double nisi2 = 0.0;    // (Ni - 1)*si^2の合計
            double nilogsi2 = 0.0; // (Ni - 1)*log(si^2)の合計
            int sumN = 0;

            for(int i = 0; i < n; i++) {
                Arrays.stream(xi[i]).forEach(stat::addValue);
                sumN += stat.getN();
                si[i] = stat.getVariance();
                nisi2 += (stat.getN() - 1) * si[i];
                nilogsi2 += (stat.getN() - 1) * Math.log(si[i]);
                stat.clear();
            }
            double sumNin = sumN - n;
            return sumNin * (Math.log(nisi2 / sumNin) - nilogsi2 / sumNin);
        }
        private double calcB(double ln2L, double[][] xi) {
            double invSumN = 0.0;
            int sumN = 0;
            DescriptiveStatistics stat = new DescriptiveStatistics();
            for(int i = 0; i < n; i++) {
                Arrays.stream(xi[i]).forEach(stat::addValue);
                invSumN += 1.0 / (stat.getN() - 1.0);
                sumN += stat.getN();
                stat.clear();
            }
            double deno = 1 + 1.0 / (3 * (n - 1))
                        * (invSumN - 1.0 / (sumN - n));
            return ln2L / deno;
        }
        public boolean test(double statistic, double a) {
            ChiSquaredDistribution chi2Dist = new ChiSquaredDistribution(n - 1);
            double r_val = chi2Dist.inverseCumulativeProbability(1.0 - a);

            return (r_val < statistic) ? true : false;
        }
    }
}

