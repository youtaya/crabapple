package com.afollestad.silk.views.list;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.afollestad.silk.adapters.ScrollStatePersister;
import com.afollestad.silk.adapters.SilkAdapter;
import com.afollestad.silk.adapters.SilkCursorAdapter;

/**
 * A {@link ListView} that connects to a {@link SilkAdapter} and notifies the adapter of its scroll state.
 * <p/>
 * When the ListView becomes idle (is no longer being scrolled or flinged), it notifies the adapter causing it to update.
 * <p/>
 * You can use getScrollState() from within a {@link SilkAdapter} to only load images when the ListView isn't scrolling.
 *
 * @author Aidan Follestad (afollestad)
 */
public class SilkListView extends ListView {

    private int lastState;
    private OnSilkScrollListener mScrollListener;

    public SilkListView(Context context) {
        super(context);
        init();
    }

    public SilkListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SilkListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (lastState == scrollState) return;
                lastState = scrollState;
                if (getAdapter() instanceof ScrollStatePersister) {
                    ((ScrollStatePersister) getAdapter()).setScrollState(scrollState);
                    if (scrollState == SCROLL_STATE_IDLE && getAdapter() instanceof BaseAdapter) {
                        // When the list is idle, notify the adapter to update (causing images to load)
                        ((BaseAdapter) getAdapter()).notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mScrollListener != null && visibleItemCount < totalItemCount) {
                    if (firstVisibleItem == 0)
                        mScrollListener.onScrollToTop();
                    else if (firstVisibleItem == (totalItemCount - visibleItemCount))
                        mScrollListener.onScrollToBottom();
                }
            }
        });
    }

    /**
     * @deprecated Use {@link #setAdapter(com.afollestad.silk.adapters.SilkAdapter)} instead.
     */
    @Override
    public void setAdapter(ListAdapter adapter) {
        if (adapter instanceof SilkAdapter) {
            setAdapter((SilkAdapter) adapter);
            return;
        } else if (adapter instanceof SilkCursorAdapter) {
            setAdapter((SilkCursorAdapter) adapter);
            return;
        }
        throw new RuntimeException("The SilkListView only accepts SilkAdapters.");
    }

    /**
     * Sets the list's adapter, enforces the use of only a SilkAdapter.
     */
    public void setAdapter(SilkAdapter adapter) {
        super.setAdapter(adapter);
    }

    /**
     * Sets the list's adapter, enforces the use of only a SilkCursorAdapter.
     */
    public void setAdapter(SilkCursorAdapter adapter) {
        super.setAdapter(adapter);
    }

    public void setOnSilkScrollListener(OnSilkScrollListener listener) {
        mScrollListener = listener;
    }
}