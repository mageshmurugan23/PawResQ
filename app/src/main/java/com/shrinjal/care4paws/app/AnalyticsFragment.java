package com.shrinjal.care4paws.app;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.shrinjal.care4paws.data.AppDatabase;

import java.util.ArrayList;

public class AnalyticsFragment extends Fragment {

    private BarChart barChart;
    private PieChart pieChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_analytics, container, false);

        barChart = view.findViewById(R.id.barChar);
        pieChart = view.findViewById(R.id.pieChar);

        loadCharts();

        return view;
    }

    private void loadCharts() {

        AppDatabase db = AppDatabase.getInstance(requireContext());

        int total = db.reportDao().getTotalCount();
        int rescued = db.reportDao().getRescuedCount();
        int pending = db.reportDao().getPendingCount();

        setupBarChart(total, rescued, pending);
        setupPieChart(rescued, pending);
    }

    // ================= BAR CHART =================
    private void setupBarChart(int total, int rescued, int pending) {

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, total));
        entries.add(new BarEntry(1, rescued));
        entries.add(new BarEntry(2, pending));

        BarDataSet dataSet = new BarDataSet(entries, "");

        dataSet.setColors(
                Color.parseColor("#2196F3"),  // Blue - Total
                Color.parseColor("#4CAF50"),  // Green - Rescued
                Color.parseColor("#F44336")   // Red - Pending
        );

        dataSet.setValueTextSize(14f);
        dataSet.setValueTextColor(Color.BLACK);

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.6f);

        barChart.setData(barData);

        // X Axis labels
        final String[] labels = new String[]{"Total", "Rescued", "Pending"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        barChart.getAxisRight().setEnabled(false);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);

        barChart.animateY(1000);
        barChart.invalidate();
    }

    // ================= PIE CHART =================
    private void setupPieChart(int rescued, int pending) {

        int total = rescued + pending;

        if (total == 0) {
            pieChart.clear();
            return;
        }

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(rescued, "Rescued"));
        entries.add(new PieEntry(pending, "Pending"));

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setColors(
                Color.parseColor("#4CAF50"),  // Green
                Color.parseColor("#F44336")   // Red
        );

        pieChart.setUsePercentValues(true);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter(pieChart));
        pieData.setValueTextSize(18f);
        pieData.setValueTextColor(Color.WHITE);

        pieChart.setData(pieData);

        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(60f);
        pieChart.setTransparentCircleRadius(65f);

        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);

        pieChart.animateY(1200);
        pieChart.invalidate();
    }
}