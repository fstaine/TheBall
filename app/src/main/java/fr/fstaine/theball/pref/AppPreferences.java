package fr.fstaine.theball.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AppPreferences {

    private final static int HIGH_SCORE_DEFAULT_VALUE = 0;
    private final static int HIGH_SCORE_SIZE = 3;
    private final static String HIGH_SCORE_SEPERATOR = ";";

    public static int getGameDifficulty(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.decode(sharedPref.getString("game_level", "0"));
    }

    /**
     * Get the highScores ordered from higher to lower
     * @param context context in which to get the the highScores values
     * @return a list of scores, in descending order
     */
    public static List<Integer> getHighScore(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String highScoresStr = sharedPref.getString("high_scores", toHighScorePref(getDefaultHighScores()));
        return toHighScoreValues(highScoresStr);
    }

    public static void updateHighScore(Context context, int newScore) {
        List<Integer> highScores = getHighScore(context);
        Integer lower = highScores.get(highScores.size() - 1);
        if (lower < newScore || highScores.size() < HIGH_SCORE_SIZE) {
            highScores.add(newScore);
            Collections.sort(highScores, Collections.<Integer>reverseOrder());
            highScores.subList(HIGH_SCORE_SIZE, highScores.size()).clear();

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("high_scores", toHighScorePref(highScores));
            editor.commit();
            Log.d("HighScore", "" + highScores);
        }
    }

    public static void resetHighScores(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("high_scores", toHighScorePref(getDefaultHighScores()));
        editor.commit();
        Log.d("HighScore", "Reset high scores...");
    }

    private static List<Integer> getDefaultHighScores() {
        List<Integer> defaultHighScores = new ArrayList<>();
        for (int i=0; i<HIGH_SCORE_SIZE; i++) {
            defaultHighScores.add(HIGH_SCORE_DEFAULT_VALUE);
        }
        return defaultHighScores;
    }

    private static String toHighScorePref(List<Integer> highScores) {
        StringBuilder builder = new StringBuilder();
        for (Integer i : highScores) {
            builder.append(i.toString());
            builder.append(HIGH_SCORE_SEPERATOR);
        }
        return builder.toString();
    }

    private static List<Integer> toHighScoreValues(String str) {
        String[] strList = str.split(HIGH_SCORE_SEPERATOR);
        List<Integer> highScores = new ArrayList<>(HIGH_SCORE_SIZE);
        for (String s : strList) {
            Log.d("STR", s);
            Integer highScore = Integer.decode(s);
            highScores.add(highScore);
        }
        return highScores;
    }
}
