package utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.util.stream.Collectors.joining;

public class DataUtil {
    public static String readInputStream(InputStream stream) {
        try {
            return new BufferedReader(new InputStreamReader(stream, "UTF-8")).lines().collect(joining("\n"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
