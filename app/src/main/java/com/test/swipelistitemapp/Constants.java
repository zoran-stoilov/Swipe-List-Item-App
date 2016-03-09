package com.test.swipelistitemapp;

/**
 * Created by Zoran on 09.03.2016.
 */
public class Constants {

    public static final String API_BASE_URL = "http://jsonplaceholder.typicode.com/";
    public static final String IMAGE_BASE_URL = "http://lorempixel.com/";
    public static final String IMAGE_SIZE_THUMB = "80/80/";
    public static final String IMAGE_SIZE_NORMAL = "400/400/";

    public enum ImageSize {
        THUMB,
        NORMAL
    }

    public enum Category {

        ABSTRACT,
        ANIMALS,
        //        BUSINESS,
        //        CATS,
        CITY,
        FOOD,
        NIGHTLIFE,
        FASHION,
        //        PEOPLE,
        NATURE,
        SPORTS,
        TECHNICS,
        TRANSPORT;

        public String label() {
            return name().toLowerCase();
        }
    }

}
