package com.qit.vtil;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

/**
 * author: Qit .
 * date:   On 2019/5/16
 */
public class HiddenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            finish();
            return;
        }
        Util.setStatusBarColor(getWindow(), Color.TRANSPARENT);
        Util.enableFullscreen(getWindow());
        FrameLayout mContainer = new FrameLayout(this);
        setContentView(mContainer);
        EditorLayout editAttrLayout = new EditorLayout(this);

        mContainer.addView(editAttrLayout);
    }
}
