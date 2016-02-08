package com.archer.survival_circle_2.game_logic;

import android.graphics.PointF;

import com.archer.survival_circle_2.Helper;

import java.lang.ref.WeakReference;
import java.util.Random;

public class CrazyPaths {

    final static int BEZIER = 0;
    final static int CIRCLE = 1;
    final static int STRAIGHT_LINE = 2;
    final static int IN_SPIRAL = 3;
    final static int OUT_SPIRAL = 4;
    final static int HYPOCYCLOID_UNSTABLE = 5;
    final static int HYPOCYCLOID_STABLE = 6;
    final static int DELTOID = 7;
    final static int ASTROID = 8;
    final static int EIGHT_CURVE = 9;
    final static int DUMBELL_CURVE = 10;
    final static int LEMNISCATE = 11;
    final static int BICORN = 12;
    final static int NEPHROID = 13;
    final static int EPICYCLOID = 14;
    final static int ELLIPSE_EVOLUTE = 15;
    final static int CORNOID = 16;

    //    int [] Paths = {ASTROID}; //,IN_SPIRAL,CIRCLE,BEZIER,STRAIGHT_LINE};//};
//swsahu
    int [] Paths =
            {
                    CIRCLE,CIRCLE,STRAIGHT_LINE,
                    CIRCLE,STRAIGHT_LINE,CIRCLE,CIRCLE,
                    BEZIER,NEPHROID,ELLIPSE_EVOLUTE,
                    ASTROID,STRAIGHT_LINE,CIRCLE,CIRCLE,
                    BEZIER,CORNOID,LEMNISCATE,
                    STRAIGHT_LINE,CIRCLE,CIRCLE,
                    BEZIER,EIGHT_CURVE,STRAIGHT_LINE,
                    CIRCLE,CIRCLE,BEZIER,BICORN,DELTOID
            };

// CIRCLE,STRAIGHT_LINE,BEZIER,NEPHROID,ELLIPSE_EVOLUTE,ASTROID,CORNOID,LEMNISCATE,EIGHT_CURVE,BICORN,DELTOID,};
//
//NOT IN USE :  HYPOCYCLOID_UNSTABLE,HYPOCYCLOID_STABLE,EPICYCLOID,IN_SPIRAL,OUT_SPIRAL
//Not working properly : IN_SPIRAL,OUT_SPIRAL


    long FRAME_INTERVAL;

    int path_type_index;
    long start_time;
    float duration;
    float anim_duration;

    boolean atDefPos;
    Random random_generator = new Random();
    int MIN_X, MIN_Y, MAX_X, MAX_Y;


    Bicorn objBicorn;
    Bezier objBezier;
    Circle objCircle;
    Cornoid objCornoid;
    Deltoid objDeltoid;
    Astroid objAstroid;
    Nephroid objNephroid;
    Spiral_in objSpiral_in;
    Spiral_out objSpiral_out;
    Lemniscate objLemniscate;
    EpiCycloid objEpiCycloid;
    EightCurve objEightCurve;
    StraightLine objStraightLine;
    DumbellCurve objDumbellCurve;
    CustomEndPoint objCustomEndPoint;
    EllipseEvolute objEllipseEvolute;
    HypoCycloid_stable objHypoCycloid_stable;
    HypoCycloid_unstable objHypoCycloidUnstable;

    HollowCircleView objSurvivalCircle;

    public CrazyPaths(WeakReference<HollowCircleView> _objSurvivalCircle, long _FRAME_INTERVAL)
    {
        objSurvivalCircle = _objSurvivalCircle.get();

        FRAME_INTERVAL = _FRAME_INTERVAL;

        MIN_X = MIN_Y = 0;
        MAX_X = (int)(Helper.SCREEN_WIDTH) - Helper.ConvertToPx(objSurvivalCircle.ct,100);
        MAX_Y = (int)(2*Helper.SCREEN_HEIGHT/3) - Helper.ConvertToPx(objSurvivalCircle.ct, 100);

        //region curves
        objBicorn = new Bicorn();
        objBezier = new Bezier();
        objCircle = new Circle();
        objCornoid = new Cornoid();
        objDeltoid = new Deltoid();
        objAstroid = new Astroid();
        objNephroid = new Nephroid();
        objSpiral_in = new Spiral_in();
        objSpiral_out = new Spiral_out();
        objLemniscate = new Lemniscate();
        objEpiCycloid = new EpiCycloid();
        objEightCurve = new EightCurve();
        objStraightLine = new StraightLine();
        objDumbellCurve = new DumbellCurve();
        objCustomEndPoint = new CustomEndPoint();
        objEllipseEvolute= new EllipseEvolute();
        objHypoCycloid_stable = new HypoCycloid_stable();
        objHypoCycloidUnstable = new HypoCycloid_unstable();
        //endregion

        path_type_index=0;
    }

    public void refresh(long _start_time,long _duration)
    {
        start_time = _start_time;
        duration = _duration;

        //path_type_index = random_generator.nextInt(Paths.length);
        path_type_index++;
        if (path_type_index==Paths.length)
            path_type_index=0;


        switch (Paths[path_type_index])
        {
            case BEZIER:
                objBezier.generateControlPoints();
                break;
            case CIRCLE:
                objCircle.generateControlPoints();
                break;
            case STRAIGHT_LINE:
                objStraightLine.generateControlPoints();
                break;
            case IN_SPIRAL:
                objSpiral_in.generateControlPoints();
                break;
            case OUT_SPIRAL:
                objSpiral_out.generateControlPoints();
                break;
            case HYPOCYCLOID_UNSTABLE:
                objHypoCycloidUnstable.generateControlPoints();
                break;
            case HYPOCYCLOID_STABLE:
                objHypoCycloid_stable.generateControlPoints();
                break;
            case DELTOID:
                objDeltoid.generateControlPoints();
                break;
            case EPICYCLOID:
                objEpiCycloid.generateControlPoints();
                break;
            case ELLIPSE_EVOLUTE:
                objEllipseEvolute.generateControlPoints();
                break;
            case EIGHT_CURVE:
                objEightCurve.generateControlPoints();
                break;
            case DUMBELL_CURVE:
                objDumbellCurve.generateControlPoints();
                break;
            case ASTROID:
                objAstroid.generateControlPoints();
                break;
            case NEPHROID:
                objNephroid.generateControlPoints();
                break;
            case BICORN:
                objBicorn.generateControlPoints();
                break;
            case LEMNISCATE:
                objLemniscate.generateControlPoints();
                break;
            case CORNOID:
                objCornoid.generateControlPoints();
                break;
        }
    }

    public PointF getNextPos(long running_time) {

        if(running_time == 0 || running_time>start_time+anim_duration)
        {
            refresh(running_time,(long)duration-2);
        }

        PointF next_pos;
        switch (Paths[path_type_index])
        {
            case BEZIER:
                next_pos = new PointF(
                        objBezier.calcBezier_x(running_time),
                        objBezier.calcBezier_y(running_time));
                break;
            case CIRCLE:
                if (atDefPos)
                {
                    next_pos = new PointF(
                            objCircle.calcCircle_x(running_time),
                            objCircle.calcCircle_y(running_time));
                }
                else
                {
                    next_pos = new PointF(
                            objCustomEndPoint.CustomEndPoint_x(running_time),
                            objCustomEndPoint.CustomEndPoint_y(running_time));
                }
                break;
            case STRAIGHT_LINE:
                next_pos = new PointF(
                        objStraightLine.StraightLine_x(running_time),
                        objStraightLine.StraightLine_y(running_time));
                break;
            case IN_SPIRAL:
                if (!atDefPos)
                {
                    next_pos = new PointF(
                            objCustomEndPoint.CustomEndPoint_x(running_time),
                            objCustomEndPoint.CustomEndPoint_y(running_time));
                }
                else
                {
                    next_pos = new PointF(
                            objSpiral_in.calcSpiral_in_x(running_time),
                            objSpiral_in.calcSpiral_in_y(running_time));
                }
                break;
            case OUT_SPIRAL:
                if (!atDefPos)
                {
                    next_pos = new PointF(
                            objCustomEndPoint.CustomEndPoint_x(running_time),
                            objCustomEndPoint.CustomEndPoint_y(running_time));
                }
                else
                {
                    next_pos = new PointF(
                            objSpiral_out.calcSpiral_out_x(running_time),
                            objSpiral_out.calcSpiral_out_y(running_time));
                }
                break;
            case HYPOCYCLOID_STABLE:
                if (!atDefPos)
                {
                    next_pos = new PointF(
                            objCustomEndPoint.CustomEndPoint_x(running_time),
                            objCustomEndPoint.CustomEndPoint_y(running_time));
                }
                else
                {
                    next_pos = new PointF(
                            objHypoCycloid_stable.calcHypoCycloid_x(running_time),
                            objHypoCycloid_stable.calcHypoCycloid_y(running_time));
                }
                break;
            case DELTOID:
                if (!atDefPos)
                {
                    next_pos = new PointF(
                            objCustomEndPoint.CustomEndPoint_x(running_time),
                            objCustomEndPoint.CustomEndPoint_y(running_time));
                }
                else
                {
                    next_pos = new PointF(
                            objDeltoid.calcDeltoid_x(running_time),
                            objDeltoid.calcDeltoid_y(running_time));
                }
                break;
            case ASTROID:
                if (!atDefPos)
                {
                    next_pos = new PointF(
                            objCustomEndPoint.CustomEndPoint_x(running_time),
                            objCustomEndPoint.CustomEndPoint_y(running_time));
                }
                else
                {
                    next_pos = new PointF(
                            objAstroid.calcAstroid_x(running_time),
                            objAstroid.calcAstroid_y(running_time));
                }
                break;
            case HYPOCYCLOID_UNSTABLE:
                if (!atDefPos)
                {
                    next_pos = new PointF(
                            objCustomEndPoint.CustomEndPoint_x(running_time),
                            objCustomEndPoint.CustomEndPoint_y(running_time));
                }
                else
                {
                    next_pos = new PointF(
                            objHypoCycloidUnstable.calcHypoCycloid_x(running_time),
                            objHypoCycloidUnstable.calcHypoCycloid_y(running_time));
                }
                break;
            case EPICYCLOID:
                if (!atDefPos)
            {
                next_pos = new PointF(
                        objCustomEndPoint.CustomEndPoint_x(running_time),
                        objCustomEndPoint.CustomEndPoint_y(running_time));
            }
            else
            {
                next_pos = new PointF(
                        objEpiCycloid.calcEpiCycloid_x(running_time),
                        objEpiCycloid.calcEpiCycloid_y(running_time));
            }
                break;
            case ELLIPSE_EVOLUTE:
                if (!atDefPos)
                {
                    next_pos = new PointF(
                            objCustomEndPoint.CustomEndPoint_x(running_time),
                            objCustomEndPoint.CustomEndPoint_y(running_time));
                }
                else
                {
                    next_pos = new PointF(
                            objEllipseEvolute.calcEllipseEvolute_x(running_time),
                            objEllipseEvolute.calcEllipseEvolute_y(running_time));
                }
                break;
            case EIGHT_CURVE:
                if (!atDefPos)
                {
                    next_pos = new PointF(
                            objCustomEndPoint.CustomEndPoint_x(running_time),
                            objCustomEndPoint.CustomEndPoint_y(running_time));
                }
                else
                {
                    next_pos = new PointF(
                            objEightCurve.calcEightCurve_x(running_time),
                            objEightCurve.calcEightCurve_y(running_time));
                }
                break;
            case DUMBELL_CURVE:
                if (!atDefPos)
                {
                    next_pos = new PointF(
                            objCustomEndPoint.CustomEndPoint_x(running_time),
                            objCustomEndPoint.CustomEndPoint_y(running_time));
                }
                else
                {
                    next_pos = new PointF(
                            objDumbellCurve.calcDumbellCurve_x(running_time),
                            objDumbellCurve.calcDumbellCurve_y(running_time));
                }
                break;
            case NEPHROID:
                if (!atDefPos)
                {
                    next_pos = new PointF(
                            objCustomEndPoint.CustomEndPoint_x(running_time),
                            objCustomEndPoint.CustomEndPoint_y(running_time));
                }
                else
                {
                    next_pos = new PointF(
                            objNephroid.calcNephroid_x(running_time),
                            objNephroid.calcNephroid_y(running_time));
                }
                break;
            case BICORN:
                if (!atDefPos)
                {
                    next_pos = new PointF(
                            objCustomEndPoint.CustomEndPoint_x(running_time),
                            objCustomEndPoint.CustomEndPoint_y(running_time));
                }
                else
                {
                    next_pos = new PointF(
                            objBicorn.calcBicorn_x(running_time),
                            objBicorn.calcBicorn_y(running_time));
                }
                break;
            case LEMNISCATE:
                if (!atDefPos)
                {
                    next_pos = new PointF(
                            objCustomEndPoint.CustomEndPoint_x(running_time),
                            objCustomEndPoint.CustomEndPoint_y(running_time));
                }
                else
                {
                    next_pos = new PointF(
                            objLemniscate.calcLemniscate_x(running_time),
                            objLemniscate.calcLemniscate_y(running_time));
                }
                break;
            case CORNOID:
                if (!atDefPos)
                {
                    next_pos = new PointF(
                            objCustomEndPoint.CustomEndPoint_x(running_time),
                            objCustomEndPoint.CustomEndPoint_y(running_time));
                }
                else
                {
                    next_pos = new PointF(
                            objCornoid.calcCornoid_x(running_time),
                            objCornoid.calcCornoid_y(running_time));
                }
                break;
            default:
                next_pos = new PointF(
                        objBezier.calcBezier_x(running_time),
                        objBezier.calcBezier_y(running_time));
        }


        return next_pos;
    }

    class Bezier
    {
        public PointF mStart = new PointF();
        public PointF mControl = new PointF();
        public PointF mEnd = new PointF();

        public void generateControlPoints()
        {
            anim_duration = duration;
            mStart.x = objSurvivalCircle.getX();
            mStart.y = objSurvivalCircle.getY();

            mEnd.x = random_generator.nextInt(MAX_X - MIN_X) + MIN_X;
            mEnd.y = random_generator.nextInt(MAX_Y - MIN_Y) + MIN_Y;


            mControl.x = random_generator.nextInt(MAX_X - MIN_X) + MIN_X;
            mControl.y = random_generator.nextInt(MAX_Y - MIN_Y) + MIN_Y;
        }

        private float calcBezier_x(long current_time) {
            float interpolatedTime = (current_time-start_time)/anim_duration;

            float p0 = mStart.x;
            float p1 = mControl.x;
            float p2 = mEnd.x;

            return calcBezier(interpolatedTime,p0,p1,p2);
        }

        private float calcBezier_y(long current_time) {
            float interpolatedTime = (current_time-start_time)/anim_duration;

            float p0 = mStart.y;
            float p1 = mControl.y;
            float p2 = mEnd.y;

            return calcBezier(interpolatedTime,p0,p1,p2);
        }

        private float calcBezier(float interpolatedTime,float p0,float p1,float p2)
        {
            return Math.round((Math.pow((1 - interpolatedTime), 2) * p0)
                    + (2 * (1 - interpolatedTime) * interpolatedTime * p1)
                    + (Math.pow(interpolatedTime, 2) * p2));
        }
    }
    class Circle
    {
        PointF mStart = new PointF();
        PointF mCenter = new PointF();
        float max_r;
        float min_r;
        float radius;

        public Circle()
        {
            max_r = Math.min(MAX_X, MAX_Y)/2;
            min_r = 90;
        }

        public void generateControlPoints()
        {
            atDefPos=false;
            radius = random_generator.nextInt((int)(max_r - min_r)) + min_r;

            mCenter.x = random_generator.nextInt((int)(MAX_X -2*radius) +1) + radius;
            mCenter.y = random_generator.nextInt((int)(MAX_Y -2*radius) +1) + radius;

            mStart.x = mCenter.x + radius;
            mStart.y = mCenter.y;

            float anim_factor = (radius - min_r)/(max_r-min_r) * 1.5f + 1;
            objCustomEndPoint.generateControlPoints(mStart,duration*anim_factor);
        }

        private float calcCircle_x(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

//            return mCenter.x + (float)(radius*Math.cos(interpolatedTime));
            return mCenter.x + (float)(radius*Math.cos((interpolatedTime)));
        }

        private float calcCircle_y(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

//            return mCenter.y + (float)(radius*Math.sin(interpolatedTime));
            return mCenter.y + (float)(radius*Math.sin((interpolatedTime)));
        }
    }
    class StraightLine
    {
        PointF mStart = new PointF();
        PointF mEnd = new PointF();

        public void generateControlPoints()
        {
            anim_duration = duration*.92f;

            mStart.x = objSurvivalCircle.getX();
            mStart.y = objSurvivalCircle.getY();

            mEnd.x = random_generator.nextInt(MAX_X - MIN_X) + MIN_X;
            mEnd.y = random_generator.nextInt(MAX_Y - MIN_Y) + MIN_Y;
        }

        private float StraightLine_x(long current_time) {
            float interpolatedTime = (current_time-start_time)/anim_duration;
            float deltaX = mEnd.x-mStart.x;
            return mStart.x+(interpolatedTime*deltaX);
        }

        private float StraightLine_y(long current_time) {
            float interpolatedTime = (current_time-start_time)/anim_duration;
            float deltaY = mEnd.y -  mStart.y;
            return mStart.y+(interpolatedTime*deltaY);
        }
    }
    class CustomEndPoint
    {

        public PointF mStart = new PointF();
        public PointF mControl = new PointF();
        public PointF mEnd = new PointF();
        float setAnimDuration;

        public void generateControlPoints(PointF _mEnd, float _setAnimDuration)
        {
            setAnimDuration=_setAnimDuration;

            anim_duration = duration;
            mStart.x = objSurvivalCircle.getX();
            mStart.y = objSurvivalCircle.getY();

            mEnd = _mEnd;

            mControl.x = (mEnd.x + mStart.x)/2;
            mControl.y = (mEnd.y + mStart.y)/2;
        }

        private float CustomEndPoint_x(long current_time) {
            float interpolatedTime = (current_time-start_time)/anim_duration;

            float p0 = mStart.x;
            float p1 = mControl.x;
            float p2 = mEnd.x;

            return calcBezier(interpolatedTime,p0,p1,p2);
        }

        private float CustomEndPoint_y(long current_time) {
            float interpolatedTime = (current_time-start_time)/anim_duration;

            float p0 = mStart.y;
            float p1 = mControl.y;
            float p2 = mEnd.y;

            float y = calcBezier(interpolatedTime,p0,p1,p2);

            if((current_time-start_time + FRAME_INTERVAL) >= anim_duration)
            {
                atDefPos = true;
                start_time = current_time;
                anim_duration=setAnimDuration;
            }

            return y;
        }

        private float calcBezier(float interpolatedTime,float p0,float p1,float p2)
        {

            return Math.round((Math.pow((1 - interpolatedTime), 2) * p0)
                    + (2 * (1 - interpolatedTime) * interpolatedTime * p1)
                    + (Math.pow(interpolatedTime, 2) * p2));
        }
    }

    //x Not Working
    class Spiral_in
    {
        PointF mStart = new PointF();
        PointF mCenter = new PointF();
        float max_r;
        float min_r;
        float radius;

        int rotation_count;
        float radius_decrement;
        float anim_speed_increment;


        public Spiral_in()
        {
            max_r = Math.min(MAX_X, MAX_Y)/2;
            min_r = 150;
        }

        public void generateControlPoints()
        {
            atDefPos=false;
            //radius = random_generator.nextInt((int)(max_r - min_r)) + min_r;
            radius += 10;
            if(radius == max_r)
                radius-=50;

            rotation_count = random_generator.nextInt(5) + 1;
         //   radius = random_generator.nextInt((int)(max_r - min_r)) + min_r;
            radius_decrement = radius-(radius/rotation_count) / (duration) *FRAME_INTERVAL;


            mCenter.x = MAX_X/2;
            mCenter.y = MAX_Y/2;
            mStart.x = mCenter.x + radius;
            mStart.y = mCenter.y;
            objCustomEndPoint.generateControlPoints(mStart,duration);
        }

        private float calcSpiral_in_x(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

//            return mCenter.x + (float)(radius*Math.cos(interpolatedTime));
            return mCenter.x + (float)(radius*Math.cos((interpolatedTime)));
        }

        private float calcSpiral_in_y(long current_time) {

            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);
            float y=  mCenter.y + (float)(radius*Math.sin(interpolatedTime));

            radius-=radius_decrement;
            if((current_time-start_time + FRAME_INTERVAL) >= anim_duration && rotation_count>0)
            {
                rotation_count--;
                start_time = current_time;
                anim_duration-=anim_speed_increment;
                radius_decrement = radius-(radius/rotation_count) / (duration) *FRAME_INTERVAL;

                anim_duration/=1.2f;
            }

            return y;
        }
    }


    //x Not Working
    class Spiral_out
    {
        PointF mStart = new PointF();
        PointF mCenter = new PointF();
        float max_r;
        float min_r;
        float radius;

        int rotation_count;
        float radius_increment;

        public Spiral_out()
        {
            max_r = Math.min(MAX_X, MAX_Y)/12;
            min_r = 18;
        }

        public void generateControlPoints()
        {
            atDefPos=false;
            rotation_count = 2; //random_generator.nextInt(5) + 1;
            radius = random_generator.nextInt((int)(max_r - min_r)) + min_r;
            radius = radius/rotation_count;

            radius_increment = radius / (duration) *FRAME_INTERVAL;


            mCenter.x = MAX_X/2;
            mCenter.y = MAX_Y/2;
            mStart.x = mCenter.x + radius;
            mStart.y = mCenter.y;
            objCustomEndPoint.generateControlPoints(mStart,duration);
        }

        private float calcSpiral_out_x(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

//            return mCenter.x + (float)(radius*Math.cos(interpolatedTime));
            return mCenter.x + (float)(radius*Math.cos((interpolatedTime)));
        }

        private float calcSpiral_out_y(long current_time) {

            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);
            float y = mCenter.y + (float)(radius*Math.sin(interpolatedTime));

            radius+= radius_increment;
            if((current_time-start_time + FRAME_INTERVAL) >= anim_duration && rotation_count>0)
            {
                rotation_count--;
                start_time = current_time;
                radius_increment = radius/1.4f / anim_duration *FRAME_INTERVAL ;

                anim_duration*=1.2f;
            }

            return y;
        }
    }


    class HypoCycloid_unstable
    {
        PointF mStart = new PointF();
        PointF mCenter = new PointF();
        float radius_1,radius_2;
        float max_r;
        float min_r;

        public HypoCycloid_unstable()
        {
            max_r = Math.min(MAX_X, MAX_Y)/2;
            min_r = 150;
        }

        public void generateControlPoints()
        {
            atDefPos=false;
            radius_1 = random_generator.nextInt((int)(max_r - min_r)) + min_r;
            radius_2 = (random_generator.nextInt((int)(radius_1 - min_r)) + min_r/2);


            mCenter.x = random_generator.nextInt((int)(MAX_X -2*radius_1)) + radius_1;
            mCenter.y = random_generator.nextInt((int)(MAX_Y -2*radius_1)) + radius_1;

            float anim_factor = (radius_1 - min_r)/(max_r-min_r) * 1.6f + 1.2f;
            anim_duration=duration*anim_factor;
            mStart.x = calcHypoCycloid_x(start_time);
            mStart.y = calcHypoCycloid_y(start_time);
            objCustomEndPoint.generateControlPoints(mStart,duration*anim_factor);

        }

        private float calcHypoCycloid_x(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

//            return mCenter.x + (float)(radius*Math.cos(interpolatedTime));
            return mCenter.x +
                    (float)((radius_1 - radius_2) * Math.cos(interpolatedTime) + radius_2 * Math.cos((radius_1-radius_2)*interpolatedTime / radius_2));
        }

        private float calcHypoCycloid_y(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

            return mCenter.y +
                    (float)((radius_1 - radius_2) * Math.sin(interpolatedTime) + radius_2 * Math.sin((radius_1 - radius_2)*interpolatedTime / radius_2));
        }
    }
    class HypoCycloid_stable
    {
        PointF mStart = new PointF();
        PointF mCenter = new PointF();
        float radius_1;
        float max_r;
        float min_r;
        int n;

        public HypoCycloid_stable()
        {
            max_r = Math.min(MAX_X, MAX_Y)/2;
            min_r = 150;
        }

        public void generateControlPoints()
        {
            atDefPos=false;
            radius_1 = random_generator.nextInt((int)(max_r - min_r)) + min_r;
            n = random_generator.nextInt(18) + 2;

            mCenter.x = random_generator.nextInt((int)(MAX_X -2*radius_1)) + radius_1;
            mCenter.y = random_generator.nextInt((int)(MAX_Y -2*radius_1)) + radius_1;

            float anim_factor = (radius_1 - min_r)/(max_r-min_r) * 1.7f + 1.2f;
            anim_duration=duration*anim_factor;
            mStart.x = calcHypoCycloid_x(start_time);
            mStart.y = calcHypoCycloid_y(start_time);
            objCustomEndPoint.generateControlPoints(mStart,duration*anim_factor);

        }

        private float calcHypoCycloid_x(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

//            return mCenter.x + (float)(radius*Math.cos(interpolatedTime));
            return (float) (radius_1 * ((n - 1) * Math.cos(interpolatedTime) - Math.cos((n - 1) * interpolatedTime)) / n) + mCenter.x;
        }

        private float calcHypoCycloid_y(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

            return (float) (radius_1 * ((n - 1) * Math.sin(interpolatedTime) + Math.sin((n - 1) * interpolatedTime)) / n) + mCenter.y;
        }
    }
    class Deltoid
    {
        PointF mStart = new PointF();
        PointF mCenter = new PointF();
        float radius_1;
        float max_r;
        float min_r;

        public Deltoid()
        {
            max_r = Math.min(MAX_X, MAX_Y)/2 ;
            min_r = 150;
            radius_1 = min_r;
        }

        public void generateControlPoints()
        {
            atDefPos=false;
            radius_1 = random_generator.nextInt((int)(max_r - min_r)) + min_r;

            mCenter.x = random_generator.nextInt((int)(MAX_X -2*radius_1 +1)) + radius_1;
            mCenter.y = random_generator.nextInt((int)(MAX_Y -2*radius_1 +1)) + radius_1;

            float anim_factor = (radius_1 - min_r)/(max_r-min_r) * 1.6f + 1.4f;
            anim_duration=duration*anim_factor;
            mStart.x = calcDeltoid_x(start_time);
            mStart.y = calcDeltoid_y(start_time);
            objCustomEndPoint.generateControlPoints(mStart,duration*anim_factor);

        }

        private float calcDeltoid_x(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

//            return mCenter.x + (float)(radius*Math.cos(interpolatedTime));
            return (float) (radius_1 * (2 * Math.cos(interpolatedTime) - Math.cos(2 * interpolatedTime)) / 3) + mCenter.x;
        }

        private float calcDeltoid_y(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

            return mCenter.y +
                    (float) (radius_1 * (2* Math.sin(interpolatedTime) + Math.sin(2*interpolatedTime)) / 3);
        }
    }
    class Astroid
    {
        PointF mStart = new PointF();
        PointF mCenter = new PointF();
        float radius_1;
        float max_r;
        float min_r;

        public Astroid()
        {
            max_r = Math.min(MAX_X, MAX_Y)/2;
            min_r = 150;
        }

        public void generateControlPoints()
        {
            atDefPos=false;
            radius_1 = random_generator.nextInt((int)(max_r - min_r)) + min_r;

            mCenter.x = random_generator.nextInt((int)(MAX_X -2*radius_1) +1) + radius_1;
            mCenter.y = random_generator.nextInt((int)(MAX_Y -2*radius_1) +1) + radius_1;

            float anim_factor = (radius_1 - min_r)/(max_r-min_r) * 1.7f + 1.2f;
            anim_duration=duration*anim_factor;
            mStart.x = calcAstroid_x(start_time);
            mStart.y = calcAstroid_y(start_time);
            objCustomEndPoint.generateControlPoints(mStart,duration*anim_factor);

        }

        private float calcAstroid_x(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

//            return mCenter.x + (float)(radius*Math.cos(interpolatedTime));
            return (float) (radius_1 * Math.pow(Math.cos(interpolatedTime), 3)) + mCenter.x;
        }

        private float calcAstroid_y(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

            return mCenter.y +
                    (float) (radius_1 * Math.pow(Math.sin(interpolatedTime),3));
        }
    }
    class EightCurve
    {
        PointF mStart = new PointF();
        PointF mCenter = new PointF();
        float radius_1;
        float max_r;
        float min_r;

        public EightCurve()
        {
            max_r = Math.min(MAX_X, MAX_Y)/2;
            min_r = 150;
        }

        public void generateControlPoints()
        {
            atDefPos=false;
            radius_1 = random_generator.nextInt((int)(max_r - min_r)) + min_r;

            mCenter.x = random_generator.nextInt((int)(MAX_X -2*radius_1) +1) + radius_1;
            mCenter.y = random_generator.nextInt((int)(MAX_Y -2*radius_1) +1) + radius_1;

            float anim_factor = (radius_1 - min_r)/(max_r-min_r) * 1.7f + 1.2f;
            anim_duration=duration*anim_factor;
            mStart.x = calcEightCurve_x(start_time);
            mStart.y = calcEightCurve_y(start_time);
            objCustomEndPoint.generateControlPoints(mStart,duration*anim_factor);

        }

        private float calcEightCurve_x(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

//            return mCenter.x + (float)(radius*Math.cos(interpolatedTime));
            return (float) (radius_1 * Math.sin(interpolatedTime)) + mCenter.x;
        }

        private float calcEightCurve_y(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

            return mCenter.y +
                    (float) (radius_1 * Math.sin(interpolatedTime) * Math.cos(interpolatedTime));
        }
    }
    class Lemniscate
    {
        PointF mStart = new PointF();
        PointF mCenter = new PointF();
        float radius_1;
        float max_r;
        float min_r;

        public Lemniscate()
        {
            max_r = Math.min(MAX_X, MAX_Y)/2;
            min_r = 180;
        }

        public void generateControlPoints()
        {
            atDefPos=false;
            radius_1 = random_generator.nextInt((int)(max_r - min_r)) + min_r;

            mCenter.x = random_generator.nextInt((int)(MAX_X -2*radius_1 +1)) + radius_1;
            mCenter.y = random_generator.nextInt((int)(MAX_Y -2*radius_1 +1)) + radius_1;

            float anim_factor = (radius_1 - min_r)/(max_r-min_r) * 1.7f + 1.2f;
            anim_duration=duration*anim_factor;
            mStart.x = calcLemniscate_x(start_time);
            mStart.y = calcLemniscate_y(start_time);
            objCustomEndPoint.generateControlPoints(mStart,duration*anim_factor);

        }

        private float calcLemniscate_x(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

//            return mCenter.x + (float)(radius*Math.cos(interpolatedTime));
            return (float) (radius_1 * Math.cos(interpolatedTime)
                    /
                    (1 + Math.pow(Math.sin(interpolatedTime), 2)))
                    + mCenter.x;
        }

        private float calcLemniscate_y(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

            return mCenter.y +
                    (float) (radius_1 * Math.cos(interpolatedTime) * Math.sin(interpolatedTime)
                            /
                            (1+Math.pow(Math.sin(interpolatedTime),2)));
        }
    }
    class Bicorn
    {
        PointF mStart = new PointF();
        PointF mCenter = new PointF();
        float radius_1;
        float max_r;
        float min_r;

        public Bicorn()
        {
            max_r = Math.min(MAX_X, MAX_Y)/2;
            min_r = 150;
        }

        public void generateControlPoints()
        {
            atDefPos=false;
            radius_1 = random_generator.nextInt((int)(max_r - min_r)) + min_r;

            mCenter.x = random_generator.nextInt((int)(MAX_X -2*radius_1) +1) + radius_1;
            mCenter.y = random_generator.nextInt((int)(MAX_Y -2*radius_1) +1) + radius_1;

            float anim_factor = (radius_1 - min_r)/(max_r-min_r) * 1.7f + 1.2f;
            anim_duration=duration*anim_factor;
            mStart.x = calcBicorn_x(start_time);
            mStart.y = calcBicorn_y(start_time);
            objCustomEndPoint.generateControlPoints(mStart,duration*anim_factor);

        }

        private float calcBicorn_x(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

//            return mCenter.x + (float)(radius*Math.cos(interpolatedTime));
            return (float) (radius_1 * Math.sin(interpolatedTime)) + mCenter.x;
        }

        private float calcBicorn_y(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

            return mCenter.y +
                    (float) ((radius_1 * Math.pow(Math.cos(interpolatedTime),2)
                    * (2+Math.cos(interpolatedTime)))
                    /
                            (3+ Math.pow(Math.sin(interpolatedTime),2)));
        }
    }
    class Nephroid
    {
        PointF mStart = new PointF();
        PointF mCenter = new PointF();
        float radius_1;
        float max_r;
        float min_r;

        public Nephroid()
        {
            max_r = Math.min(MAX_X, MAX_Y)/4;
            min_r = 50;
        }

        public void generateControlPoints()
        {
            atDefPos=false;
            radius_1 = random_generator.nextInt((int)(max_r - min_r)) + min_r;

            mCenter.x = MAX_X/2;
            mCenter.y = MAX_Y/2;

            float anim_factor = (radius_1 - min_r)/(max_r-min_r) * 1.7f + 1.2f;
            anim_duration=duration*anim_factor;
            mStart.x = calcNephroid_x(start_time);
            mStart.y = calcNephroid_y(start_time);
            objCustomEndPoint.generateControlPoints(mStart,duration*anim_factor);

        }

        private float calcNephroid_x(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);
            return mCenter.x +
                    (float) (radius_1 *
                            ( 3*Math.cos(interpolatedTime) - Math.cos(3 * interpolatedTime))
                            / 2 );
        }

        private float calcNephroid_y(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

            //            float y = mCenter.y +
//                    (float) (4*radius_1 * Math.pow(Math.sin(interpolatedTime),3));


            //customized y
            return mCenter.y +
                    (float) (2.7*radius_1 * Math.pow(Math.sin(interpolatedTime),3));
        }
    }
    class EpiCycloid
    {
        PointF mStart = new PointF();
        PointF mCenter = new PointF();
        float radius_1;
        int n;
        float max_r;
        float min_r;

        public EpiCycloid()
        {
            max_r = Math.min(MAX_X, MAX_Y)/3;
            min_r = 50;
        }

        public void generateControlPoints()
        {
            atDefPos=false;
            radius_1 = random_generator.nextInt((int)(max_r - min_r)) + min_r;
            n = random_generator.nextInt(15) + 4;


            mCenter.x = random_generator.nextInt((int)(MAX_X -2*radius_1)) + radius_1;
            mCenter.y = random_generator.nextInt((int)(MAX_Y -2*radius_1)) + radius_1;

            float anim_factor = (radius_1 - min_r)/(max_r-min_r) * 1.7f + 1.2f;
            anim_duration=duration*anim_factor;
            mStart.x = calcEpiCycloid_x(start_time);
            mStart.y = calcEpiCycloid_y(start_time);
            objCustomEndPoint.generateControlPoints(mStart,duration*anim_factor);

        }

        private float calcEpiCycloid_x(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

//            return mCenter.x + (float)(radius*Math.cos(interpolatedTime));

            return mCenter.x +
                    (float) (radius_1 *
                            ( (n+1)*Math.cos(interpolatedTime) - Math.cos((n + 1) * interpolatedTime))
                             / n );
        }

        private float calcEpiCycloid_y(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

            return mCenter.y +
                    (float) (radius_1 *
                            ( (n+1)*Math.sin(interpolatedTime) - Math.sin((n + 1) * interpolatedTime))
                            / n );
        }
    }
    class EllipseEvolute
    {
        PointF mStart = new PointF();
        PointF mCenter = new PointF();
        float radius_1;
        float radius_2;

        float max_r;
        float min_r;

        public EllipseEvolute()
        {
            max_r = Math.min(MAX_X, MAX_Y)/2;
            min_r = 150;
        }

        public void generateControlPoints()
        {
            atDefPos=false;
            radius_1 = random_generator.nextInt((int)(max_r - min_r) ) + min_r;
            radius_2 = random_generator.nextInt((int)(max_r - min_r) ) + min_r;

            float lim = Math.max(radius_1,radius_2);
            mCenter.x = random_generator.nextInt((int)(MAX_X -2*lim +1)) + lim;
            mCenter.y = random_generator.nextInt((int)(MAX_Y -2*lim +1)) + lim;

            float anim_factor = (radius_1 - min_r)/(max_r-min_r) * 2.7f + 1.2f;
            anim_duration=duration*anim_factor;
            mStart.x = calcEllipseEvolute_x(start_time);
            mStart.y = calcEllipseEvolute_y(start_time);
            objCustomEndPoint.generateControlPoints(mStart,duration*anim_factor);

        }

        private float calcEllipseEvolute_x(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

//            return mCenter.x + (float)(radius*Math.cos(interpolatedTime));
            return (float) (radius_1 * Math.cos(interpolatedTime)) + mCenter.x;
        }

        private float calcEllipseEvolute_y(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

            return (float) (radius_2 * Math.sin(interpolatedTime)) + mCenter.y;
        }
    }
    class Cornoid
    {
        PointF mStart = new PointF();
        PointF mCenter = new PointF();
        float radius_1;
        float max_r;
        float min_r;

        public Cornoid()
        {
            max_r = Math.min(MAX_X, MAX_Y)/2;
            min_r = 200;
        }

        public void generateControlPoints()
        {
            atDefPos=false;
            radius_1 = random_generator.nextInt((int)(max_r - min_r) +1) + min_r;

            mCenter.x = random_generator.nextInt((int)(MAX_X -2*radius_1 +1)) + radius_1;
            mCenter.y = random_generator.nextInt((int)(MAX_Y -2*radius_1 +1)) + radius_1;

            float anim_factor = (radius_1 - min_r)/(max_r-min_r) * 2.1f + 1.2f;
            anim_duration=duration*anim_factor;
            mStart.x = calcCornoid_x(start_time);
            mStart.y = calcCornoid_y(start_time);
            objCustomEndPoint.generateControlPoints(mStart,duration*anim_factor);

        }

        private float calcCornoid_x(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

//            return mCenter.x + (float)(radius*Math.cos(interpolatedTime));
            return mCenter.x +
                    (float) (radius_1 *
                            Math.cos(interpolatedTime) *
                            ( 1 - Math.pow(Math.sin(interpolatedTime),2))
                    );
        }

        private float calcCornoid_y(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

            return mCenter.y +
                    (float) (radius_1 *
                            Math.sin(interpolatedTime) *
                            ( 1 - Math.pow(Math.cos(interpolatedTime),2))
                    );
        }
    }

    //Not Working
    class DumbellCurve
    {
        PointF mStart = new PointF();
        PointF mCenter = new PointF();
        float radius_1;
        float max_r;
        float min_r;

        public DumbellCurve()
        {
            max_r = Math.min(MAX_X, MAX_Y)/2;
            min_r = 150;
        }

        public void generateControlPoints()
        {
            atDefPos=false;
            radius_1 = random_generator.nextInt((int)(max_r - min_r)) + min_r;

            mCenter.x = random_generator.nextInt((int)(MAX_X -2*radius_1)) + radius_1;
            mCenter.y = random_generator.nextInt((int)(MAX_Y -2*radius_1)) + radius_1;

            float anim_factor = (radius_1 - min_r)/(max_r-min_r) * 1.7f + 1.2f;
            anim_duration=duration*anim_factor;
            mStart.x = calcDumbellCurve_x(start_time);
            mStart.y = calcDumbellCurve_y(start_time);
            objCustomEndPoint.generateControlPoints(mStart,duration*anim_factor);

        }

        private float calcDumbellCurve_x(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

//            return mCenter.x + (float)(radius*Math.cos(interpolatedTime));
            return mCenter.x + radius_1 * interpolatedTime;
        }

        private float calcDumbellCurve_y(long current_time) {
            float interpolatedTime = (float) ((current_time-start_time)/anim_duration * 2 * Math.PI);

            float time_square =(float) Math.pow(interpolatedTime,2);
            return (float) (radius_1 * time_square * Math.sqrt(1 - time_square)) + mCenter.y;
        }
    }
}
