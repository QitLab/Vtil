package com.qit.vtil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static com.qit.vtil.DimenUtil.dip2px;

/**
 * author: Qit .
 * date:   On 2019/5/16
 */
public class EditorLayout extends View {
    private EditorLayoutTool layoutTool;
    private List<Element> elements = new ArrayList<>();
    private Element selElement;

    public EditorLayout(Context context) {
        super(context);
        layoutTool = new EditorLayoutTool();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        final Activity targetActivity = Vtil.getCurrentActivity();
        fillElementsTree(targetActivity.getWindow().getDecorView());
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        targetElement = null;
        if (dialog != null) {
            dialog.dismiss();
        }
    }


    private void fillElementsTree(View view) {
        if (view.getAlpha() == 0 || view.getVisibility() != View.VISIBLE) return;
        if (view instanceof ViewGroup) {
            elements.add(0, new Element(view));
            ViewGroup parent = (ViewGroup) view;
            for (int i = 0; i < parent.getChildCount(); i++) {
                fillElementsTree(parent.getChildAt(i));
            }
        } else {
            elements.add(new Element(view));
        }
    }

    private Element getTargetElement(float x, float y) {
        Element target = null;
        for (int i = elements.size() - 1; i >= 0; i--) {
            final Element element = elements.get(i);
            if (element.getRect().contains((int) x, (int) y)) {
                if (isParentNotVisible(element.getParentElement())) {
                    continue;
                }
                if (selElement != element) {
                    selElement = element;
                    target = selElement;
                    break;
                } else {
                    target = selElement.getParentElement() == null ? selElement : selElement.getParentElement();
                    break;
                }
            }
        }
        if (target == null) {
            Toast.makeText(getContext(), "(" + x + "," + y + ")处无控件", Toast.LENGTH_SHORT).show();
        }
        return target;
    }

    private boolean isParentNotVisible(Element parent) {
        if (parent == null) {
            return false;
        }
        if (parent.getRect().left >= DimenUtil.getScreenWidth()
                || parent.getRect().top >= DimenUtil.getScreenHeight()) {
            return true;
        } else {
            return isParentNotVisible(parent.getParentElement());
        }
    }

    private Element targetElement;
    private AttrsDialog dialog;
    private EditorLayout.IMode mode = new EditorLayout.ShowMode();
    private float lastX, lastY;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (targetElement != null) {
            canvas.drawRect(targetElement.getRect(), layoutTool.areaPaint);
            mode.onDraw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                mode.triggerActionUp(event);
                break;
            case MotionEvent.ACTION_MOVE:
                mode.triggerActionMove(event);
                break;
        }
        return true;
    }


    class MoveMode implements EditorLayout.IMode {

        @Override
        public void onDraw(Canvas canvas) {
            Rect rect = targetElement.getRect();
            canvas.drawRect(rect, layoutTool.dashLinePaint);

            Element parentElement = targetElement.getParentElement();
            if (parentElement != null) {
                Rect parentRect = parentElement.getRect();
                int x = rect.left + rect.width() / 2;
                int y = rect.top + rect.height() / 2;
                layoutTool.drawLineWithText(canvas, rect.left, y, parentRect.left, y, dip2px(2));
                layoutTool.drawLineWithText(canvas, x, rect.top, x, parentRect.top, dip2px(2));
                layoutTool.drawLineWithText(canvas, rect.right, y, parentRect.right, y, dip2px(2));
                layoutTool.drawLineWithText(canvas, x, rect.bottom, x, parentRect.bottom, dip2px(2));
            }
        }

        @Override
        public void triggerActionMove(MotionEvent event) {
            if (targetElement != null) {
                boolean changed = false;
                View view = targetElement.getView();
                float diffX = event.getX() - lastX;
                if (Math.abs(diffX) >= layoutTool.moveUnit) {
                    view.setTranslationX(view.getTranslationX() + diffX);
                    lastX = event.getX();
                    changed = true;
                }
                float diffY = event.getY() - lastY;
                if (Math.abs(diffY) >= layoutTool.moveUnit) {
                    view.setTranslationY(view.getTranslationY() + diffY);
                    lastY = event.getY();
                    changed = true;
                }
                if (changed) {
                    targetElement.initRect();
                    invalidate();
                }
            }
        }

        @Override
        public void triggerActionUp(MotionEvent event) {

        }
    }


    class ShowMode implements EditorLayout.IMode {

        @Override
        public void onDraw(Canvas canvas) {
            Rect rect = targetElement.getRect();
            layoutTool.drawLineWithText(canvas, rect.left, rect.top - layoutTool.lineMargin, rect.right, rect.top - layoutTool.lineMargin);
            layoutTool.drawLineWithText(canvas, rect.right + layoutTool.lineMargin, rect.top, rect.right + layoutTool.lineMargin, rect.bottom);
        }

        @Override
        public void triggerActionMove(MotionEvent event) {
            lastX = event.getX();
            lastY = event.getY();
            final Element element = getTargetElement(event.getX(), event.getY());
            if (element != null) {
                targetElement = element;
                invalidate();
            }
            selElement = null;

        }

        @Override
        public void triggerActionUp(final MotionEvent event) {
            final Element element = getTargetElement(event.getX(), event.getY());
            if (element != null) {
                targetElement = element;
                new PluginSocket().send(targetElement).start();

                invalidate();
                if (dialog == null) {
                    dialog = new AttrsDialog(getContext());
                    dialog.setAttrDialogCallback(new AttrDialogCallback() {
                        @Override
                        public void enableMove() {
                            mode = new EditorLayout.MoveMode();
                            dialog.dismiss();
                        }

                        @Override
                        public void refresh() {
                            if (targetElement != null) {
                                targetElement.initRect();
                                invalidate();
                            }
                        }
                    });
                    dialog.setOnDismissListener(dialog -> {
                        if (targetElement != null) {
                            targetElement.initRect();
                            invalidate();
                        }
                    });
                }
                dialog.show(targetElement);
            }
        }
    }

    public interface IMode {
        void onDraw(Canvas canvas);

        void triggerActionMove(MotionEvent event);

        void triggerActionUp(MotionEvent event);
    }

}
