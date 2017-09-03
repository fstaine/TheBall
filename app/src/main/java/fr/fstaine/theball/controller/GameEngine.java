package fr.fstaine.theball.controller;

import android.app.Service;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import fr.fstaine.theball.GameActivity;
import fr.fstaine.theball.physic.Ball;
import fr.fstaine.theball.physic.Bonus;

public class GameEngine {
	private final Ball ball;
	private final Bonus bonus;
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
		this.ball = b;
		this.bonus = bonus;

		mActivity = pView;
		mManager = (SensorManager) mActivity.getBaseContext().getSystemService(Service.SENSOR_SERVICE);
		mAccelerometer = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mManager.registerListener(mSensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);

		new Thread() {
			public void run(){
				while(true) {
                    if (isOnBonus()) {
                        ball.catchBall();
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

}
