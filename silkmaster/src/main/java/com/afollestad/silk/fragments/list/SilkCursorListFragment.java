package com.afollestad.silk.fragments.list;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import com.afollestad.silk.adapters.SilkCursorAdapter;
import com.afollestad.silk.caching.SilkComparable;
import com.afollestad.silk.caching.SilkCursorItem;

/**
 * A {@link com.afollestad.silk.fragments.base.SilkFragment} that shows a list, with an empty text, and has progress bar support. Has other various
 * convenience methods and handles a lot of things on its own to make things easy.
 * <p/>
 * The fragment uses a {@link com.afollestad.silk.adapters.SilkAdapter} to display items of type ItemType.
 *
 * @param <ItemType> The type of items held in the fragment's list.
 * @author Aidan Follestad (afollestad)
 */
public abstract class SilkCursorListFragment<ItemType extends SilkCursorItem & SilkComparable> extends SilkListFragment<ItemType> implements LoaderManager.LoaderCallbacks<Cursor> {

    protected abstract Uri getLoaderUri();

    protected abstract String getLoaderSelection();

    protected abstract String[] getLoaderProjection();

    protected abstract String getLoaderSort();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onInitialRefresh();
    }

    protected void onInitialRefresh() {
        setLoading(true);
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public final Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), getLoaderUri(), getLoaderProjection(), getLoaderSelection(), null, getLoaderSort());
    }

    @Override
    public final void onLoadFinished(Loader<Cursor> arg0, Cursor data) {
        setLoadComplete(false);
        if (data == null || data.getColumnCount() == 0 || data.getCount() == 0) {
            onCursorEmpty();
            return;
        }
        if (getAdapter() != null) onPostLoadFromCursor(data);
    }

    @Override
    public final void onLoaderReset(Loader<Cursor> arg0) {
        if (getAdapter() != null) {
            ((SilkCursorAdapter) getAdapter()).changeCursor(null);
        }
    }

    @Override
    public SilkCursorAdapter<ItemType> getAdapter() {
        return (SilkCursorAdapter<ItemType>) super.getAdapter();
    }

    protected abstract SilkCursorAdapter<ItemType> initializeAdapter();

    protected void onCursorEmpty() {
        setLoadComplete(false);
    }

    protected void onPostLoadFromCursor(Cursor cursor) {
        ((SilkCursorAdapter) getAdapter()).changeCursor(cursor);
        setLoadComplete(false);
    }

    protected void clearProvider() {
        getActivity().getContentResolver().delete(getLoaderUri(), null, null);
    }
}