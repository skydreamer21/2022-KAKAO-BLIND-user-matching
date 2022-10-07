package main;

import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import main.WaitPlayer;

public class Match {
    static Api APIs = new Api();
    HashMap<Integer, Integer> matchGradeLimit;

    static final int MAX_GAME_TIME = 15;
    static final int EMPTY = -1;

    static final String COLOR_MATCH = "MATCH ---> ";
    static final String COLOR_DEBUG = "DEBUG ---> ";

    public Match() {
        System.out.printf("실행??\n");
        this.matchGradeLimit = new HashMap<>();
        for (int t=0; t<=5; t++) matchGradeLimit.put(t, 14140);
        for (int i=1; i<=5; i++) {
            int gradeLimit = 14140 + (5656*i);
            matchGradeLimit.put(4 + (2*i), gradeLimit);
            matchGradeLimit.put(5 + (2*i), gradeLimit);
        }
    }

    public void matchUsers(String AUTH_KEY, HashMap<Integer, Integer> userScore, JSONArray waiting, int gameTime) throws IOException, ParseException {
        if (waiting.isEmpty()) {
            APIs.matchAPI(AUTH_KEY, new ArrayList<>());
            return;
        };

        int numOfWaiting = waiting.size();
        WaitPlayer[] waitingList = WaitPlayer.getWaitingList(waiting, userScore, gameTime);
        Arrays.sort(waitingList);
        boolean[] isMatched = new boolean[numOfWaiting];
        ArrayList<int[]> matchPairs = new ArrayList<>();

        for (int first=0; first<numOfWaiting; first++) {
            if (isMatched[first]) continue;
            int matchingGradeLimit = this.matchGradeLimit.get(waitingList[first].waitingTime);
            int maxSecondPlayerPriority = 0;
            int secondPlayerIdx = EMPTY;

            for (int second = first+1; second < numOfWaiting; second++) {
                if (isMatched[second]) continue;
                int gradeDiff = Math.abs(waitingList[first].grade - waitingList[second].grade);
                if (gradeDiff > matchingGradeLimit) continue;

                int prorityScoreForSecondPlayer =  secondPlayerPriority(waitingList[second], matchingGradeLimit, gradeDiff, gameTime);
                if (prorityScoreForSecondPlayer >  maxSecondPlayerPriority) {
                    secondPlayerIdx = second;
                    maxSecondPlayerPriority = prorityScoreForSecondPlayer;

                }
            }

            if (secondPlayerIdx == EMPTY) {
                System.out.printf("%d user는 매칭이 안되었습니다.\n", waitingList[first].id);
                continue;
            }

            matchPairs.add(new int[] {waitingList[first].id, waitingList[secondPlayerIdx].id});
            isMatched[first] = true;
            isMatched[secondPlayerIdx] = true;

            System.out.printf(COLOR_MATCH + "first : %d, second : %d, gradeDiff : %d, time : (%d, %d)\n",
                    waitingList[first].id, waitingList[secondPlayerIdx].id, Math.abs(waitingList[first].grade - waitingList[secondPlayerIdx].grade),
                    waitingList[first].waitingTime, waitingList[secondPlayerIdx].waitingTime);
        }

        APIs.matchAPI(AUTH_KEY, matchPairs);
    }

    public int secondPlayerPriority (WaitPlayer secondPlayer, int matchingGradeLimit, int gradeDiff, int gameTime) {
        final int GRADE_WEIGHT = 1;
        final int WAITING_TIME_WEIGHT = 1 ;

        double diffScore = ((matchingGradeLimit - gradeDiff) / matchingGradeLimit) * GRADE_WEIGHT;
        double waitingTimeScore = (secondPlayer.waitingTime / MAX_GAME_TIME) * WAITING_TIME_WEIGHT;
        return (int) Math.round(( diffScore + waitingTimeScore ) * 1000);
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