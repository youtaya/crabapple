package com.afollestad.silk.views;

import android.app.Activity;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.silk.R;

/**
 * Almost a replica of the stock {@link android.widget.Toast}, but has an OnClickListener that notifies you when the toast is tapped.
 *
 * @author Aidan Follestad (afollestad)
 */
public class ClickableToast {

    public final static int LENGTH_SHORT = 0;
    public final static int LENGTH_LONG = 1;

    private final Handler mHandler;
    private final View mParent;
    private final int mDuration;
    private final View mView;
    private View.OnClickListener mListener;
    private PopupWindow mWindow;
    private int mGravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
    private int mVerticalOffset;

    private ClickableToast(Activity context, View parent, int duration) {
        mHandler = new Handler();
        mParent = parent;
        mDuration = duration;
        mView = context.getLayoutInflater().inflate(R.layout.clickable_toast, null);
        mVerticalOffset = context.getResources().getDimensionPixelSize(R.dimen.toast_offset);

        assert mView != null;
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
                if (mListener != null) mListener.onClick(v);
            }
        });
    }

    public ClickableToast setText(int text) {
        ((TextView) mView.findViewById(android.R.id.message)).setText(text);
        return this;
    }

    public ClickableToast setGravity(int gravity) {
        mGravity = gravity;
        return this;
    }

    public ClickableToast setVerticalOffset(int verticalOffset) {
        mVerticalOffset = verticalOffset;
        return this;
    }

    public ClickableToast setOnClickListener(View.OnClickListener listener) {
        mListener = listener;
        return this;
    }

    public static ClickableToast makeText(Activity context, View parent, int text, int duration) {
        return new ClickableToast(context, parent, duration).setText(text);
    }

    public boolean isShowing() {
        return mWindow != null && mWindow.isShowing();
    }

    public void show() {
        mWindow = new PopupWindow(mView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mWindow.setAnimationStyle(R.style.ClickableToastAnimation);
        mWindow.showAtLocation(mParent, mGravity, 0, mVerticalOffset);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cancel();
            }
        }, mDuration == Toast.LENGTH_LONG ? 3000 : 1500);
    }

    public void cancel() {
        if (mWindow == null) return;
        mWindow.dismiss();
    }
}