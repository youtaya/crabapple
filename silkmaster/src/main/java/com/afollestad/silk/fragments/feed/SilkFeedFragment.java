package com.afollestad.silk.fragments.feed;

import android.os.Bundle;
import com.afollestad.silk.caching.SilkComparable;
import com.afollestad.silk.fragments.list.SilkListFragment;

import java.util.List;

/**
 * @author Aidan Follestad (afollestad)
 */
public abstract class SilkFeedFragment<ItemType extends SilkComparable> extends SilkListFragment<ItemType> {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onInitialRefresh();
    }

    protected void onPreLoad(boolean isPaginating) {
    }

    protected void onPostLoad(List<ItemType> results, boolean paginated) {
        if(paginated) {
            getAdapter().add(results);
        } else {
            getAdapter().set(results);
        }
        setLoadComplete(false);
    }

    protected abstract List<ItemType> refresh(boolean isPaginating) throws Exception;

    protected abstract void onError(Exception e);

    public void performRefresh(boolean showProgress) {
        if (isLoading()) return;
        setLoading(showProgress);
        onPreLoad(false);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<ItemType> items = refresh(false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onPostLoad(items, false);
                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onError(e);
                            setLoadComplete(true);
                        }
                    });
                }
            }
        });
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    public void performPaginate() {
        if (isLoading()) return;
        setLoading(false);
        onPreLoad(true);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<ItemType> items = refresh(true);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onPostLoad(items, true);
                        }
                    });
                } catch (final Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            onError(e);
                            setLoadComplete(true);
                        }
                    });
                }
            }
        });
        t.setPriority(Thread.MAX_PRIORITY);
        t.start();
    }

    protected void onInitialRefresh() {
        performRefresh(true);
    }
}