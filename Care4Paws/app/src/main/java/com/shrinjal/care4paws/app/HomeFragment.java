package com.shrinjal.care4paws.app;

import android.annotation.SuppressLint;
import android.app.AlertDialog; // 🔥 ADD
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shrinjal.care4paws.app.adapter.FeedbackAdapter;
import com.shrinjal.care4paws.data.AppDatabase;
import com.shrinjal.care4paws.data.dao.NotificationDao; // 🔥 ADD
import com.shrinjal.care4paws.data.dao.ReportDao;
import com.shrinjal.care4paws.data.entity.FeedbackEntity;
import com.shrinjal.care4paws.data.entity.NotificationEntity; // 🔥 ADD

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    TextView txtTotalReports, txtUserName, txtUsers;

    // 🔔 ADD THIS
    TextView notifBell;

    LinearLayout btnViewReport;
    FloatingActionButton fabAddReport;

    RecyclerView recyclerFeedback;
    FeedbackAdapter adapter;

    FirebaseAuth mAuth;
    FirebaseFirestore fStore;

    // 🔔 ADD
    NotificationDao notificationDao;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        txtTotalReports = view.findViewById(R.id.txtTotalReportsDashboard);
        txtUserName = view.findViewById(R.id.txtUserName);
        txtUsers = view.findViewById(R.id.txtUsers);

        // 🔔 INIT
        notifBell = view.findViewById(R.id.notifBell);

        btnViewReport = view.findViewById(R.id.btnViewReport);
        fabAddReport = view.findViewById(R.id.fabAddReport);

        recyclerFeedback = view.findViewById(R.id.recyclerFeedback);

        recyclerFeedback.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FeedbackAdapter(new ArrayList<>());
        recyclerFeedback.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // 🔔 DAO INIT
        notificationDao = AppDatabase.getInstance(requireContext()).notificationDao();

        btnViewReport.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), ViewReportsActivity.class))
        );

        fabAddReport.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), AddFeedbackActivity.class))
        );

        // 🔔 CLICK LISTENER
        notifBell.setOnClickListener(v -> showNotifications());

        loadUserName();
        loadDashboard();
        loadFeedback();

        // 🔔 LOAD COUNT
        loadNotificationCount();

        return view;
    }

    // 🔔 LOAD COUNT
    private void loadNotificationCount() {
        new Thread(() -> {

            if (mAuth.getCurrentUser() == null) return;

            String uid = mAuth.getCurrentUser().getUid();

            List<NotificationEntity> list =
                    notificationDao.getUnreadNotifications(uid);

            int count = list.size();

            if (getActivity() == null) return;

            requireActivity().runOnUiThread(() ->
                    notifBell.setText("🔔 " + count)
            );

        }).start();
    }

    // 🔔 SHOW POPUP
    private void showNotifications() {
        new Thread(() -> {

            if (mAuth.getCurrentUser() == null) return;

            String uid = mAuth.getCurrentUser().getUid();

            List<NotificationEntity> list =
                    notificationDao.getUnreadNotifications(uid);

            if (getActivity() == null) return;

            requireActivity().runOnUiThread(() -> {

                if (list.isEmpty()) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Notifications")
                            .setMessage("No new notifications")
                            .setPositiveButton("OK", null)
                            .show();
                    return;
                }

                StringBuilder msg = new StringBuilder();

                for (NotificationEntity n : list) {
                    msg.append("• ").append(n.message).append("\n\n");
                }

                new AlertDialog.Builder(getContext())
                        .setTitle("Notifications 🐾")
                        .setMessage(msg.toString())
                        .setPositiveButton("OK", (d, w) -> {

                            new Thread(() ->
                                    notificationDao.markAllRead(uid)
                            ).start();

                            loadNotificationCount();
                        })
                        .show();
            });

        }).start();
    }

    private void loadUserName() {
        if (mAuth.getCurrentUser() == null) return;

        String uid = mAuth.getCurrentUser().getUid();

        fStore.collection("users").document(uid)
                .get()
                .addOnSuccessListener(doc -> {
                    String name = doc.getString("name");
                    if (name != null) {
                        txtUserName.setText("Hello, " + name + " 👋");
                    }
                });
    }

    private void loadDashboard() {
        new Thread(() -> {

            if (mAuth.getCurrentUser() == null) return;

            String uid = mAuth.getCurrentUser().getUid();

            AppDatabase db = AppDatabase.getInstance(requireContext());
            ReportDao dao = db.reportDao();

            int userReports = dao.getUserReportCount(uid);
            int totalUsers = dao.getTotalUsers();

            if (getActivity() == null) return;

            requireActivity().runOnUiThread(() -> {
                txtTotalReports.setText(String.valueOf(userReports));
                txtUsers.setText(String.valueOf(totalUsers));
            });

        }).start();
    }

    private void loadFeedback() {
        new Thread(() -> {
            List<FeedbackEntity> list =
                    AppDatabase.getInstance(requireContext())
                            .feedbackDao()
                            .getAllFeedback();

            if (getActivity() == null) return;

            requireActivity().runOnUiThread(() -> {
                adapter.setData(list);
            });
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserName();
        loadDashboard();
        loadFeedback();

        // 🔔 refresh notifications
        loadNotificationCount();
    }
}