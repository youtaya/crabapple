package com.afollestad.cardsui;

import android.content.Context;
import android.graphics.Rect;
import android.util.SparseIntArray;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import com.afollestad.silk.adapters.SilkAdapter;

/**
 * A {@link SilkAdapter} that displays {@link Card} and {@link CardHeader} objects in a {@link CardListView}.
 *
 * @author Aidan Follestad (afollestad)
 */
public class CardAdapter<ItemType extends CardBase<ItemType>> extends SilkAdapter<ItemType> {

    private final static int TYPE_REGULAR = 0;
    private final static int TYPE_NO_CONTENT = 1;
    private final static int TYPE_HEADER = 2;
    private final static int DEFAULT_TYPE_COUNT = 3;

    private final static int POPUP_MENU_THEME = android.R.style.Theme_Holo_Light;
    private final SparseIntArray mViewTypes;
    protected int mAccentColor;
    private int mPopupMenu = -1;
    private Card.CardMenuListener<ItemType> mPopupListener;
    private int mLayout = R.layout.list_item_card;
    private int mLayoutNoContent = R.layout.list_item_card_nocontent;
    private final int mLayoutHeader = R.layout.list_item_header;

    /**
     * Initializes a new CardAdapter instance.
     *
     * @param context        The context used to inflate layouts and retrieve resources.
     * @param accentColorRes The color used for header actions and card titles, 0 will specify black.
     */
    public CardAdapter(Context context, int accentColorRes) {
        super(context);
        mAccentColor = context.getResources().getColor(accentColorRes != 0 ? accentColorRes : android.R.color.black);
        mViewTypes = new SparseIntArray();
        registerLayout(R.layout.list_item_card_compressed);
        registerLayout(R.layout.list_item_header_centered);
    }

    public static void setupTouchDelegate(Context context, final View menu) {
        final int offset = context.getResources().getDimensionPixelSize(R.dimen.card_action_touchdelegate);
        assert menu.getParent() != null;
        ((View) menu.getParent()).post(new Runnable() {
            public void run() {
                Rect delegateArea = new Rect();
                menu.getHitRect(delegateArea);
                delegateArea.top -= offset;
                delegateArea.bottom += offset;
                delegateArea.left -= offset;
                delegateArea.right += offset;
                TouchDelegate expandedArea = new TouchDelegate(delegateArea, menu);
                ((View) menu.getParent()).setTouchDelegate(expandedArea);
            }
        });
    }

    @Override
    public final boolean isEnabled(int position) {
        ItemType item = getItem(position);
        if (item.isHeader())
            return item.getActionCallback() != null;
        return item.isClickable();
    }

    /**
     * Sets a popup menu used for every card in the adapter, this will not override individual card popup menus.
     * You <b>should</b> call this method before adding any cards to the adapter to avoid issues.
     *
     * @param menuRes  The menu resource ID to use for the card's popup menu.
     * @param listener A listener invoked when an option in the popup menu is tapped by the user.
     */
    public final CardAdapter<ItemType> setPopupMenu(int menuRes, Card.CardMenuListener<ItemType> listener) {
        mPopupMenu = menuRes;
        mPopupListener = listener;
        return this;
    }

    protected Card.CardMenuListener<ItemType> getMenuListener() {
        return mPopupListener;
    }

    @Override
    public final int getLayout(int index, int type) {
        CardBase card = getItem(index);
        if (type == TYPE_NO_CONTENT)
            return mLayoutNoContent;
        else if (type == TYPE_HEADER)
            return R.layout.list_item_header;
        int layout = card.getLayout();
        if (layout <= 0) {
            // If no layout was specified for the individual card, use the adapter's set layout
            layout = mLayout;
        }
        return layout;
    }

    private void setupHeader(ItemType header, View view) {
        TextView title = (TextView) view.findViewById(android.R.id.title);
        if (title == null)
            throw new RuntimeException("Your header layout must contain a TextView with the ID @android:id/title.");
        TextView subtitle = (TextView) view.findViewById(android.R.id.content);
        if (subtitle == null)
            throw new RuntimeException("Your header layout must contain a TextView with the ID @android:id/content.");
        title.setText(header.getTitle());
        if (header.getContent() != null && !header.getContent().trim().isEmpty()) {
            subtitle.setVisibility(View.VISIBLE);
            subtitle.setText(header.getContent());
        } else subtitle.setVisibility(View.GONE);
        TextView button = (TextView) view.findViewById(android.R.id.button1);
        if (button == null)
            throw new RuntimeException("The header layout must contain a TextView with the ID @android:id/button1.");
        if (header.getActionCallback() != null) {
            button.setVisibility(View.VISIBLE);
            button.setBackgroundColor(mAccentColor);
            String titleTxt = header.getActionTitle();
            if (header.getActionTitle() == null || header.getActionTitle().trim().isEmpty())
                titleTxt = getContext().getString(R.string.see_more);
            button.setText(titleTxt);
        } else button.setVisibility(View.GONE);
    }

    private void invalidatePadding(int index, View view) {
        int top = index == 0 ? R.dimen.card_outer_padding_firstlast : R.dimen.card_outer_padding_top;
        int bottom = index == (getCount() - 1) ? R.dimen.card_outer_padding_firstlast : R.dimen.card_outer_padding_bottom;
        view.setPadding(view.getPaddingLeft(),
                getContext().getResources().getDimensionPixelSize(top),
                view.getPaddingRight(),
                getContext().getResources().getDimensionPixelSize(bottom));
    }

    @Override
    public View onViewCreated(int index, View recycled, ItemType item) {
        if (item.isHeader()) {
            setupHeader(item, recycled);
            return recycled;
        }

        TextView title = (TextView) recycled.findViewById(android.R.id.title);
        if (title != null) onProcessTitle(title, item, mAccentColor);
        TextView content = (TextView) recycled.findViewById(android.R.id.content);
        if (content != null) onProcessContent(content, item);
        ImageView icon = (ImageView) recycled.findViewById(android.R.id.icon);
        if (icon != null) {
            if (onProcessThumbnail(icon, item)) {
                icon.setVisibility(View.VISIBLE);
            } else {
                icon.setVisibility(View.GONE);
            }
        }
        View menu = recycled.findViewById(android.R.id.button1);
        if (menu != null) {
            if (onProcessMenu(menu, item)) {
                menu.setVisibility(View.VISIBLE);
            } else {
                menu.setOnClickListener(null);
                menu.setVisibility(View.INVISIBLE);
            }
        }
        invalidatePadding(index, recycled);
        return recycled;
    }

    @Override
    public final Object getItemId(ItemType item) {
        return item.getSilkId();
    }

    @Override
    public final int getViewTypeCount() {
        return mViewTypes.size() + DEFAULT_TYPE_COUNT;
    }

    /**
     * Registers a custom layout in the adapter, that isn't one of the default layouts, and that was passed in the adapter's constructor.
     * <p/>
     * This must be used if you override getLayout() and specify custom layouts for certain list items.
     */
    public final CardAdapter<ItemType> registerLayout(int layoutRes) {
        if (layoutRes == mLayout || layoutRes == mLayoutNoContent || layoutRes == mLayoutHeader) return this;
        if (mViewTypes.get(layoutRes, 0) != 0) {
            String name = getContext().getResources().getResourceName(layoutRes);
            throw new RuntimeException("The layout " + name + " is already registered!");
        }
        mViewTypes.put(layoutRes, mViewTypes.size() + DEFAULT_TYPE_COUNT);
        return this;
    }

    @Override
    public final int getItemViewType(int position) {
        CardBase item = getItem(position);
        if (item.getLayout() > 0) {
            if (mViewTypes.get(item.getLayout()) != 0)
                return mViewTypes.get(item.getLayout());
            else if (mLayout == item.getLayout())
                return TYPE_REGULAR;
            else if (mLayoutNoContent == item.getLayout())
                return TYPE_NO_CONTENT;
            else if (mLayoutHeader == item.getLayout())
                return TYPE_HEADER;
            String name = getContext().getResources().getResourceName(item.getLayout());
            throw new RuntimeException("The layout " + name + " is not registered.");
        } else {
            if (item.isHeader())
                return TYPE_HEADER;
            else if ((item.getContent() == null || item.getContent().trim().isEmpty()))
                return TYPE_NO_CONTENT;
            else return TYPE_REGULAR;
        }
    }

    protected boolean onProcessTitle(TextView title, ItemType card, int accentColor) {
        if (title == null) return false;
        title.setText(card.getTitle());
        title.setTextColor(accentColor);
        return true;
    }

    protected boolean onProcessThumbnail(ImageView icon, ItemType card) {
        if (icon == null) return false;
        if (card.getThumbnail() == null) return false;
        icon.setImageDrawable(card.getThumbnail());
        return true;
    }

    protected boolean onProcessContent(TextView content, ItemType card) {
        content.setText(card.getContent());
        return false;
    }

    protected boolean onProcessMenu(final View view, final ItemType card) {
        if (card.getPopupMenu() < 0) {
            // Menu for this card is disabled
            return false;
        }
        int menuRes = mPopupMenu;
        if (card.getPopupMenu() != 0) menuRes = card.getPopupMenu();
        if (menuRes < 0) {
            // No menu for the adapter or the card
            return false;
        }
        setupTouchDelegate(getContext(), view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int menuRes = mPopupMenu;
                if (card.getPopupMenu() != 0) menuRes = card.getPopupMenu();
                // Force the holo light theme on every card's popup menu
                Context themedContext = getContext();
                themedContext.setTheme(POPUP_MENU_THEME);
                PopupMenu popup = new PopupMenu(themedContext, view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(menuRes, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (card.getPopupMenu() > 0 && card.getPopupListener() != null) {
                            // This individual card has it unique menu
                            card.getPopupListener().onMenuItemClick(card, item);
                        } else if (mPopupListener != null) {
                            // The card does not have a unique menu, use the adapter's default
                            mPopupListener.onMenuItemClick(card, item);
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });
        return true;
    }
}