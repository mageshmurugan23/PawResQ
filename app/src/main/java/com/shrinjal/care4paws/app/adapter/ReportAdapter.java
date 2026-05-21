package com.shrinjal.care4paws.app.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.shrinjal.care4paws.app.R;
import com.shrinjal.care4paws.model.ReportModel;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    List<ReportModel> list;
    List<String> docIds;

    public ReportAdapter(List<ReportModel> list, List<String> docIds) {
        this.list = list;
        this.docIds = docIds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int i) {

        ReportModel r = list.get(i);

        h.details.setText(
                "Animal: " + r.animalType +
                        "\nLocation: " + r.location +
                        "\nStatus: " + r.status
        );

        h.resolve.setOnClickListener(v ->
                FirebaseFirestore.getInstance()
                        .collection("reports")
                        .document(docIds.get(i))
                        .update("status", "Resolved")
        );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView details;
        Button resolve;

        ViewHolder(View v) {
            super(v);
            details = v.findViewById(R.id.description);
            resolve = v.findViewById(R.id.submitBtn);
        }
    }
}
