package fr.fstaine.theball;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import fr.fstaine.theball.controller.GameEngine;
import fr.fstaine.theball.view.GameView;

public class GameActivity extends Activity {

    GameEngine gameEngine;
    GameView gameView;
    TextView mTextScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mTextScore = findViewById(R.id.textScore);
        Log.d("GameActivity", "" + mTextScore);
        gameView = findViewById(R.id.gameView);

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

    public void updateScore(final int score) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextScore.setText("" + score);
            }
        });
    }

}
