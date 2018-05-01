package com.example.leidong.gobang.utils;

import android.graphics.Point;

import java.util.List;

public class ChessUtils {
    private static int MAX_LINE = 10;
    private static int MAX_COUNT_IN_LINE = 5;

    /**
     * 判断是否赢得比赛
     * @param points
     * @return
     */
    public static boolean checkWin(List<Point> points) {
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
    private static boolean checkRightRhombic(int x, int y, List<Point> points) {
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
    private static boolean checkLeftRhombic(int x, int y, List<Point> points) {
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
    private static boolean checkVertical(int x, int y, List<Point> points) {
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
    private static boolean checkHorizontal(int x, int y, List<Point> points) {
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
}
