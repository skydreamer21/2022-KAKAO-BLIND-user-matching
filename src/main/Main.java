package main;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.net.MalformedURLException;
import util.Util;
import org.json.simple.JSONObject;
import java.util.HashMap;


public class Main {
    static Util utils = new Util();
    static final String AUTH_TOKEN = "3ebf8579866e4e88c49da86d2a8b94fe";
    public static void main(String[] argv) throws Exception{
        URL url = new URL("https://huqeyhi95c.execute-api.ap-northeast-2.amazonaws.com/prod/start");
//        utils.printFields(url);
//        utils.printMethods(url);

        HttpsURLConnection httpsConn = (HttpsURLConnection) url.openConnection();
//        utils.printFields(httpsConn);
//        utils.printMethods(httpsConn);
        httpsConn.setRequestMethod("POST");
        httpsConn.setRequestProperty("X-Auth-Token", AUTH_TOKEN);
        httpsConn.setRequestProperty("Content-Type", "application/json");
        httpsConn.setConnectTimeout(1000);
        httpsConn.setReadTimeout(1000);
        httpsConn.setDoOutput(true);

        HashMap<String, Integer> map = new HashMap<>();
        map.put("problem", 1);
        JSONObject data = new JSONObject(map);

        OutputStream os = httpsConn.getOutputStream();
        byte[] input = data.toString().getBytes("utf-8");
        os.write(input, 0, input.length);

        int responseCode = httpsConn.getResponseCode();
        System.out.printf("code : %d\n",responseCode);

//        BufferedReader br = new BufferedReader(new InputStreamReader());
        httpsConn.disconnect();
    }
}