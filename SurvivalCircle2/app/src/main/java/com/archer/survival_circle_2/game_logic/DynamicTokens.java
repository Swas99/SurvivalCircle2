package com.archer.survival_circle_2.game_logic;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.archer.survival_circle_2.Helper;
import com.archer.survival_circle_2.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Swastik on 13-01-2016.
 */
public class DynamicTokens {

    Random objRnd;
    ViewGroup mainContainer;
    List<View> activeTokens = new ArrayList<>();

    int max_x;
    int min_x;
    int max_y;
    int min_y;

    SinglePlayerGame objSinglePlayerGame;
    PlusOneToken objPlusOneToken = new PlusOneToken();
    PlusTwoToken objPlusTwoToken = new PlusTwoToken();
    PlusThreeToken objPlusThreeToken = new PlusThreeToken();
    MinusOneToken objMinusOneToken = new MinusOneToken();

    HeartToken objHeartToken;

    public DynamicTokens(WeakReference<SinglePlayerGame> _objSinglePlayerGame)
    {
        objRnd = new Random();
        objSinglePlayerGame = _objSinglePlayerGame.get();
        mainContainer = (RelativeLayout)objSinglePlayerGame.objBall.getParent();

        int TWENTY_DIP = Helper.ConvertToPx(objSinglePlayerGame.mContext,20);
        min_x = TWENTY_DIP *2;
        min_y = TWENTY_DIP *2;
        max_x = (int)( Helper.SCREEN_WIDTH - 3*TWENTY_DIP);
        max_y = (int)( 2* Helper.SCREEN_HEIGHT/3 - 3*TWENTY_DIP);
    }


    public void addPlusOneToken()
    {
        objPlusOneToken.CreatePlusOneToken(8200);
    }
    public void addPlusTwoToken()
    {
        objPlusTwoToken.CreatePlusTwoToken(8200);
    }
    public void addPlusThreeToken()
    {
        objPlusThreeToken.CreatePlusThreeToken(8200);
    }
    public void addMinusOneToken()
    {
        objMinusOneToken.CreateMinusOneToken(8200);
    }
    public void addHeartToken()
    {
        objHeartToken = new HeartToken();
        objHeartToken.CreateHeartToken(10000,9);
    }
    private View getTokenView(int background_res,String text)
    {
        RelativeLayout cell = new RelativeLayout(objSinglePlayerGame.mContext);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cell.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams layoutParams_r = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams_r.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);


        TextView textView = new TextView(objSinglePlayerGame.mContext);
        textView.setLayoutParams(layoutParams_r);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
        textView.setTypeface(Typeface.DEFAULT_BOLD);

        cell.setBackgroundResource(background_res);

        cell.addView(textView);
        cell.setX(objRnd.nextInt(max_x - min_x) + min_x);
        cell.setY(objRnd.nextInt( max_y - min_y) + min_y);
        return cell;
    }

    class PlusOneToken
    {
        public void CreatePlusOneToken(long milliSecondsTillTimeOut)
        {
            final View tokenView = getTokenView(R.drawable.background_blue_circle,"1");
            mainContainer.addView(tokenView);
            activeTokens.add(tokenView);

            final CountDownTimer objCountDownTimer = new CountDownTimer(milliSecondsTillTimeOut,milliSecondsTillTimeOut) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    removeToken(tokenView);
                }
            };
            tokenView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                        objSinglePlayerGame.mContext.objSoundManager
                                .Play(objSinglePlayerGame.mContext.objSoundManager.PLUS_ONE);
                        objSinglePlayerGame.Score += 1;

                    removeToken(tokenView);
                    objCountDownTimer.cancel();
                    return false;
                }
            });
            objCountDownTimer.start();
        }
    }

    class PlusTwoToken
    {
        public void CreatePlusTwoToken(long milliSecondsTillTimeOut)
        {
            final View tokenView = getTokenView(R.drawable.background_red_circle,"2");
            mainContainer.addView(tokenView);
            activeTokens.add(tokenView);

            final CountDownTimer objCountDownTimer = new CountDownTimer(milliSecondsTillTimeOut,milliSecondsTillTimeOut) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    removeToken(tokenView);
                }
            };
            tokenView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    objSinglePlayerGame.mContext.objSoundManager
                            .Play(objSinglePlayerGame.mContext.objSoundManager.PLUS_TWO);
                    objSinglePlayerGame.Score += 2;

                    removeToken(tokenView);
                    objCountDownTimer.cancel();
                    return false;
                }
            });
            objCountDownTimer.start();
        }
    }

    class PlusThreeToken
    {
        public void CreatePlusThreeToken(long milliSecondsTillTimeOut)
        {
            final View tokenView = getTokenView(R.drawable.background_black_circle,"3");
            mainContainer.addView(tokenView);
            activeTokens.add(tokenView);

            final CountDownTimer objCountDownTimer = new CountDownTimer(milliSecondsTillTimeOut,milliSecondsTillTimeOut) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    removeToken(tokenView);
                }
            };
            tokenView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    objSinglePlayerGame.mContext.objSoundManager
                            .Play(objSinglePlayerGame.mContext.objSoundManager.PLUS_THREE);
                    objSinglePlayerGame.Score += 3;
                    objCountDownTimer.cancel();

                    removeToken(tokenView);
                    return false;
                }
            });
            objCountDownTimer.start();
        }
    }

    class MinusOneToken
    {
        public void CreateMinusOneToken(long milliSecondsTillTimeOut)
        {
            final View tokenView = getTokenView(R.drawable.background_blue_square,"-1");
            mainContainer.addView(tokenView);
            activeTokens.add(tokenView);

            final CountDownTimer objCountDownTimer = new CountDownTimer(milliSecondsTillTimeOut,milliSecondsTillTimeOut) {
                @Override
                public void onTick(long millisUntilFinished) {
                }

                @Override
                public void onFinish() {
                    removeToken(tokenView);
                }
            };

            tokenView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    objSinglePlayerGame.mContext.objSoundManager
                            .Play(objSinglePlayerGame.mContext.objSoundManager.MINUS_ONE);
                    objSinglePlayerGame.Score -= 1;

                    removeToken(tokenView);
                    objCountDownTimer.cancel();
                    return false;
                }
            });
            objCountDownTimer.start();
        }
    }

    class HeartToken
    {
        boolean flag;
        public void CreateHeartToken(long milliSecondsTillTimeOut, final int duration)
        {
            final TextView textView = new TextView(objSinglePlayerGame.mContext);
            textView.setWidth(Helper.ConvertToPx(objSinglePlayerGame.mContext, 45));
            textView.setHeight(Helper.ConvertToPx(objSinglePlayerGame.mContext, 45));
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            textView.setBackgroundResource(R.drawable.img_heart);
            textView.setX(objRnd.nextInt(max_x-min_x) + min_x);
            textView.setY(objRnd.nextInt(max_y-min_y) + min_y);

            mainContainer.addView(textView);
            activeTokens.add(textView);
            final CountDownTimer Heart_countDownTimer = new CountDownTimer(milliSecondsTillTimeOut,1000) {
                int text = duration;
                @Override
                public void onTick(long millisUntilFinished) {
                    objSinglePlayerGame.mContext.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView.setText(String.valueOf(text--));
                            textView.setScaleX(.9f);
                            textView.setScaleY(.9f);
                            textView.animate().scaleX(1.01f).scaleY(1.01f);
                        }
                    });
                }

                @Override
                public void onFinish() {
                    if(!flag && objSinglePlayerGame.isAlive)
                    {
                        if(activeTokens.contains(textView))
                            objSinglePlayerGame.stop_game();
                    }
                    if(!flag)
                        removeToken(textView);
                }
            };

            textView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if(objSinglePlayerGame.isAlive)
                    {
                        objSinglePlayerGame.mContext
                                .objSoundManager.
                                Play(objSinglePlayerGame.mContext.objSoundManager.HEART_TAP);
                        objSinglePlayerGame.Score += 1;
                    }
                    flag = true;
                    removeToken(textView);
                    Heart_countDownTimer.cancel();
                    return false;
                }
            });
            Heart_countDownTimer.start();
        }
    }


    public void removeToken(View v)
    {
        if(mainContainer.indexOfChild(v)>=0)
            mainContainer.removeView(v);
    }

    public void flushAllTokensFromScreen()
    {
        for (View v : activeTokens)
            removeToken(v);
        activeTokens.clear();
    }

}
