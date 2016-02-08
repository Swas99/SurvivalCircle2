package com.archer.survival_circle_2.game_logic;

import android.content.Context;
import android.graphics.Canvas;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.archer.survival_circle_2.R;

import java.lang.ref.WeakReference;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Swastik on 18-01-2016.
 */
public class DirectionPadView extends View {

    Context ct;
    private BallView objBall;
    private SinglePlayerGame objGame;

    float radius;
    float centerX;
    float centerY;

    boolean isActivated;


    /**
     * Default constructor
     *
     * @param ct {@link Context}
     */
    public DirectionPadView(final Context ct) {
        super(ct);
        this.ct = ct;
    }

    public DirectionPadView(final Context ct, final AttributeSet attrs) {
        super(ct, attrs);
        this.ct = ct;
    }

    public DirectionPadView(final Context ct, final AttributeSet attrs, final int defStyle) {
        super(ct, attrs, defStyle);
        this.ct = ct;
    }

    @Override
    public void onDraw(final Canvas canv) {
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);

        radius = Math.min(w/2f,h/2f);
        centerX = getX()+w/2f;
        centerY = getY()+h/2f;
        objBall.max_y_force = radius;
        objBall.max_x_force = radius;
    }

    public void init(WeakReference<BallView> _ball, WeakReference<SinglePlayerGame> singlePlayerGameWeakReference)
    {
        objBall = _ball.get();
        objGame = singlePlayerGameWeakReference.get();
        isActivated = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!isActivated)
            return true;

        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            if(!objGame.isAlive)
                objGame.start();
        }


        float xTouch = event.getX();
        float yTouch = event.getY();
        setForce(xTouch, yTouch);
        return true;
    }

    public void setForce(float x,float y) {
        float deltaX = x - centerX;
        float deltaY = y - centerY;

        objBall.x_force = deltaX;
        objBall.y_force = deltaY;

        if(Math.abs(deltaX)>radius)
            objBall.x_force = radius * Math.signum(deltaX);
        if(Math.abs(deltaY)>radius)
            objBall.y_force = radius * Math.signum(deltaY);
    }

}
