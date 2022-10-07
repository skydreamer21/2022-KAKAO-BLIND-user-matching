package util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import main.WaitPlayer;

public class Json {
//    public void waitingParser()

    public JSONArray commandsToJSONArray(HashMap<Integer, Integer> commands) {
        JSONArray jsonArray = new JSONArray();

        for (Map.Entry<Integer, Integer> entry : commands.entrySet()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", entry.getKey());
            jsonObject.put("grade", entry.getValue());
            jsonArray.add(jsonObject);
        }

        return jsonArray;
    }

    public JSONArray pairsToJSONArray(ArrayList<int[]> arr) {
        int len = arr.size();

        JSONArray jsonArray = new JSONArray();
        for (int i=0; i<len; i++) {
            jsonArray.add(intArrToJson(arr.get(i)));
        }
        return jsonArray;
    }

    public JSONArray intArrToJson (int[] arr) {
        JSONArray jsonArray = new JSONArray();
        for (int num : arr) jsonArray.add(num);
        return jsonArray;
    }
}
