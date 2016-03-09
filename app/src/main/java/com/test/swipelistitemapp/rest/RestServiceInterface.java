package com.test.swipelistitemapp.rest;

import com.test.swipelistitemapp.dom.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Zoran on 09.03.2016.
 */
public interface RestServiceInterface {

    @GET("posts")
    Call<List<Post>> getPostsFromUser(@Query("userId") int userId);

    @GET("posts/{postId}")
    Call<Post> getPostById(@Path("postId") String postId);

}
