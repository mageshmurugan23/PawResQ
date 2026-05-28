package com.shrinjal.care4paws.app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.shrinjal.care4paws.adapter.Admin_Report_Adapter;
import com.shrinjal.care4paws.app.adapter.FeedbackAdapter;
import com.shrinjal.care4paws.data.AppDatabase;
import com.shrinjal.care4paws.data.dao.ReportDao;
import com.shrinjal.care4paws.data.entity.ReportEntity;
import com.shrinjal.care4paws.data.entity.FeedbackEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AdminDashboardActivity extends AppCompatActivity {

    RecyclerView recyclerReports, recyclerFeedback;

    Button btnAll, btnPending, btnRescued, btnFeedback;
    TextView txtStats;

    Admin_Report_Adapter reportAdapter;
    FeedbackAdapter feedbackAdapter;

    ReportDao reportDao;

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // ================= INIT =================
        recyclerReports = findViewById(R.id.recyclerAdminReports);
        recyclerFeedback = findViewById(R.id.recyclerAdminFeedback);

        btnAll = findViewById(R.id.btnAll);
        btnPending = findViewById(R.id.btnPending);
        btnRescued = findViewById(R.id.btnRescued);
        btnFeedback = findViewById(R.id.btnFeedback);

        txtStats = findViewById(R.id.txtStats);

        recyclerReports.setLayoutManager(new LinearLayoutManager(this));
        recyclerFeedback.setLayoutManager(new LinearLayoutManager(this));

        reportDao = AppDatabase.getInstance(this).reportDao();

        reportAdapter = new Admin_Report_Adapter(null);
        recyclerReports.setAdapter(reportAdapter);

        feedbackAdapter = new FeedbackAdapter(new ArrayList<>());
        recyclerFeedback.setAdapter(feedbackAdapter);

        loadReports();
        loadStats();

        // ================= BUTTONS =================

        btnAll.setOnClickListener(v -> {
            showReports();
            loadReports();
        });

        btnPending.setOnClickListener(v -> {
            showReports();
            executor.execute(() -> {
                List<ReportEntity> list = reportDao.getPendingReports();
                runOnUiThread(() -> reportAdapter.setData(list));
            });
        });

        btnRescued.setOnClickListener(v -> {
            showReports();
            executor.execute(() -> {
                List<ReportEntity> list = reportDao.getRescuedReports();
                runOnUiThread(() -> reportAdapter.setData(list));
            });
        });

        btnFeedback.setOnClickListener(v -> {
            showFeedback();
            loadFeedback();
        });
    }

    // ================= UI SWITCH =================

    private void showReports() {
        recyclerReports.setVisibility(View.VISIBLE);
        recyclerFeedback.setVisibility(View.GONE);
    }

    private void showFeedback() {
        recyclerReports.setVisibility(View.GONE);
        recyclerFeedback.setVisibility(View.VISIBLE);
    }

    // ================= DATA =================

    private void loadReports() {
        executor.execute(() -> {
            List<ReportEntity> list = reportDao.getAllReports();
            runOnUiThread(() -> reportAdapter.setData(list));
        });
    }

    private void loadStats() {
        executor.execute(() -> {
            int total = reportDao.getTotalCount();
            int pending = reportDao.getPendingCount();
            int rescued = reportDao.getRescuedCount();

            runOnUiThread(() -> txtStats.setText(
                    "Total: " + total +
                            " | Pending: " + pending +
                            " | Rescued: " + rescued
            ));
        });
    }

    private void loadFeedback() {
        executor.execute(() -> {
            List<FeedbackEntity> list =
                    AppDatabase.getInstance(this)
                            .feedbackDao()
                            .getAllFeedback();

            runOnUiThread(() -> feedbackAdapter.setData(list));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReports();
        loadStats();
    }
}