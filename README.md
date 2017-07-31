## Twitterキャンペーンのサンプル
よく見かけるTwitterをつかったキャンペーンを再現したサンプル。

よく見る以下のパターンをチェックするサンプル
* 対象のアカウントをフォローしている
* 対象のツイートをリツートしている

主に使っているのは以下の通り
* Spring Boot (Spring Social Twitter, Thymeleaf)
* Twitter4J

また、実行するには以下の情報をプロパティファイル（`src/main/resources/application.properties`）か<br>
環境変数に設定する必要がある。

キー | 説明
--- | ---
spring.social.twitter.appId | TwitterアプリのConsumer Key
spring.social.twitter.appSecret | TwitterアプリのConsumer Secret
twitter.screenName | このスクリーン名のユーザのフォローのチェックを行う
twitter.tweetId | このツイートをリツイートしているかのチェックを行う
