package com.test.swipelistitemapp.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.swipelistitemapp.R;
import com.test.swipelistitemapp.dom.Post;
import com.test.swipelistitemapp.rest.RestService;
import com.test.swipelistitemapp.ui.adapter.PostAdapter;
import com.test.swipelistitemapp.ui.listener.LeftSwipeTouchListener;
import com.test.swipelistitemapp.ui.listener.OnDeleteButtonListener;
import com.test.swipelistitemapp.ui.listener.OnListItemImageClickListener;
import com.test.swipelistitemapp.ui.view.popup.YesNoPopup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Zoran on 09.03.2016.
 */
public class PostListFragment extends RecyclerViewFragment implements OnDeleteButtonListener {

    private static final String ARG_COLUMN_COUNT = "column_count";
    private static final String TAG = PostListFragment.class.getCanonicalName();
    private static final String ITEMS = "items";
    private int mColumnCount = 1;
    private List<Post> posts = new ArrayList<>();
    private PostAdapter adapter;
    private View rootView;
    private OnListItemImageClickListener mImageListener;
    private LeftSwipeTouchListener mLeftSwipeTouchListener;

    public PostListFragment() {
    }

    @SuppressLint("ValidFragment")
    private PostListFragment(String tag) {
        super(tag);
    }

    public static PostListFragment newInstance(int columnCount) {
        PostListFragment fragment = new PostListFragment(PostListFragment.class.getCanonicalName());
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Log.d(TAG, "savedInstanceState != null");
            if (savedInstanceState.getParcelableArrayList(ITEMS) != null) {
                posts = savedInstanceState.getParcelableArrayList(ITEMS);
            }
        }
        rootView = inflater.inflate(R.layout.list_posts, container, false);

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        refreshLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.list);
        Context context = rootView.getContext();
        if (mColumnCount <= 1) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        // Set the adapter
        if (posts == null) {
            posts = new ArrayList<>();
        }
        initTouchListener();
        adapter = new PostAdapter(posts, this, mImageListener,
                mLeftSwipeTouchListener, this);
        mRecyclerView.setAdapter(adapter);

        if (posts.isEmpty()) {
            getData(new Random().nextInt(10) + 1, true);
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ITEMS, (ArrayList<? extends Parcelable>) posts);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListItemImageClickListener) {
            mImageListener = (OnListItemImageClickListener) context;
        }
    }

    protected void getData(int userId, final boolean clearData) {
        Log.d(TAG, "getData");
        onRefreshStart();
        Call<List<Post>> call = RestService.getInstance().getPostsFromUser(userId);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                Log.d(TAG, response.code() + "; " + response.message());
                if (response.isSuccess()) {
                    if (clearData) {
                        posts.clear();
                    }
                    for (Post post : response.body()) {
                        Log.d(TAG, post.toString());
                        posts.add(post);
                    }
                    adapter.notifyDataSetChanged();
                }
                onRefreshFinish();
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                onRefreshFinish();
            }
        });
    }

    private void initTouchListener() {
        mLeftSwipeTouchListener = new LeftSwipeTouchListener(getActivity(), mRecyclerView) {
            @Override
            protected void onSwipeStart(View view) {
                closeOpenedView(this, view);
            }

            @Override
            protected void removeView(int position, View viewToRemove) {
                View view = mRecyclerView.getLayoutManager().findViewByPosition(position);
                if (view != null) {
                    view.setTag(LeftSwipeTouchListener.SWIPE_STATE, LeftSwipeTouchListener.SWIPE_OPEN);
                }
            }
        };
    }

    private void closeOpenedView(LeftSwipeTouchListener touchListener, View v) {
        Log.d(TAG, "closeOpenedView position: " + mRecyclerView.getLayoutManager().getPosition(v));
        Log.d(TAG, "closeOpenedView getChildCount() = " + mRecyclerView.getChildCount());
        for (int i = 0; i < mRecyclerView.getChildCount(); i++) {
            View view = mRecyclerView.getChildAt(i);
            if (view.getTag(LeftSwipeTouchListener.SWIPE_STATE) != null
                    && view.getTag(LeftSwipeTouchListener.SWIPE_STATE).equals(LeftSwipeTouchListener.SWIPE_OPEN)) {
                Log.d(TAG, "closeOpenedView i=" + i);
                if (mRecyclerView.getChildAdapterPosition(view) != mRecyclerView.getChildAdapterPosition(v)) {
                    view.setTag(LeftSwipeTouchListener.SWIPE_STATE, null);
                    touchListener.removeCancel(view.findViewById(R.id.row_card), true);
                }
            }
        }
    }

    @Override
    public void onDeleteClick(Post item, View cardView) {
        showDeleteCommentPopup(item, cardView);
    }

    private void showDeleteCommentPopup(final Post post, final View viewToRemove) {
        YesNoPopup pop = new YesNoPopup().newInstance(
                getString(R.string.title_popup_delete_post),
                getString(R.string.msg_delete_post),
                getString(R.string.action_yes),
                getString(R.string.action_no),
                new YesNoPopup.YesNoCallback() {

                    @Override
                    public void action() {
                        Log.d(TAG, "Yes Button");
                        int position = posts.indexOf(post);
                        posts.remove(post);
                        adapter.notifyItemRemoved(position);
                        mLeftSwipeTouchListener.removeSuccess(viewToRemove);
                    }

                    @Override
                    public void cancel() {
                        Log.d(TAG, "No Button");
                        mLeftSwipeTouchListener.removeCancel(viewToRemove, true);
                    }
                });

        pop.show(getFragmentManager(), null);
    }

}
