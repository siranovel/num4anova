# 区間
module MyIntervalMatcher
  class Matcher
    def initialize(expected, n)
      @expected = expected
      @n = n
    end
    def matches?(actual)
        ret = false
        @actual = actual
        act_min = actual[:min]
        act_max = actual[:max]

        if (act_min.round(@n) == @expected[:min]) &&
           (act_max.round(@n) == @expected[:max]) then
            ret = true
        end
        return ret
    end
    def failure_message
      "#{@expected} expected but got #{@actual}"
    end
  end
  def interval(expected, n)
    Matcher.new(expected, n)
  end
end

