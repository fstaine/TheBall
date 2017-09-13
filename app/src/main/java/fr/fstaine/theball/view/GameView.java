package fr.fstaine.theball.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import fr.fstaine.theball.physic.Ball;
import fr.fstaine.theball.physic.Bonus;

public class GameView extends SurfaceView implements SurfaceHolder.Callback, Runnable
{
    private SurfaceHolder mSurfaceHolder;
    private Thread mThread;
    private Paint mPaint;

    private Ball mBall;
    private Bonus mBonus;

    private boolean keepDrawing = true;

    public GameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameView(Context context) {
        super(context);
        init();
    }

    public void init() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        mPaint = new Paint();

        mBall = new Ball(this.getHeight(), this.getWidth());
        mBonus = new Bonus(this.getHeight(), this.getWidth());
    }

	public Ball getBall()
	{
        return this.mBall;
    }

    public Bonus getBonus() {
        return this.mBonus;
    }

    private void update(Canvas can) {
        mPaint.setStyle(Paint.Style.FILL);
        can.drawColor(Color.WHITE);

        mPaint.setColor(Color.RED);
        can.drawCircle(mBall.getPosX(), mBall.getPosY(), mBall.getRadius(), mPaint);

        mPaint.setColor(Color.BLUE);
        can.drawCircle(mBonus.getPosX(), mBonus.getPosY(), mBonus.getRadius(), mPaint);
    }

	@Override
	public void surfaceCreated(SurfaceHolder pHolder) {
        keepDrawing = true;
        mThread = new Thread(this);
        mThread.start();
    }

	@Override
	public void surfaceChanged(SurfaceHolder holder, int pFormat, int pWidth, int pHeight) {
        this.mBall.setHeight((float) pHeight);
        this.mBall.setWidth((float) pWidth);

        this.mBonus.setHeight((float) pHeight);
        this.mBonus.setWidth((float) pWidth);
        this.mBonus.setRandomPosition();
    }

	@Override
    public void surfaceDestroyed(SurfaceHolder pHolder) {
        keepDrawing = false;
        boolean retry = true;
        while (retry) {
			try {
				mThread.join();
				retry = false;
			} catch (InterruptedException e) {
                e.printStackTrace();}
		}
	}

    @Override
    public void run() {
        Canvas canvas;
        while (keepDrawing) {
            canvas = null;
            try {
                canvas = mSurfaceHolder.lockCanvas();
                if (canvas != null) {
                    synchronized (mSurfaceHolder) {
                        update(canvas);
                    }
                }
            } finally {
                if (canvas != null)
                    mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
            // 50 fps
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
