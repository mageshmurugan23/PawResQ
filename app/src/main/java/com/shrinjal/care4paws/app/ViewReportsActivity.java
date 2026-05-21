package com.shrinjal.care4paws.app;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.shrinjal.care4paws.adapter.Admin_Report_Adapter;
import com.shrinjal.care4paws.data.AppDatabase;
import com.shrinjal.care4paws.data.entity.ReportEntity;

import java.util.List;

public class ViewReportsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Admin_Report_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_reports_activity);

        recyclerView = findViewById(R.id.recyclerReports);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new Admin_Report_Adapter(null);
        recyclerView.setAdapter(adapter);

        loadUserReports();
    }

    private void loadUserReports() {

        new Thread(() -> {

            if (FirebaseAuth.getInstance().getCurrentUser() == null) return;

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            List<ReportEntity> list =
                    AppDatabase.getInstance(this)
                            .reportDao()
                            .getReportsByUser(uid); // 🔥 MAIN FIX

            runOnUiThread(() -> adapter.setData(list));

        }).start();
    }
}