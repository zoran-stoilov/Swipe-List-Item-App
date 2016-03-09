package com.test.swipelistitemapp.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by Zoran on 09.03.2016.
 */
public class ScreenUtils {

    public static int convertDpToPx(Context context, float dp) {
        Resources resources = context.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

}
