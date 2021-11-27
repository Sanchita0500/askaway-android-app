package com.example.askaway.Functionalities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.askaway.Objects.Feed;
import com.example.askaway.R;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder>  {

    ArrayList<Feed> list;

    public SearchAdapter(ArrayList<Feed> list){
        this.list = list;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_layout, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.QueView.setText(list.get(position).getQuestion());
        holder.AnsView.setText(list.get(position).getAnswer());
        holder.Ans_byView.setText(list.get(position).getBy());

        holder.implementListener(holder.itemView.getContext(),list.get(position).getQuestion(),list.get(position).getAnswer(),list.get(position).getBy());

        String check_answer=list.get(position).getAnswer();

        if(check_answer.equals("")) {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
        }
        else
        {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView QueView,AnsView,Ans_byView;

        public SearchViewHolder(@NonNull View itemView){
            super(itemView);
            mView = itemView;
            QueView = mView.findViewById(R.id.feed_que);
            AnsView = mView.findViewById(R.id.feed_ans);
            Ans_byView = mView.findViewById(R.id.answer_by);
        }

        public void implementListener(final Context context, final String que, final String ans, final String by){
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailFeed.class);
                    intent.putExtra("id", "manual");
                    intent.putExtra("que", que);
                    intent.putExtra("ans", ans);
                    intent.putExtra("by", by);
                    context.startActivity(intent);
                }
            });
        }
    }
}
