package com.example.mohamed.mymedeciene.utils;

import android.widget.ImageView;

import com.example.mohamed.mymedeciene.data.Post;
import com.like.LikeButton;

/**
 * Created by Mohammad mabrouk
 * 0201152644726
 * on 4/10/2018.  time :22:00
 */
public interface PostActionLisenter {
    void onLike(boolean isLike, Post post);
    void onCommentAction();
    void onSendComment(String comment,Post post);
    void onIMGClick(ImageView imageView,String url);
    void onClickCommentWrite();
}
