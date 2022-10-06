package main;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.net.MalformedURLException;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import util.Util;
import org.json.simple.JSONObject;
import java.util.HashMap;


public class Main {
    static HttpsURLConnection httpsConn;
    static Util utils = new Util();
    static String AUTH_KEY;
    static final String AUTH_TOKEN = "3ebf8579866e4e88c49da86d2a8b94fe";
    static final String BASE_URL = "https://huqeyhi95c.execute-api.ap-northeast-2.amazonaws.com/prod";
    static final String GET = "GET";
    static final String POST = "POST";
    static final String PUT = "PUT";
    static final String WAITING_LINE = "waiting_line";
    static final String GAME_RESULT = "game_result";
    static final String USER_INFO = "user_info";

    static final String COLOR_INFO = "INFO ---> ";
    static final String COLOR_DEBUG = "DEBUG ---> ";
    static final String COLOR_TRACE = "TRACE ---> ";
    static final String COLOR_WARNING = "WARNING ---> ";
    static final String COLOR_ERROR = "ERROR ---> ";
    static final String COLOR_FATAL = "FATAL ---> ";


    public static void main(String[] argv) throws Exception{

        JSONObject start = startAPI(1);
        AUTH_KEY = (String) start.get("auth_key");
        int time = (int) ((long) start.get("time"));
        System.out.printf("authKey : %s, time : %d\n", AUTH_KEY, time);

        JSONObject userInfo = getInfoAPI(USER_INFO);
        System.out.println(userInfo.toString());

        JSONObject match1 = matchAPI(new int[][] {});
        System.out.println(match1.toString());

        JSONObject waiting = getInfoAPI(WAITING_LINE);
        System.out.println(waiting.toString());

        HashMap<Integer, Integer> commands = new HashMap<>();
        commands.put(1, 1000);
        commands.put(2, 1500);
        commands.put(3, 1123);

        JSONObject change = changeGradeAPI(commands);
        System.out.println(change.toString());

        JSONObject userInfo2 = getInfoAPI(USER_INFO);
        System.out.println(COLOR_INFO + userInfo2.toString());
    }

    public static JSONObject startAPI(int problem) throws IOException, ParseException {
        URL url = new URL(BASE_URL + "/start");
        HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();

        httpsConn.setRequestMethod(POST);
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

    public static JSONObject getInfoAPI(String apiName) throws IOException, ParseException {
        URL url = new URL(BASE_URL + "/" + apiName);
        HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
        httpsConn.setRequestMethod(GET);
        httpsConn.setRequestProperty("Authorization", AUTH_KEY);
        httpsConn.setRequestProperty("Content-Type", "application/json");

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

    public static JSONObject matchAPI(int[][] pairs) throws IOException, ParseException {
        URL url = new URL(BASE_URL + "/match");
        HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();

        httpsConn.setRequestMethod(PUT);
        httpsConn.setRequestProperty("Authorization", AUTH_KEY);
        httpsConn.setRequestProperty("Content-Type", "application/json");
        httpsConn.setConnectTimeout(1000);
        httpsConn.setReadTimeout(1000);
        httpsConn.setDoOutput(true);

        HashMap<String, JSONArray> map = new HashMap<>();
        JSONArray pairsJSON = utils.pairsToJSONArray(pairs);
        map.put("pairs", pairsJSON);
        JSONObject data = new JSONObject(map);
//        System.out.println(data.toString());

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

    public static JSONObject changeGradeAPI(HashMap<Integer, Integer> commands) throws IOException, ParseException {
        URL url = new URL(BASE_URL + "/change_grade");
        HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();

        httpsConn.setRequestMethod(PUT);
        httpsConn.setRequestProperty("Authorization", AUTH_KEY);
        httpsConn.setRequestProperty("Content-Type", "application/json");
        httpsConn.setConnectTimeout(1000);
        httpsConn.setReadTimeout(1000);
        httpsConn.setDoOutput(true);

        HashMap<String, JSONArray> map = new HashMap<>();
        JSONArray pairsJSON = utils.commandsToJSONArray(commands);
        map.put("commands", pairsJSON);
        JSONObject data = new JSONObject(map);
//        System.out.println(data.toString());

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