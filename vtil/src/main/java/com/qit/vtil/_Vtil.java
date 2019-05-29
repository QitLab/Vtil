package com.qit.vtil;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Field;
import java.util.Map;

import static com.qit.vtil.Util.getApplicationContext;

/**
 * author: Qit .
 * date:   On 2019/5/16
 */
final class _Vtil {
    private static volatile _Vtil instance;

    private MenuLayout mVtilBtn;
    private Activity currentActivity;

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
    }


    void show() {
        View vi = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_menu, null);
        mVtilBtn = vi.findViewById(R.id.id_menu);
        mVtilBtn.show();

//        if (mVtilBtn == null)
//            mVtilBtn = new MenuLayout(mContext);
//        if (mVtilBtn.isOpen())
//            mVtilBtn.toggleMenu(600);
        mVtilBtn.setOnMenuItemClickListener((view, pos) -> {
            switch (pos) {
                case 1:
                    startMeasure();
                    break;
                case 2:
                    startScalpel();
                    break;
            }
//            Toast.makeText(getApplicationContext(), pos + ":" + view.getTag(), Toast.LENGTH_SHORT).show()
        });
//

    }

    private void setCurrentActivity(Activity activity) {
        this.currentActivity = activity;
    }

    static Activity getCurrentActivity() {
        return instance.currentActivity;
    }

    private static Activity initCurrentActivity() {
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


    private void startMeasure() {
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

    private void startScalpel(){
        ViewGroup decorView = (ViewGroup) initCurrentActivity().getWindow().getDecorView();
        ViewGroup content = decorView.findViewById(android.R.id.content);
        View contentChild = content.getChildAt(0);
        if (contentChild != null) {
            if (contentChild instanceof ScalpelFrameLayout) {
                content.removeAllViews();
                View originContent = ((ScalpelFrameLayout) contentChild).getChildAt(0);
                ((ScalpelFrameLayout) contentChild).removeAllViews();
                content.addView(originContent);
            } else {
                content.removeAllViews();
                ScalpelFrameLayout frameLayout = new ScalpelFrameLayout(getApplicationContext());
                frameLayout.setLayerInteractionEnabled(true);
                frameLayout.setDrawIds(true);
                frameLayout.addView(contentChild);
                content.addView(frameLayout);
            }
        }
    }

}
