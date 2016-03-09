package com.test.swipelistitemapp.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.test.swipelistitemapp.ui.listener.OnListItemInteractionListener;

/**
 * Created by Zoran on 09.03.2016.
 */
public abstract class RecyclerViewFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        OnListItemInteractionListener {

    protected static String TAG = RecyclerViewFragment.class.getCanonicalName();
    protected SwipeRefreshLayout refreshLayout;
    protected RecyclerView mRecyclerView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecyclerViewFragment() {
    }

    protected RecyclerViewFragment(String tag) {
        TAG = tag;
    }

    @Override
    public void onRefresh() {
        getData(1, true);
    }


    protected void onRefreshStart() {
        if (refreshLayout != null) {
            refreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(true);
                }
            });
        }
    }

    protected void onRefreshFinish() {
        if (refreshLayout != null) {
            refreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    refreshLayout.setRefreshing(false);
                }
            });
        }
    }

    protected abstract void getData(int page, boolean refresh);

    @Override
    public void onListItemInteraction(Object item) {
        Log.d(TAG, "onListItemInteraction");
    }

}
