package com.shrinjal.care4paws.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shrinjal.care4paws.app.R;
import com.shrinjal.care4paws.data.entity.FeedbackEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {

    List<FeedbackEntity> list;

    public FeedbackAdapter(List<FeedbackEntity> list) {
        this.list = list;
    }

    public void setData(List<FeedbackEntity> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feedback, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FeedbackEntity fb = list.get(position);

        holder.txtMessage.setText(fb.message);
        holder.txtUser.setText(fb.userName);

        // Format time
        String time = new SimpleDateFormat("hh:mm a", Locale.getDefault())
                .format(new Date(fb.timestamp));

        holder.txtTime.setText(time);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtUser, txtMessage, txtTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtUser = itemView.findViewById(R.id.txtUser);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }
}