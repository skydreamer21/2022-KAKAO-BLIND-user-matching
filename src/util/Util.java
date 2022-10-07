package util;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.PriorityQueue;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class Util {
    public <T> void printFields(T o) {
        String className = o.getClass().getName();
        System.out.printf("[INFO] this is Fields about %s\n", className);
        Arrays.stream(o.getClass().getFields()).forEach(System.out::println);
        System.out.println();
    }

    public <T> void printMethods(T o) {
        Method[] methods = o.getClass().getMethods();
        String className = o.getClass().getName();
        System.out.printf("[INFO] this is Methods about %s\n", className);
        for (Method method : methods) {
            System.out.println(method.toString());
        }
        System.out.println();
    }

    }
}

