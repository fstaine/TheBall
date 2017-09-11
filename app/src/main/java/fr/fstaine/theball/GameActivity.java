package fr.fstaine.theball;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

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

        Fragment gameFragment = GameFragment.newInstance(AppPreferences.getGameDifficulty(this));

        fragmentTransaction.add(R.id.game_fragment_container, gameFragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Fragment call ?
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(getApplicationContext(), GameSettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    //////////////////////////////
    @Override
    public void onGameStart() {

    }

    @Override
    public void onGamePause() {

    }

    @Override
    public void onGameResume() {

    }

    @Override
    public void onGameEnd(int playerScore) {
        Fragment scoreFragment = ScoreFragment.newInstance(playerScore);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.game_fragment_container, scoreFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBonusCaught(int reward) {

    }

    @Override
    public void onStartGame() {
        Log.d(TAG, "Restart a new game...");
        Fragment gameFragment = GameFragment.newInstance(AppPreferences.getGameDifficulty(this));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.game_fragment_container, gameFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    ////////////////////////////////
}
