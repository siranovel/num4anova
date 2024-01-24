# 配列
module MyIsArrMatcher
  class Matcher
    def initialize(expected)
      @expected = expected
    end
    def matches?(actual)
        @actual = actual
        return @actual == @expected 
    end
    def failure_message
      "#{@expected} expected but got #{@actual}"
    end

  end
  def is_array(expected)
    Matcher.new(expected)
  end
end

