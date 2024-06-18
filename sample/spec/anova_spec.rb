require 'spec_helper'
require 'num4anova'

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
        it '#replicate_plot' do
            vals = {
                "stageA1" => [27, 52, 18, 21, 32],
                "stageA2" => [52, 72, 31, 50, 45],
                "stageA3" => [47, 54, 29, 43, 32],
                "stageA4" => [28, 50, 22, 26, 29],
            }
            expect(
                oneWay.replicate_plot("aaa", vals)
            ).to is_exist("replicate.jpeg")
        end
        it '#replicate_test' do
            xi = [
                [27, 52, 18, 21, 32],
                [52, 72, 31, 50, 45],
                [47, 54, 29, 43, 32],
                [28, 50, 22, 26, 29],
            ]
            expect(
                oneWay.replicate_test(xi, a)
            ).to eq true
        end
        it '#kruskalwallis_test' do
            xi = [
                [12.2, 18.8, 18.2],
                [22.2, 20.5, 14.6, 20.8, 19.5, 26.3],
                [26.4, 32.5, 31.3, 24.5, 21.2, 22.4],
            ]
            expect(
                oneWay.kruskalwallis_test(xi, a)
            ).to eq true

        end
    end
    describe Num4AnovaLib::TwoWayLayoutLib do
        let!(:twoWay) { Num4AnovaLib::TwoWayLayoutLib.new }
        it '#twoway_anova 1' do
            xij = [
                    [
                      [13.2, 15.7, 11.9],
                      [16.1, 15.7, 15.1],
                      [9.1,  10.3,  8.2],
                    ],
                    [
                      [22.8, 25.7, 18.5],
                      [24.5, 21.2, 24.2],
                      [11.9, 14.3, 13.7],
                    ],
                    [
                      [21.8, 26.3, 32.1],
                      [26.9, 31.3, 28.3],
                      [15.1, 13.6, 16.2],
                    ],
                    [
                      [25.7, 28.8, 29.5],
                      [30.1, 33.8, 29.6],
                      [15.2, 17.3, 14.8],
                    ],
            ]
            res = [true, true, true]
            expect(
                twoWay.twoway_anova(xij, a)
            ).to is_array(res)
        end
        it '#twoway_anova 2' do
            xij = [
                    [
                      [13.2, 15.7, 11.9],
                      [16.1, 15.7, 15.1],
                    ],
                    [
                      [22.8, 25.7, 18.5],
                      [24.5, 21.2, 24.2],
                    ],
                    [
                      [21.8, 26.3, 32.1],
                      [26.9, 31.3, 28.3],
                    ],
                    [
                      [25.7, 28.8, 29.5],
                      [30.1, 33.8, 29.6],
                    ],
            ]
            res = [true, false, false]
            expect(
                twoWay.twoway_anova(xij, a)
            ).to is_array(res)
        end
        it '#twoway2_anova' do
            xij = [
                [13.6, 15.6, 9.2],
                [22.3, 23.3, 13.3],
                [26.7, 28.8, 15.0],
                [28.0, 31.2, 15.8],
            ]
            res = [true, true]
            expect(
                twoWay.twoway2_anova(xij, a)
            ).to is_array(res)
        end
        it '#friedman_test' do
            xij = [
                [13.6, 15.6, 9.2],
                [22.3, 23.3, 13.3],
                [26.7, 28.8, 15.0],
                [28.0, 31.2, 15.8],
            ]
            expect(
                twoWay.friedman_test(xij, a)
            ).to eq true
        end
        it '#create_oneway' do
            xij = [
                    [
                      [13.2, 15.7, 11.9],
                      [16.1, 15.7, 15.1],
                      [9.1,  10.3,  8.2],
                    ],
                    [
                      [22.8, 25.7, 18.5],
                      [24.5, 21.2, 24.2],
                      [11.9, 14.3, 13.7],
                    ],
                    [
                      [21.8, 26.3, 32.1],
                      [26.9, 31.3, 28.3],
                      [15.1, 13.6, 16.2],
                    ],
                    [
                      [25.7, 28.8, 29.5],
                      [30.1, 33.8, 29.6],
                      [15.2, 17.3, 14.8],
                    ],
            ]
            res = [
                [13.6, 15.6, 9.2],
                [22.3, 23.3, 13.3],
                [26.7, 28.8, 15.0],
                [28.0, 31.2, 15.8],
            ]
            expect(
                twoWay.create_oneway(xij)
            ).to is_rounds(res, 1)
        end
    end
    describe Num4AnovaLib::Num4AncovaLib do
        let!(:ancova) { Num4AnovaLib::Num4AncovaLib.new }
        it '#parallel_test' do
            yi = [
                [3, 5, 3],
                [3, 3, 8],
                [2, 2, 2],
                [3, 4, 2],
                [1, 2, 0],
           ]
           xi = [
              [35, 38, 39],
              [36, 39, 54],
              [40, 45, 39],
              [47, 52, 48],
              [64, 80, 70],
            ]
            expect(
               ancova.parallel_test(yi, xi, a)
            ).to eq false
        end
        it '#significance_test' do
            yi = [
                [3, 5, 3],
                [3, 3, 8],
                [2, 2, 2],
                [3, 4, 2],
                [1, 2, 0],
           ]
           xi = [
              [35, 38, 39],
              [36, 39, 54],
              [40, 45, 39],
              [47, 52, 48],
              [64, 80, 70],
            ]
            expect(
               ancova.significance_test(yi, xi, a)
            ).to eq true
        end
        it '#difference_test' do
            yi = [
                [3, 5, 3],
                [3, 3, 8],
                [2, 2, 2],
                [3, 4, 2],
                [1, 2, 0],
           ]
           xi = [
              [35, 38, 39],
              [36, 39, 54],
              [40, 45, 39],
              [47, 52, 48],
              [64, 80, 70],
            ]
            expect(
               ancova.difference_test(yi, xi, a)
            ).to eq true
        end
        it '#interval_estim' do
            yi = [
                [3, 5, 3],
                [3, 3, 8],
                [2, 2, 2],
                [3, 4, 2],
                [1, 2, 0],
           ]
           xi = [
              [35, 38, 39],
              [36, 39, 54],
              [40, 45, 39],
              [47, 52, 48],
              [64, 80, 70],
            ]
            res = [
                    { "min":  4.47, "max":  7.19 },
                    { "min":  5.06, "max":  6.39 },
                    { "min":  2.51, "max":  4.25 },
                    { "min":  2.81, "max":  2.96 },
                    { "min": -6.30, "max": -0.66 },
                  ]
            expect(
               ancova.interval_estim(yi, xi, a)
            ).to intervals(res, 2)
        end
    end    
end

