package com.test.swipelistitemapp.ui.adapter.holder;

import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.test.swipelistitemapp.Constants;
import com.test.swipelistitemapp.R;
import com.test.swipelistitemapp.dom.Post;
import com.test.swipelistitemapp.ui.listener.LeftSwipeTouchListener;
import com.test.swipelistitemapp.ui.listener.OnDeleteButtonListener;
import com.test.swipelistitemapp.ui.listener.OnListItemImageClickListener;
import com.test.swipelistitemapp.ui.listener.OnListItemInteractionListener;
import com.test.swipelistitemapp.ui.view.CustomDraweeView;
import com.test.swipelistitemapp.util.ImageUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Zoran on 09.03.2016.
 */
public class PostViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.row_card)
    View mCardView;
    View mView;
    @Bind(R.id.image_drawee)
    CustomDraweeView mImage;
    @Bind(R.id.image_overlay)
    View mImageOverlay;
    @Bind(R.id.item_id)
    TextView mIdView;
    @Bind(R.id.item_content)
    TextView mContentView;
    @Bind(R.id.button_delete)
    View btnDelete;

    private OnListItemInteractionListener mListener;
    private OnListItemImageClickListener mImageClickListener;
    private LeftSwipeTouchListener mTouchListener;
    private OnDeleteButtonListener mDeleteButtonListener;

    public PostViewHolder(View view,
                          OnListItemInteractionListener listener,
                          OnListItemImageClickListener imageClickListener,
                          LeftSwipeTouchListener touchListener,
                          OnDeleteButtonListener deleteListener) {
        super(view);
        mView = view;
        ButterKnife.bind(this, view);

        mListener = listener;
        mImageClickListener = imageClickListener;
        mTouchListener = touchListener;
        mDeleteButtonListener = deleteListener;
    }

    public void bindView(final Post post) {

        mIdView.setText(post.getId() + ".");
        mContentView.setText(post.getTitle());

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListItemInteraction(post);
                }
            }
        });

        mCardView.setOnTouchListener(mTouchListener);

        mImage.setImageURL(ImageUtils.getImageUrl(post.getId(), Constants.ImageSize.THUMB));

        mImageOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, post.getId() + " clicked", Snackbar.LENGTH_SHORT).show();
                if (mImageClickListener != null) {
                    mImageClickListener.onListItemImageClickListener(mImage, post);
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mView.getTag(LeftSwipeTouchListener.SWIPE_STATE) != null) {
                    mDeleteButtonListener.onDeleteClick(post, mCardView);
                }
            }
        });

        mView.setTag(LeftSwipeTouchListener.SWIPE_STATE, null);
        mTouchListener.removeCancel(mCardView, false);
    }

}
