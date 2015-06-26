package com.afollestad.cardsui;

import android.content.Context;

/**
 * A header centered horizontally with no action button.
 *
 * @author Aidan Follestad (afollestad)
 */
public class CardCenteredHeader extends Card {

    public CardCenteredHeader(String title) {
        super(title);
    }

    public CardCenteredHeader(Context context, int title) {
        super(context, title);
    }

    @Override
    public int getLayout() {
        return R.layout.list_item_header_centered;
    }

    @Override
    public boolean isClickable() {
        return false;
    }
}
