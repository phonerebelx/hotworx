package com.hotworx.ui.fragments.Nutritionist;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.hotworx.R;
import com.hotworx.Singletons.ApiHeaderSingleton;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.Utils;
import com.hotworx.requestEntity.NintyDaysCaloriesSession;
import com.hotworx.requestEntity.NutritionistSummaryResponse;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.ui.adapters.NutritionistSummaryAdapter;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.views.TitleBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NutritionistSummaryFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.barChart)
    BarChart barChart;

    @BindView(R.id.tv_daysIn)
    TextView tvDaysIn;
    @BindView(R.id.tv_avgBmr)
    TextView tvAvgBmr;
    @BindView(R.id.items_rv)
    RecyclerView recyclerView;

    List<String> caloriesIntakeCount;
    List<String> caloriesOutCount;
    List<String> calories;
    NutritionistSummaryAdapter nutritionistSummaryAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nutritionist_summary, container, false);
        unbinder = ButterKnife.bind(this,view);
        calories = new ArrayList<>();
        caloriesIntakeCount = new ArrayList<>();
        caloriesOutCount = new ArrayList<>();

        return view;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.just_summary));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiCallForSummary();
        setCalories();
    }

    private void apiCallForSummary() {
        getServiceHelper().enqueueCall(getWebService().viewNutritionistSummary(ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.VIEW_NUTRITION_SUMMARY, true);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void ResponseFailure(String message, String tag){
        switch (tag) {
            case WebServiceConstants.VIEW_NUTRITION_SUMMARY:
                Utils.customToast(requireContext(),"ninty days calories not found");
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void ResponseSuccess(String result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.VIEW_NUTRITION_SUMMARY:
                NutritionistSummaryResponse mContentPojo = GsonFactory.getConfiguredGson().fromJson(result, NutritionistSummaryResponse.class);

                if (mContentPojo != null) {

                    caloriesIntakeCount.add(String.valueOf(mContentPojo.getData().get(0).getNinety_days_in()));
                    caloriesIntakeCount.add(String.valueOf(mContentPojo.getData().get(0).getNinety_days_out()));

                    tvDaysIn.setText(mContentPojo.getData().get(0).getTotal_days_tracked());

                    String averageBmrDeficit = mContentPojo.getData().get(0).getAvcerage_bmr_deficit();
                    int bmrNum;
                    if (averageBmrDeficit.equalsIgnoreCase("NAN")) {
                        bmrNum = 0;
                    }
                    else {
                        bmrNum = Integer.parseInt(mContentPojo.getData().get(0).getAvcerage_bmr_deficit());
                    }
                    if (bmrNum > 0) {
                        tvAvgBmr.setTextColor(requireContext().getResources().getColor(R.color.colorRed));
                    }
                    tvAvgBmr.setText(mContentPojo.getData().get(0).getAvcerage_bmr_deficit() + "%");
                    setBarChart();
                    if (mContentPojo.getData().get(0).getNinty_days_calories_session() != null) {
                        updateAdapter(mContentPojo.getData().get(0).getNinty_days_calories_session());
                    }

                }
                break;
        }
    }

    private void setCalories() {
        calories = new ArrayList<>();
        calories.add("Cal intake");
        calories.add("Cal Burned");
    }

    private void updateAdapter(List<NintyDaysCaloriesSession> data) {
        nutritionistSummaryAdapter = new NutritionistSummaryAdapter(myDockActivity, data, getContext());
        recyclerView.setLayoutManager((new LinearLayoutManager(myDockActivity, LinearLayoutManager.VERTICAL, false)));
        recyclerView.setAdapter(nutritionistSummaryAdapter);
    }

    private void setBarChart() {
//        BarData dataIn = new BarData(calories, getDataIn(caloriesIntakeCount));

        BarData dataIn = new BarData( getDataIn(caloriesIntakeCount));
        YAxis axisLeft = barChart.getAxisLeft();
        float max_cal_in = Float.parseFloat(Collections.max(caloriesIntakeCount));

        axisLeft.setAxisMaxValue(max_cal_in);
        axisLeft.setAxisMinValue(0);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axisLeft.setDrawGridLines(false);
        barChart.getAxisRight().setDrawLabels(true);
        barChart.setDrawValueAboveBar(false);
        barChart.setData(dataIn);
//        barChart.setDescription("");
        barChart.getLegend().setEnabled(false);
        barChart.animateY(2500);
    }

    private static List<IBarDataSet> getDataIn(List<String> cal_in) {

        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        for (int counter = 0; counter < cal_in.size(); counter++) {
            BarEntry barEntry = new BarEntry(Float.parseFloat(cal_in.get(counter)), counter);
            valueSet1.add(barEntry);
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Group 1");
        barDataSet1.setColors(new int[] {Color.GREEN, Color.RED});

        barDataSet1.setDrawValues(false);
        barDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);

        List<IBarDataSet> dataset = new ArrayList<>();
        dataset.add(barDataSet1);
        return dataset;
    }

}