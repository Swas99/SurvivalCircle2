package com.archer.survival_circle_2.game_logic;

import android.graphics.PointF;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.archer.survival_circle_2.Helper;
import com.archer.survival_circle_2.MainActivity;
import com.archer.survival_circle_2.R;

import java.lang.ref.WeakReference;
import java.util.Random;


public class SinglePlayerGame
{
    public MainActivity mContext;

    TextView tvScore;
    TextView tvAvgScore;
    TextView tvBestScore;
    TextView tvInstruction;
    TextView tvInstruction2;
    int d_pad_background_index;


    Handler mHandler;
    Runnable survival_circle_driver;

    long Score;
    int game_time;
    long prev_average;
    long running_time;
    long curve_duration;
    long FRAME_INTERVAL;
    boolean isAlive;
    CountDownTimer score_countdown_timer;


    public BallView objBall;
    public DirectionPadView dPad_1;
    public CrazyPaths objCrazyPath;
    public DynamicTokens objDynamicTokens;
    public HollowCircleView objSurvivalCircle;


    public SinglePlayerGame(WeakReference<MainActivity> m_context)
    {
        mContext = m_context.get();
        mHandler = new Handler();

        initialize_screen_controls();
        init_game();
        init_game_driver();
        init_scoring_countdown_timer();
    }

    boolean play_flag_top_score;
    private void play_top_score_music()
    {
        if(play_flag_top_score)
        {
            play_flag_top_score = false;
            mContext.objSoundManager.Play(mContext.objSoundManager.YAY);
        }
    }



    private void init_scoring_countdown_timer() {
        score_countdown_timer = new CountDownTimer(1000000000l,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Score++;
                game_time++;
                mContext.TotalScore++;
                mContext.game_duration++;
                tvScore.setText(String.valueOf(Score));
                if(Score>mContext.BestScore)
                {
                    mContext.BestScore = Score;
                    tvBestScore.setText(String.valueOf(Score));
                    play_top_score_music();
                }

                long avg = mContext.getAverageScore();
                if(prev_average < avg)
                {
                    prev_average=avg;
                    tvAvgScore.setText("Avg: " + String.valueOf(avg));
                    if(mContext.TotalGames>27)
                        mContext.objSoundManager.Play(mContext.objSoundManager.YAY);
                }


                addToken();
            }

            public void addToken() {
              if(game_time%13 == 0)
                      objDynamicTokens.addPlusOneToken();
                if(game_time%17 == 0)
                    objDynamicTokens.addPlusTwoToken();
                if(game_time>30 && game_time%29 == 0)
                    objDynamicTokens.addPlusThreeToken();
                if(game_time%37 == 0)
                    objDynamicTokens.addPlusOneToken();
            }

            @Override
            public void onFinish() {

            }
        };
    }

    public void initialize_screen_controls()
    {
        tvScore = (TextView)mContext.findViewById(R.id.tvScore);
        tvBestScore = (TextView)mContext.findViewById(R.id.tvBestScore_single_player);
        tvAvgScore = (TextView)mContext.findViewById(R.id.tvAverageScore_single_player);

        tvScore.setText("0");
        tvBestScore.setText(String.valueOf(mContext.BestScore));
        tvAvgScore.setText("Avg: " + String.valueOf(mContext.getAverageScore()));

        tvScore.setTextScaleX(1.2f);
        mContext.findViewById(R.id.tvScoreText).animate().scaleY(1.15f);

        tvInstruction = (TextView)mContext.findViewById(R.id.tvInstruction);
        tvInstruction2 = (TextView)mContext.findViewById(R.id.tvInstruction2);
    }

    private void init_game() {
        running_time=0;
        FRAME_INTERVAL = 25;
        objBall = (BallView)mContext.findViewById(R.id.ball);
        dPad_1 = (DirectionPadView)mContext.findViewById(R.id.d_pad_one);
        objSurvivalCircle = (HollowCircleView)mContext.findViewById(R.id.survival_circle);

        objSurvivalCircle.init();

        objDynamicTokens = new DynamicTokens(new WeakReference<>(this));
        dPad_1.init(new WeakReference<>(objBall), new WeakReference<>(this));
        objCrazyPath = new CrazyPaths(new WeakReference<>(objSurvivalCircle),FRAME_INTERVAL);

        objBall.setDimensions();
        Random objRandom = new Random();
        d_pad_background_index = objRandom.nextInt(7);
        if(d_pad_background_index==mContext.objMainViewManager.Background_Index)
            d_pad_background_index++;
        if(d_pad_background_index>=6)
            d_pad_background_index = 0;
    }

    private void change_d_pad_background()
    {
        d_pad_background_index++;
        if(d_pad_background_index >= mContext.objMainViewManager.All_Background_Res.length)
            d_pad_background_index=0;
        if(d_pad_background_index == mContext.objMainViewManager.Background_Index)
        {
            d_pad_background_index++;
            if(d_pad_background_index >= mContext.objMainViewManager.All_Background_Res.length)
                d_pad_background_index = 0;
        }
        mContext.objMainViewManager.changeViewBackground(d_pad_background_index,dPad_1);
    }

    public void start() {

        Score = 0;
        game_time =0;
        running_time=0;
        curve_duration = 3000;
        mContext.TotalGames++;
        prev_average=mContext.getAverageScore();
        tvAvgScore.setText("Avg: " + String.valueOf(mContext.getAverageScore()));
        play_flag_top_score=true;

        isAlive = true;
        change_d_pad_background();

        objBall.reset_force();
        bringBallToCircle();
        long animation_duration = objBall.animate().getDuration();

        new CountDownTimer(animation_duration+30, animation_duration+30) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                tvInstruction.animate().alpha(0);
                tvInstruction2.animate().alpha(0);

                objBall.recompute_position();
                objSurvivalCircle.recompute_position();
                objCrazyPath.refresh(running_time, curve_duration);

                reDrawSurvivalCircle();

                score_countdown_timer.cancel();
                score_countdown_timer.start();

            }
        }.start();
    }

    public void bringBallToCircle()
    {
        objBall.animate()
                .scaleX(1)
                .scaleY(1)
                .x(objSurvivalCircle.getX() + objSurvivalCircle.radius - objBall.radius)
                .y(objSurvivalCircle.getY() + objSurvivalCircle.radius - objBall.radius);
    }

    private void init_game_driver() {
        survival_circle_driver = new Runnable() {
            @Override
            public void run() {
                reDrawSurvivalCircle();
            }
        };
    }

    public void reDrawSurvivalCircle()
    {
        if(!isAlive)
            return;

        running_time+=FRAME_INTERVAL;
        PointF nextPos = getNextPos();
        objSurvivalCircle.setX(nextPos.x);
        objSurvivalCircle.setY(nextPos.y);
        objBall.move();
//        objSurvivalCircle.animate().x(nextPos.x).y(nextPos.y);
//        moveView(objSurvivalCircle,nextPos.x,nextPos.y);
        checkIfAlive();
        if(isAlive)
            mHandler.postDelayed(survival_circle_driver, FRAME_INTERVAL);
    }

    private PointF getNextPos() {
        return objCrazyPath.getNextPos(running_time);
    }

    public void checkIfAlive()
    {
        objSurvivalCircle.recompute_position();
        objBall.recompute_position();
        float distance;
        float r1,r2;
        r1=objSurvivalCircle.radius;
        r2=objBall.radius;

        float deltaX = objSurvivalCircle.centerX - objBall.centerX;
        float deltaY = objSurvivalCircle.centerY - objBall.centerY;

        distance =deltaX*deltaX + deltaY*deltaY;
        if (distance > (r1 + r2)*(r1 + r2)  )  // No overlap
        {
            stop_game();
        }

        //checkForTokenOverlap();
    }

    public void checkForTokenOverlap()
    {
        for (View v : objDynamicTokens.activeTokens)
        {
            if(v.getTag().equals("0"))
               continue;

            String tag[] = String.valueOf(v.getTag()).split("_");

            v.setX(objSurvivalCircle.centerX + Integer.parseInt(tag[0]));
            v.setY(objSurvivalCircle.centerY + Integer.parseInt(tag[1]));


            float centerX = v.getX()+v.getWidth()/2;
            float centerY = v.getY()+v.getHeight()/2;
            float deltaX = objBall.centerX - centerX;
            float deltaY = objBall.centerY - centerY;
            float distance =deltaX*deltaX + deltaY*deltaY;
            float r1 = objBall.radius;
            float r2 = v.getWidth()/2;
            if ( distance < (r1 + r2)*(r1 + r2)  )
            {
                switch (String.valueOf(v.getTag()))
                {
                    case "1":
                        Score += 1;
                        objDynamicTokens.removeToken(v);
                        mContext.objSoundManager.Play(mContext.objSoundManager.PLUS_ONE);
                        break;
                    case "2":
                        Score += 2;
                        objDynamicTokens.removeToken(v);
                        mContext.objSoundManager.Play(mContext.objSoundManager.PLUS_TWO);
                        break;
                    case "-1":
                        Score -= 1;
                        objDynamicTokens.removeToken(v);
                        mContext.objSoundManager.Play(mContext.objSoundManager.MINUS_ONE);
                        break;
                    case "x":
                        Score += 3;
                        objDynamicTokens.removeToken(v);
                        stop_game();
                        break;
                }
                v.setTag("0");
            }
        }
    }

    public void stop_game() {
        isAlive = false;
        dPad_1.isActivated = false;

        if(mContext.vibration_on)
            mContext.objVibrator.vibrate(100);

        shrinkBall();

        tvInstruction.setText(mContext.getString(R.string.dead_));
        tvInstruction.animate().alpha(1);
        tvInstruction2.setText(mContext.getString(R.string.intruction_4));
        tvInstruction2.animate().alpha(1);

        updateScoringData();
        score_countdown_timer.cancel();
        objDynamicTokens.flushAllTokensFromScreen();
        mHandler.removeCallbacks(survival_circle_driver);
        mContext.objSoundManager.Play(mContext.objSoundManager.POP);


        //Show Ad
        if(mContext.game_duration>270 && !mContext.objManageAds.adFreeVersion)
        {
            mContext.objManageAds.ShowRemoveAdDialog();
        }
    }

    private void shrinkBall() {
        objBall.recompute_position();
        objBall.animate().cancel();

        int duration = 407;
        objBall.setAlpha(1);
        objBall.animate().scaleX(0.3f).scaleY(0.3f).setDuration(duration);

        new CountDownTimer(duration+20, duration+20) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                dPad_1.isActivated = true;
            }
        }.start();
    }

    public void stopGame()
    {
        isAlive = false;
        updateScoringData();
        score_countdown_timer.cancel();
        mHandler.removeCallbacks(survival_circle_driver);
    }

    public void updateScoringData()
    {
        Runnable myRunnable = new Runnable(){
            public void run(){
                WeakReference<MainActivity> m_context = new WeakReference<>(mContext);
                String score_data = String.valueOf(mContext.BestScore)
                        + "_"
                        + String.valueOf(mContext.TotalGames)
                        + "_"
                        + String.valueOf(mContext.TotalScore);

                Helper.writeToFile(m_context, Helper.TOP_SCORE_FILE, score_data);

                mContext.objGameServices.process_score(Score);
            }
        };
        Thread thread = new Thread(myRunnable);
        thread.start();
    }



}
