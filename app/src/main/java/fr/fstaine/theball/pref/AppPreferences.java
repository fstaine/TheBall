package fr.fstaine.theball.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPreferences {
    // Get game difficulty
    public static int getGameDifficulty(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return Integer.decode(sharedPref.getString("game_level", "0"));
    }

    // Get best scores

    //
}
