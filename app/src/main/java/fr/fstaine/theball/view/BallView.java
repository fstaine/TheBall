package fr.fstaine.theball.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import fr.fstaine.theball.physic.Ball;
import fr.fstaine.theball.physic.Bonus;

public class BallView extends SurfaceView implements SurfaceHolder.Callback
{
	SurfaceHolder mSurfaceHolder;
	DrawingThread mThread;

	Paint mPaint;
	Ball ball;
	Bonus bonus;

	public BallView(Context context)
	{
		super(context);
		mSurfaceHolder = getHolder();
		mSurfaceHolder.addCallback(this);
		mThread = new DrawingThread();
		mPaint = new Paint();
		mPaint.setStyle(Paint.Style.FILL);

		ball = new Ball(this.getHeight(), this.getWidth());
		bonus = new Bonus(this.getHeight(), this.getWidth());
	}

	public Ball getBall()
	{
		return this.ball;
	}

	@Override
	public void onDraw(Canvas can)
	{
		can.drawColor(Color.WHITE);

		mPaint.setColor(Color.RED);
		can.drawCircle(ball.getPosX(), ball.getPosY(), ball.getRadius(), mPaint);

		mPaint.setColor(Color.BLUE);
		can.drawCircle(bonus.getPosX(), bonus.getPosY(), bonus.getRadius(), mPaint);
	}

	@Override
	public void surfaceCreated(SurfaceHolder pHolder) {
		mThread.keepDrawing = true;
		mThread.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int pFormat, int pWidth, int pHeight) {
		this.ball.setHeight((float) pHeight);
		this.ball.setWidth((float) pWidth);

		this.bonus.setHeight((float) pHeight);
		this.bonus.setWidth((float) pWidth);
		this.bonus.setRandomPosition();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder pHolder)
	{
		mThread.keepDrawing = false;
		boolean retry = true;
		while (retry) {
			try {
				mThread.join();
				retry = false;
			} catch (InterruptedException e) {}
		}
	}

	public Bonus getBonus() {
		return bonus;
	}

	private class DrawingThread extends Thread {
		boolean keepDrawing = true;

		@Override
		public void run() {
			Canvas canvas;
			while (keepDrawing) {
				canvas = null;
				try {
					canvas = mSurfaceHolder.lockCanvas();
					synchronized (mSurfaceHolder) {
						onDraw(canvas);
					}
				} finally {
					if (canvas != null)
						mSurfaceHolder.unlockCanvasAndPost(canvas);
				}
				// Pour dessiner à 50 fps
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {}
			}
		}
	}



}
