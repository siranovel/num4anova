import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.ChartFactory;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import org.jfree.chart.ChartUtils;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.math3.stat.inference.OneWayAnova;
public class Anova {
    public static void boxWhiskerPlot(String dname, double[][] xi) {
        ChartPlot plot = new BoxWhiskerPlot();
        JFreeChart chart = plot.createChart(dname, xi);

        plot.writeJPEG("boxWhisker.jpeg", chart, 500, 300);
    }
    public static boolean onewayanova(double[][] xi, double a) {
        List<double[]> data = new ArrayList<double[]>();
        OneWayAnova anova = new OneWayAnova();

        for(double[] x: xi) {
            data.add(x);
        }

        return anova.anovaTest(data, a);
    }


    private interface ChartPlot {
        JFreeChart createChart(String dname, double[][] xi);
        default void writeJPEG(String fname, JFreeChart chart, int width, int height) {
            File file = new File(fname);
            try {
                ChartUtils.saveChartAsJPEG(file, chart, width, height);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static class BoxWhiskerPlot implements ChartPlot {
        public JFreeChart createChart(String dname, double[][] xi) {
            ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme());
            return ChartFactory.createBoxAndWhiskerChart("title", "x", "y", createDataset(dname, xi), true);
        }
        private BoxAndWhiskerCategoryDataset createDataset(String dname, double[][] xi) {
            DefaultBoxAndWhiskerCategoryDataset data = 
                new DefaultBoxAndWhiskerCategoryDataset();
            List<List<Double>> list = new ArrayList<List<Double>>();

            for(int i = 0; i < xi.length; i++) {
                list.add(new  ArrayList<Double>());
            }
            for(int i = 0; i < list.size(); i++) {
                for(int j = 0; j < xi[i].length; j++) {
                    list.get(i).add(xi[i][j]);
                }
            }
            for(int i = 0; i < list.size(); i++) {
                data.add(list.get(i), dname, String.format("col%02d", i+1));
            }
            return data;
        }
    }
}

