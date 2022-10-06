package main;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.net.MalformedURLException;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import util.Util;
import org.json.simple.JSONObject;
import java.util.HashMap;


public class Main {
    static HttpsURLConnection httpsConn;
    static Util utils = new Util();
    static final String AUTH_TOKEN = "3ebf8579866e4e88c49da86d2a8b94fe";
    static final String BASE_URL = "https://huqeyhi95c.execute-api.ap-northeast-2.amazonaws.com/prod";

    public static void main(String[] argv) throws Exception{

        JSONObject start = startAPI(1);
        String authKey = (String) start.get("auth_key");
        int time = (int) ((long) start.get("time"));

        System.out.printf("authKey : %s, time : %d\n", authKey, time);

    }

    public static JSONObject startAPI(int problem) throws IOException, ParseException {
        URL url = new URL(BASE_URL + "/start");
        HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();

        httpsConn.setRequestMethod("POST");
        httpsConn.setRequestProperty("X-Auth-Token", AUTH_TOKEN);
        httpsConn.setRequestProperty("Content-Type", "application/json");
        httpsConn.setConnectTimeout(1000);
        httpsConn.setReadTimeout(1000);
        httpsConn.setDoOutput(true);

        HashMap<String, Integer> map = new HashMap<>();
        map.put("problem", problem);
        JSONObject data = new JSONObject(map);

        OutputStream os = httpsConn.getOutputStream();
        byte[] input = data.toString().getBytes("utf-8");
        os.write(input, 0, input.length);

        BufferedReader br = new BufferedReader(new InputStreamReader(httpsConn.getInputStream(),"utf-8"));
        StringBuilder response = new StringBuilder();
        String responseLine = null;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        httpsConn.disconnect();

        JSONParser jsonParser = new JSONParser();
        return (JSONObject) jsonParser.parse(response.toString());
    }
}