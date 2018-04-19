package com.example.mohamed.mymedeciene.Holder;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mohamed.mymedeciene.R;
import com.example.mohamed.mymedeciene.appliction.MyApp;
import com.example.mohamed.mymedeciene.data.Comment;
import com.example.mohamed.mymedeciene.data.Pharmacy;
import com.example.mohamed.mymedeciene.data.Post;
import com.example.mohamed.mymedeciene.utils.PostActionLisenter;
import com.example.mohamed.mymedeciene.utils.RxBus;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.like.LikeButton;
import com.like.OnAnimationEndListener;
import com.like.OnLikeListener;

import io.reactivex.disposables.Disposable;

/**
 * Created by Mohamed mabrouk
 * 0201152644726
 * on 31/01/2018.  time :00:50
 */

public class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    private Activity activity;
    public ImageView postIMG;
    public TextView postContent;
    private EditText comment;
    private Post post;
    private Disposable disposable;
    public Button send,commentButton,likeButton;
    private PostActionLisenter mLisenter;
    private boolean isLiked;
    private RecyclerView commentsRecyclerView;
    private DatabaseReference mDatabaseReference;
    private FirebaseRecyclerOptions options;
    private Query query;
    private FirebaseRecyclerAdapter adapter;

    public PostHolder(View itemView, Activity activity,PostActionLisenter mLisenter) {
        super(itemView);
        this.activity=activity;
        this.mLisenter=mLisenter;
        init();
    }

    private void init(){
        postIMG=itemView.findViewById(R.id.img_post);
        postContent=itemView.findViewById(R.id.txt_post);
        comment=itemView.findViewById(R.id.comment_write);
        send=itemView.findViewById(R.id.send_comment);
        likeButton=itemView.findViewById(R.id.like_button);
        commentButton=itemView.findViewById(R.id.comment_button);
        commentsRecyclerView=itemView.findViewById(R.id.comment_rcl_view);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(activity));
        mDatabaseReference= MyApp.getmDatabaseReference().child("Comments");

        send.setOnClickListener(this);
        commentButton.setOnClickListener(this);
        likeButton.setOnClickListener(this);
        comment.setOnClickListener(this);
        likeButton.setEnabled(true);
        postIMG.setOnClickListener(this);

    }

    private void IsLike( Boolean isLiked){
        this.isLiked=isLiked;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                likeButton.setBackgroundResource(isLiked?R.drawable.ic_like:R.drawable.ic_unlike);
                RxBus.getRxBus().unSubscribe(disposable);
            }
        });
    }

    public void bind(Post post){
        this.post=post;
        isLiked=post.isLiked();
        likeButton.setBackgroundResource(isLiked?R.drawable.ic_like:R.drawable.ic_unlike);
        if (!TextUtils.isEmpty(post.getImgUrl())) {
            postIMG.setVisibility(View.VISIBLE);
            Glide.with(activity.getApplicationContext()).load(post.getImgUrl()).into(postIMG);
        }
        postContent.setText(post.getContent());
        query=mDatabaseReference.child(post.getId());
        options=new FirebaseRecyclerOptions.Builder<Comment>().setQuery(query,Comment.class).build();
        setComments();
        adapter.startListening();
    }

    public void comment(Boolean aBoolean){
        activity.runOnUiThread(() -> {
            comment.setText(null);
            RxBus.getRxBus().unSubscribe(disposable);
            adapter.startListening();
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_comment:
                disposable=RxBus.getRxBus().subscribeComment(this::comment);
                mLisenter.onSendComment(comment.getText().toString(),post);
                break;
            case R.id.comment_button:
                mLisenter.onCommentAction();
                break;
            case R.id.like_button:
                disposable=RxBus.getRxBus().subscribeIsLike(this::IsLike);
                isLiked=!isLiked;
                mLisenter.onLike(isLiked,post);
                break;
            case R.id.img_post:
                mLisenter.onIMGClick(postIMG,post.getImgUrl());
                break;
            case R.id.comment_write:
                mLisenter.onClickCommentWrite();
                break;
        }
    }

    private void setComments(){
        adapter= new FirebaseRecyclerAdapter<Comment, CommentHolder>(options) {
            @Override
            public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(activity).inflate(R.layout.comment_item_view,parent,false);
                return new CommentHolder(view,activity);
            }

            @Override
            protected void onBindViewHolder(@NonNull CommentHolder holder, int position, @NonNull Comment model) {
                 MyApp.getmDatabaseReference().child("Pharmacy").child(model.getUserId()).addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         Pharmacy pharmacy=dataSnapshot.getValue(Pharmacy.class);
                         model.setPharmacy(pharmacy);
                         holder.bind(model);
                     }

                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 });
            }
        };
        commentsRecyclerView.setAdapter(adapter);
    }
}
