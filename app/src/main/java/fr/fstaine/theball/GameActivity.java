package fr.fstaine.theball;


import android.app.Activity;
import android.os.Bundle;

import fr.fstaine.theball.controller.GameEngine;
import fr.fstaine.theball.view.BallView;

public class GameActivity extends Activity {

    BallView ballView;
    GameEngine gameEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        ballView = new BallView(this);
        setContentView(ballView);

        gameEngine = new GameEngine(this, ballView.getBall(), ballView.getBonus());
    }
}
