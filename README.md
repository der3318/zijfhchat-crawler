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
|線上專案反編譯服務|[Java Decompilers](http://www.javadecompilers.com/apk)|
|圖形介面反編譯工具|[JADX v1.1.0](https://github.com/skylot/jadx)|
|逆向工程軟件|[IDA Pro v7.0](http://www.42xz.com/soft/2307.html)|
|手機封包監聽|[Packet Capture v1.5.0](https://play.google.com/store/apps/details?id=app.greyshirts.sslcapture&hl=en_US)|
|測試模擬工具|[API Tester](https://apitester.com/)|
|簡訊斷線通知|[Twilio](https://www.twilio.com/console)|
|手機訊息推播|[LINE Notify](https://notify-bot.line.me/en/)|


### 運行與部署
#### 1. 下載遊戲並抽取安裝檔，或是透過封包監聽，查詢遊戲伺服器相關資訊
|![Imgur](https://i.imgur.com/6Vx8JDx.png)|![Imgur](https://i.imgur.com/xjrABRc.png)|![Imgur](https://i.imgur.com/YkNVcBS.png)|
|:-:|:-:|:-:|

|![Imgur](https://i.imgur.com/nr6eDh2.png)|![Imgur](https://i.imgur.com/2GN0Pht.png)|
|:-:|:-:|


#### 2. 參數修改與設定
* 包含了 Twilio 帳號與電話、Line Notify，以及伺服器相關資訊
* 主要修改檔案 `conf/application.conf`
* 可能需要修改的檔案 `src/main/java/starter/gradle/ZijfhBackgroundProcess.java`


#### 3. 網站架設與背景服務掛載
參考[Jooby 官方網站](https://jooby.org)與[教學專案](https://github.com/jooby-project/gradle-starter)


#### (Optional) Docker 部署運行
* `$ gradlew shadowJar` 生成 Fat JAR
* `$ cp -rf build/libs/gradle-starter-1.0-all.jar zijfhchat-crawler.jar` 來移動並重新命名
* `$ docker build -t zijfhchat-crawler .` 生成鏡像
* 如有必要，可修改 `DockerFile`、查看鏡像 `$ docker images` 或刪除它 `$ docker rmi -f [image ID]`
* 掛載 `/DatabaseDir/jooby.db` 並運行 Docker: `$ docker run -p 3318:3318 -v /DatabaseDir:/HostDB zijfhchat-crawler`


##### 網頁介面
|![Imgur](https://i.imgur.com/pKuuUpo.png)|![Imgur](https://i.imgur.com/2ta4K03.png)|
|:-:|:-:|


##### 背景程式
![Imgur](https://i.imgur.com/LWYXCIu.png)


##### 資料庫
![Imgur](https://i.imgur.com/Tdg72Nb.png)


##### LINE 訊息推播
|![Imgur](https://i.imgur.com/0yWg21g.png)|![Imgur](https://i.imgur.com/70K9p79.png)|![Imgur](https://i.imgur.com/qSFiw1E.png)|
|:-:|:-:|:-:|


