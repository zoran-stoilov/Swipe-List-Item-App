package com.test.swipelistitemapp.ui.view;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.test.swipelistitemapp.R;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Zoran on 09.03.2016.
 */
public class CustomDraweeView extends SimpleDraweeView {

    private final static String TAG = CustomDraweeView.class.getCanonicalName();
    private GenericDraweeHierarchyBuilder builder = new GenericDraweeHierarchyBuilder(getResources());
    private GenericDraweeHierarchy hierarchy = builder
            .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
            .setFadeDuration(300)
            .build();

    public CustomDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        applyCustomSettings();
    }

    public CustomDraweeView(Context context) {
        super(context);
        applyCustomSettings();
    }

    public CustomDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomSettings();
    }

    public CustomDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        applyCustomSettings();
    }

    private void applyCustomSettings() {
        setHierarchy(hierarchy);
    }

    /**
     * Displays an image given by the URL as String.
     *
     * @param urlString The URL String of the requested image
     */
    public void setImageURL(String urlString) {
        setImageURL(urlString, true);
    }

    /**
     * Displays an image given by the URL.
     *
     * @param urlString       The URL String of the requested image
     * @param withPlaceholder Show placeholder or not
     */
    public void setImageURL(String urlString, boolean withPlaceholder) {
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }
        setImageURL(url, withPlaceholder);
    }

    /**
     * Displays an image given by the URL.
     *
     * @param url             The URL of the requested image
     * @param withPlaceholder Show placeholder or not
     */
    public void setImageURL(URL url, boolean withPlaceholder) {
        if (url != null) {
            setImageControllerFromURI(Uri.parse(url.toString()), withPlaceholder);
        }
    }

    /**
     * Displays an image given by the uri.
     *
     * @param uri             The Uri of the requested image
     * @param withPlaceholder Show placeholder or not
     */
    private void setImageControllerFromURI(Uri uri, boolean withPlaceholder) {
        Log.d(TAG, "setImageControllerFromURI: " + uri.toString());
        if (withPlaceholder) {
            getHierarchy().setPlaceholderImage(ContextCompat.getDrawable(getContext(), R.mipmap.ic_launcher));
        }
        super.setImageURI(uri);
    }

}
