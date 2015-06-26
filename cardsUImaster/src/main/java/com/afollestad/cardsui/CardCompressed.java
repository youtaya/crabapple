package com.afollestad.cardsui;

import android.content.Context;

/**
 * A vertically compressed Card with no thumbnail. The title is displayed on the left, the content on the right.
 *
 * @author Aidan Follestad (afollestad)
 */
public class CardCompressed extends Card {

    public CardCompressed(String title, String content) {
        super(title, content);
    }

    public CardCompressed(Context context, int title, int subtitle) {
        super(context, title, subtitle);
    }

    public CardCompressed(Context context, int title, String subtitle) {
        super(context, title, subtitle);
    }

    public CardCompressed(Context context, String title, int subtitle) {
        super(context, title, subtitle);
    }

    @Override
    public int getLayout() {
        return R.layout.list_item_card_compressed;
    }
}
