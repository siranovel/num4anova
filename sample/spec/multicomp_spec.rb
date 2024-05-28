require 'spec_helper'
require 'num4multicomp'

RSpec.describe Num4MultiCompLib do
    let!(:a) { 0.05 }
    describe Num4MultiCompLib::ParametrixTestLib do
        let!(:paraTest) { Num4MultiCompLib::ParametrixTestLib.new }
        it '#turkey_test' do
            xi = [
                [12.2, 18.8, 18.2],
                [22.2, 20.5, 14.6],
                [20.8, 19.5, 26.3],
                [26.4, 32.5, 31.3],
                [24.5, 21.2, 22.4],
            ]
            res = [
                [false, false, false, true,  false],
                [false, false, false, true,  false],
                [false, false, false, false, false],
                [false, false, false, false, false],
                [false, false, false, false, false],
            ]
            expect(
                paraTest.turkey_test(xi, a)
            ).to is_array(res)
        end
        it '#bonferrono_test' do
            xi = [
                [12.2, 18.8, 18.2],
                [22.2, 20.5, 14.6],
                [20.8, 19.5, 26.3],
                [26.4, 32.5, 31.3],
                [24.5, 21.2, 22.4],
            ]
            res = [
                [false, false, false, true,  false],
                [false, false, false, true,  false],
                [false, false, false, false, false],
                [false, false, false, false, false],
                [false, false, false, false, false],
            ]
            expect(
                paraTest.bonferrono_test(xi, a)
            ).to is_array(res)
        end
        it '#twoside_test' do
            xi = [
                [12.2, 18.8, 18.2],
                [22.2, 20.5, 14.6],
                [20.8, 19.5, 26.3],
                [26.4, 32.5, 31.3],
                [24.5, 21.2, 22.4],
            ]
            res = [
                [false, false, false, true,  false],
                [false, false, false, true,  false],
                [false, false, false, false, false],
                [true,  true,  false, false, false],
                [false, false, false, false, false],
            ]
            expect(
                paraTest.twoside_test(xi, a)
            ).to is_array(res)
        end
        it '#rightside_test' do
            xi = [
                [12.2, 18.8, 18.2],
                [22.2, 20.5, 14.6],
                [20.8, 19.5, 26.3],
                [26.4, 32.5, 31.3],
                [24.5, 21.2, 22.4],
            ]
            res = [
                [false, false, true,  true,  true],
                [false, false, false, true,  false],
                [false, false, false, true,  false],
                [false, false, false, false, false],
                [false, false, false, true,  false],
            ]
            expect(
                paraTest.rightside_test(xi, a)
            ).to is_array(res)
        end
        it '#leftside_test' do
            xi = [
                [12.2, 18.8, 18.2],
                [22.2, 20.5, 14.6],
                [20.8, 19.5, 26.3],
                [26.4, 32.5, 31.3],
                [24.5, 21.2, 22.4],
            ]
            res = [
                [false, false, false, false, false],
                [false, false, false, false, false],
                [true,  false, false, false, false],
                [true,  true,  true,  false, true],
                [true,  false, false, false, false],
            ]
            expect(
                paraTest.leftside_test(xi, a)
            ).to is_array(res)
        end
    end
    describe Num4MultiCompLib::NonParametrixTestLib do
        let!(:nonParaTest) { Num4MultiCompLib::NonParametrixTestLib.new }
        it '#bonferrono_test' do
            xi = [
                [12.2, 18.8, 18.2],
                [22.2, 20.5, 14.6],
                [20.8, 19.5, 26.3],
                [26.4, 32.5, 31.3],
                [24.5, 21.2, 22.4],
            ]
            res = [
                [false, false, true,  true,  true],
                [false, false, false, true,  true],
                [false, false, false, true,  false],
                [false, false, false, false, true],
                [false, false, false, false, false],
            ]
            expect(
                nonParaTest.bonferrono_test(xi, a)
            ).to is_array(res)
        end
    end
end

