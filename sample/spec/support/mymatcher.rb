RSpec.configure do |config|
  config.include MyFileMatcher
  config.include MyIsArrMatcher
  config.include MyIntervalMatcher
end


