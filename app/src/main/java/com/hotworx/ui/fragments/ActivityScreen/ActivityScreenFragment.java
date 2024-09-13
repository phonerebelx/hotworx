package com.hotworx.ui.fragments.ActivityScreen;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.hotworx.R;
import com.passio.modulepassio.Singletons.ApiHeaderSingleton;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.requestEntity.CaloriesObject;
import com.hotworx.requestEntity.ViewActivityResponse;
import com.hotworx.requestEntity.YearObject;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.views.TitleBar;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ActivityScreenFragment extends BaseFragment {
    Unbinder unbinder;

    @BindView(R.id.dashboard_barchart)
    BarChart barChart;
    @BindView(R.id.btn_weekly)
    RadioButton monthly;
    @BindView(R.id.btn_monthly)
    RadioButton ninety_days;
//    @BindView(R.id.segmented2)
//    SegmentedGroup segmentedGroup;
//    @BindView(R.id.val_tot_session)
//    TextView val_tot_session;
//    @BindView(R.id.val_tot_cal)
//    TextView val_tot_cal;
    List<CaloriesObject> month_list, ninety_list;
    List<String> entries_forBarChart_Count;
    List<String> entries_forBarChart_YearDays;
    YearObject yearObject;
    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_screen, container, false);
        unbinder = ButterKnife.bind(this, view);
        monthly.setChecked(true);
        entries_forBarChart_YearDays = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setYearsArrayList();
        apiCallForAcitvitiesData();

    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.activity));
    }

    private void setYearsArrayList() {
        entries_forBarChart_YearDays = new ArrayList<>();
        entries_forBarChart_YearDays.add("Jan");
        entries_forBarChart_YearDays.add("Feb");
        entries_forBarChart_YearDays.add("Mar");
        entries_forBarChart_YearDays.add("Apr");
        entries_forBarChart_YearDays.add("May");
        entries_forBarChart_YearDays.add("Jun");
        entries_forBarChart_YearDays.add("Jul");
        entries_forBarChart_YearDays.add("Aug");
        entries_forBarChart_YearDays.add("Sep");
        entries_forBarChart_YearDays.add("Oct");
        entries_forBarChart_YearDays.add("Nov");
        entries_forBarChart_YearDays.add("Dec");
    }

    private void apiCallForAcitvitiesData() {
        getServiceHelper().enqueueCall(getWebService().getUserActivity(ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.GET_USER_ACTIVITY, true);
    }

    @Override
    public void ResponseSuccess(String result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_USER_ACTIVITY:
                ViewActivityResponse mContentPojo = GsonFactory.getConfiguredGson().fromJson(result, ViewActivityResponse.class);
//                if (mContentPojo.getAllData().get(0).getInsideData().getTotal_session() != null)
//                    val_tot_session.setText(mContentPojo.getAllData().get(0).getInsideData().getTotal_session());

//                if (mContentPojo.getAllData().get(0).getInsideData().getTotal_calories_burned() != null)
//                    val_tot_cal.setText(mContentPojo.getAllData().get(0).getInsideData().getTotal_calories_burned());

                if (mContentPojo.getAllData().get(0).getInsideData().getTotal_calories_burned_yearly_graph() != null && mContentPojo.getAllData().get(0).getInsideData().getTotal_calories_burned_yearly_graph().size() > 0) {
                    yearObject = mContentPojo.getAllData().get(0).getInsideData().getTotal_calories_burned_yearly_graph().get(0);

                    entries_forBarChart_Count = new ArrayList<>();
                    if (yearObject.getJanuary() != null)
                        entries_forBarChart_Count.add(yearObject.getJanuary());

                    if (yearObject.getFebruary() != null)
                        entries_forBarChart_Count.add(yearObject.getFebruary());

                    if (yearObject.getMarch() != null)
                        entries_forBarChart_Count.add(yearObject.getMarch());

                    if (yearObject.getApril() != null)
                        entries_forBarChart_Count.add(yearObject.getApril());

                    if (yearObject.getMay() != null)
                        entries_forBarChart_Count.add(yearObject.getMay());

                    if (yearObject.getJune() != null)
                        entries_forBarChart_Count.add(yearObject.getJune());

                    if (yearObject.getJuly() != null)
                        entries_forBarChart_Count.add(yearObject.getJuly());

                    if (yearObject.getAugust() != null)
                        entries_forBarChart_Count.add(yearObject.getAugust());

                    if (yearObject.getSeptember() != null)
                        entries_forBarChart_Count.add(yearObject.getSeptember());

                    if (yearObject.getOctober() != null)
                        entries_forBarChart_Count.add(yearObject.getOctober());

                    if (yearObject.getNovember() != null)
                        entries_forBarChart_Count.add(yearObject.getNovember());

                    if (yearObject.getDecember() != null)
                        entries_forBarChart_Count.add(yearObject.getDecember());

                    setBarChart();

                }

                if (mContentPojo.getAllData().get(0).getInsideData().getMonthly_calories_session() != null
                        && mContentPojo.getAllData().get(0).getInsideData().getMonthly_calories_session().size() > 0) {
                    month_list = mContentPojo.getAllData().get(0).getInsideData().getMonthly_calories_session();
                }

                if (mContentPojo.getAllData().get(0).getInsideData().getNinty_days_calories_session() != null
                        && mContentPojo.getAllData().get(0).getInsideData().getNinty_days_calories_session().size() > 0) {
                    ninety_list = mContentPojo.getAllData().get(0).getInsideData().getNinty_days_calories_session();
                }

                getChildFragmentManager().beginTransaction().
                        replace(R.id.list_container, ListMonthlyFragment.newInstance(month_list)).commit();


                break;

        }
    }

    private void setBarChart() {
//        BarData data = new BarData(entries_forBarChart_YearDays, getDataSet(entries_forBarChart_Count));
        BarData data = new BarData(getDataSet(entries_forBarChart_Count));
        YAxis yAxis = barChart.getAxisLeft();
        float max_this_week = Float.parseFloat(Collections.max(entries_forBarChart_Count));
        yAxis.setAxisMaximum(max_this_week);
        yAxis.setAxisMinimum(0);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        yAxis.setDrawGridLines(false);
        barChart.getAxisRight().setDrawLabels(false);
        barChart.getAxisRight().setEnabled(false);
        barChart.setDrawValueAboveBar(false);
        barChart.setData(data);
        //barChart.setDescription("");
        barChart.getLegend().setEnabled(false);
        barChart.animateY(2500);
    }

    private static List<IBarDataSet> getDataSet(List<String> this_week) {
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();

        for (int counter = 0; counter < this_week.size(); counter++) {
            BarEntry barEntry = new BarEntry(counter, Float.parseFloat(this_week.get(counter)));
            valueSet1.add(barEntry);
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "");
        barDataSet1.setColor(Color.parseColor("#f26a1e"));
        barDataSet1.setDrawValues(false);
        barDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);
        List<IBarDataSet> dataset = new ArrayList<>();
        dataset.add(barDataSet1);
        return dataset;
    }

    @SuppressLint("ResourceAsColor")
    @OnClick({R.id.btn_monthly, R.id.btn_weekly})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_monthly:
                Log.d("ActivityScreenFragment", "Monthly button clicked");

                // Update UI to reflect the selected button
                ninety_days.setChecked(false);
                monthly.setChecked(true);
                monthly.setBackgroundResource(R.drawable.multicolor_background);
                monthly.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
                ninety_days.setBackgroundResource(R.color.colorWhite);
                ninety_days.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack));

                // Reload RecyclerView with monthly data
                ListMonthlyFragment monthlyFragment = (ListMonthlyFragment) getChildFragmentManager().findFragmentById(R.id.list_container);
                if (month_list != null && !month_list.isEmpty()) {
                    if (monthlyFragment != null) {
                        monthlyFragment.updateList(month_list);
                    } else {
                        getChildFragmentManager().beginTransaction()
                                .replace(R.id.list_container, ListMonthlyFragment.newInstance(month_list))
                                .commitAllowingStateLoss();
                    }
                    Log.d("ActivityScreenFragment", "Monthly data loaded");
                } else {
                    Toast.makeText(myDockActivity, "No data found..!!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.btn_weekly:
                Log.d("ActivityScreenFragment", "Weekly button clicked");

                // Update UI to reflect the selected button
                ninety_days.setChecked(true);
                monthly.setChecked(false);
                ninety_days.setBackgroundResource(R.drawable.multicolor_background);
                ninety_days.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorWhite));
                monthly.setBackgroundResource(R.color.colorWhite);
                monthly.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorBlack));

                // Reload RecyclerView with 90 days data
                ListNinetyDaysFragment ninetyDaysFragment = (ListNinetyDaysFragment) getChildFragmentManager().findFragmentById(R.id.list_container);

                if (ninetyDaysFragment != null && !ninety_list.isEmpty()) {
                    ninetyDaysFragment.updateList(ninety_list);
                } else {
                    getChildFragmentManager().beginTransaction()
                            .replace(R.id.list_container, ListNinetyDaysFragment.newInstance(ninety_list))
                            .commitAllowingStateLoss();
                }

//                if (ninety_list != null && !ninety_list.isEmpty()) {
//
//                    Log.d("ActivityScreenFragment", "Ninety days data loaded");
//                } else {
//                    Toast.makeText(myDockActivity, "No data found..!!", Toast.LENGTH_SHORT).show();
//                }
                break;
        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
       // unbinder.unbind();
    }
}
