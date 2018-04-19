package com.example.mohamed.mymedeciene.presenter.posts;

import com.example.mohamed.mymedeciene.presenter.base.MainPresnter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.view.PostsView;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 31/01/2018.  time :00:36
 */

public interface PostsPresenter<v extends PostsView> extends MainPresnter<v> {
    void addPost(AddListener listener);
    void like(boolean isLike,String postId);
    void sendComment(String comment,String postId);
    
}
