package com.qit.vtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;


import static com.qit.vtil.DimenUtil.dip2px;
import static com.qit.vtil.DimenUtil.getScreenHeight;
import static com.qit.vtil.DimenUtil.getScreenWidth;
import static com.qit.vtil.DimenUtil.px2dip;

public class AttrsDialog extends Dialog {

    private AttrDialogCallback callback;
    private Element element;

    private TextView tvType, tvID;
    private EditText etWidth, etHeight, etPaddingL, etPaddingT, etPaddingR, etPaddingB;
    private EditText etBg;
    private View bgColor;

    AttrsDialog(Context context) {
        super(context, R.style.VtilTheme_Holo_Dialog_background_Translucent);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_attrs);

        /*
          可拖拽
         */
        View move = findViewById(R.id.move);
        ((TextView) move.findViewById(R.id.name)).setText("Move");
        move.findViewById(R.id.switch_view).setOnClickListener(v -> callback.enableMove());

        /*
         * 类型
         */
        View type = findViewById(R.id.type);
        ((TextView) type.findViewById(R.id.name)).setText("Class");
        tvType = type.findViewById(R.id.detail);
        /*
         * id
         */
        View id = findViewById(R.id.id);
        ((TextView) id.findViewById(R.id.name)).setText("ID");
        tvID = id.findViewById(R.id.detail);

        /*
         * width
         */
        View width = findViewById(R.id.width);
        ((TextView) width.findViewById(R.id.name)).setText("Width");
        etWidth = width.findViewById(R.id.detail);
        width.findViewById(R.id.add).setOnClickListener(v -> etWidth.setText((Integer.valueOf(etWidth.getText().toString()) + 1) + ""));
        width.findViewById(R.id.minus).setOnClickListener(v -> etWidth.setText((Integer.valueOf(etWidth.getText().toString()) - 1) + ""));
        etWidth.addTextChangedListener(new MyTextWatcher(MyTextWatcher.WIDTH));
        /*
         * height
         */
        View height = findViewById(R.id.height);
        ((TextView) height.findViewById(R.id.name)).setText("Height");
        etHeight = height.findViewById(R.id.detail);
        height.findViewById(R.id.add).setOnClickListener(v -> etHeight.setText((Integer.valueOf(etHeight.getText().toString()) + 1) + ""));
        height.findViewById(R.id.minus).setOnClickListener(v -> etHeight.setText((Integer.valueOf(etHeight.getText().toString()) - 1) + ""));
        etHeight.addTextChangedListener(new MyTextWatcher(MyTextWatcher.HEIGHT));

        /*
         * bg
         */
        View bg = findViewById(R.id.bg);
        ((TextView) bg.findViewById(R.id.name)).setText("Background");
        etBg = bg.findViewById(R.id.detail);
        etBg.addTextChangedListener(new MyTextWatcher(MyTextWatcher.BACKGROUND));
        bgColor = bg.findViewById(R.id.color);
        /*
         * padding-L
         */
        View paddingL = findViewById(R.id.pl);
        ((TextView) paddingL.findViewById(R.id.name)).setText("padding-L");
        etPaddingL = paddingL.findViewById(R.id.detail);
        paddingL.findViewById(R.id.add).setOnClickListener(v -> etPaddingL.setText((Integer.valueOf(etPaddingL.getText().toString()) + 1) + ""));
        paddingL.findViewById(R.id.minus).setOnClickListener(v -> etPaddingL.setText((Integer.valueOf(etPaddingL.getText().toString()) - 1) + ""));
        etPaddingL.addTextChangedListener(new MyTextWatcher(MyTextWatcher.PADDINGL));


        /*
         * padding-T
         */
        View paddingT = findViewById(R.id.pt);
        ((TextView) paddingT.findViewById(R.id.name)).setText("padding-T");
        etPaddingT = paddingT.findViewById(R.id.detail);
        paddingT.findViewById(R.id.add).setOnClickListener(v -> etPaddingT.setText((Integer.valueOf(etPaddingT.getText().toString()) + 1) + ""));
        paddingT.findViewById(R.id.minus).setOnClickListener(v -> etPaddingT.setText((Integer.valueOf(etPaddingT.getText().toString()) - 1) + ""));
        etPaddingT.addTextChangedListener(new MyTextWatcher(MyTextWatcher.PADDINFT));


        /*
         * padding-R
         */
        View paddingR = findViewById(R.id.pr);
        ((TextView) paddingR.findViewById(R.id.name)).setText("padding-R");
        etPaddingR = paddingR.findViewById(R.id.detail);
        paddingR.findViewById(R.id.add).setOnClickListener(v -> etPaddingR.setText((Integer.valueOf(etPaddingR.getText().toString()) + 1) + ""));
        paddingR.findViewById(R.id.minus).setOnClickListener(v -> etPaddingR.setText((Integer.valueOf(etPaddingR.getText().toString()) - 1) + ""));
        etPaddingR.addTextChangedListener(new MyTextWatcher(MyTextWatcher.PATTINGR));


        /*
         * padding-B
         */
        View paddingB = findViewById(R.id.pb);
        ((TextView) paddingB.findViewById(R.id.name)).setText("padding-B");
        etPaddingB = paddingB.findViewById(R.id.detail);
        paddingB.findViewById(R.id.add).setOnClickListener(v -> etPaddingB.setText((Integer.valueOf(etPaddingB.getText().toString()) + 1) + ""));
        paddingB.findViewById(R.id.minus).setOnClickListener(v -> etPaddingB.setText((Integer.valueOf(etPaddingB.getText().toString()) - 1) + ""));
        etPaddingB.addTextChangedListener(new MyTextWatcher(MyTextWatcher.PADDINGB));


    }


    @SuppressLint("SetTextI18n")
    void show(Element element) {
        this.element = element;
        show();
        Window dialogWindow = getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.START | Gravity.TOP);
        lp.x = element.getRect().left;
        lp.y = element.getRect().bottom;
        lp.width = getScreenWidth() - dip2px(30);
        lp.height = getScreenHeight() / 2;
        dialogWindow.setAttributes(lp);


        tvType.setText(element.getView().getClass().getName());
        tvID.setText(Util.getResId(element.getView()) + ":" + Util.getResourceName(element.getView().getId()));
        etPaddingB.setText(px2dip(element.getPaddingB()) + "");
        etPaddingR.setText(px2dip(element.getPaddingR()) + "");
        etPaddingT.setText(px2dip(element.getPaddingT()) + "");
        etPaddingL.setText(px2dip(element.getPaddingL()) + "");
        etHeight.setText(px2dip(element.getHeight()) + "");
        etWidth.setText(px2dip(element.getWidth()) + "");
        etBg.setText(element.bgColor);
        try {
            if (TextUtils.isEmpty(element.bgColor)) {
                bgColor.setBackgroundDrawable(new BitmapDrawable(null, element.bgBitmap));
            } else {
                bgColor.setBackgroundColor(Color.parseColor(element.bgColor));
            }
        } catch (Exception ignored) {

        }
    }

    void setAttrDialogCallback(AttrDialogCallback callback) {
        this.callback = callback;
    }

    class MyTextWatcher implements TextWatcher {
        int type;
        static final int WIDTH = 1;
        static final int HEIGHT = 2;
        static final int PADDINGL = 3;
        static final int PADDINFT = 4;
        static final int PATTINGR = 5;
        static final int PADDINGB = 6;
        static final int BACKGROUND = 7;


        MyTextWatcher(int tag) {
            this.type = tag;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (type == BACKGROUND) {
                element.setBgColor(s.toString());
                try {
                    bgColor.setBackgroundColor(Color.parseColor(s.toString()));
                } catch (Exception ignored) {

                }
                return;
            }
            int f = dip2px(Float.valueOf(s.toString()));
            if (type == WIDTH) {
                element.setWidth(f);
            } else if (type == HEIGHT)
                element.setHeight(f);
            else if (type == PADDINGL)
                element.setPaddingL(f);
            else if (type == PADDINFT)
                element.setPaddingT(f);
            else if (type == PATTINGR)
                element.setPaddingR(f);
            else if (type == PADDINGB)
                element.setPaddingB(f);
            callback.refresh();
        }

        @Override
        public void afterTextChanged(Editable s) {


        }
    }


}

