require 'java'
require 'num4anova.jar'
require 'jfreechart-1.5.4.jar'
require 'commons-math3-3.6.1.jar'

java_import 'OneWayLayout'
java_import 'TwoWayLayout'
java_import 'Ancova'
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
        # クラスカル・ウォリスの検定
        #
        # @overload kruskalwallis_test(xi, a)
        #   @param [array]  xi データ(double[][])
        #   @param [double] a         有意水準
        #   @return [boolean] 検定結果(true:棄却域内 false:棄却域外)
        # @example
        #   xi = [
        #        [12.2, 18.8, 18.2],
        #        [22.2, 20.5, 14.6, 20.8, 19.5, 26.3],
        #        [26.4, 32.5, 31.3, 24.5, 21.2, 22.4],
        #   ]
        #   oneWay = Num4AnovaLib::OneWayLayoutLib.new 
        #   oneWay.kruskalwallis_test(xi, 0.05)
        #   => true
        def kruskalwallis_test(xi, a)
            return @oneWay.kruskalWallisTest(xi.to_java(Java::double[]), a)
        end
    end

    # 二元配置の分散分析
    class TwoWayLayoutLib
        def initialize
            @twoWay = TwoWayLayout.getInstance()
        end
        # 二元配置の分散分析
        #  (繰り返し数が等しい時)
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
        # 二元配置の分散分析
        #  (繰り返しのない時)
        #
        # @overload twoway2_anova(xij, a)
        #   @param [array]  xij データ(double[][])
        #   @param [double] a         有意水準
        #   @return [Array] 検定結果(boolean[] true:棄却域内 false:棄却域外)
        # @example
        #   xij = [
        #       [13.6, 15.6, 9.2],
        #       [22.3, 23.3, 13.3],
        #       [26.7, 28.8, 15.0],
        #       [28.0, 31.2, 15.8],
        #   ]
        #   twoWay = Num4AnovaLib::TwoWayLayoutLib.new 
        #   twoWay.twoway2_anova(xij, 0.05)
        #   =>
        #     [true, true]
        def twoway2_anova(xij, a)
            ret = @twoWay.twoway2Anova(xij.to_java(Java::double[]), a)
            return ret.to_a
        end
        # フリードマン検定
        #
        # @overload friedman_test(xij, a)
        #   @param [array]  xij データ(double[][])
        #   @param [double] a         有意水準
        #   @return [boolean] 検定結果(boolean true:棄却域内 false:棄却域外)
        # @example
        #   xij = [
        #       [13.6, 15.6, 9.2],
        #       [22.3, 23.3, 13.3],
        #       [26.7, 28.8, 15.0],
        #       [28.0, 31.2, 15.8],
        #   ]
        #   twoWay = Num4AnovaLib::TwoWayLayoutLib.new 
        #   twoWay.friedman_test(xij, 0.05)
        #   =>
        #     true
        def friedman_test(xij, a)
            ret = @twoWay.friedmanTest(xij.to_java(Java::double[]), a)
            return ret
        end
        # 1元配置用データ作成
        #
        # @overload create_oneway(xij)
        #   @param  [array]  xij データ(double[][][])
        #   @return [array]  3次元データを2次元データに変換した値
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
        #   twoWay.create_oneway(xij)
        #   =>
        #     xij = [
        #       [13.6, 15.6, 9.2],
        #       [22.3, 23.3, 13.3],
        #       [26.7, 28.8, 15.0],
        #       [28.0, 31.2, 15.8],
        #     ]
        def create_oneway(xij)
            ret = @twoWay.createOneWay(xij.to_java(Java::double[][]))
            return ret.to_a
        end        
    end
    # 共分散分析
    class Num4AncovaLib
        def initialize
            @ancova = Ancova.getInstance()
        end
        # 回帰直線の平行性検定
        #
        # @overload parallel_test(xi, a)
        #   @param [array]  xi データ(double[][][])
        #   @param [double] a  有意水準
        #   @return [boolean]  検定結果(boolean true:棄却域内 false:棄却域外)
        # @example
        #   xi = [
        #      [
        #          [3,35], [5,38], [3,39],
        #      ],
        #      [
        #          [3,36], [3,39], [8,54],
        #      ],
        #      [
        #          [2,40], [2,45], [2,39],
        #      ],
        #      [
        #          [3,47], [4,52], [2,48],
        #      ],
        #      [
        #          [1,64], [2,80], [0,70],
        #      ],
        #    ]
        #    ancova = Num4AnovaLib::Num4AncovaLib.new
        #    ancova.parallel_test(xi, 0.05)
        #    => false
        def parallel_test(xi, a)
            @ancova.parallelTest(xi.to_java(Java::double[][]), a)
        end
        # 回帰直線の有意性検定
        #
        # @overload significance_test(xi, a)
        #   @param [array]  xi データ(double[][][])
        #   @param [double] a  有意水準
        #   @return [boolean]  検定結果(boolean true:棄却域内 false:棄却域外)
        # @example
        #   xi = [
        #      [
        #          [3,35], [5,38], [3,39],
        #      ],
        #      [
        #          [3,36], [3,39], [8,54],
        #      ],
        #      [
        #          [2,40], [2,45], [2,39],
        #      ],
        #      [
        #          [3,47], [4,52], [2,48],
        #      ],
        #      [
        #          [1,64], [2,80], [0,70],
        #      ],
        #    ]
        #    ancova = Num4AnovaLib::Num4AncovaLib.new
        #    ancova.significance_test(xi, 0.05)
        #    => true
        def significance_test(xi, a)
            @ancova.significanceTest(xi.to_java(Java::double[][]), a)
        end
        # 水準間の差の検定
        #
        # @overload difference_test(xi, a)
        #   @param [array]  xi データ(double[][][])
        #   @param [double] a  有意水準
        #   @return [boolean]  検定結果(boolean true:棄却域内 false:棄却域外)
        # @example
        #   xi = [
        #      [
        #          [3,35], [5,38], [3,39],
        #      ],
        #      [
        #          [3,36], [3,39], [8,54],
        #      ],
        #      [
        #          [2,40], [2,45], [2,39],
        #      ],
        #      [
        #          [3,47], [4,52], [2,48],
        #      ],
        #      [
        #          [1,64], [2,80], [0,70],
        #      ],
        #    ]
        #    ancova = Num4AnovaLib::Num4AncovaLib.new
        #    ancova.difference_test(xi, 0.05)
        #    => true
        def difference_test(xi, a)
            @ancova.differenceTest(xi.to_java(Java::double[][]), a)
        end
        # 区間推定
        #
        # @overload interval_estim(xi, a)
        #   @param [array]  xi データ(double[][][])
        #   @param [double] a  有意水準
        #   @return [Hash]  (min:下限信頼区間 max:上限信頼区間)
        # @example
        #   xi = [
        #      [
        #          [3,35], [5,38], [3,39],
        #      ],
        #      [
        #          [3,36], [3,39], [8,54],
        #      ],
        #      [
        #          [2,40], [2,45], [2,39],
        #      ],
        #      [
        #          [3,47], [4,52], [2,48],
        #      ],
        #      [
        #          [1,64], [2,80], [0,70],
        #      ],
        #    ]
        #    ancova = Num4AnovaLib::Num4AncovaLib.new
        #    ancova.interval_estim(xi, 0.05)
        #    =>
        #       {:min=>4.466605469341916,  :max=>7.1909253948556096}
        #       {:min=>5.05699825110459,   :max=>6.386335082228742}
        #       {:min=>2.510804295684195,  :max=>4.250430272217034}
        #       {:min=>2.8089257316042135, :max=>2.9566298239513418}
        #       {:min=>-6.303283147572267, :max=>-0.6577045067487104}
        def interval_estim(xi, a)
            retRb = []
            retJava = @ancova.intervalEstim(xi.to_java(Java::double[][]), a)
            sz = retJava.size
            sz.times do |i|
                retRb.push(
                  {
                    "min": retJava[i].getMin(),
                    "max": retJava[i].getMax()
                  }
                )
            end
            return retRb
        end
    end
end

