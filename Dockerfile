FROM java:8
MAINTAINER der3318
COPY ./ /ZijfhchatCrawler/
VOLUME /HostDB
EXPOSE 3318
WORKDIR /ZijfhchatCrawler
ENTRYPOINT ["java","-jar","zijfhchat-crawler.jar","port=3318","db=jdbc:sqlite:/HostDB/jooby.db"]
