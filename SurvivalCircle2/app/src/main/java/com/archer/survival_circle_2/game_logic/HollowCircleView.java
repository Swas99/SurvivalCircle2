package com.archer.survival_circle_2.game_logic;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.archer.survival_circle_2.Helper;

/**
 * Created by Swastik on 17-01-2016.
 */
public class HollowCircleView extends View
{
    float radius;
    float centerX;
    float centerY;

    Context ct;

    /**
     * Default constructor
     *
     * @param ct {@link Context}
     */
    public HollowCircleView(final Context ct) {
        super(ct);
        this.ct = ct;
    }

    public HollowCircleView(final Context ct, final AttributeSet attrs) {
        super(ct, attrs);
        this.ct = ct;
    }

    public HollowCircleView(final Context ct, final AttributeSet attrs, final int defStyle) {
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

    private void setDimensions()
    {
        radius = Helper.ConvertToPx(ct,50);
    }

    public void recompute_position()
    {
        centerX = getX()+radius;
        centerY = getY()+radius;
    }

    public void init() {
        setDimensions();
    }
}
