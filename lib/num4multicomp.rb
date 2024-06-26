require 'java'
require 'num4anova.jar'
require 'jfreechart-1.5.4.jar'
require 'commons-math3-3.6.1.jar'

require_relative('dunnet')

java_import 'MultiComp'
# 多重比較を行う
#  (Apache commoms math3使用)
module Num4MultiCompLib
    # パラメトリック検定
    class ParametrixTestLib
        include DunnetTestLib
        def initialize
            @paramTest = MultiComp::ParametrixTest.getInstance()
        end
        # turkeyの方法による多重比較
        #
        # @overload turkey_test(xi, a)
        #   @param [array]  xi データ(double[][])
        #   @param [double] a         有意水準
        #   @return [Array] 検定結果(boolean[][] true:棄却域内 false:棄却域外)
        # @example
        #    xi = [
        #        [12.2, 18.8, 18.2],
        #        [22.2, 20.5, 14.6],
        #        [20.8, 19.5, 26.3],
        #        [26.4, 32.5, 31.3],
        #        [24.5, 21.2, 22.4],
        #    ]
        #    paraTest = MultiCompLib::ParametrixTestLib.new
        #    paraTest.turkey_test(xi, 0.05)
        #    => 
        #      [
        #        [false, false, false, true, false],
        #        [false, false, false, true, false],
        #        [false, false, false, false, false],
        #        [false, false, false, false, false],
        #        [false, false, false, false, false],
        #     ]
        def turkey_test(xi, a)
            ret = @paramTest.turkeyTest(xi.to_java(Java::double[]), a)
            return ret.to_a
        end
        # ボンフェロー二の不等式による多重比較(T検定)
        #
        # @overload bonferrono_test(xi, a)
        #   @param [array]  xi データ(double[][])
        #   @param [double] a         有意水準
        #   @return [Array] 検定結果(boolean[][] true:棄却域内 false:棄却域外)
        # @example
        #    xi = [
        #        [12.2, 18.8, 18.2],
        #        [22.2, 20.5, 14.6],
        #        [20.8, 19.5, 26.3],
        #        [26.4, 32.5, 31.3],
        #        [24.5, 21.2, 22.4],
        #    ]
        #    paraTest = MultiCompLib::ParametrixTestLib.new
        #    paraTest.bonferrono_test(xi, 0.05)
        #    => 
        #      [
        #        [false, false, false, true, false],
        #        [false, false, false, true, false],
        #        [false, false, false, false, false],
        #        [false, false, false, false, false],
        #        [false, false, false, false, false],
        #     ]
        def bonferrono_test(xi, a)
            ret = @paramTest.bonferronoTest(xi.to_java(Java::double[]), a)
            return ret.to_a
        end
    end
    # ノンパラメトリック検定
    class NonParametrixTestLib
        def initialize
            @nonParamTest = MultiComp::NonParametrixTest.getInstance()
        end
        # ボンフェロー二の不等式による多重比較(マン・ホイットニーU検定)
        #
        # @overload bonferrono_test(xi, a)
        #   @param [array]  xi データ(double[][])
        #   @param [double] a         有意水準
        #   @return [Array] 検定結果(boolean[][] true:棄却域内 false:棄却域外)
        # @example
        #    xi = [
        #        [12.2, 18.8, 18.2],
        #        [22.2, 20.5, 14.6],
        #        [20.8, 19.5, 26.3],
        #        [26.4, 32.5, 31.3],
        #        [24.5, 21.2, 22.4],
        #    ]
        #    nonParaTest = MultiCompLib::NonParametrixTestLib.new
        #    nonParaTest.bonferrono_test(xi, 0.05)
        #    => 
        #      [
        #        [false, false, true,  true,  true],
        #        [false, false, false, true,  true],
        #        [false, false, false, true,  false],
        #        [false, false, false, false, true],
        #        [false, false, false, false, false],
        #     ]
        def bonferrono_test(xi, a)
            ret = @nonParamTest.bonferronoTest(xi.to_java(Java::double[]), a)
            return ret.to_a
        end

    end
end
