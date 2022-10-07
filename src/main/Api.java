package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.ArrayList;

import util.Util;
import util.Json;

public class Api {
    static final int HTTP_TIME = 5000;

    static Util utils = new Util();
    static Json jsons = new Json();
    static final String AUTH_TOKEN = "3ebf8579866e4e88c49da86d2a8b94fe";
    static final String BASE_URL = "https://huqeyhi95c.execute-api.ap-northeast-2.amazonaws.com/prod";
    static final String GET = "GET";
    static final String POST = "POST";
    static final String PUT = "PUT";

    public JSONObject startAPI(int problem) throws IOException, ParseException {
        URL url = new URL(BASE_URL + "/start");
        HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();

        httpsConn.setRequestMethod(POST);
        httpsConn.setRequestProperty("X-Auth-Token", AUTH_TOKEN);
        httpsConn.setRequestProperty("Content-Type", "application/json");
        httpsConn.setConnectTimeout(HTTP_TIME);
        httpsConn.setReadTimeout(HTTP_TIME);
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

    public JSONObject getInfoAPI(String AUTH_KEY, String apiName) throws IOException, ParseException {
        URL url = new URL(BASE_URL + "/" + apiName);
        HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
        httpsConn.setRequestMethod(GET);
        httpsConn.setConnectTimeout(HTTP_TIME);
        httpsConn.setReadTimeout(HTTP_TIME);
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

    public JSONObject matchAPI(String AUTH_KEY, ArrayList<int[]> pairs) throws IOException, ParseException {
        URL url = new URL(BASE_URL + "/match");
        HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();

        httpsConn.setRequestMethod(PUT);
        httpsConn.setRequestProperty("Authorization", AUTH_KEY);
        httpsConn.setRequestProperty("Content-Type", "application/json");
        httpsConn.setConnectTimeout(HTTP_TIME);
        httpsConn.setReadTimeout(HTTP_TIME);
        httpsConn.setDoOutput(true);

        HashMap<String, JSONArray> map = new HashMap<>();
        JSONArray pairsJSON = jsons.pairsToJSONArray(pairs);
        map.put("pairs", pairsJSON);
        JSONObject data = new JSONObject(map);
        System.out.println(data.toString());

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

    public JSONObject changeGradeAPI(String AUTH_KEY, HashMap<Integer, Integer> commands) throws IOException, ParseException {
        URL url = new URL(BASE_URL + "/change_grade");
        HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();

        httpsConn.setRequestMethod(PUT);
        httpsConn.setRequestProperty("Authorization", AUTH_KEY);
        httpsConn.setRequestProperty("Content-Type", "application/json");
        httpsConn.setConnectTimeout(HTTP_TIME);
        httpsConn.setReadTimeout(HTTP_TIME);
        httpsConn.setDoOutput(true);

        HashMap<String, JSONArray> map = new HashMap<>();
        JSONArray pairsJSON = jsons.commandsToJSONArray(commands);
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
