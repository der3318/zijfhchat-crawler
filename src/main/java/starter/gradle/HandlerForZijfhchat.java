package starter.gradle;

import org.jdbi.v3.core.Jdbi;
import org.jooby.Request;
import org.jooby.Results;
import org.jooby.Route;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class HandlerForZijfhchat implements Route.OneArgHandler {

    /**
     * information of record
     */
    public static class ChatRecord {
        public String time, name, message;
        public int isGM, userid, level, sex, chenghao, vip;

        public ChatRecord(ResultSet _rs) throws SQLException {
            this.time = _rs.getString("time").replace("+0800", "").replaceAll("[0-9][0-9][0-9][0-9]/", "");
            this.name = _rs.getString("name");
            this.message = _rs.getString("message");
            this.isGM = _rs.getInt("isGM");
            this.userid = _rs.getInt("userid");
            this.level = _rs.getInt("level");
            this.sex = _rs.getInt("sex");
            this.chenghao = _rs.getInt("chenghao");
            this.vip = _rs.getInt("vip");
        }
    }

    private App app;

    public HandlerForZijfhchat(App _app) {
        this.app = _app;
    }

    @Override
    public Object handle(Request req) throws Exception {
        /** database interface */
        Jdbi jdbi = this.app.require(Jdbi.class);
        /** parameters from request */
        int sevid = req.param("sevid").intValue(-1), n = req.param("n").intValue(100);
        String op = req.param("op").value(""), query = req.param("query").value("");
        boolean reload = req.param("reload").booleanValue(false);
        /** query records from database */
        List<ChatRecord> recordList = jdbi.withHandle(h -> {
            String sqlFormat = "SELECT time, name, message, isGM, userid, level, sex, chenghao, vip FROM zijfhchat%d ORDER BY id DESC LIMIT %d";
            String sql = String.format(sqlFormat, sevid, n);
            if (op.equals("keyword")) {
                String token = "%" + query + "%";
                sql = sql.replace("ORDER", String.format("WHERE name LIKE '%s' OR message LIKE '%s' ORDER", token, token));
            } else if (op.equals("userid")) {
                String token = query;
                if (!query.matches("-?\\d+")) token = "-1";
                sql = sql.replace("ORDER", String.format("WHERE userid = %s ORDER", token));
            }
            return h.createQuery(sql).mapTo(ChatRecord.class).list();
        });
        /** check connection status */
        boolean offline = false;
        if (this.app.futureMap.get(sevid).isDone()) offline = true;
        /** return reversed json array response */
        Collections.reverse(recordList);
        return Results.html("zijfhchat").put("recordList", recordList)
                .put("sevid", sevid).put("offline", offline)
                .put("op", op).put("query", query).put("n", n)
                .put("reload", reload);
    }

}
