package com.test.swipelistitemapp.util;

import android.util.Log;

import com.test.swipelistitemapp.Constants;

/**
 * Created by Zoran on 09.03.2016.
 */
public class ImageUtils {

    private static final String TAG = ImageUtils.class.getCanonicalName();

    public static String getImageUrl(int postId, Constants.ImageSize size) {
        String imageSize;
        switch (size) {
            case NORMAL:
                imageSize = Constants.IMAGE_SIZE_NORMAL;
                break;
            case THUMB:
                imageSize = Constants.IMAGE_SIZE_THUMB;
                break;
            default:
                imageSize = Constants.IMAGE_SIZE_NORMAL;
                break;
        }
        String imageUrl = Constants.IMAGE_BASE_URL +
                imageSize +
                Constants.Category.values()[(postId - 1) / 10].label() +
                "/" + ((postId - 1) % 10 + 1);

        Log.d(TAG, "imageUrl=" + imageUrl);
        return imageUrl;
    }

}
