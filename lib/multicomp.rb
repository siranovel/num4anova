require 'java'
require 'num4anova.jar'
require 'jfreechart-1.5.4.jar'
require 'commons-math3-3.6.1.jar'

java_import 'MultiComp'
# 多重比較を行う
#  (Apache commoms math3使用)
module MultiCompLib
    class ParametrixTestLib
        def initialize
            @paramTest = MultiComp::ParametrixTest.getInstance()
        end
        def turkey_test(xi, a)
            ret = @paramTest.turkeyTest(xi.to_java(Java::double[]), a)
            return ret.to_a
        end
        def bonferrono_test(xi, a)
            ret = @paramTest.bonferronoTest(xi.to_java(Java::double[]), a)
            return ret.to_a
        end
    end
    class NonParametrixTestLib
    end
end
