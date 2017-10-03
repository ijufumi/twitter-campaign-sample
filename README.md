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

## 実装機能一覧（チェックリスト）
### 全体
- [x] リクエスト毎にTwitterのコネクションの有無をチェックする
- [x] TwitterのコネクションがなければTwitterのログイン画面に遷移する
- [x] Twitterから戻ってきたら元のURLに遷移する

### トップ画面
- [x] キャンペーンの一覧を表示する
- [x] 並び順は開始が未来の順
- [x] 有効期間と表示期間を分ける

### キャンペーン詳細
- [ ] キャンペーンを実施する
- [ ] キャンペーンキーを指定してキャンペーンを特定する
- [ ] RTのチェック＆フォローのチェックを行う
- [ ] すでに抽選したものについては、結果画面を表示する
- [ ] キャンペーンキーとTwitterのidを元に抽選結果の有無をチェックする
- [x] 埋め込み用APIを使って、HTMLを取得する

### キャンペーン詳細（抽選）
- [ ] DBのデータを元に抽選を行う。当選したら個人情報入力画面を表示する

### キャンペーン詳細（当選）
- [ ] メールアドレスを入力する画面を表示する
- [ ] Twitterからメールアドレスが取得できている場合は、それを初期表示する
- [ ] メールアドレスの重複チェックは行わない
- [ ] メールアドレス入力後にトップページ（一覧画面）に遷移するボタンを表示する

### キャンペーン詳細（落選）
- [ ] 「落ちました」っぽいメッセージを表示する
- [ ] トップページ（一覧画面）に遷移するボタンを表示する