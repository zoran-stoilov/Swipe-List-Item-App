package com.test.swipelistitemapp.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.swipelistitemapp.R;
import com.test.swipelistitemapp.dom.Post;
import com.test.swipelistitemapp.ui.adapter.holder.PostViewHolder;
import com.test.swipelistitemapp.ui.listener.LeftSwipeTouchListener;
import com.test.swipelistitemapp.ui.listener.OnDeleteButtonListener;
import com.test.swipelistitemapp.ui.listener.OnListItemImageClickListener;
import com.test.swipelistitemapp.ui.listener.OnListItemInteractionListener;

import java.util.List;

/**
 * Created by Zoran on 09.03.2016.
 */
public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {

    private final List<Post> mValues;
    private final OnListItemInteractionListener mListener;
    private final OnListItemImageClickListener mImageClickListener;
    private LeftSwipeTouchListener mTouchListener;
    private OnDeleteButtonListener mDeleteListener;

    public PostAdapter(List<Post> items,
                       OnListItemInteractionListener listener,
                       OnListItemImageClickListener imageClickListener,
                       LeftSwipeTouchListener touchListener,
                       OnDeleteButtonListener deleteListener) {
        mValues = items;
        mListener = listener;
        mImageClickListener = imageClickListener;
        mTouchListener = touchListener;
        mDeleteListener = deleteListener;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_post, parent, false);
        return new PostViewHolder(view, mListener, mImageClickListener, mTouchListener, mDeleteListener);
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.bindView(mValues.get(position));
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

}
