package com.shrinjal.care4paws.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shrinjal.care4paws.adapter.User_Report_Adapter;
import com.shrinjal.care4paws.data.AppDatabase;
import com.shrinjal.care4paws.data.entity.ReportEntity;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {

    RecyclerView recyclerView;
    TextView txtEmail, txtTotal, txtPending, txtRescued;
    Button btnLogout;

    FirebaseAuth mAuth;
    User_Report_Adapter adapter;

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        // ===== INIT =====
        recyclerView = v.findViewById(R.id.recyclerReports);
        txtEmail = v.findViewById(R.id.txtUserEmail);
        txtTotal = v.findViewById(R.id.txtTotalReports);
        txtPending = v.findViewById(R.id.txtPendingReports);
        txtRescued = v.findViewById(R.id.txtRescuedReports);
        btnLogout = v.findViewById(R.id.btnLogout);

        // ===== RECYCLER =====
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new User_Report_Adapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // ===== FIREBASE =====
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        // ===== EMAIL DISPLAY =====
        if (user != null) {
            txtEmail.setText(user.getEmail());
        } else {
            txtEmail.setText("Not logged in");
        }

        // ===== LOGOUT =====
        btnLogout.setOnClickListener(view -> {

            new AlertDialog.Builder(requireContext())
                    .setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, which) -> {

                        mAuth.signOut();

                        Intent intent = new Intent(getActivity(), SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) return;

        String uid = currentUser.getUid();

        // 🔥 BACKGROUND THREAD
        new Thread(() -> {

            AppDatabase db = AppDatabase.getInstance(requireContext());

            // ✅ DATABASE CALLS
            int total = db.reportDao().getTotalByUser(uid);
            int pending = db.reportDao().getPendingByUser(uid);
            int rescued = db.reportDao().getRescuedByUser(uid);

            List<ReportEntity> reportList = db.reportDao().getReportsByUser(uid);

            if (getActivity() == null) return;

            getActivity().runOnUiThread(() -> {

                if (!isAdded()) return;

                // ===== UPDATE TEXT =====
                txtTotal.setText("Total Reports: " + total);
                txtPending.setText("🔴 Pending: " + pending);
                txtRescued.setText("🟢 Rescued: " + rescued);

                // ===== UPDATE LIST =====
                adapter.setData(reportList);
            });

        }).start();
    }
}