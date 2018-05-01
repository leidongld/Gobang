package com.example.leidong.gobang.views;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.example.leidong.gobang.R;
import com.example.leidong.gobang.utils.ChessUtils;

import java.util.ArrayList;

public class GoBangPanel extends View {
    private int mPanelWidth;
    private float mLineHeight;
    private int MAX_LINE = 10;
    private int MAX_COUNT_IN_LINE = 5;

    private Paint mPaint = new Paint();

    private Bitmap mWhiteChess;
    private Bitmap mBlackChess;

    private float mRadio = 0.75f;

    private boolean mIsWhite = false;
    private ArrayList<Point> mWhiteArray = new ArrayList<>();
    private ArrayList<Point> mBlackArray = new ArrayList<>();

    private boolean mIsGameOver = false;
    private boolean mIsWhiteWin = false;

    public GoBangPanel(Context context) {
        super(context);
    }

    public GoBangPanel(Context context, AttributeSet attrs){
        super(context, attrs);
        init();
    }

    /**
     * 初始化相关操作
     */
    private void init() {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mWhiteChess = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        mBlackChess = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);

        if(widthMode == MeasureSpec.UNSPECIFIED){
            width = heightSize;
        }
        else if(heightMode == MeasureSpec.UNSPECIFIED){
            width = widthSize;
        }

        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mPanelWidth = w;
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE;

        int chessWidth = (int) (mLineHeight * mRadio);
        mWhiteChess = Bitmap.createScaledBitmap(mWhiteChess, chessWidth, chessWidth, false);
        mBlackChess = Bitmap.createScaledBitmap(mBlackChess, chessWidth, chessWidth, false);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        drawBoard(canvas);

        drawChesses(canvas);

        checkGameOver();
    }

    /**
     * 查看游戏是否结束
     */
    private void checkGameOver() {
        boolean whiteWin = ChessUtils.checkWin(mWhiteArray);
        boolean blackWin = ChessUtils.checkWin(mBlackArray);
        if(whiteWin || blackWin){
            mIsGameOver = true;
            mIsWhite = whiteWin;

            String text = mIsWhite ? "白棋赢" : "黑棋赢";

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("提示");
            builder.setMessage(text);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mWhiteArray.clear();
                    mBlackArray.clear();
                    mIsGameOver = false;
                    mIsWhite = false;
                    invalidate();
                }
            });
            builder.setNegativeButton("取消", null);
            builder.create().show();
        }
    }

    /**
     * 绘制棋子
     */
    private void drawChesses(Canvas canvas) {
        for(int i = 0; i < mWhiteArray.size(); i++){
            Point whiteP = mWhiteArray.get(i);
            canvas.drawBitmap(mWhiteChess,
                    (whiteP.x + (1 - mRadio) / 2) * mLineHeight,
                    (whiteP.y + (1 - mRadio) / 2) * mLineHeight,
                    null);
        }

        for(int i = 0; i < mBlackArray.size(); i++){
            Point blackP = mBlackArray.get(i);
            canvas.drawBitmap(mBlackChess,
                    (blackP.x + (1 - mRadio) / 2) * mLineHeight,
                    (blackP.y + (1 - mRadio) / 2) * mLineHeight,
                    null);
        }
    }

    /**
     * 绘制棋盘
     */
    private void drawBoard(Canvas canvas) {
        int w = mPanelWidth;
        float lineHeight = mLineHeight;

        for(int i = 0; i < MAX_LINE; i++){
            float startX = (int) (lineHeight / 2);
            float endX = (int) (w - lineHeight / 2);

            float y = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(startX, y, endX,  y, mPaint);
            canvas.drawLine(y, startX, y, endX, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mIsGameOver){
            return false;
        }

        int action = event.getAction();
        if(action == MotionEvent.ACTION_UP){
            int x = (int) event.getX();
            int y = (int) event.getY();

            Point p = getValidPoint(x, y);

            if(mWhiteArray.contains(p) || mBlackArray.contains(p)){
                return false;
            }

            if(mIsWhite){
                mWhiteArray.add(p);
            }
            else{
                mBlackArray.add(p);
            }
            invalidate();
            mIsWhite = !mIsWhite;
            return true;
        }
        return true;
    }

    /**
     * 得到合法的Point点
     * @param x
     * @param y
     * @return
     */
    private Point getValidPoint(int x, int y) {
        return new Point((int) (x / mLineHeight), (int) (y / mLineHeight));
    }

    private static final String INSTANCE = "instance";
    private static final String INSTANCE_GAME_OVER = "instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY = "instance_black_array";

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE, super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER, mIsGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY, mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY, mBlackArray);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if(state instanceof Bundle){
            Bundle bundle = (Bundle) state;
            mIsGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhiteArray = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return ;
        }
        super.onRestoreInstanceState(state);
    }
}
