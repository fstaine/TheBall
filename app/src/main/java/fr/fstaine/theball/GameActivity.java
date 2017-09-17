package fr.fstaine.theball;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import fr.fstaine.theball.pref.AppPreferences;

public class GameActivity extends AppCompatActivity implements GameFragment.OnGameFragmentInteractionListener, ScoreFragment.OnScoreFragmentInteractionListener {
    private static final String TAG = "GameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment gameFragment = GameFragment.newInstance();

        fragmentTransaction.add(R.id.game_fragment_container, gameFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_reset_scores:
                resetHighScores();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGameEnd(int playerScore) {
        Fragment scoreFragment = ScoreFragment.newInstance(playerScore);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.game_fragment_container, scoreFragment, "fragment_score");
        transaction.commit();
    }

    @Override
    public void onStartGame() {
        Log.d(TAG, "Restart a new game...");
        Fragment gameFragment = GameFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.game_fragment_container, gameFragment);
        transaction.commit();
    }

    private void resetHighScores() {
        AppPreferences.resetHighScores(this);
        ScoreFragment scoreFragment = (ScoreFragment) getSupportFragmentManager().findFragmentByTag("fragment_score");
        if (scoreFragment != null) {
            scoreFragment.fillHighScores();
        } else{
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.toast_check_icon,
                    (ViewGroup) findViewById(R.id.toast_container));
            Toast toast = new Toast(this);
            toast.setView(layout);
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
