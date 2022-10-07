package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;

public class WaitPlayer implements Comparable<WaitPlayer> {
    final int PRIORITY_TIME = 10;
    int id, waitingTime, grade;

    public WaitPlayer(int id, int waitingTime, int grade) {
        this.id = id;
        this.waitingTime = waitingTime;
        this.grade = grade;
    }

    @Override
    public int compareTo (WaitPlayer o) {
        if (this.waitingTime >= PRIORITY_TIME && o.waitingTime < PRIORITY_TIME) return -1;
        else if (this.waitingTime < PRIORITY_TIME && o.waitingTime >= PRIORITY_TIME) return 1;
        else {
            return this.grade - o.grade;
        }
    }

    public static WaitPlayer[] getWaitingList (JSONArray waiting, HashMap<Integer, Integer> userGrade, int gameTime) {
        int len = waiting.size();
        WaitPlayer[] waitingList = new WaitPlayer[len];

        for (int i=0; i<len; i++) {
            JSONObject wait = (JSONObject) waiting.get(i);
            int id = (int)(long)wait.get("id");
            int from = (int)(long)wait.get("from");
            waitingList[i] = new WaitPlayer(id, gameTime - from, userGrade.get(id));
        }
        return waitingList;
    }
}