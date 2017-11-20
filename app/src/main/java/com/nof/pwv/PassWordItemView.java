package com.nof.pwv;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2017/11/17.
 */

public class PassWordItemView extends View {

    private int mReadyToInputColor;
    private int mAlreadyInputColor;

    private int mShape;
    public static final int SHAPE_CICLR = 0;
    public static final int SHAPE_RECF = 1;
    public static final int SHAPE_LINE = 2;

    private String mPassText;
    private int mPassType;
    public static final int PASSTYPE_CICLR = 0;
    public static final int PASSTYPE_STAR = 1;
    public static final int PASSTYPE_TEXT = 2;

    private int mState = 0;//0 红色，待输入；1 红色，正在输入；2 灰色，已经输入
    private boolean mRemindLineDrawing = false;

    private Context mContext;
    private Paint mPaint;
    private Handler mHandler;
    private Runnable mRunnable;

    public PassWordItemView(Context context) {
        this(context,null);
    }

    public PassWordItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PassWordItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(3);
    }

    public void initParams(int mReadyToInputColor,int mAlreadyInputColor,int mShape,
                           int mPassType,String mPassText){
        this.mReadyToInputColor = mReadyToInputColor;
        this.mAlreadyInputColor = mAlreadyInputColor;
        this.mShape = mShape;
        this.mPassType = mPassType;
        this.mPassText = mPassText;
        changeState(0,"");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        int width;
        int height;
        if(modeWidth == MeasureSpec.EXACTLY){
            width = sizeWidth;
        }else{
            width = Util.dp2px(sizeWidth);
            if(modeWidth == MeasureSpec.AT_MOST){
                width = Math.min(width,sizeWidth);
            }
        }

        if(modeHeight == MeasureSpec.EXACTLY){
            height = sizeHeight;
        }else{
            height = Util.dp2px(sizeHeight);
            if(modeHeight == MeasureSpec.AT_MOST){
                height = Math.min(height,sizeHeight);
            }
        }
        setMeasuredDimension(Math.min(width,height),Math.min(width,height));
    }

    public void changeState(int state, String text){
        this.mState = state;
        this.mPassText = text;
        invalidate();
        if(state==1){
            mHandler = new Handler();
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    mRemindLineDrawing = !mRemindLineDrawing;
                    invalidate();
                    mHandler.postDelayed(this, 600);
                }
            };
            mHandler.post(mRunnable);
        }else{
            if(mHandler!=null&&mRunnable!=null){
                mHandler.removeCallbacks(mRunnable);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setPassState(mState,canvas);
    }

    private void setPassState(int state,Canvas canvas){
        switch (state){
            case 0:
                drawInputBox(canvas);
                break;
            case 1:
                drawInputBox(canvas);
                drawRemindLine(canvas);
                break;
            case 2:
                drawInputBox(canvas);
                drawInputText(canvas);
                break;
        }
    }



    private void drawInputBox(Canvas canvas){
        mPaint.setColor(mState == 2 ? mAlreadyInputColor : mReadyToInputColor);
        mPaint.setStyle(Paint.Style.STROKE);
        switch (mShape){
            case SHAPE_CICLR:
                canvas.drawCircle(getMeasuredWidth()/2,
                        getMeasuredHeight()/2,
                        getMeasuredWidth()/2-5,
                        mPaint);
                break;
            case SHAPE_RECF:
                RectF rectF = new RectF(0,0,getMeasuredWidth(),getMeasuredHeight());
                canvas.drawRoundRect(rectF,6,6,mPaint);

                break;
            case SHAPE_LINE:
                canvas.drawLine(0,getMeasuredHeight(),getMeasuredWidth(),
                        getMeasuredHeight(),mPaint);
                break;
        }
    }

    private void drawInputText(Canvas canvas){
        if(mState == 2){
            mPaint.setColor(mAlreadyInputColor);
            mPaint.setStyle(Paint.Style.FILL);
            switch (mPassType){
                case PASSTYPE_CICLR:
                    canvas.drawCircle(getMeasuredWidth()/2,getMeasuredHeight()/2,
                            getMeasuredWidth()/4,mPaint);
                    break;
                case PASSTYPE_STAR:
                    mPaint.setTextSize(getMeasuredHeight() / 3 * 2);
                    mPaint.setTextAlign(Paint.Align.CENTER);
                    float h1 = mPaint.descent() - mPaint.ascent();
                    canvas.drawText("*",getMeasuredWidth()/2,
                            getMeasuredHeight()/2 + h1/2 - 10, mPaint);
                    break;
                case PASSTYPE_TEXT:
                    mPaint.setTextSize(getMeasuredHeight() / 3 * 2);
                    mPaint.setTextAlign(Paint.Align.CENTER);
                    float h2 = mPaint.descent() - mPaint.ascent();
                    canvas.drawText(mPassText,getMeasuredWidth()/2,
                            getMeasuredHeight()/2 + h2 / 3, mPaint);
                    break;
            }
        }
    }

    private void drawRemindLine(Canvas canvas){
        if(mRemindLineDrawing){
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mReadyToInputColor);
            canvas.drawLine(getMeasuredWidth()/2,getMeasuredHeight()/6,
                    getMeasuredWidth()/2,getMeasuredHeight()/6*5,mPaint);
        }
    }
}
