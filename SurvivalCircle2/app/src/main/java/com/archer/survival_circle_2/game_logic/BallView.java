package com.archer.survival_circle_2.game_logic;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.archer.survival_circle_2.Helper;


public class BallView extends View
{
    Context ct;
    float radius;
    float centerX;
    float centerY;
    float MAX_X_VELOCITY;
    float MAX_Y_VELOCITY;
    float MAX_Y;
    float MAX_X;
    public float x_force;
    public float y_force;
    public float max_y_force;
    public float max_x_force;


    /**
     * Default constructor
     *
     * @param ct {@link Context}
     */
    public BallView(final Context ct) {
        super(ct);
        this.ct = ct;
    }

    public BallView(final Context ct, final AttributeSet attrs) {
        super(ct, attrs);
        this.ct = ct;
    }

    public BallView(final Context ct, final AttributeSet attrs, final int defStyle) {
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

    public void setDimensions()
    {
        radius = Helper.ConvertToPx(ct,27);

        MAX_X = Helper.SCREEN_WIDTH - 2*radius;
        MAX_Y = 2*Helper.SCREEN_HEIGHT/3 - 2*radius;

        MAX_X_VELOCITY = 20;
        MAX_Y_VELOCITY = 20; //MAX_X_VELOCITY * MAX_Y / MAX_X;
    }

    public void recompute_position()
    {
        centerX = getX()+radius;
        centerY = getY()+radius;
    }

    public void move()
    {
        moveX();
        moveY();
    }

    public void moveX()
    {
        float delta_x = get_raw_delta_x();

        if(getX()+delta_x>MAX_X)
            delta_x=MAX_X-getX();
        else if(getX()+delta_x<0)
            delta_x=-getX();

        setX(getX() + delta_x);
    }

    public void moveY()
    {
        float delta_y = get_raw_delta_y();

        if(getY()+delta_y>MAX_Y)
            delta_y=MAX_Y-getY();
        else if(getY() +delta_y<0)
            delta_y=-getY();

        setY(getY() + delta_y);
    }

    public float get_raw_delta_x()
    {
        float xForce = x_force;
        return MAX_X_VELOCITY *xForce/ max_x_force;
    }

    public float get_raw_delta_y()
    {
        float yForce = y_force;
        return MAX_Y_VELOCITY *yForce/ max_y_force;
    }


    public void reset_force()
    {
        x_force = 0;
        y_force = 0;
    }

}