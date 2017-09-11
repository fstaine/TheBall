package fr.fstaine.theball.controller;

import android.app.Service;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import fr.fstaine.theball.GameFragment;
import fr.fstaine.theball.physic.Ball;
import fr.fstaine.theball.physic.Bonus;
import fr.fstaine.theball.view.GameView;

public class GameEngine {
    private static final String TAG = "GameEngine";

    private final Ball ball;
	private final Bonus bonus;
    private GameFragment mContainer;
    private OnGameEventListener mGameEventListener;
    // TODO: Put reward in Bonus ?
    private int reward;
    private int timer = 10000;

    private SensorManager mManager;
    private Sensor mAccelerometer;
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

    private EventThread mEventThread = new EventThread();

    private AsyncTask<Integer, Integer, Integer> timerTask = new AsyncTask<Integer, Integer, Integer>() {
        @Override
        protected Integer doInBackground(Integer... integers) {
            if (integers.length > 1) {
                Log.d(TAG, "timerTask must be called with only one parameter");
            }
            int seconds = integers[0];
            while (seconds > 0) {
                try {
                    Thread.currentThread().sleep(100);
                    seconds -= 100;
                    publishProgress(seconds);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (isCancelled()) break;
            }
            return seconds;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            stop();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mGameEventListener.onTimerChanged(values[0]);
        }

        @Override
        protected void onCancelled(Integer integer) {
            super.onCancelled(integer);
        }
    };

    public GameEngine(GameFragment pView, GameView gameView) {
        if (pView instanceof OnGameEventListener) {
            mGameEventListener = pView;
        } else {
            throw new RuntimeException(pView.toString()
                    + " must implement " + OnGameEventListener.class);
        }
        this.mContainer = pView;
        this.ball = gameView.getBall();
        this.bonus = gameView.getBonus();

        updateGameParams();

        mManager = (SensorManager) mContainer.getActivity().getBaseContext().getSystemService(Service.SENSOR_SERVICE);
        mAccelerometer = mManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        timerTask.execute(timer);
    }

    public void updateGameParams() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContainer.getContext());
        int gameLevel = Integer.decode(sharedPref.getString("game_level", "0"));
        Log.d(TAG, "Game level: " + gameLevel);

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

    public void start() {
        //mGameView.init();
        resetScore();
        mManager.registerListener(mSensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
        mEventThread = new EventThread();
        mEventThread.start();
        Log.d(TAG, "Start game engine");
    }

	public void stop() {
        Log.d(TAG, "Stopping game engine...");
        mManager.unregisterListener(mSensorEventListener, mAccelerometer);
        mEventThread.continueRunning = false;
        try {
            mEventThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mGameEventListener.onGameEnd();
        Log.d(TAG, "Game engine stopped");
    }

    private boolean isOnBonus() {
        return ball.getPosition().distance(bonus.getPosition()) < 2 * ball.getRadius();
    }

    public void resetScore() {
        ball.resetScore();
        mContainer.updateScore(ball.getScore());
    }

    public void updateScore(int reward) {
        ball.incrementScore(reward);
        mContainer.updateScore(ball.getScore());
    }

    public interface OnGameEventListener {
        void onTimerChanged(int msRemaining);

        void onGameEnd();
    }

    public final static class GameLevel {
        public final static int EASY = 0;
        public final static int MEDIUM = 1;
        public final static int HARD = 2;
    }

    private class EventThread extends Thread {
        boolean continueRunning = true;

        public void run() {
            while (continueRunning) {
                if (isOnBonus()) {
                    updateScore(reward);
                    bonus.setRandomPosition();
                }
                ball.update();
                try {
                    Thread.currentThread().sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
