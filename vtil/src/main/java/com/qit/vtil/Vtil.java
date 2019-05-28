package com.qit.vtil;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import static com.qit.vtil.Util.getApplicationContext;

/**
 * author: Qit .
 * date:   On 2019/5/16
 */
public class Vtil {

    public static void register() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getApplicationContext().getPackageName()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);
                return;
            }
        }
        _Vtil.getInstance().show();
    }

    public static void unregister() {
        _Vtil.getInstance().detach();
    }

    static Activity getCurrentActivity() {
        return _Vtil.getCurrentActivity();
    }

}
