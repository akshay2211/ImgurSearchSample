package com.interview.project.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.interview.project.R;
import com.interview.project.model.Comments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by akshay on 27,October,2020
 * akshay2211@github.io
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private final ArrayList<Comments> list = new ArrayList();

    public void addDataInList(List<Comments> list) {
        this.list.clear();
        Collections.reverse(list);
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_row, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindTo(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);

        }

        public void bindTo(Comments comments) {
            TextView tv = itemView.findViewById(R.id.comments_text_view);
            tv.setText(comments.getComment_content());
        }
    }
}
