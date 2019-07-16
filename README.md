## 紫禁繁花 - 聊天室爬蟲與即時查詢
### 簡介
透過安裝檔拆包與封包監聽，擷取伺服器網址與相關請求的標頭及參數，最後偽裝在線帳號備份聊天頻道至資料庫，並提供網頁介面瀏覽及查詢


### 框架及相關工具
|網站開源框架|[Jooby](https://jooby.org/)|
|:-:|:-:|
|資料庫瀏覽|[SQLite](https://www.sqlite.org/index.html)|
|自動化建置|[Gradle v4.8](https://gradle.org/)|
|網頁前端框架|[Semantic UI v2.4.1](https://semantic-ui.com/)|
|安裝檔抽取|[APK 抽取 v1.3.7](https://play.google.com/store/apps/details?id=com.pandaz.apkextraction&hl=zh_TW&showAllReviews=true)|
|專案反編譯|[Java Decompilers](http://www.javadecompilers.com/apk)|
|手機封包監聽|[Packet Capture v1.5.0](https://play.google.com/store/apps/details?id=app.greyshirts.sslcapture&hl=en_US)|
|測試模擬工具|[API Tester](https://apitester.com/)|
|簡訊斷線通知|[Twilio](https://www.twilio.com/console)|
|手機訊息推播|[LINE Notify](https://notify-bot.line.me/en/)|


### 運行與佈署
#### 1. 下載遊戲並抽取安裝檔，或是透過封包監聽，查詢遊戲伺服器相關資訊
|![Imgur](https://i.imgur.com/6Vx8JDx.png)|![Imgur](https://i.imgur.com/xjrABRc.png)|![Imgur](https://i.imgur.com/YkNVcBS.png)|
|:-:|:-:|:-:|


#### 2. 參數修改與設定
* 包含了 Twilio 帳號與電話、Line Notify，以及伺服器相關資訊
* 主要修改檔案 `conf/application.conf`
* 可能需要修改的檔案 `src/main/java/starter/gradle/ZijfhBackgroundProcess.java`


#### 3. 網站架設與背景服務掛載
參考[Jooby 官方網站](https://jooby.org)與[教學專案](https://github.com/jooby-project/gradle-starter)


##### 網頁介面
![Imgur](https://i.imgur.com/GeU8I5T.png)


##### 背景程式
![Imgur](https://i.imgur.com/LWYXCIu.png)


##### 資料庫
![Imgur](https://i.imgur.com/Tdg72Nb.png)


##### LINE 訊息推播
|![Imgur](https://i.imgur.com/0yWg21g.png)|![Imgur](https://i.imgur.com/70K9p79.png)|![Imgur](https://i.imgur.com/qSFiw1E.png)|
|:-:|:-:|:-:|


