package com.example.mohamed.mymedeciene.view;

import com.example.mohamed.mymedeciene.utils.PostActionLisenter;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 31/01/2018.  time :00:34
 */

public interface PostsView extends MainView,PostActionLisenter {
    void showProgress();
    void hideProgress();
    void showPosts();
}
