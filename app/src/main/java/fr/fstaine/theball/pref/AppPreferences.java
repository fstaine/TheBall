package fr.fstaine.theball.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class AppPreferences {

    public static int getGameDifficulty(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.decode(sharedPref.getString("game_level", "0"));
    }

    public static TreeSet<Integer> getHighScore(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        Set<String> highScoresStr = sharedPref.getStringSet("high_scores", getDefaultHighScores());
        TreeSet<Integer> highScore = new TreeSet<>();
        for (String s : highScoresStr) {
            highScore.add(Integer.decode(s));
        }
        return highScore;
    }

    public static void updateHighScore(Context context, int newScore) {
        TreeSet<Integer> highScore = getHighScore(context);
        if (highScore.first() < newScore) {
            TreeSet<Integer> newHighScore = new TreeSet<>();
            newHighScore.addAll(highScore);
            newHighScore.add(newScore);
            while (newHighScore.size() > 3) {
                newHighScore.pollFirst();
            }
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPref.edit();
            TreeSet<String> newHighScoreStr = new TreeSet<>();
            for (Integer i : newHighScore) {
                newHighScoreStr.add(i.toString());
            }
            editor.putStringSet("high_scores", newHighScoreStr);
            editor.commit();
        }
    }

    public static SortedSet<String> getDefaultHighScores() {
        TreeSet<String> defaultHighScores = new TreeSet<>();
        defaultHighScores.add("0");
        return defaultHighScores;
    }
}
