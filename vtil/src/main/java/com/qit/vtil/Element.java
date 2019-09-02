package com.qit.vtil;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import static com.qit.vtil.DimenUtil.dip2px;

/**
 * author: Qit .
 * date:   On 2019/5/16
 */
public class Element {
    private View view;
    private int width, height, paddingL, paddingT, paddingR, paddingB;
    String bgColor;
    String text;
    Bitmap bgBitmap;

    private Rect rect = new Rect();
    private Element parentElement;

    Element(View view) {
        this.view = view;
        this.width = view.getWidth();
        this.height = view.getHeight();
        this.paddingL = view.getPaddingLeft();
        this.paddingT = view.getPaddingTop();
        this.paddingR = view.getPaddingRight();
        this.paddingB = view.getPaddingBottom();
        Object background = Util.getBackground(view);
        if (background instanceof String) {
            bgColor = (String) background;
        }
        if (view instanceof TextView) {
            this.text = ((TextView) view).getText().toString();
        }
        initRect();
    }

    View getView() {
        return view;
    }

    Rect getRect() {
        return rect;
    }

    void initRect() {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int left = location[0];
        int right = location[0] + getWidth();
        int top = location[1];
        int bottom = location[1] + getHeight();
        rect.set(left, top, right, bottom);
    }

    Element getParentElement() {
        if (parentElement == null) {
            Object parentView = view.getParent();
            if (parentView instanceof View) {
                parentElement = new Element((View) parentView);
            }
        }
        return parentElement;
    }

    int getWidth() {
        return width;
    }

    void setWidth(int width) {
        this.width = width;
        if (Math.abs(width - view.getWidth()) >= dip2px(1)) {
            if (view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height, 0f);
                view.setLayoutParams(lp);
            } else {
                view.getLayoutParams().width = width;
                view.requestLayout();
            }
        }
    }

    int getHeight() {
        return height;
    }

    void setHeight(int height) {
        this.height = height;
        if (Math.abs(height - view.getHeight()) >= dip2px(1)) {
            if (view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height, 0f);
                view.setLayoutParams(lp);
            } else {
                view.getLayoutParams().height = height;
                view.requestLayout();
            }
        }
    }

    int getPaddingL() {
        return paddingL;
    }

    void setPaddingL(int paddingL) {
        this.paddingL = paddingL;
        if (Math.abs(paddingL - view.getPaddingLeft()) >= dip2px(1)) {
            view.setPadding(paddingL, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    int getPaddingT() {
        return paddingT;
    }

    void setPaddingT(int paddingT) {
        this.paddingT = paddingT;
        if (Math.abs(paddingT - view.getPaddingTop()) >= dip2px(1)) {
            view.setPadding(view.getPaddingLeft(), paddingT, view.getPaddingRight(), view.getPaddingBottom());
        }
    }

    int getPaddingR() {
        return paddingR;
    }

    void setPaddingR(int paddingR) {
        this.paddingR = paddingR;
        if (Math.abs(paddingR - view.getPaddingRight()) >= dip2px(1)) {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), paddingR, view.getPaddingBottom());
        }
    }

    int getPaddingB() {
        return paddingB;
    }

    void setPaddingB(int paddingB) {
        this.paddingB = paddingB;
        if (Math.abs(paddingB - view.getPaddingBottom()) >= dip2px(1)) {
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), paddingB);
        }
    }

    public String getBgColor() {
        return bgColor;
    }

    void setBgColor(String bgColor) {
        this.bgColor = bgColor;
        try {
            view.setBackgroundColor(Color.parseColor(bgColor));
        } catch (Exception ignored) {
        }
    }

    public String getText() {
        return text;
    }

    void setText(String text) {
        this.text = text;
    }


    public Bitmap getBgBitmap() {
        return bgBitmap;
    }

    public void setBgBitmap(Bitmap bgBitmap) {
        this.bgBitmap = bgBitmap;
    }
}
