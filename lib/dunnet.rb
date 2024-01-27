# Dunnet検定
#  (Apache commoms math3使用)
module DunnetTestLib
    # Dunnet検定の両側検定
    #
    # @overload twoside_test(xi, a)
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
    #    paraTest.twoside_test(xi, 0.05)
    #    => 
    #      res = [
    #          [false, false, false, true, false],
    #          [false, false, false, true, false],
    #          [false, false, false, false, false],
    #          [true,  true,  false, false, false],
    #          [false, false, false, false, false],
    #      ]
    def twoside_test(xi, a)
        ret = @paramTest.twosideTest(xi.to_java(Java::double[]), a)
        return ret.to_a
    end
    # Dunnet検定の右側検定
    #
    # @overload rightside_test(xi, a)
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
    #    paraTest.rightside_test(xi, 0.05)
    #    => 
    #      res = [
    #          [false, false, true,  true,  true],
    #          [false, false, false, true,  false],
    #          [false, false, false, true,  false],
    #          [false, false, false, false, false],
    #          [false, false, false, true, false],
    #      ]
    def rightside_test(xi, a)
        ret = @paramTest.rightsideTest(xi.to_java(Java::double[]), a)
        return ret.to_a
    end
    # Dunnet検定の左側検定
    #
    # @overload leftside_test(xi, a)
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
    #    paraTest.leftside_test(xi, 0.05)
    #    => 
    #      res = [
    #          [false, false, false, false, false],
    #          [false, false, false, false, false],
    #          [true,  false, false, false, false],
    #          [true,  true,  true,  false, true],
    #          [true,  false, false, false, false],
    #      ]
    def leftside_test(xi, a)
        ret = @paramTest.leftsideTest(xi.to_java(Java::double[]), a)
        return ret.to_a
    end
end

