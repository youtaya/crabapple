package com.afollestad.silk.adapters;

import android.content.Context;
import android.database.Cursor;
import com.afollestad.silk.caching.SilkComparable;
import com.afollestad.silk.caching.SilkCursorItem;

import java.lang.reflect.Method;

/**
 * A CursorAdapter wrapper that makes creating list adapters easier. Contains various convenience methods and handles
 * recycling views on its own.
 *
 * @param <ItemType> The type of items held in the adapter.
 * @author Aidan Follestad (afollestad)
 */
public abstract class SilkCursorAdapter<ItemType extends SilkCursorItem & SilkComparable> extends SilkAdapter<ItemType> implements ScrollStatePersister {

    private final Class<? extends SilkCursorItem> mClass;

    public SilkCursorAdapter(Context context, Class<? extends SilkCursorItem> cls) {
        super(context);
        mClass = cls;
    }

    public static Object performConvert(Cursor cursor, Class<? extends SilkCursorItem> type) {
        try {
            Object o = type.newInstance();
            Method m = type.getDeclaredMethod("convert", Cursor.class);
            return m.invoke(o, cursor);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while invoking convert() of class " + type.getName() + ": " + e.getMessage());
        }
    }

    public final void changeCursor(Cursor cursor) {
        clear();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ItemType item = (ItemType) performConvert(cursor, mClass);
                if (item != null) add(item);
            }
        }
    }
}