package fr.fstaine.theball;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import fr.fstaine.theball.controller.GameEngine;
import fr.fstaine.theball.view.GameView;

public class GameActivity extends AppCompatActivity {

    GameEngine gameEngine;

    GameView gameView;
    TextView mTextScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mTextScore = (TextView) findViewById(R.id.textScore);
        gameView = (GameView) findViewById(R.id.gameView);

        gameEngine = new GameEngine(this, gameView.getBall(), gameView.getBonus());
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameEngine.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameEngine.stop();
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

    public void updateScore(final int score) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextScore.setText("" + score);
            }
        });
    }

}
