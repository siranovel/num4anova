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
        it '#twoway_anova 1' do
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
    end
    describe Num4AnovaLib::Num4AncovaLib do
        let!(:ancova) { Num4AnovaLib::Num4AncovaLib.new }
        it '#parallel_test' do
           xi = [
              [
                  [3,35], [5,38], [3,39],
              ],
              [
                  [3,36], [3,39], [8,54],
              ],
              [
                  [2,40], [2,45], [2,39],
              ],
              [
                  [3,47], [4,52], [2,48],
              ],
              [
                  [1,64], [2,80], [0,70],
              ],
            ]
            expect(
               ancova.parallel_test(xi, a)
            ).to eq false
        end
        it '#significance_test' do
           xi = [
              [
                  [3,35], [5,38], [3,39],
              ],
              [
                  [3,36], [3,39], [8,54],
              ],
              [
                  [2,40], [2,45], [2,39],
              ],
              [
                  [3,47], [4,52], [2,48],
              ],
              [
                  [1,64], [2,80], [0,70],
              ],
            ]
            expect(
               ancova.significance_test(xi, a)
            ).to eq true
        end
        it '#difference_test' do
           xi = [
              [
                  [3,35], [5,38], [3,39],
              ],
              [
                  [3,36], [3,39], [8,54],
              ],
              [
                  [2,40], [2,45], [2,39],
              ],
              [
                  [3,47], [4,52], [2,48],
              ],
              [
                  [1,64], [2,80], [0,70],
              ],
            ]
            expect(
               ancova.difference_test(xi, a)
            ).to eq true
        end
        it '#interval_estim' do
           xi = [
              [
                  [3,35], [5,38], [3,39],
              ],
              [
                  [3,36], [3,39], [8,54],
              ],
              [
                  [2,40], [2,45], [2,39],
              ],
              [
                  [3,47], [4,52], [2,48],
              ],
              [
                  [1,64], [2,80], [0,70],
              ],
            ]
            res = {
                    "min": 4.47,
                    "max": 7.19
                  }
            expect(
               ancova.interval_estim(xi, a)
            ).to interval(res, 2)
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
end

