package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;

public class Grade {
    static Api apis = new Api();

    static final int MIN_GRADE = 0;
    static final int MAX_GRADE = 9999;
    static final int MIN_SCORE = 1000;
    static final int MAX_SCORE = 100_000;
    static final int MAX_DIFF = 99_000;
    static final int DEFAULT = 2000;
    static final int SCORE_PER_GAME = 5000;
    static final int VARIATION = 20000;
    static final int MAX_GAME_TIME = 40;

    public void editGrade (String AUTH_KEY, HashMap<Integer, Integer> userGrade, HashMap<Integer, Integer> userScore, JSONArray gameResults) throws IOException, ParseException {
        if (gameResults.isEmpty()) return;

        final int WINNER = 0;
        final int LOSER = 1;

        HashMap<Integer, Integer> commands = new HashMap<>();

        for (Object o : gameResults) {
            JSONObject result = (JSONObject) o;
            int loseUserId = (int)(long) result.get("lose");
            int winUserId = (int)(long) result.get("win");
            int[] calculatedScore = calculateGrade(result, userGrade);

            int[] calculatedGrade = new int[2];
            double gradeScoreRatio = MAX_GRADE / MAX_SCORE;
            for (int i=0; i<2; i++) {
                int id = i==WINNER ? winUserId : loseUserId;
                int grade = userGrade.get(id);
                int score = userScore.get(id);
                int gradeDiff = (int) Math.round((calculatedScore[i] - score) * gradeScoreRatio);
                calculatedGrade[i] = i==WINNER ? Math.min(grade + gradeDiff, MAX_GRADE) : Math.max(grade + gradeDiff, MIN_GRADE);
                userGrade.put(id, calculatedGrade[i]);
            }

            userScore.put(winUserId, calculatedScore[WINNER]);
            userScore.put(loseUserId, calculatedScore[LOSER]);
            commands.put(winUserId, calculatedGrade[WINNER]);
            commands.put(loseUserId, calculatedGrade[LOSER]);
        }

        apis.changeGradeAPI(AUTH_KEY, commands);
    }

    public int[] calculateGrade(JSONObject result, HashMap<Integer, Integer> userGrade) {
        int takenTime = (int)(long) result.get("taken"); // 추정 점수치
        int loseUserId = (int)(long) result.get("lose");
        int winUserId = (int)(long) result.get("win");

        int loseUserGrade = userGrade.get(loseUserId);
        int winUserGrade = userGrade.get(winUserId);
        int diff = winUserGrade - loseUserGrade;

        winUserGrade = Math.min(winUserGrade + DEFAULT, MAX_SCORE);
        loseUserGrade = Math.max(loseUserGrade - DEFAULT, MIN_SCORE);

        // 1. 실력차와 게임결과과 동일할 때
        if (diff > 0) {
            // 1-1. 실력차가 많이 날때,
            if (diff > VARIATION) {
                return new int[] {winUserGrade, loseUserGrade};
            }

            // 1-2. 비슷할 때
            else {
                double ratio = (MAX_DIFF - diff) / MAX_DIFF;
                int gameScore = (int) Math.round(SCORE_PER_GAME * ratio);
                winUserGrade = Math.min(winUserGrade + gameScore, MAX_SCORE);
                loseUserGrade = Math.max(loseUserGrade - gameScore, MIN_SCORE);
            }
        }

        // 2. 실력차와 게임 결과가 반대일 때
        else {
            double ratio = diff / MAX_DIFF;
            int gameScore = (int) Math.round(SCORE_PER_GAME * (1+ratio));
            winUserGrade = Math.min(winUserGrade + gameScore, MAX_SCORE);
            loseUserGrade = Math.max(loseUserGrade - gameScore, MIN_SCORE);
        }

        return new int[] {winUserGrade, loseUserGrade};
    }

    public int getGradeDiffExpected (int time) {
        final int DIFF_PER_MIN = 2828;
        return DIFF_PER_MIN * (MAX_GAME_TIME - time);
    }

    public void initUserInfo(HashMap<Integer, Integer> userGrade, HashMap<Integer, Integer> userScore, int problem) {
        int numOfUser = problem == 1 ? 30 : 900;
        int initGrade = 5000;
        int initScore = 40000;
        for (int i=1; i<=numOfUser; i++) {
            userGrade.put(i, initGrade);
            userScore.put(i, initScore);
        }
    }
}
