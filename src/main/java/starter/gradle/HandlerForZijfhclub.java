package starter.gradle;

import org.jooby.Request;
import org.jooby.Results;
import org.jooby.Route;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HandlerForZijfhclub implements Route.OneArgHandler {

    /**
     * information of a club
     */
    public static class ClubInfo {
        public String name, id, level, exp, fund, isJoin;
        public int rid, num, allShiLi;

        public ClubInfo(JSONObject _json) {
            this.rid = _json.getInt("rid");
            this.name = _json.getString("name");
            this.id = _json.getString("id");
            this.level = _json.getString("level");
            this.exp = _json.getString("exp");
            this.fund = _json.getString("fund");
            this.num = _json.getInt("num");
            this.allShiLi = _json.getInt("allShiLi");
            this.isJoin = _json.getString("isJoin");
        }
    }

    @Override
    public Object handle(Request req) throws Exception {
        /** parameters from request */
        int sevid = req.param("sevid").intValue(-1);
        File file = new File(String.format("./public/zijfhclub%s.json", sevid));
        /** retrieve data from local json file and render */
        List<ClubInfo> clubInfoList = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        JSONArray clubs = new JSONObject(scanner.nextLine()).getJSONObject("a").getJSONObject("club").getJSONArray("clubList");
        for (int rankIdx = 0; rankIdx < clubs.length(); rankIdx++) {
            clubInfoList.add(new ClubInfo(clubs.getJSONObject(rankIdx)));
        }
        scanner.close();
        /** check file update time */
        String updateTime = new SimpleDateFormat("yyyy/MM/dd HH:mm").format(file.lastModified());
        /** render html page */
        return Results.html("zijfhclub").put("clubInfoList", clubInfoList).put("updateTime", updateTime);
    }

}
