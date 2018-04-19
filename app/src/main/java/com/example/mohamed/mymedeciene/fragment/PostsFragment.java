package com.example.mohamed.mymedeciene.fragment;

import android.animation.Animator;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamed.mymedeciene.Holder.PostHolder;
import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.appliction.DataManager;
import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.data.Drug;
import com.example.mohamed.mymedeciene.data.Pharmacy;
import com.example.mohamed.mymedeciene.data.Post;
import com.example.mohamed.mymedeciene.presenter.myDrugs.DrugsViewPresenter;
import com.example.mohamed.mymedeciene.presenter.posts.PostsViewPresenter;
import com.example.mohamed.mymedeciene.utils.AddListener;
import com.example.mohamed.mymedeciene.utils.ZoomIMG;
import com.example.mohamed.mymedeciene.view.PostsView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 31/01/2018.  time :00:40
 */

public class PostsFragment extends Fragment implements PostsView,View.OnClickListener{
    private FloatingActionButton addButton;
    private FloatingActionsMenu actionMenu;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View view;
    private FirebaseRecyclerAdapter adapter;
    private DatabaseReference mDatabaseReference;
    private Query query;
    private PostsViewPresenter presenter;
    private FirebaseAuth mAuth;
    private FirebaseRecyclerOptions options;
    private DataManager dataManager;
    private FrameLayout drugContainer;
    private ImageView drug_preview;
    private Animator mCurrentAnimator;
    private int mShortAnimationDuration;
    private ZoomIMG zoomIMG;
    private TextView errorView;
    private PostHolder mPostHolder;

    public static PostsFragment newFragment(){
        return new PostsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.my_drugs_list, container, false);
        init();
        iniRecyl();
        iniSwipe();
        showPosts();
        return view;
    }

    private void init() {
        //noinspection ConstantConditions
        dataManager = ((MyApp) getActivity().getApplication()).getData();
        presenter = new PostsViewPresenter(getActivity());
        presenter.attachView(this);
        errorView = view.findViewById(R.id.errors);
        drug_preview = view.findViewById(R.id.drug_preview);
        drugContainer = view.findViewById(R.id.drug_container);
        actionMenu = view.findViewById(R.id.menu);
        mAuth = MyApp.getmAuth();
        mDatabaseReference = MyApp.getmDatabaseReference();
        //noinspection ConstantConditions
        query = mDatabaseReference.child("Posts").child("AllPosts");
        options = new FirebaseRecyclerOptions.Builder<Post>().setQuery(query, Post.class).build();
        addButton = new FloatingActionButton(getActivity());
        addButton.setTitle("Add new Post");
        addButton.setColorDisabled(R.color.white_pressed);
        addButton.setIcon(R.drawable.ic_add);
        addButton.setSize(FloatingActionButton.SIZE_MINI);
        addButton.setColorDisabled(R.color.white_pressed);
        actionMenu.addButton(addButton);
        addButton.setOnClickListener(this);
        zoomIMG = new ZoomIMG();
        errorView.setText("No Posts");
    }


    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    private void iniRecyl() {
        mRecyclerView = view.findViewById(R.id.my_drugs_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void iniSwipe() {
        mSwipeRefreshLayout = view.findViewById(R.id.my_drugs_stl);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                showPosts();
                adapter.startListening();
            }
        });
    }



    @Override
    public void showProgress() {
        if (!mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
        }
    }

    @Override
    public void hideProgress() {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showPosts() {
        showProgress();
        errorView.setVisibility(View.VISIBLE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgress();
            }
        }, 1000L);

        adapter=new FirebaseRecyclerAdapter<Post,PostHolder>(options) {
            @Override
            public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(getActivity()).inflate(R.layout.post_item,parent,false);
                return new PostHolder(view,getActivity(),PostsFragment.this);
            }

            @Override
            protected void onBindViewHolder(@NonNull final PostHolder holder, int position, @NonNull final Post model) {
                errorView.setVisibility(View.GONE);
                mPostHolder=holder;
                MyApp.getmDatabaseReference().child("Pharmacy").child(model.getUserId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot mDataSnapshot) {
                        MyApp.getmDatabaseReference().child("Likes").child(model.getId()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (mAuth.getUid()!=null) {
                                    model.setLiked(dataSnapshot.hasChild(mAuth.getUid()));
                                }
                                holder.bind(model,mDataSnapshot.getValue(Pharmacy.class));
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
             //  holder.bind(model);


            }
        };

        mRecyclerView.setAdapter(adapter);
    }



    @Override
    public void onClick(final View view) {
        presenter.addPost(new AddListener() {
            @Override
            public void onSuccess(String success) {
                presenter.showSnakBar(view, getString(R.string.drug_added));
            }

            @Override
            public void OnError(String error) {
                presenter.showSnakBar(view, error);

            }
        });
        actionMenu.collapse();
    }


    @Override
    public void onLike(boolean isLike,Post post) {
        if (mAuth.getUid() == null) {
             presenter.ShowRegisterDao();
        } else {
            presenter.like(isLike, post.getId());
        }
    }



    @Override
    public void onCommentAction() {
        Toast.makeText(getContext(), "comment", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSendComment(String comment,Post post) {
        if (mAuth.getUid() == null) {
           presenter.ShowRegisterDao();
        } else {
            presenter.sendComment(comment, post.getId());
        }
    }

    @Override
    public void onIMGClick(ImageView imageView, String url) {
        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);
        zoomIMG.zoomImageFromThumb(getActivity(), imageView, url, mCurrentAnimator, drug_preview, drugContainer
                , mShortAnimationDuration
        );
    }

    @Override
    public void onClickCommentWrite() {
        checkSoftKeyBoard();
    }

    private void checkSoftKeyBoard(){
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Rect r = new Rect();
                view.getWindowVisibleDisplayFrame(r);
                int screenHeight = view.getRootView().getHeight();

                // r.bottom is the position above soft keypad or device button.
                // if keypad is shown, the r.bottom is smaller than that before.
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    // keyboard is opened
                    actionMenu.setVisibility(View.GONE);
                }
                else {
                    // keyboard is closed
                    actionMenu.setVisibility(View.VISIBLE);
                }
            }
        });
    }


}
