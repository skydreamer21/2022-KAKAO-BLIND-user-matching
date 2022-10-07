package main;

import javax.net.ssl.HttpsURLConnection;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import util.Util;
import util.Json;

public class Main {
    static HttpsURLConnection httpsConn;
    static Util utils = new Util();
    static Api APIs = new Api();
    static Match match = new Match();
    static String AUTH_KEY;

    static int problem;
    static int numOfUsers;
    static int gameTime = 0; // matchAPI 호출 시 1씩 증가
    static HashMap<Integer, Integer> waitingList = new HashMap<>();
    static HashMap<Integer, Integer> userGrade = new HashMap<>();

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
        problem = 1;
        numOfUsers = problem == 1 ? 30 : 900;

        JSONObject start = APIs.startAPI(problem);
        AUTH_KEY = (String) start.get("auth_key");
        int time = (int) ((long) start.get("time"));
        System.out.printf("authKey : %s, time : %d\n", AUTH_KEY, time);
        match.initUserGrade(userGrade, problem);

        // matchAPI 호출로 매칭 시작
        APIs.matchAPI(AUTH_KEY, new ArrayList<>());
        gameTime++;
        utils.printTime(gameTime);
        // 첫번째 waiting Line
        JSONObject resJson = APIs.getInfoAPI(AUTH_KEY, WAITING_LINE);
//        match.matchUsers(AUTH_KEY, userGrade, (JSONArray) resJson.get("waiting_line"), gameTime);
        // 첫번째 매칭은 user에 대해 아는 정보가 없기 때문에 임의로 진행
        match.initMatch(AUTH_KEY, (JSONArray) resJson.get("waiting_line"));
        gameTime++;
        utils.printTime(gameTime);

        int MAX_TIME = 50;
        for (int t = gameTime; t<MAX_TIME; t++) {
            JSONObject gameResult = APIs.getInfoAPI(AUTH_KEY, GAME_RESULT);
            System.out.println(gameResult.toString());
            /*
            1. waiting_line을 받아서 적절한 user Match
            2. game_result를 받아서 user_grade 수정
             */
            JSONObject waiting = APIs.getInfoAPI(AUTH_KEY, WAITING_LINE);
            System.out.println(waiting.toString());
            match.matchUsers(AUTH_KEY, userGrade, (JSONArray) waiting.get("waiting_line"), gameTime);
            gameTime++;
            utils.printTime(gameTime);
        }


        JSONObject waiting = APIs.getInfoAPI(AUTH_KEY, WAITING_LINE);
        System.out.println(waiting.toString());


        /*
        JSONObject userInfo = APIs.getInfoAPI(AUTH_KEY, USER_INFO);
        System.out.println(userInfo.toString());

        JSONObject match1 = APIs.matchAPI(AUTH_KEY, new int[][] {});
        System.out.println(match1.toString());

        JSONObject waiting = APIs.getInfoAPI(AUTH_KEY, WAITING_LINE);
        System.out.println(waiting.toString());

        JSONObject match2 = APIs.matchAPI(AUTH_KEY, new int[][] {{21,22}, {23,24}, {25,26}, {27,28}, {29,30}});
        System.out.println(match2.toString());

        JSONObject match3 = APIs.matchAPI(AUTH_KEY, new int[][] {{1,2}, {3,4}, {5,6}, {7,8}, {9,10}, {11, 12}, {13,14}, {15,16}, {17,18}, {19,20}});
        System.out.println(match3.toString());

        JSONObject gameResult = APIs.getInfoAPI(AUTH_KEY, GAME_RESULT);
        System.out.println(gameResult.toString());

        for (int i=0; i<45; i++) {
            JSONObject match_res = APIs.matchAPI(AUTH_KEY, new int[][] {});
            System.out.println(COLOR_INFO + match_res.toString());
            System.out.println(COLOR_INFO + APIs.getInfoAPI(AUTH_KEY, GAME_RESULT).toString());
            System.out.println(COLOR_DEBUG + APIs.getInfoAPI(AUTH_KEY, WAITING_LINE).toString());
            System.out.println();
        }

        JSONObject waiting2 = APIs.getInfoAPI(AUTH_KEY, WAITING_LINE);
        System.out.println(waiting2.toString());

        HashMap<Integer, Integer> commands = new HashMap<>();
        commands.put(1, 1000);
        commands.put(2, 1500);
        commands.put(3, 1123);

        JSONObject change = APIs.changeGradeAPI(AUTH_KEY, commands);
        System.out.println(change.toString());

        JSONObject userInfo2 = APIs.getInfoAPI(AUTH_KEY, USER_INFO);
        System.out.println(COLOR_INFO + userInfo2.toString());
         */
    }
}