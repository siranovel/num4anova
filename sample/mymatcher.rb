require_relative('myfilematcher')
require_relative('myisarrmatcher')
require_relative('myintervalmatcher')

RSpec.configure do |config|
  config.include MyFileMatcher
  config.include MyIsArrMatcher
  config.include MyIntervalMatcher
end


