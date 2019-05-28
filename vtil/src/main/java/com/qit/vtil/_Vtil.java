package com.qit.vtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.util.Map;

import static com.qit.vtil.Util.getApplicationContext;

/**
 * author: Qit .
 * date:   On 2019/5/16
 */
class _Vtil {
    private static volatile _Vtil instance;

    private Button mVtilBtn;
    private Context mContext = getApplicationContext();
    private Activity currentActivity;
    private WindowManager.LayoutParams params = new WindowManager.LayoutParams();

    private _Vtil() {
    }

    static _Vtil getInstance() {
        if (instance == null) {
            synchronized (_Vtil.class) {
                if (instance == null) {
                    instance = new _Vtil();
                }
            }
        }
        return instance;
    }


    void detach() {
        instance = null;
        mContext = null;
    }


    void show() {
        if (mVtilBtn == null)
            mVtilBtn = new Button(mContext);
        mVtilBtn.setOnClickListener(v -> start());
        mVtilBtn.setOnLongClickListener(v -> {
            ViewGroup decorView = (ViewGroup) Vtil.getCurrentActivity().getWindow().getDecorView();
            ViewGroup content = decorView.findViewById(android.R.id.content);
            View contentChild = content.getChildAt(0);
            if (contentChild != null) {
                content.removeAllViews();
                ScalpelFrameLayout frameLayout = new ScalpelFrameLayout(getApplicationContext());
                frameLayout.setLayerInteractionEnabled(true);
                frameLayout.setDrawIds(true);
                frameLayout.addView(contentChild);
                content.addView(frameLayout);
            }
            return true;
        });
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        windowManager.addView(mVtilBtn, getLayoutParams());
    }

    private WindowManager.LayoutParams getLayoutParams() {
        params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
        params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 10;
        params.y = 10;
        return params;
    }

    private void start() {
        Activity currentTopActivity = initCurrentActivity();
        if (currentTopActivity == null) {
            return;
        } else if (currentTopActivity.getClass() == HiddenActivity.class) {
            currentTopActivity.finish();
            return;
        }
        Intent intent = new Intent(currentTopActivity, HiddenActivity.class);
        currentTopActivity.startActivity(intent);
        currentTopActivity.overridePendingTransition(0, 0);
        instance.setCurrentActivity(currentTopActivity);
    }

    private void setCurrentActivity(Activity activity) {
        this.currentActivity = activity;
    }

    static Activity getCurrentActivity() {
        return instance.currentActivity;
    }

    protected static Activity initCurrentActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(
                    null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (Map) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    return (Activity) activityField.get(activityRecord);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
