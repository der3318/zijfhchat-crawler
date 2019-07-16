package starter.gradle;

import com.typesafe.config.Config;

import org.jdbi.v3.core.Jdbi;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ZijfhBackgroundProcess implements Runnable {

    private App app;
    private int sevid;
    private String uid, token, ver;

    public ZijfhBackgroundProcess(App _app) {
        Config conf = _app.require(Config.class);
        this.app = _app;
        this.sevid = conf.getInt("zijfhchat.sevid");
        this.uid = conf.getString("zijfhchat.uid");
        this.token = conf.getString("zijfhchat.token");
        this.ver = conf.getString("zijfhchat.ver");
    }

    private void sendLineNotification(String _msg) throws Exception {
        Config conf = this.app.require(Config.class);
        String urlFormat = conf.getString("line.notificationURL");
        URL url = new URL(urlFormat);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        String urlParameters = "msg=" + Utils.encodeToURL(_msg);
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        in.read();
        in.close();
    }

    @Override
    public void run() {
        /** database interface */
        Jdbi jdbi = this.app.require(Jdbi.class);
        try {
            while (true) {
                /** http request */
                String urlFormat = "http://152.32.166.11/servers/s%d.php?ver=%s&platform=epandxianyuovergat_zjfh&lang=tw&sevid=%d&uid=%s&token=%s";
                URL url = new URL(String.format(urlFormat, this.sevid, this.ver, this.sevid, this.uid, this.token));
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                /** request property */
                con.setRequestMethod("POST");
                con.setRequestProperty("Accept-Encoding", "identity");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                con.setRequestProperty("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 8.0.0; G3426 Build/48.1.A.0.138)");
                con.setRequestProperty("Host", "game-epzjfhovergat.xianyuyouxi.com");
                /** parameters */
                String urlParameters = "{\"user\":{\"adok\":{\"label\":\"\"}}}";
                /** streaming */
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(urlParameters);
                wr.flush();
                wr.close();
                /** get response */
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = in.readLine()) != null) response.append(inputLine);
                in.close();
                /** parse json */
                JSONObject j = new JSONObject(response.toString());
                /** case no.1 - no status */
                if (!j.has("s")) break;
                /** case no.2 - status is 0 */
                if (j.getInt("s") == 0) break;
                /** case no.3 - retrieve chat info */
                if (j.has("u") && j.get("u") instanceof JSONObject && j.getJSONObject("u").has("chat") && j.getJSONObject("u").getJSONObject("chat").has("sev")) {
                    JSONArray ja = j.getJSONObject("u").getJSONObject("chat").getJSONArray("sev");
                    for (int jaIdx = 0; jaIdx < ja.length(); jaIdx++) {
                        /** define info */
                        JSONObject obj = ja.getJSONObject(jaIdx);
                        DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm Z");
                        String time = format.format(Calendar.getInstance().getTime());
                        String name = obj.getJSONObject("user").getString("name").replaceAll("'", "");
                        String message = obj.getString("msg").replaceAll("'", "");
                        int isGM = obj.getInt("isGM");
                        int userid = obj.getJSONObject("user").getInt("uid");
                        int level = obj.getJSONObject("user").getInt("level");
                        int sex = obj.getJSONObject("user").getInt("sex");
                        int chenghao = obj.getJSONObject("user").getInt("chenghao");
                        int vip = obj.getJSONObject("user").getInt("vip");
                        /** print to standard error */
                        System.err.print("----------------------------------------INFO--------------------------------------------\n");
                        String chatFormat = "\t%s %s - %s\n\tisGM=%d userid=%d level=%d sex=%d chenghao=%d vip=%d\n";
                        System.err.print(String.format(chatFormat, time, name, message, isGM, userid, level, sex, chenghao, vip));
                        System.err.print(String.format("---------------------------------------------------------------------%s\n\n", Utils.getCurrentDateString()));
                        /** add to database */
                        jdbi.useHandle(h -> {
                            String sqlFormat = "INSERT INTO zijfhchat%d (time, name, message, isGM, userid, level, sex, chenghao, vip) VALUES ('%s', '%s', '%s', %d, %d, %d, %d, %d, %d)";
                            h.createUpdate(String.format(sqlFormat, this.sevid, time, name, message, isGM, userid, level, sex, chenghao, vip)).execute();
                        });
                        /** send line notification */
                        String lineMsgFormat = "\n\uD83C\uDD94 %s \uD83C\uDFC5 %s\n\uD83D\uDC64 %s\n\u270F\uFE0F %s";
                        this.sendLineNotification(String.format(lineMsgFormat, userid, vip, name, message));
                    }
                }
                /** sleep five seconds */
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Config conf = this.app.require(Config.class);
            if (conf.getInt("twilio.activate") > 0) {
                Utils.sendTwilioSMS(conf.getString("twilio.e164From"), conf.getString("twilio.e164To"), conf.getString("twilio.disconnectedMsg"));
            }
            System.err.print("----------------------------------------INFO--------------------------------------------\n");
            System.err.print("\tZijfh Background Process Disconnected\n");
            System.err.print(String.format("\tsevid=%d uid=%s token=%s\n", this.sevid, this.uid, this.token));
            System.err.print(String.format("---------------------------------------------------------------------%s\n\n", Utils.getCurrentDateString()));
        }
    }

}
