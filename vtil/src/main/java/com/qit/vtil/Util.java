package com.qit.vtil;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.graphics.NinePatch;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.text.SpannedString;
import android.text.style.ImageSpan;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.NO_ID;

public class Util {

    static Context getApplicationContext() {
        Application application = null;
        Class<?> activityThreadClass;
        Object localObject;
        Method method;
        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");
            final Method method2 = activityThreadClass.getMethod(
                    "currentActivityThread", new Class[0]);
            // 得到当前的ActivityThread对象
            localObject = method2.invoke(null, (Object[]) null);

            method = activityThreadClass
                    .getMethod("getApplication");
            application = (Application) method.invoke(localObject, (Object[]) null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return application;

    }

    static void enableFullscreen(@NonNull Window window) {
        if (Build.VERSION.SDK_INT >= 21) {
            addSystemUiFlag(window, 1280);
        }
    }

    private static void addSystemUiFlag(Window window, int flag) {
        View view = window.getDecorView();
        view.setSystemUiVisibility(view.getSystemUiVisibility() | flag);
    }

    static void setStatusBarColor(@NonNull Window window, int color) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(color);
        }
    }

    static String getResourceName(int id) {
        Resources resources = getApplicationContext().getResources();
        try {
            if (id == NO_ID || id == 0) {
                return "";
            } else {
                return resources.getResourceEntryName(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    static String getResId(View view) {
        try {
            int id = view.getId();
            if (id == NO_ID) {
                return "";
            } else {
                return "0x" + Integer.toHexString(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String intToHexColor(int color) {
        return "#" + Integer.toHexString(color).toUpperCase();
    }

    static Object getBackground(View view) {
        Drawable drawable = view.getBackground();
        if (drawable instanceof ColorDrawable) {
            return intToHexColor(((ColorDrawable) drawable).getColor());
        } else if (drawable instanceof GradientDrawable) {
            try {
                Field mFillPaintField = GradientDrawable.class.getDeclaredField("mFillPaint");
                mFillPaintField.setAccessible(true);
                Paint mFillPaint = (Paint) mFillPaintField.get(drawable);
                Shader shader = mFillPaint.getShader();
                if (shader instanceof LinearGradient) {
                    Field mColorsField = LinearGradient.class.getDeclaredField("mColors");
                    mColorsField.setAccessible(true);
                    int[] mColors = (int[]) mColorsField.get(shader);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0, N = mColors.length; i < N; i++) {
                        sb.append(intToHexColor(mColors[i]));
                        if (i < N - 1) {
                            sb.append(" -> ");
                        }
                    }
                    return sb.toString();
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    static List<Pair<String, Bitmap>> getTextViewBitmap(TextView textView) {
        List<Pair<String, Bitmap>> bitmaps = new ArrayList<>();
        bitmaps.addAll(getTextViewDrawableBitmap(textView));
        bitmaps.addAll(getTextViewImageSpanBitmap(textView));
        return bitmaps;
    }

    private static List<Pair<String, Bitmap>> getTextViewDrawableBitmap(TextView textView) {
        List<Pair<String, Bitmap>> bitmaps = new ArrayList<>();
        try {
            Drawable[] drawables = textView.getCompoundDrawables();
            bitmaps.add(new Pair<>("DrawableLeft", getDrawableBitmap(drawables[0])));
            bitmaps.add(new Pair<>("DrawableTop", getDrawableBitmap(drawables[1])));
            bitmaps.add(new Pair<>("DrawableRight", getDrawableBitmap(drawables[2])));
            bitmaps.add(new Pair<>("DrawableBottom", getDrawableBitmap(drawables[3])));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmaps;
    }

    private static List<Pair<String, Bitmap>> getTextViewImageSpanBitmap(TextView textView) {
        List<Pair<String, Bitmap>> bitmaps = new ArrayList<>();
        try {
            CharSequence text = textView.getText();
            if (text instanceof SpannedString) {
                Field mSpansField = Class.forName("android.text.SpannableStringInternal").getDeclaredField("mSpans");
                mSpansField.setAccessible(true);
                Object[] spans = (Object[]) mSpansField.get(text);
                for (Object span : spans) {
                    if (span instanceof ImageSpan) {
                        bitmaps.add(new Pair<>("SpanBitmap", getDrawableBitmap(((ImageSpan) span).getDrawable())));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmaps;
    }

    static Bitmap getImageViewBitmap(ImageView imageView) {
        return getDrawableBitmap(imageView.getDrawable());
    }

    private static Bitmap getDrawableBitmap(Drawable drawable) {
        try {
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            } else if (drawable instanceof NinePatchDrawable) {
                NinePatch ninePatch;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Field mNinePatchStateFiled = NinePatchDrawable.class.getDeclaredField("mNinePatchState");
                    mNinePatchStateFiled.setAccessible(true);
                    Object mNinePatchState = mNinePatchStateFiled.get(drawable);
                    Field mNinePatchFiled = mNinePatchState.getClass().getDeclaredField("mNinePatch");
                    mNinePatchFiled.setAccessible(true);
                    ninePatch = (NinePatch) mNinePatchFiled.get(mNinePatchState);
                    return ninePatch.getBitmap();
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Field mNinePatchFiled = NinePatchDrawable.class.getDeclaredField("mNinePatch");
                    mNinePatchFiled.setAccessible(true);
                    ninePatch = (NinePatch) mNinePatchFiled.get(drawable);
                    return ninePatch.getBitmap();
                }
            } else if (drawable instanceof ClipDrawable) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return ((BitmapDrawable) ((ClipDrawable) drawable).getDrawable()).getBitmap();
                }
            } else if (drawable instanceof StateListDrawable) {
                return ((BitmapDrawable) drawable.getCurrent()).getBitmap();
            } else if (drawable instanceof VectorDrawableCompat) {
                Field mVectorStateField = VectorDrawableCompat.class.getDeclaredField("mVectorState");
                mVectorStateField.setAccessible(true);
                Field mCachedBitmapField = Class.forName("android.support.graphics.drawable.VectorDrawableCompat$VectorDrawableCompatState").getDeclaredField("mCachedBitmap");
                mCachedBitmapField.setAccessible(true);
                return (Bitmap) mCachedBitmapField.get(mVectorStateField.get(drawable));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static String getImageViewScaleType(ImageView imageView) {
        return imageView.getScaleType().name();
    }

    public static void clipText(String clipText) {
        Context context = getApplicationContext();
        ClipData clipData = ClipData.newPlainText("", clipText);
        ((ClipboardManager) (context.getSystemService(Context.CLIPBOARD_SERVICE))).setPrimaryClip(clipData);
        Toast.makeText(context, "copied", Toast.LENGTH_SHORT).show();
    }
}
