package com.hotworx.ui.fragments.Nutritionist;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.crashlytics.internal.model.CrashlyticsReport;
import com.hotworx.R;
import com.hotworx.Singletons.ApiHeaderSingleton;
import com.hotworx.global.Constants;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.Utils;
import com.hotworx.interfaces.BasketClickListener;
import com.hotworx.interfaces.OnItemClickInterface;
import com.hotworx.models.AddNutritionistRequestBody;
import com.hotworx.models.CartData;
import com.hotworx.requestEntity.Branded;
import com.hotworx.requestEntity.DayData;
import com.hotworx.requestEntity.GenericMsgResponse;
import com.hotworx.requestEntity.GetFoodResponse;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.ui.adapters.BasketItemAdapter;
import com.hotworx.ui.adapters.FreeFormAdapter;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.views.TitleBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BasketFragment extends BaseFragment implements BasketClickListener {

    Unbinder unbinder;
    List<Branded> brandedData;
    ArrayList<Branded> fastingData = new ArrayList<>();
    private static final RequestOptions requestOptions = new RequestOptions();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.today_item)
    TextView todayItem;
    @BindView(R.id.tv_totalCal)
    TextView tvTotalCal;
    @BindView(R.id.clear_basket)
    TextView tvClearBasket;
    @BindView(R.id.fasting_item)
    TextView tvFasting;
    @BindView(R.id.breakfast)
    Button breakfastBtn;
    @BindView(R.id.lunch)
    Button lunchBtn;
    @BindView(R.id.dinner)
    Button dinnerBtn;
    @BindView(R.id.am_snack)
    Button amSnackBtn;
    @BindView(R.id.pm_snack)
    Button pmSnackBtn;
    @BindView(R.id.late_snack)
    Button lateSnackBtn;
    @BindView(R.id.search_btn)
    Button logBtn;

    String basketName;
    String productName;
    String productId;
    Boolean btnCheck = false;
    BasketItemAdapter basketItemAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.basket_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);


        if (getArguments() != null && getArguments().getString(Constants.isFasting).equals("no")) {
            brandedData = (List<Branded>) getArguments().getSerializable(Constants.FreeFormData);
            basketName = getArguments().getString(Constants.BasketName);
            Log.i("xxData", brandedData.toString());
            logBtn.setText(String.valueOf(brandedData.size()) + " Log Food");
            tvTotalCal.setText(String.valueOf(getTotalCalories()));
        } else {
            brandedData = new ArrayList<Branded>();
            Branded branded = new Branded();
            branded.setFood_name("");
            branded.setNf_calories(0);
            branded.setServing_qty(0);
            brandedData.add(branded);
            productName = getArguments().getString(Constants.FastingItem);
            basketName = getArguments().getString(Constants.FastingItem);
            productId = getArguments().getString(Constants.FastingItemId);
        }

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        //df.setTimeZone(TimeZone.getTimeZone("gmt"));
        String currentDate = df.format(c);

        logBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (getArguments() != null && getArguments().getString(Constants.isFasting).equals("no")) {
                    List<CartData> cartData = new ArrayList<>();
                    for (Branded element : brandedData) {
                        CartData mCartData = new CartData();
                        mCartData.setType(todayItem.getText().toString());
                        mCartData.setUnit(element.getServing_unit());
//                        mCartData.setProduct_id(productId);
                        mCartData.setProduct_id(String.valueOf(element.getNix_item_id()));
                        mCartData.setClaorie_count(String.valueOf(element.getNf_calories()));
                        try {
                            mCartData.setImg_url(element.getPhoto().getThumb());
                        } catch (Exception e) {
                            mCartData.setImg_url("");
                        }
                        mCartData.setQty(String.valueOf(element.getServing_qty()));
//                        mCartData.setProduct_name(productName);
                        mCartData.setProduct_name(String.valueOf(element.getFood_name()));
                        cartData.add(mCartData);
                    }

                    AddNutritionistRequestBody addNutritionistRequestBody = new AddNutritionistRequestBody();
//                    addNutritionistRequestBody.setUser_id(prefHelper.getUserId());
                    addNutritionistRequestBody.setCart_data(cartData);
                    addNutritionistRequestBody.setRecording_date(currentDate);
                    addNutritionistRequestBody.setLocal_date(currentDate);
                    apiCallForAddNutritionist(addNutritionistRequestBody);
                    cartData.clear();
                } else {
                    List<CartData> cartData = new ArrayList<>();
                    CartData mCartData = new CartData();
                        mCartData.setType(todayItem.getText().toString());
                        mCartData.setUnit("");
                        mCartData.setProduct_id(productId);
                        mCartData.setClaorie_count("0");
                        mCartData.setImg_url("");
                        mCartData.setQty("1.0");
                        mCartData.setProduct_name(productName);
                        cartData.add(mCartData);
                    AddNutritionistRequestBody addNutritionistRequestBody = new AddNutritionistRequestBody();
//                    addNutritionistRequestBody.setUser_id(prefHelper.getUserId());
                    addNutritionistRequestBody.setCart_data(cartData);
                    addNutritionistRequestBody.setLocal_date(currentDate);
                    addNutritionistRequestBody.setRecording_date(currentDate);
                    addNutritionistRequestBody.setIntermittent_hr("yes");
                    apiCallForAddIntermittent(addNutritionistRequestBody);
                    cartData.clear();
                }

            }
        });
        return view;
    }


    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.basket));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        buttonClick();
        setButtonSelection();
        if (getArguments() != null && getArguments().getString(Constants.isFasting).equals("no")) {
            updateAdapter(brandedData);
        } else {
            Branded branded = new Branded();
            branded.setFood_name(productName);
            branded.setServing_qty(1.0);
            branded.setServing_unit("");
            fastingData.add(branded);
            updateFastingAdapter(fastingData);
        }

    }

    private double getTotalCalories() {
        double totalCalories = 0;

        for (Branded element: brandedData) {
            totalCalories = totalCalories + (element.getNf_calories() * element.getServing_qty());
        }

        return totalCalories;
    }

    private void updateAdapter(List<Branded> data) {
        basketItemAdapter = new BasketItemAdapter(myDockActivity, data, getContext(), this, false);
        recyclerView.setLayoutManager((new LinearLayoutManager(myDockActivity, LinearLayoutManager.VERTICAL, false)));
        recyclerView.setAdapter(basketItemAdapter);
    }

    private void updateFastingAdapter(List<Branded> data) {
        basketItemAdapter = new BasketItemAdapter(myDockActivity, data, getContext(), this, true);
        recyclerView.setLayoutManager((new LinearLayoutManager(myDockActivity, LinearLayoutManager.VERTICAL, false)));
        recyclerView.setAdapter(basketItemAdapter);
    }

    private void apiCallForAddNutritionist(AddNutritionistRequestBody addNutritionistRequestBody) {
        getServiceHelper().enqueueCall(getWebService().addCart(ApiHeaderSingleton.apiHeader(requireContext()), addNutritionistRequestBody), WebServiceConstants.ADD_CALORIE, true);
    }

    private void apiCallForAddIntermittent(AddNutritionistRequestBody addNutritionistRequestBody) {
        getServiceHelper().enqueueCall(getWebService().addCartIntermittent(ApiHeaderSingleton.apiHeader(requireContext()), addNutritionistRequestBody), WebServiceConstants.ADD_INTERMITTENT_CALORIE, true);
    }

    @Override
    public void ResponseSuccess(String result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.ADD_CALORIE:
                GenericMsgResponse mContentPojo = GsonFactory.getConfiguredGson().fromJson(result, GenericMsgResponse.class);
                if (mContentPojo != null && mContentPojo.getAllData().size() > 0) {
                    if (mContentPojo.getAllData().get(0).getMessage().equals("success")) {
                        brandedData.clear();
                        myDockActivity.replaceDockableFragment(new NutritionistFragment());
                    }
                }
                break;

            case WebServiceConstants.ADD_INTERMITTENT_CALORIE:
                GenericMsgResponse mContent = GsonFactory.getConfiguredGson().fromJson(result, GenericMsgResponse.class);
                if (mContent != null && mContent.getAllData().size() > 0) {
                    if (mContent.getAllData().get(0).getMessage().equals("success")) {
                        brandedData.clear();
                        myDockActivity.replaceDockableFragment(new InterNutritionistFragment());
                    }
                }
                break;
        }
    }

    private void setButtonSelection() {
        if (basketName.equals("Breakfast")){
            breakfastBtn.setBackgroundColor(requireContext().getResources().getColor(R.color.selectedBoxColor));
            todayItem.setText("Breakfast");
        }
        if (basketName.equals("Lunch")){
            lunchBtn.setBackgroundColor(requireContext().getResources().getColor(R.color.selectedBoxColor));
            todayItem.setText("Lunch");
        }
        if (basketName.equals("Dinner")){
            dinnerBtn.setBackgroundColor(requireContext().getResources().getColor(R.color.selectedBoxColor));
            todayItem.setText("Dinner");
        }
        if (basketName.equals("Snack")){
            lateSnackBtn.setBackgroundColor(requireContext().getResources().getColor(R.color.selectedBoxColor));
            todayItem.setText("Snack");
        }
    }

    private void buttonClick() {
        breakfastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todayItem.setText("Breakfast");
                if (!btnCheck) {
                    breakfastBtn.setBackgroundColor(requireContext().getResources().getColor(R.color.selectedBoxColor));
                    lunchBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    dinnerBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    lateSnackBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    amSnackBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    pmSnackBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    btnCheck = false;
                }

            }
        });
        lunchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todayItem.setText("Lunch");
                if (!btnCheck) {
                    lunchBtn.setBackgroundColor(requireContext().getResources().getColor(R.color.selectedBoxColor));
                    breakfastBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    dinnerBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    lateSnackBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    amSnackBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    pmSnackBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    btnCheck = false;
                }

            }
        });
        dinnerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todayItem.setText("Dinner");
                if (!btnCheck) {
                    dinnerBtn.setBackgroundColor(requireContext().getResources().getColor(R.color.selectedBoxColor));
                    lunchBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    breakfastBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    lateSnackBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    amSnackBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    pmSnackBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    btnCheck = false;
                }

            }
        });
        amSnackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todayItem.setText("AM Snack");
                if (!btnCheck) {
                    amSnackBtn.setBackgroundColor(requireContext().getResources().getColor(R.color.selectedBoxColor));
                    lunchBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    dinnerBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    lateSnackBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    breakfastBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    pmSnackBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    btnCheck = false;
                }

            }
        });

        pmSnackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todayItem.setText("PM Snack");
                if (!btnCheck) {
                    pmSnackBtn.setBackgroundColor(requireContext().getResources().getColor(R.color.selectedBoxColor));
                    lunchBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    dinnerBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    lateSnackBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    amSnackBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    breakfastBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    btnCheck = false;
                }

            }
        });

        lateSnackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                todayItem.setText("Late Snack");
                if (!btnCheck) {
                    lateSnackBtn.setBackgroundColor(requireContext().getResources().getColor(R.color.selectedBoxColor));
                    lunchBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    dinnerBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    pmSnackBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    amSnackBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    breakfastBtn.setBackground(requireContext().getResources().getDrawable(R.drawable.table_border));
                    btnCheck = false;
                }

            }
        });

        tvClearBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                brandedData.clear();
                tvTotalCal.setText("0");
                updateAdapter(brandedData);
            }
        });
    }

    @Override
    public void onItemClick(String value, float totalCal) {
     //   tvTotalCal.setText(String.valueOf(totalCal));
    }

    @Override
    public void onUpdateQuantity(float quantity, int position) {
        //basketItemAdapter.notifyDataSetChanged();
        tvTotalCal.setText(String.valueOf(getTotalCalories()));
    }
}