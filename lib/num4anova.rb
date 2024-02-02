require 'java'
require 'num4anova.jar'
require 'jfreechart-1.5.4.jar'
require 'commons-math3-3.6.1.jar'

java_import 'OneWayLayout'
java_import 'TwoWayLayout'
java_import 'java.util.HashMap'
# 分散分析を行う
#  (Apache commoms math3使用)
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
        #   xi = [
        #       [12.2, 18.8, 18.2],
        #       [22.2, 20.5, 14.6],
        #       [20.8, 19.5, 26.3],
        #       [26.4, 32.5, 31.3],
        #       [24.5, 21.2, 22.4],
        #   ]
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
        #   xi = [
        #       [12.2, 18.8, 18.2],
        #       [22.2, 20.5, 14.6],
        #       [20.8, 19.5, 26.3],
        #       [26.4, 32.5, 31.3],
        #       [24.5, 21.2, 22.4],
        #   ]
        #   oneWay = Num4AnovaLib::OneWayLayoutLib.new 
        #   oneWay.bartlet(xi, 0.05)
        #   => true
        def bartlet(xi, a)
            return @oneWay.bartletTest(xi.to_java(Java::double[]), a)
        end
        # 反復測定Plot
        #
        # @overload replicate_plot(dname, vals)
        #   @param [String] dname データ名
        #   @param [Hash] vals Hash(String, double[])
        #   @return [void]  replicate.jpegファイルを出力
        # @example
        #   vals = {
        #        "stageA1" => [27, 52, 18, 21, 32],
        #        "stageA2" => [52, 72, 31, 50, 45],
        #        "stageA3" => [47, 54, 29, 43, 32],
        #        "stageA4" => [28, 50, 22, 26, 29],
        #       }
        #   oneWay = Num4AnovaLib::OneWayLayoutLib.new 
        #   oneWay.replicate_plot("LDH", vals)
        #   => replicate.jpeg
        # @note
        #   グラフは、jfreechartを使用
        def replicate_plot(dname, vals)
            o = HashMap.new
            vals.each{|k, v|
                o[k] = v.to_java(Java::double)
            }
            return @oneWay.replicatePlot(dname, o)
        end
        # 反復測定検定
        #
        # @overload replicate_test(xi, a)
        #   @param [array]  xi データ(double[][])
        #   @param [double] a         有意水準
        #   @return [boolean] 検定結果(true:棄却域内 false:棄却域外)
        # @example
        #   xi = [
        #       [27, 52, 18, 21, 32],
        #       [52, 72, 31, 50, 45],
        #       [47, 54, 29, 43, 32],
        #       [28, 50, 22, 26, 29],
        #   ]
        #   oneWay = Num4AnovaLib::OneWayLayoutLib.new 
        #   oneWay.replicate_test(xi, 0.05)
        #   => true
        def replicate_test(xi, a)
            return @oneWay.replicateTest(xi.to_java(Java::double[]), a)
        end
    end

    # 二元配置の分散分析
    class TwoWayLayoutLib
        def initialize
            @twoWay = TwoWayLayout.getInstance()
        end
        # 二元配置の分散分析
        #
        # @overload twoway_anova(xij, a)
        #   @param [array]  xij データ(double[][][])
        #   @param [double] a         有意水準
        #   @return [Array] 検定結果(boolean[] true:棄却域内 false:棄却域外)
        # @example
        #   xij = [
        #           [
        #             [13.2, 15.7, 11.9],
        #             [16.1, 15.7, 15.1],
        #             [9.1,  10.3,  8.2],
        #           ],
        #           [
        #             [22.8, 25.7, 18.5],
        #             [24.5, 21.2, 24.2],
        #             [11.9, 14.3, 13.7],
        #           ],
        #           [
        #             [21.8, 26.3, 32.1],
        #             [26.9, 31.3, 28.3],
        #             [15.1, 13.6, 16.2],
        #           ],
        #           [
        #             [25.7, 28.8, 29.5],
        #             [30.1, 33.8, 29.6],
        #             [15.2, 17.3, 14.8],
        #           ],
        #   ]
        #   twoWay = Num4AnovaLib::TwoWayLayoutLib.new 
        #   twoWay.twoway_anova(xij, 0.05)
        #   =>
        #     [true, true, true]
        def twoway_anova(xij, a)
            ret = @twoWay.twowayAnova(xij.to_java(Java::double[][]), a)
            return ret.to_a
        end
    end
end

