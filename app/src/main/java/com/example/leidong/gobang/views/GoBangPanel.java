package com.example.leidong.gobang.views;

import android.content.Context;
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
import android.widget.Toast;

import com.example.leidong.gobang.R;

import java.util.ArrayList;
import java.util.List;

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
        //setBackgroundColor(0x44ff0000);
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

        chackGameOver();
    }

    /**
     * 查看游戏是否结束
     */
    private void chackGameOver() {
        boolean whiteWin = checkWin(mWhiteArray);
        boolean blackWin = checkWin(mBlackArray);
        if(whiteWin || blackWin){
            mIsGameOver = true;
            mIsWhite = whiteWin;

            String text = mIsWhite ? "白棋赢" : "黑棋赢";

            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断是否赢得比赛
     * @param points
     * @return
     */
    private boolean checkWin(List<Point> points) {
        for (Point p : points) {
            int x = p.x;
            int y = p.y;
            boolean winHorizontal = checkHorizontal(x, y, points);
            boolean winVertical = checkVertical(x, y, points);
            boolean winLeftRhombic = checkLeftRhombic(x, y, points);
            boolean winRightRhombic = checkRightRhombic(x, y, points);
            if(winHorizontal || winVertical || winLeftRhombic || winRightRhombic){
                return true;
            }
        }
        return false;
    }

    /**
     * （x,y）坐标的棋子是否右斜方连成5个
     * @param x
     * @param y
     * @param points
     * @return
     */
    private boolean checkRightRhombic(int x, int y, List<Point> points) {
        int count = 1;
        //右上
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++){
            if(points.contains(new Point(x + i, y - i))){
                count++;
            }
            else{
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE){
            return true;
        }

        //左下
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x - i, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE){
            return true;
        }
        return false;
    }

    /**
     * （x,y）坐标的棋子是否左斜方连成5个
     * @param x
     * @param y
     * @param points
     * @return
     */
    private boolean checkLeftRhombic(int x, int y, List<Point> points) {
        int count = 1;
        //左上
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++){
            if(points.contains(new Point(x - i, y - i))){
                count++;
            }
            else{
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE){
            return true;
        }

        //右下
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE){
            return true;
        }
        return false;
    }

    /**
     * （x,y）坐标相邻的棋子是否纵向连成5个
     * @param x
     * @param y
     * @param points
     * @return
     */
    private boolean checkVertical(int x, int y, List<Point> points) {
        int count = 1;
        //上边
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++){
            if(points.contains(new Point(x, y - i))){
                count++;
            }
            else{
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE){
            return true;
        }

        //下边
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x, y + i))) {
                count++;
            } else {
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE){
            return true;
        }
        return false;
    }

    /**
     * （x,y）坐标的棋子是否横向连成5个
     * @param x
     * @param y
     * @param points
     * @return
     */
    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count = 1;
        //左边
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++){
            if(points.contains(new Point(x - i, y))){
                count++;
            }
            else{
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE){
            return true;
        }

        //右边
        for(int i = 1; i < MAX_COUNT_IN_LINE; i++) {
            if (points.contains(new Point(x + i, y))) {
                count++;
            } else {
                break;
            }
        }
        if(count == MAX_COUNT_IN_LINE){
            return true;
        }
        return false;
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
