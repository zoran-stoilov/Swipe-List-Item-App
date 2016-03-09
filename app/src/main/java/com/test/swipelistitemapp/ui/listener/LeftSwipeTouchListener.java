package com.test.swipelistitemapp.ui.listener;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import com.test.swipelistitemapp.R;
import com.test.swipelistitemapp.util.ScreenUtils;

/**
 * Created by Zoran on 09.03.2016.
 */
public abstract class LeftSwipeTouchListener implements View.OnTouchListener {

    public static final int SWIPE_STATE = R.id.item_swipe_state;
    public static final String SWIPE_OPEN = "open";
    private static final String TAG = LeftSwipeTouchListener.class.getName();
    private final int STOP_POSITION_MARGIN = 78;
    private final int SWIPE_OFFSET = 10;
    private boolean isSwiping = false;
    private boolean animationInProgress = false;
    private boolean mItemPressed = false;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private View mViewContainer;
    private float mDownX;
    private int mSwipeSlop = 0;
    private float totalDeltaX = 0f;

    public LeftSwipeTouchListener(Context context, RecyclerView recyclerView) {
        this.mContext = context;
        this.mRecyclerView = recyclerView;
    }

    @Override
    public boolean onTouch(final View view, MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN && mViewContainer == null) {
            return false;
        }
        if (animationInProgress) {
            return false;
        }
        if (mSwipeSlop < 0) {
            mSwipeSlop = ViewConfiguration.get(mContext).getScaledTouchSlop();
        }
        if (Build.VERSION.SDK_INT >= 21) {
            float x = event.getX() + view.getLeft();
            float y = event.getY() + view.getTop();
            view.drawableHotspotChanged(x, y);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mItemPressed) {
                    // Multi-item swipes not handled
                    return false;
                }
                mItemPressed = true;
                view.setPressed(true);
                mViewContainer = view;
                onSwipeStart((View) mViewContainer.getParent());
                mDownX = event.getRawX() - mViewContainer.getX();
                totalDeltaX = 0f;
                break;
            case MotionEvent.ACTION_CANCEL:
                view.setPressed(false);
                actionUp(view, event);
                break;
            case MotionEvent.ACTION_MOVE: {
                view.setPressed(false);
                float x = event.getRawX();
                float deltaX = x - mDownX;
                totalDeltaX = deltaX;
                float deltaXAbs = Math.abs(deltaX);
                if (!isSwiping) {
                    if (deltaXAbs > mSwipeSlop) {
                        isSwiping = true;
                    }
                }
                if (isSwiping) {
                    if (totalDeltaX > 0) {
                        totalDeltaX = 0f;
                    }
                    mViewContainer.setX(totalDeltaX);
                }
            }
            break;
            case MotionEvent.ACTION_UP: {
                Log.d(TAG, "ACTION_UP");
                view.setPressed(false);
                actionUp(view, event);
            }

            break;
            default:
                return true;
        }
        return true;
    }

    protected abstract void onSwipeStart(View view);

    private void actionUp(View view, MotionEvent event) {
        if (isSwiping) {
            float x = event.getRawX();
            float deltaX = x - mDownX;
            float deltaXAbs = Math.abs(deltaX);
            final boolean remove;
            if (totalDeltaX < 0
                    && deltaXAbs > ScreenUtils.convertDpToPx(mContext, STOP_POSITION_MARGIN + SWIPE_OFFSET)) {
                // Greater than min swipe activation width - animate it out
                remove = true;
            } else {
                // Not far enough - animate it back
                remove = false;
            }

            if (remove) {
                animateRemoval(view);
                mItemPressed = false;
                return;
            }
            ObjectAnimator animX = ObjectAnimator.ofFloat(mViewContainer, "x", 0);
            animX.setDuration(300);
            animX.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    animationInProgress = true;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mViewContainer.setX(0);
                    isSwiping = false;
                    animationInProgress = false;
                    mRecyclerView.setEnabled(true);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animX.start();
        }
        mItemPressed = false;
    }

    private void animateRemoval(final View viewToRemove) {
        try {
            final int positionX = -ScreenUtils.convertDpToPx(mContext, STOP_POSITION_MARGIN);
            ObjectAnimator animX = ObjectAnimator.ofFloat(mViewContainer, "x", positionX);
            animX.setDuration(300);
            animX.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                    animationInProgress = true;
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    mViewContainer.setX(positionX);
                    isSwiping = false;
                    if (mRecyclerView != null && viewToRemove != null) {
                        try {
                            int position = mRecyclerView.getLayoutManager().getPosition((View) viewToRemove.getParent());
                            removeView(position, viewToRemove);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    animationInProgress = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animX.start();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    protected abstract void removeView(int position, View viewToRemove);

    public void removeSuccess(View viewToRemove) {
        mRecyclerView.setEnabled(true);
        animationInProgress = false;
    }

    public void removeCancel(final View viewToRemove, boolean animate) {
        Log.d(TAG, "removeCancel " + viewToRemove.getX());
        final int positionX = 0;
        if (!animate) {
            viewToRemove.setX(positionX);
            return;
        }
        ObjectAnimator animX = ObjectAnimator.ofFloat(viewToRemove, "x", positionX);
        animX.setDuration(300);
        animX.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                isSwiping = false;
                mRecyclerView.setEnabled(true);
                animationInProgress = false;
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                viewToRemove.setX(positionX);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animX.start();
    }

}
