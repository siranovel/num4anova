require 'java'
require 'num4anova.jar'
require 'jfreechart-1.5.4.jar'
require 'commons-math3-3.6.1.jar'

java_import 'OneWayLayout'
java_import 'java.util.HashMap'
# 数値計算による分散分析を行う
module Num4AnovaLib
    # 一元配置の分散分析
    class OneWayLayoutLib
        def initialize
            @oneWay = OneWayLayout.getInstance()
        end
        # 箱ひげ図
        #
        # @overload boxWhiskerPlot(dname, vals)
        #   @param [String] dname データ名
        #   @param [Hash] vals Hash(String, double[])
        #   @return [void]  boxWhisker.jpegファイルを出力
        # @example
        #   vals = {
        #         "stage51" => [12.2, 18.8, 18.2],
        #         "stage55" => [22.2, 20.5, 14.6],
        #         "stage57" => [20.8, 19.5, 26.3],
        #         "stage59" => [26.4, 32.5, 31.3],
        #         "stage61" => [24.5, 21.2, 22.4],
        #       }
        #   oneWay = Num4AnovaLib::OneWayLayoutLib.new 
        #   oneWay.boxWhiskerPlot("LDH", vals)
        #   => boxWhisker.jpeg
        # @note
        #   グラフは、jfreechartを使用
        def boxWhiskerPlot(dname, vals)
            o = HashMap.new
            vals.each{|k, v|
                o[k] = v.to_java(Java::double)
            }
            @oneWay.boxWhiskerPlot(dname, o)
        end
        # 一元散布図
        #
        # @overload oneway_scatter_plot(dname, vals)
        #   @param [String] dname データ名
        #   @param [Hash] vals Hash(String, double[])
        #   @return [void]  scatter.jpegファイルを出力
        # @example
        #   vals = {
        #         "stage51" => [12.2, 18.8, 18.2],
        #         "stage55" => [22.2, 20.5, 14.6],
        #         "stage57" => [20.8, 19.5, 26.3],
        #         "stage59" => [26.4, 32.5, 31.3],
        #         "stage61" => [24.5, 21.2, 22.4],
        #       }
        #   oneWay = Num4AnovaLib::OneWayLayoutLib.new 
        #   oneWay.oneway_scatter_plot("LDH", vals)
        #   => scatter.jpeg
        # @note
        #   グラフは、jfreechartを使用
        def oneway_scatter_plot(dname, vals)
            o = HashMap.new
            vals.each{|k, v|
                o[k] = v.to_java(Java::double)
            }
            @oneWay.oneWayScatterPlot(dname, o)
        end
        # 一元配置分散分析
        #
        # @overload oneway_anova(xi, a)
        #   @param [array]  xi データ(double[][])
        #   @param [double] a         有意水準
        #   @return [boolean] 検定結果(true:棄却域内 false:棄却域外)
        # @example
        #    xi = [
        #        [12.2, 18.8, 18.2],
        #        [22.2, 20.5, 14.6],
        #        [20.8, 19.5, 26.3],
        #        [26.4, 32.5, 31.3],
        #        [24.5, 21.2, 22.4],
        #    ]
        #   oneWay = Num4AnovaLib::OneWayLayoutLib.new 
        #   oneWay.oneWay.oneway_anova(xi, 0.05)
        #   => true
        def oneway_anova(xi, a)
            return @oneWay.onewayanova(xi.to_java(Java::double[]), a)
        end
        # バートレット検定
        #
        # @overload bartlet(xi, a)
        #   @param [array]  xi データ(double[][])
        #   @param [double] a         有意水準
        #   @return [boolean] 検定結果(true:棄却域内 false:棄却域外)
        # @example
        #    xi = [
        #        [12.2, 18.8, 18.2],
        #        [22.2, 20.5, 14.6],
        #        [20.8, 19.5, 26.3],
        #        [26.4, 32.5, 31.3],
        #        [24.5, 21.2, 22.4],
        #    ]
        #   oneWay = Num4AnovaLib::OneWayLayoutLib.new 
        #   oneWay.bartlet(xi, 0.05)
        #   => true
        def bartlet(xi, a)
            return @oneWay.bartletTest(xi.to_java(Java::double[]), a)
        end
    end
end

