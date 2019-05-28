package com.qit.vtil;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;

import static com.qit.vtil.DimenUtil.dip2px;
import static com.qit.vtil.DimenUtil.getScreenHeight;
import static com.qit.vtil.DimenUtil.getScreenWidth;
import static com.qit.vtil.DimenUtil.px2dip;
import static com.qit.vtil.DimenUtil.sp2px;

/**
 * author: Qit .
 * date:   On 2019/5/16
 */
class EditorLayoutTool {
    private final int lineEndHeight = 4;
    private final int textLineDistance = 8;
    final int moveUnit = 1;
    final int lineMargin = 8;
    private final int screenWidth = getScreenWidth();
    private final int screenHeight = getScreenHeight();
    private Paint textPaint = new Paint() {
        {
            setAntiAlias(true);
            setTextSize(sp2px(10));
            setColor(Color.RED);
            setStrokeWidth(dip2px(1));
        }
    };

    private Paint textBgPaint = new Paint() {
        {
            setAntiAlias(true);
            setColor(Color.WHITE);
            setStrokeJoin(Join.ROUND);
        }
    };

    Paint dashLinePaint = new Paint() {
        {
            setAntiAlias(true);
            setColor(0x90FF0000);
            setStyle(Style.STROKE);
            setPathEffect(new DashPathEffect(new float[]{dip2px(4), dip2px(8)}, 0));
        }
    };
    Paint areaPaint = new Paint() {
        {
            setAntiAlias(true);
            setColor(0x30000000);
        }
    };


    private void drawText(Canvas canvas, String text, float x, float y) {
        float left = x;
        float top = y - getTextHeight(text);
        float right = x + getTextWidth(text);
        float bottom = y;
        // ensure text in screen bound
        if (left < 0) {
            right -= left;
            left = 0;
        }
        if (top < 0) {
            bottom -= top;
            top = 0;
        }
        if (bottom > screenHeight) {
            float diff = top - bottom;
            bottom = screenHeight;
            top = bottom + diff;
        }
        if (right > screenWidth) {
            float diff = left - right;
            right = screenWidth;
            left = right + diff;
        }
        canvas.drawRect(left, top, right, bottom, textBgPaint);
        canvas.drawText(text, left, bottom, textPaint);
    }

    private void drawLineWithEndPoint(Canvas canvas, int startX, int startY, int endX, int endY) {
        canvas.drawLine(startX, startY, endX, endY, textPaint);
        if (startX == endX) {
            canvas.drawLine(startX - lineEndHeight, startY, endX + lineEndHeight, startY, textPaint);
            canvas.drawLine(startX - lineEndHeight, endY, endX + lineEndHeight, endY, textPaint);
        } else if (startY == endY) {
            canvas.drawLine(startX, startY - lineEndHeight, startX, endY + lineEndHeight, textPaint);
            canvas.drawLine(endX, startY - lineEndHeight, endX, endY + lineEndHeight, textPaint);
        }
    }

    void drawLineWithText(Canvas canvas, int startX, int startY, int endX, int endY) {
        drawLineWithText(canvas, startX, startY, endX, endY, 0);
    }

    void drawLineWithText(Canvas canvas, int startX, int startY, int endX, int endY, int endPointSpace) {

        if (startX == endX && startY == endY) {
            return;
        }

        if (startX > endX) {
            int tempX = startX;
            startX = endX;
            endX = tempX;
        }
        if (startY > endY) {
            int tempY = startY;
            startY = endY;
            endY = tempY;
        }

        if (startX == endX) {
            drawLineWithEndPoint(canvas, startX, startY + endPointSpace, endX, endY - endPointSpace);
            String text = px2dip(endY - startY, true);
            drawText(canvas, text, startX + textLineDistance, startY + ((endY - startY) >> 1) + getTextHeight(text) / 2);
        } else if (startY == endY) {
            drawLineWithEndPoint(canvas, startX + endPointSpace, startY, endX - endPointSpace, endY);
            String text = px2dip(endX - startX, true);
            drawText(canvas, text, startX + ((endX - startX) >> 1) - getTextWidth(text) / 2, startY - textLineDistance);
        }
    }

    private float getTextHeight(String text) {
        Rect rect = new Rect();
        textPaint.getTextBounds(text, 0, text.length(), rect);
        return rect.height();
    }

    private float getTextWidth(String text) {
        return textPaint.measureText(text);
    }

}
