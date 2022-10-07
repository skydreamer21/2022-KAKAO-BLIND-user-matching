package main;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

public class Match {
    static Api APIs = new Api();

    public void initUserGrade(HashMap<Integer, Integer> userGrade, int problem) {
        int numOfUser = problem == 1 ? 30 : 900;
        int initGrade = 40000;
        for (int i=1; i<=numOfUser; i++) {
            userGrade.put(i, initGrade);
        }
    }

    public void initMatch(String AUTH_KEY, JSONArray firstWaiting) throws IOException, ParseException {
        int len = firstWaiting.size();
        ArrayList<int[]> matchPairs = new ArrayList<>();

        for (int i=0; i<len; i++) {
            if (i%2 == 1) {
                int user1 = (int) (long)((JSONObject)firstWaiting.get(i-1)).get("id");
                int user2 = (int) (long)((JSONObject)firstWaiting.get(i)).get("id");
                matchPairs.add(new int[] {user1, user2});
            }
        }

        APIs.matchAPI(AUTH_KEY, matchPairs);
    }
}