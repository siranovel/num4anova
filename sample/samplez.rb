require 'num4anova'
require 'multicomp'
require_relative('mymatcher')

RSpec.describe Num4AnovaLib do
    let!(:a) { 0.05 }
    describe Num4AnovaLib::OneWayLayoutLib do
        let!(:oneWay) { Num4AnovaLib::OneWayLayoutLib.new }
        it '#boxWhiskerPlot' do
            vals = {
                 "stage51" => [12.2, 18.8, 18.2],
                 "stage55" => [22.2, 20.5, 14.6],
                 "stage57" => [20.8, 19.5, 26.3],
                 "stage59" => [26.4, 32.5, 31.3],
                 "stage61" => [24.5, 21.2, 22.4],
               }
            expect(
                oneWay.boxWhiskerPlot("aaa", vals)
            ).to is_exist("boxWhisker.jpeg")
        end
        it '#oneway_scatter_plot' do
            vals = {
                 "stage51" => [12.2, 18.8, 18.2],
                 "stage55" => [22.2, 20.5, 14.6],
                 "stage57" => [20.8, 19.5, 26.3],
                 "stage59" => [26.4, 32.5, 31.3],
                 "stage61" => [24.5, 21.2, 22.4],
               }
            expect(
                oneWay.oneway_scatter_plot("aaa", vals)
            ).to is_exist("scatter.jpeg")
        end
        it '#oneway_anova' do
            xi = [
                [12.2, 18.8, 18.2],
                [22.2, 20.5, 14.6],
                [20.8, 19.5, 26.3],
                [26.4, 32.5, 31.3],
                [24.5, 21.2, 22.4],
            ]
            expect(
                oneWay.oneway_anova(xi, a)
            ).to eq true
        end
        it '#bartlet' do
            xi = [
                [12.2, 18.8, 18.2],
                [22.2, 20.5, 14.6],
                [20.8, 19.5, 26.3],
                [26.4, 32.5, 31.3],
                [24.5, 21.2, 22.4],
            ]
            expect(
                oneWay.bartlet(xi, a)
            ).to eq false
        end
    end
end

RSpec.describe MultiCompLib do
    let!(:a) { 0.05 }
    describe MultiCompLib::ParametrixTestLib do
        let!(:paraTest) { MultiCompLib::ParametrixTestLib.new }
        it '#turkey_test' do
            xi = [
                [12.2, 18.8, 18.2],
                [22.2, 20.5, 14.6],
                [20.8, 19.5, 26.3],
                [26.4, 32.5, 31.3],
                [24.5, 21.2, 22.4],
            ]
            res = [
                [false, false, false, true, false],
                [false, false, false, true, false],
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
                [false, false, false, true, false],
                [false, false, false, true, false],
                [false, false, false, false, false],
                [false, false, false, false, false],
                [false, false, false, false, false],
            ]
            expect(
                paraTest.bonferrono_test(xi, a)
            ).to is_array(res)
        end
    end
end

