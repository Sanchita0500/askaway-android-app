package com.example.askaway.Functionalities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.askaway.HomeActivity;
import com.example.askaway.Objects.Feed;
import com.example.askaway.Objects.Prevalant;
import com.example.askaway.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AnswersFragment extends Fragment {

    private RecyclerView feedView;
    private ProgressDialog mDialog;
    private LinearLayoutManager manager;
    private DatabaseReference mRef;

    public AnswersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRef = FirebaseDatabase.getInstance().getReference().child("Questions");

        mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Please wait..");
        mDialog.setTitle("Loading");
        mDialog.setCanceledOnTouchOutside(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_answers, container, false);

        feedView = view.findViewById(R.id.ans_feed);
        manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, true);
        //manager.setStackFromEnd(true);
        manager.setReverseLayout(true);
        feedView.setLayoutManager(manager);
        feedView.hasFixedSize();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mDialog.show();

        FirebaseRecyclerOptions<Feed> options=new FirebaseRecyclerOptions.Builder<Feed>()
                .setQuery(mRef,Feed.class)
                .build();
        FirebaseRecyclerAdapter<Feed, HomeActivity.FeedViewHolder> adapter=new FirebaseRecyclerAdapter<Feed, HomeActivity.FeedViewHolder>(options) {

            String check_by,check_answer;

            @Override
            protected void onBindViewHolder(@NonNull HomeActivity.FeedViewHolder holder, int position, @NonNull Feed model) {
                check_by=model.getBy();
                check_answer=model.getAnswer();
                holder.setmView(model.getQuestion(), model.getAnswer(), model.getBy());
                holder.implementListener(getContext(), getRef(position).getKey());
                if(!check_by.equals(Prevalant.currentOnlineUser.getName())) {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
                }
                else
                {
                    holder.itemView.setVisibility(View.VISIBLE);
                    holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                }
                mDialog.dismiss();
            }

            @NonNull
            @Override
            public HomeActivity.FeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_layout, parent, false);
                return new HomeActivity.FeedViewHolder(view);
            }
        };
        feedView.setAdapter(adapter);
        adapter.startListening();
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView QueView,AnsView,Ans_byView;

        public FeedViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            QueView = mView.findViewById(R.id.feed_que);
            AnsView = mView.findViewById(R.id.feed_ans);
            Ans_byView = mView.findViewById(R.id.answer_by);
        }

        public void setmView(String que, String ans, String ans_by){
            QueView.setText(que);
            AnsView.setText(ans);
            Ans_byView.setText(ans_by);
        }

        public void implementListener(final Context context, final String id){
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailFeed.class);
                    intent.putExtra("id", id);
                    context.startActivity(intent);
                }
            });
        }
    }
}