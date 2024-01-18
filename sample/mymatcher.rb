require_relative('myfilematcher')
require_relative('myisarrmatcher')

RSpec.configure do |config|
  config.include MyFileMatcher
  config.include MyIsArrMatcher
end


