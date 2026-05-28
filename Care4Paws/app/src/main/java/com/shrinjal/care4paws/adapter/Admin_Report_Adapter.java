package com.shrinjal.care4paws.adapter;

import android.content.res.ColorStateList;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.shrinjal.care4paws.app.R;
import com.shrinjal.care4paws.data.AppDatabase;
import com.shrinjal.care4paws.data.entity.ReportEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Admin_Report_Adapter
        extends RecyclerView.Adapter<Admin_Report_Adapter.ViewHolder> {

    private List<ReportEntity> list;

    // ✅ Constructor FIX (avoid null)
    public Admin_Report_Adapter(List<ReportEntity> list) {
        this.list = list != null ? list : new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_admin_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder, int position) {

        ReportEntity report = list.get(position);

        // ✅ IMAGE FIX (same as user adapter)
        if (report.imagePath != null) {
            File file = new File(report.imagePath);
            if (file.exists()) {
                holder.imgReport.setImageBitmap(
                        BitmapFactory.decodeFile(file.getAbsolutePath())
                );
            } else {
                holder.imgReport.setImageResource(R.drawable.ic_launcher_background);
            }
        } else {
            holder.imgReport.setImageResource(R.drawable.ic_launcher_background);
        }

        // 🐶 Animal
        holder.txtAnimal.setText("Animal: " + report.animalType);

        // 👤 User
        holder.txtUser.setText(
                report.userName != null ? "User: " + report.userName : "User: N/A"
        );

        // 📍 Location
        holder.txtLocation.setText(
                "📍 " + report.latitude + ", " + report.longitude
        );

        // 🚦 Status
        holder.txtStatus.setText("Status: " + report.status);

        if ("Pending".equalsIgnoreCase(report.status)) {
            holder.txtStatus.setTextColor(Color.parseColor("#D32F2F"));
        } else {
            holder.txtStatus.setTextColor(Color.parseColor("#388E3C"));
        }

        applyButtonColors(holder, report.status);

        // 🟢 Rescued
        // 🔥 ONLY THIS PART UPDATED

        holder.btnRescued.setOnClickListener(v -> {
            updateStatus(v, report, "Rescued");
            notifyItemChanged(position);

            // 🔔 Notification Logic (ADDED)
            new Thread(() -> {
                AppDatabase db = AppDatabase.getInstance(v.getContext());

                db.notificationDao().insert(
                        new com.shrinjal.care4paws.data.entity.NotificationEntity(
                                report.userId,
                                "Your rescue request has been accepted 🐾"
                        )
                );
            }).start();

            Toast.makeText(v.getContext(), "Marked as Rescued", Toast.LENGTH_SHORT).show();
        });

        // 🔴 Pending
        holder.btnPending.setOnClickListener(v -> {
            updateStatus(v, report, "Pending");
            notifyItemChanged(position);
            Toast.makeText(v.getContext(), "Marked as Pending", Toast.LENGTH_SHORT).show();
        });
    }

    // ✅ setData FIX (no crash)
    public void setData(List<ReportEntity> newList) {
        if (newList == null) return;
        this.list.clear();
        this.list.addAll(newList);
        notifyDataSetChanged();
    }

    private void applyButtonColors(ViewHolder h, String status) {

        h.btnPending.setBackgroundTintList(
                ColorStateList.valueOf(Color.parseColor("#D32F2F"))
        );

        if ("Rescued".equalsIgnoreCase(status)) {
            h.btnRescued.setBackgroundTintList(
                    ColorStateList.valueOf(Color.parseColor("#388E3C"))
            );
        } else {
            h.btnRescued.setBackgroundTintList(
                    ColorStateList.valueOf(Color.parseColor("#D32F2F"))
            );
        }
    }

    private void updateStatus(View v, ReportEntity report, String status) {

        new Thread(() -> {
            AppDatabase.getInstance(v.getContext())
                    .reportDao()
                    .updateStatus(report.id, status);
        }).start();

        report.status = status;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtAnimal, txtLocation, txtStatus, txtUser;
        Button btnPending, btnRescued;
        ImageView imgReport;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgReport = itemView.findViewById(R.id.imgReport);
            txtAnimal = itemView.findViewById(R.id.txtAnimal);
            txtLocation = itemView.findViewById(R.id.txtLocation);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            txtUser = itemView.findViewById(R.id.txtUser);
            btnPending = itemView.findViewById(R.id.btnPending);
            btnRescued = itemView.findViewById(R.id.btnRescued);
        }
    }
}