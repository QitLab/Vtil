package com.qit.vtil;

import android.util.TypedValue;

import static com.qit.vtil.Util.getApplicationContext;

public class DimenUtil {

    private DimenUtil() {

    }

    static int px2dip(float pxValue) {
        return Integer.parseInt(px2dip(pxValue, false));
    }

    static String px2dip(float pxValue, boolean withUnit) {
        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5F) + (withUnit ? "dp" : "");
    }

    static int dip2px(float dpValue) {
        float scale = getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5F);
    }

    static int sp2px(float sp) {
        return (int) TypedValue.applyDimension(2, sp, getApplicationContext().getResources().getDisplayMetrics());
    }

    static String px2sp(float pxValue) {
        final float fontScale = getApplicationContext().getResources().getDisplayMetrics().scaledDensity;
        return String.valueOf((int) (pxValue / fontScale + 0.5f));
    }

    static int getScreenWidth() {
        return getApplicationContext().getResources().getDisplayMetrics().widthPixels;
    }

    static int getScreenHeight() {
        return getApplicationContext().getResources().getDisplayMetrics().heightPixels;
    }
}
