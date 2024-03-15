import os
import tweepy

CONSUMER_KEY = os.environ.get("TWITTER_CONSUMER_KEY")
CONSUMER_SECRET = os.environ.get("TWITTER_CONSUMER_SECRET")
ACCESS_KEY = os.environ.get("TWITTER_ACCESS_TOKEN")
ACCESS_KEY_SECRET = os.environ.get("TWITTER_ACCESS_TOKEN_SECRET")

# オブジェクト作成
client = tweepy.Client(
    consumer_key = CONSUMER_KEY,
    consumer_secret = CONSUMER_SECRET,
    access_token = ACCESS_KEY,
    access_token_secret = ACCESS_KEY_SECRET)

# ツィートする
tweet = 'New commit pushed! num4anova'
client/create_tweet(text=tweet)

