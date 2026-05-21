package com.shrinjal.care4paws.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shrinjal.care4paws.app.R;
import com.shrinjal.care4paws.app.ViewReportsActivity;
import com.shrinjal.care4paws.data.entity.ReportEntity;

import java.io.File;
import java.util.List;

public class User_Report_Adapter
        extends RecyclerView.Adapter<User_Report_Adapter.ViewHolder> {

    private List<ReportEntity> list;

    public User_Report_Adapter(List<ReportEntity> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder h,
            int position) {

        ReportEntity r = list.get(position);

        // ✅ MAIN FIX HERE
        h.txtUser.setText("User: " + r.userName);

        h.txtAnimal.setText("Animal: " + r.animalType);
        h.txtLocation.setText("Location: " + r.latitude + ", " + r.longitude);
        h.txtStatus.setText("Status: " + r.status);

        if (r.imagePath != null) {
            File img = new File(r.imagePath);
            if (img.exists()) {
                h.img.setImageBitmap(
                        BitmapFactory.decodeFile(img.getAbsolutePath())
                );
            }
        }

        h.itemView.setOnClickListener(v -> {
            Context context = v.getContext();

            Intent intent = new Intent(context, ViewReportsActivity.class);

            intent.putExtra("animal", r.animalType);
            intent.putExtra("location", r.latitude + ", " + r.longitude);
            intent.putExtra("status", r.status);
            intent.putExtra("image", r.imagePath);

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public void setData(List<ReportEntity> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView img;
        TextView txtUser, txtAnimal, txtLocation, txtStatus;

        ViewHolder(@NonNull View v) {
            super(v);

            img = v.findViewById(R.id.imgReport);
            txtUser = v.findViewById(R.id.txtUser);
            txtAnimal = v.findViewById(R.id.txtAnimal);
            txtLocation = v.findViewById(R.id.txtLocation);
            txtStatus = v.findViewById(R.id.txtStatus);
        }
    }
}