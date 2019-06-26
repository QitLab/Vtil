package com.qit.vtil.sample;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.qit.vtil.Vtil;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        Vtil.register();
        refreshNoticeList();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Vtil.unregister();
    }


    private void refreshNoticeList() {
        String[] strs = {
                "支持点击控件显示其属性",
                "支持控件属性修改",
                "支持Move模式,控件屏幕随意拖动并显示距离",
                "支持选中子空间时再点击选中其父控件",
                "支持3d效果展示控件的绘制情况",
                "支持安装Vtil对应插件实现与Ide的交互",
        };
        ViewFlipper mViewFlipper = findViewById(R.id.vf_view_flipper);
        mViewFlipper.setFlipInterval(4000);
        for (String notice : strs) {
            TextView textView = new TextView(this);
            ViewFlipper.LayoutParams layoutParams = new ViewFlipper.LayoutParams(
                    ViewFlipper.LayoutParams.MATCH_PARENT,
                    ViewFlipper.LayoutParams.MATCH_PARENT
            );
            textView.setLayoutParams(layoutParams);
            textView.setTextSize(12f);
            textView.setTextColor(Color.parseColor("#666666"));
            textView.setSingleLine(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setGravity(Gravity.CENTER_VERTICAL);

            textView.setText(notice);
            mViewFlipper.addView(textView);
        }
        mViewFlipper.startFlipping();

    }
}
