package com.hotworx.ui.fragments.BodyFatPercentage;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hotworx.R;
import com.hotworx.Singletons.ApiHeaderSingleton;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.models.BodyFatGraphData.DailyBodyfat;
import com.hotworx.models.BodyFatGraphData.BodyFatGraphOPOJO;
import com.hotworx.models.BodyFatMonthData.GetMonthDataResp;
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

public class BodyFatPercentageFragment extends BaseFragment {
    Unbinder unbinder;
    static String TAG = "BodyFatPercentage";

    @BindView(R.id.bodyFatBarChart)
    BarChart barChart;
    @BindView(R.id.btnRecord)
    Button btnRecord;
    @BindView(R.id.tvTotalSession)
    TextView tvTotalSession;
    @BindView(R.id.tvCalBurned)
    TextView tvCalBurned;
    @BindView(R.id.tvAfterBurned)
    TextView tvAfterBurned;
    @BindView(R.id.tvTotalCalBurned)
    TextView tvTotalCalBurned;
    @BindView(R.id.tvLastWeightBurned)
    TextView tvLastWeightBurned;
    @BindView(R.id.tvLastBodyFatBurned)
    TextView tvLastBodyFatBurned;
    List<String> entriesForDays;
    List<String> entriesForWeight;
    List<String> entriesForFatPerc;
    List<DailyBodyfat> dataList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_body_fat_percentage,container,false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiCallForSummaryThirtyDays();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.body_fat_percentage));
    }

    @OnClick(R.id.btnRecord)
    public void onClick(){
        myDockActivity.replaceDockableFragment(new RecordBodyFatFragment());
    }

    private void apiCallForSummaryThirtyDays() {
       getServiceHelper().enqueueCall(getWebService().getSummaryThirtyDays(ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.GET_THIRTY_DAY_SUMMARY,true);
    }

    private void apiCallForBodyGraph(){
        getServiceHelper().enqueueCall(getWebService().getBodyFatGraph(ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.GET_BODY_FAT_GRAPH,true);
    }


    @Override
    public void ResponseSuccess(String result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_THIRTY_DAY_SUMMARY:
                GetMonthDataResp mContentPojo = GsonFactory.getConfiguredGson().fromJson(result, GetMonthDataResp.class);
                Log.d("ResponseSuccess: ",mContentPojo.getData().get(0).getData().getLast_body_fat_reading());
                if (mContentPojo.getData().get(0) != null) {
                    tvTotalSession.setText(mContentPojo.getData().get(0).getData().getTotal_sessions());
                    tvCalBurned.setText(mContentPojo.getData().get(0).getData().getWorkout_calorie_burned());
                    tvAfterBurned.setText(mContentPojo.getData().get(0).getData().getAfterburn_calorie_burned());
                    tvTotalCalBurned.setText(mContentPojo.getData().get(0).getData().getTotal_calorie_burned());
                    tvLastWeightBurned.setText(mContentPojo.getData().get(0).getData().getLast_weight_reading());
                    tvLastBodyFatBurned.setText(mContentPojo.getData().get(0).getData().getLast_body_fat_reading().concat(" %"));
                }
                apiCallForBodyGraph();
                break;

            case WebServiceConstants.GET_BODY_FAT_GRAPH:
                BodyFatGraphOPOJO graphOPOJO = GsonFactory.getConfiguredGson().fromJson(result, BodyFatGraphOPOJO.class);
//                getInsideBodyFatData
                if (graphOPOJO.getData().get(0) != null && graphOPOJO.getData().get(0).getData().getDaily_bodyfat() != null && graphOPOJO.getData().get(0).getData().getDaily_bodyfat().size() > 0) {
                    dataList = graphOPOJO.getData().get(0).getData().getDaily_bodyfat();
                    entriesForDays = new ArrayList<>();
                    entriesForWeight = new ArrayList<>();
                    entriesForFatPerc = new ArrayList<>();
                    for (int i = 0; i < dataList.size(); i++) {
                        entriesForDays.add(String.valueOf(dataList.get(i).getDay()));
                        entriesForWeight.add(dataList.get(i).getWeight());
                        entriesForFatPerc.add(dataList.get(i).getBody_fat().split("%")[0]);
                    }
                    setBarChart();
                }
                break;
        }
    }

    private void setBarChart() {

//        BarData data = new BarData(entriesForDays, getDataSet(entriesForWeight, entriesForFatPerc));
        BarData data = new BarData( getDataSet(entriesForWeight, entriesForFatPerc));
        YAxis yAxis = barChart.getAxisLeft();
        float max_this_week = Float.parseFloat(Collections.max(entriesForWeight));
        yAxis.setAxisMaxValue(max_this_week);
        yAxis.setAxisMinValue(0);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        yAxis.setDrawGridLines(false);
        barChart.getAxisRight().setDrawLabels(true);
        barChart.setDrawValueAboveBar(false);
        barChart.setData(data);
//        barChart.setDescription("");
        barChart.getLegend().setEnabled(false);
        barChart.animateY(2500);
    }


    private static List<IBarDataSet> getDataSet(List<String> weightList, List<String> fatPercList) {
        ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        ArrayList<BarEntry> valueSet2 = new ArrayList<>();

        for (int counter = 0; counter < weightList.size(); counter++) {
            BarEntry barEntry = new BarEntry(Float.parseFloat(weightList.get(counter)), counter);
            valueSet1.add(barEntry);
        }

        for (int counter = 0; counter < fatPercList.size(); counter++) {
            BarEntry barEntry = new BarEntry(Float.parseFloat(fatPercList.get(counter)), counter);
            valueSet2.add(barEntry);
        }

        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Group 1");
        BarDataSet barDataSet2 = new BarDataSet(valueSet2, "Group 2");

        barDataSet1.setColor(Color.parseColor("#f26a1e"));
        barDataSet2.setColor(Color.parseColor("#e10100"));

        barDataSet1.setDrawValues(false);
        barDataSet1.setAxisDependency(YAxis.AxisDependency.LEFT);

        barDataSet2.setDrawValues(false);
        barDataSet2.setAxisDependency(YAxis.AxisDependency.RIGHT);

        List<IBarDataSet> dataset = new ArrayList<>();
        dataset.add(barDataSet1);
        dataset.add(barDataSet2);

        return dataset;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
      //  unbinder.unbind();
    }
}
