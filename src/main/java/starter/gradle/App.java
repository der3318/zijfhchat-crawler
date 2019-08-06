package starter.gradle;

import com.twilio.Twilio;
import com.typesafe.config.Config;

import org.jdbi.v3.core.Jdbi;
import org.jooby.Jooby;
import org.jooby.exec.Exec;
import org.jooby.jdbc.Jdbc;
import org.jooby.jdbi.Jdbi3;
import org.jooby.json.Jackson;
import org.jooby.pebble.Pebble;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * web application
 */
public class App extends Jooby {

    /**
     * future map for background processes
     */
    public Map<Integer, Future> futureMap;

    {
        /** static images */
        assets("/images/**");

        /** javascripts */
        assets("/javascripts/**");

        /** pebble */
        use(new Pebble("templates", ".html"));

        /** database */
        use(new Jdbc());
        use(new Jdbi3());

        /** json */
        use(new Jackson());

        /** executor */
        use(new Exec());
        futureMap = new HashMap<>();

        onStart(() -> {
            /** config */
            Config conf = require(Config.class);
            /** registry mapper */
            Jdbi jdbi = require(Jdbi.class);
            jdbi.registerRowMapper(HandlerForZijfhchat.ChatRecord.class, (rs, ctx) -> new HandlerForZijfhchat.ChatRecord(rs));
            /** create database tables if needed */
            List<String> tableNameList = jdbi.withHandle(h -> {
                String sql = "SELECT name FROM sqlite_master WHERE type='table' ORDER BY name";
                return h.createQuery(sql).mapTo(String.class).stream().map(String::toLowerCase).collect(Collectors.toList());
            });
            /** sevid for zijfhchat */
            int sevid = conf.getInt("zijfhchat.sevid");
            /** table zijfhchat */
            if (!tableNameList.contains(String.format("zijfhchat%d", sevid))) {
                jdbi.useHandle(h -> {
                    String sqlFormat = "CREATE TABLE zijfhchat%d (id INTEGER PRIMARY KEY, time VARCHAR(255), name VARCHAR(255), message VARCHAR(255), isGM INTEGER, userid INTEGER, level INTEGER, sex INTEGER, chenghao INTEGER, vip INTEGER)";
                    h.createUpdate(String.format(sqlFormat, sevid)).execute();
                });
            }
            /** twilio init */
            Twilio.init(conf.getString("twilio.sid"), conf.getString("twilio.token"));
            /** executor bot */
            ExecutorService executor = require(ExecutorService.class);
            futureMap.put(sevid, executor.submit(new ZijfhBackgroundProcess(this)));
        });

        onStop(() -> {
            /** shutdown executor bot */
            ExecutorService executor = require(ExecutorService.class);
            executor.shutdown();
        });

        /** show connection info */
        use("/**", (req, rsp, chain) -> {
            synchronized (App.this) {
                System.err.print("----------------------------------------INFO--------------------------------------------\n");
                System.err.print(String.format("\tip=%s method=%s length=%d hostname=%s\n", req.ip(), req.method(), (int) req.length(), req.hostname()));
                System.err.print(String.format("\t%s %s\n", req.rawPath(), req.params()));
                System.err.print(String.format("---------------------------------------------------------------------%s\n\n", Utils.getCurrentDateString()));
                chain.next(req, rsp);
            }
        });

        /** GET - home page */
        get("/", new HandlerForHomePage());

        /** GET - zijfhchat */
        get("/zijfhchat/{sevid}", new HandlerForZijfhchat(this));

        /** GET - zijfhclub */
        get("/zijfhclub/{sevid}", new HandlerForZijfhclub());
    }

    public static void main(String[] args) {
        run(App::new, args);
    }
}
