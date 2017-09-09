package fr.fstaine.theball.controller;

import android.app.Service;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.util.Log;

import fr.fstaine.theball.GameActivity;
import fr.fstaine.theball.physic.Ball;
import fr.fstaine.theball.physic.Bonus;

public class GameEngine {

	private final Ball ball;
	private final Bonus bonus;
    private int reward;
    private SensorEventListener mSensorEventListener = new SensorEventListener() {
        @Override
		public void onSensorChanged(SensorEvent pEvent) {
			float x = pEvent.values[0];
			float y = pEvent.values[1];

			ball.setAcceleration(x, y);
		}

		@Override
		public void onAccuracyChanged(Sensor pSensor, int pAccuracy) {
		}
	};
    private GameActivity mActivity;
    private SensorManager mManager;
    private Sensor mAccelerometer;
	public GameEngine(GameActivity pView, Ball b, final Bonus bonus)
	{
        this.mActivity = pView;
        this.ball = b;
		this.bonus = bonus;

        updateGameParams();

		mManager = (SensorManager) mActivity.getBaseContext().getSystemService(Service.SENSOR_SERVICE);
		mAccelerometer = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mManager.registerListener(mSensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);

		new Thread() {
			public void run(){
				while(true) {
                    if (isOnBonus()) {
                        ball.incrementScore(reward);
                        mActivity.updateScore(ball.getScore());
                        bonus.setRandomPosition();
                    }
					try {
						Thread.currentThread().sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

    public void updateGameParams() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mActivity);
        Log.d("Pref", sharedPref.getString("game_level", "0"));
        int gameLevel = Integer.decode(sharedPref.getString("game_level", "0"));

        if (gameLevel == GameLevel.EASY) {
            reward = 10;
            ball.setParams(0.25f, 10f, 0.1f);
        } else if (gameLevel == GameLevel.MEDIUM) {
            reward = 10;
            ball.setParams(0.2f, 20f, 0.2f);
        } else {
            reward = 10;
            ball.setParams(0.20f, 40f, 0.4f);
        }
    }

	// Arrête le capteur
	public void stop() {
		mManager.unregisterListener(mSensorEventListener, mAccelerometer);
	}

	// Redémarre le capteur
	public void resume() {
		mManager.registerListener(mSensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
	}

    private boolean isOnBonus() {
        return ball.getPosition().distance(bonus.getPosition()) < 2 * ball.getRadius();
    }

    private final static class GameLevel {
        public final static int EASY = 0;
        public final static int MEDIUM = 1;
        public final static int HARD = 2;
    }

}
