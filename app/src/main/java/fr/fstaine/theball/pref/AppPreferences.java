package fr.fstaine.theball.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fr.fstaine.theball.controller.GameEngine;

public class AppPreferences {
    private final static int HIGH_SCORE_SIZE = 3;

    private final static String HIGH_SCORE_SEPARATOR = ";";
    private final static String ELEMENT_SEPARATOR = ",";

    private final static HighScore HIGH_SCORE_DEFAULT_VALUE = new HighScore(0, GameEngine.GameLevel.EASY);

    public static int getGameDifficulty(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.decode(sharedPref.getString("game_level", "0"));
    }

    /**
     * Get the highScores ordered from higher to lower
     * @param context context in which to get the the highScores values
     * @return a list of scores, in descending order
     */
    public static List<HighScore> getHighScore(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        String highScoresStr = sharedPref.getString("high_scores", toHighScorePref(getDefaultHighScores()));
        return toHighScoreValues(highScoresStr);
    }

    public static void updateHighScore(Context context, int newScore, int difficulty) {
        List<HighScore> highScores = getHighScore(context);
        HighScore newHighScore = new HighScore(newScore, difficulty);
        if (highScores.size() < HIGH_SCORE_SIZE || highScores.get(highScores.size() - 1).compareTo(newHighScore) < 0) {
            highScores.add(newHighScore);
            Collections.sort(highScores, Collections.<HighScore>reverseOrder());
            if (highScores.size() > HIGH_SCORE_SIZE) {
                highScores.subList(HIGH_SCORE_SIZE, highScores.size()).clear();
            }
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("high_scores", toHighScorePref(highScores));
            // TODO: check if it doesn't cause problem when highScores are showed in the next view
            editor.apply();
            Log.d("HighScore", "" + highScores);
        }
    }

    public static void resetHighScores(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("high_scores", toHighScorePref(getDefaultHighScores()));
        editor.apply();
        Log.d("HighScore", "Reset high scores...");
    }

    private static List<HighScore> getDefaultHighScores() {
        List<HighScore> defaultHighScores = new ArrayList<>();
        return defaultHighScores;
    }

    private static String toHighScorePref(List<HighScore> highScores) {
        StringBuilder builder = new StringBuilder();
        for (HighScore i : highScores) {
            builder.append(i.toString());
            builder.append(HIGH_SCORE_SEPARATOR);
        }
        return builder.toString();
    }

    private static List<HighScore> toHighScoreValues(String str) {
        String[] strList = str.split(HIGH_SCORE_SEPARATOR);
        List<HighScore> highScores = new ArrayList<>(HIGH_SCORE_SIZE);
        for (String s : strList) {
            if (!s.isEmpty()) {
                highScores.add(HighScore.decode(s));
            }
        }
        return highScores;
    }

    public static class HighScore implements Comparable<HighScore> {
        int score, difficulty;

        public HighScore(int score, int difficulty) {
            this.score = score;
            this.difficulty = difficulty;
        }

        private HighScore(int score) {
            this.score = score;
            this.difficulty = GameEngine.GameLevel.EASY;
        }

        private HighScore() {
            this.score = 0;
            this.difficulty = GameEngine.GameLevel.EASY;
        }

        public int getScore() {
            return this.score;
        }

        public int getDifficulty() {
            return this.difficulty;
        }

        @Override
        public int compareTo(@NonNull HighScore highScore) {
            int order1 = this.getScore() - highScore.getScore();
            if (order1 != 0) {
                return order1;
            } else {
                return this.getDifficulty() - highScore.getDifficulty();
            }
        }

        @Override
        public String toString() {
            return Integer.toString(getScore()) + ELEMENT_SEPARATOR + Integer.toString(getDifficulty());
        }

        public static HighScore decode(String str) {
            String[] elems = str.split(ELEMENT_SEPARATOR);
            try {
                if (elems.length < 2) {
                    Log.e("HighScore", "Incorrect number of elements in: " + str);
                    if (elems.length == 1) {
                        int score = Integer.decode(elems[0]);
                        return new HighScore(score);
                    } else {
                        return new HighScore();
                    }
                }
                int score = Integer.decode(elems[0]);
                int difficulty = Integer.decode(elems[1]);
                return new HighScore(score, difficulty);
            } catch (NumberFormatException e) {
                Log.e("HighScore", e.getMessage());
                return new HighScore();
            }
        }
    }
}
