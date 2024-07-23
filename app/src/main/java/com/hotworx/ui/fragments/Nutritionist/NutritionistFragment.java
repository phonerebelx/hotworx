package com.hotworx.ui.fragments.Nutritionist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hotworx.R;
import com.hotworx.Singletons.ApiHeaderSingleton;
import com.hotworx.global.Constants;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.Utils;
import com.hotworx.interfaces.OnNutritionistItemClick;
import com.hotworx.requestEntity.AddExerciseDataModel;
import com.hotworx.requestEntity.CartDataExercise;
import com.hotworx.requestEntity.DayData;
import com.hotworx.requestEntity.DeleteCalRequestBody;
import com.hotworx.requestEntity.ExerciseData;
import com.hotworx.requestEntity.GenericMsgResponse;
import com.hotworx.requestEntity.GetExerciseDataResponse;
import com.hotworx.requestEntity.NutritionCaloriesResponse;
import com.hotworx.requestEntity.ParentNutritionistItem;
import com.hotworx.requestEntity.UserData;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.ui.adapters.NutritionExerciseParentAdapter;
import com.hotworx.ui.adapters.NutritionistChildItemAdapter;
import com.hotworx.ui.adapters.NutritionistExerciseAdapter;
import com.hotworx.ui.adapters.NutritionistParentItemAdapter;
import com.hotworx.ui.fragments.AboutUsFragment;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.fragments.HomeFragment;
import com.hotworx.ui.views.TitleBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class NutritionistFragment extends BaseFragment implements OnNutritionistItemClick {

    Calendar calendar;
    String formattedDate;
    String currentDate;
    String utcDate;
    SimpleDateFormat dmFormat;
    SimpleDateFormat ymdFormat;
    private NutritionistParentItemAdapter nutritionistParentItemAdapter;
    private NutritionistExerciseAdapter exerciseParentAdapter;

    Unbinder unbinder;
    @BindView(R.id.date_tv)
    TextView dateTv;
    @BindView(R.id.backward_iv)
    ImageView backDateIv;
    @BindView(R.id.forward_iv)
    ImageView forwardDateIv;

    @BindView(R.id.fat_pb)
    ProgressBar fatPb;
    @BindView(R.id.carbs_pb)
    ProgressBar carbsPb;
    @BindView(R.id.protein_pb)
    ProgressBar proteinPb;

    @BindView(R.id.calIntake_tv)
    TextView tvCalIntake;
    @BindView(R.id.calBurned_tv)
    TextView tvCalBurned;
    @BindView(R.id.remainingCal_tv)
    TextView tvRemainingCal;
    @BindView(R.id.protein_tv)
    TextView tvProtein;
    @BindView(R.id.carbs_tv)
    TextView tvCarbs;
    @BindView(R.id.fats_tv)
    TextView tvFats;
    @BindView(R.id.currentWeight_tv)
    TextView tvCurrentWeight;
    @BindView(R.id.bmrCalBurned_tv)
    TextView tvBmrCalBurned;
    @BindView(R.id.total_cal_exercise)
    TextView exerciseCalTv;

    @BindView(R.id.breakfast_rv)
    RecyclerView breakfastRv;
    @BindView(R.id.exercise_rv)
    RecyclerView exerciseRv;

    @BindView(R.id.aboutUs_Iv)
    ImageView aboutUs;
    @BindView(R.id.add_exercise)
    ImageView addExercise;

    @BindView(R.id.view_summary_tv)
    TextView viewSummary;
    @BindView(R.id.ll_weight)
    LinearLayout llWeight;
    @BindView(R.id.ll_height)
    LinearLayout llHeight;
    @BindView(R.id.ll_main_progress)
    LinearLayout llMainProgress;
    @BindView(R.id.ll_exercise)
    LinearLayout llExercise;

    @BindView(R.id.green_view)
    View greenView;
    @BindView(R.id.yellow_view)
    View yellowView;
    @BindView(R.id.red_view)
    View redView;

    List<DayData> breakfastList = new ArrayList<>();
    List<DayData> lunchList = new ArrayList<>();
    List<DayData> dinnerList = new ArrayList<>();
    List<DayData> snacksList = new ArrayList<>();
    List<ExerciseData> exerciseList = new ArrayList<>();
    ArrayList<CartDataExercise> cartDataExercises = new ArrayList<>();

    int breakfastTotalCal = 0;
    int lunchTotalCal = 0;
    int dinnerTotalCal = 0;
    int snackTotalCal = 0;
    int exerciseTotalCal = 0;
    String weightResponse = "";
    String heightResponse = "";
    String dobResponse = "";
    String sessionName = "";
    String calBurn = "";
    String time = "";
    Dialog dialog;
    Dialog ExerciseDialog;
    int greenPercentageValue = 0;
    int redPercentageValue = 0;
    int yellowPercentageValue = 0;
    AddExerciseDataModel addExerciseDataModel = new AddExerciseDataModel();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.nutritionist_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        calendar = Calendar.getInstance();
        dialog = new Dialog(requireContext());
        ExerciseDialog = new Dialog(requireContext());

        dmFormat = new SimpleDateFormat("MM/dd");
        ymdFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        formattedDate = dmFormat.format(calendar.getTime());
        currentDate = dmFormat.format(calendar.getTime());
        utcDate = ymdFormat.format(calendar.getTime());

        dateTv.setText(String.format("Today,%s", formattedDate));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        previousDate();
        forwardDate();
        apiCallForNutritionCalories(utcDate);
        setNavigation();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.diettrax));
    }

    public void previousDate() {
        backDateIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar.add(Calendar.DATE, -1);
                formattedDate = dmFormat.format(calendar.getTime());
                utcDate = ymdFormat.format(calendar.getTime());
                dateTv.setText(formattedDate);
                apiCallForNutritionCalories(utcDate);
            }
        });
    }

    public void forwardDate() {
        forwardDateIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (formattedDate.equals(currentDate)) {
                    dateTv.setText(String.format("Today,%s", currentDate));
                    addExercise.setVisibility(View.VISIBLE);
                } else {
                    calendar.add(Calendar.DATE, 1);
                    formattedDate = dmFormat.format(calendar.getTime());
                    utcDate = ymdFormat.format(calendar.getTime());
                    dateTv.setText(formattedDate);
                    apiCallForNutritionCalories(utcDate);
                }
            }
        });
    }

    private void apiCallForNutritionCalories(String date) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//        dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
//        try {
//            Date utcDate = dateFormat.parse(date);
//            assert utcDate != null;
//            getServiceHelper().enqueueCall(getWebService().getNutritionCalories(prefHelper.getUserId(), dateFormat.format(utcDate)
//            ,date,"yes"), WebServiceConstants.GET_NUTRITION_CALORIES, true);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        getServiceHelper().enqueueCall(getWebService().getNutritionCalories(ApiHeaderSingleton.apiHeader(requireContext()), date,
                date, "yes"), WebServiceConstants.GET_NUTRITION_CALORIES, true);
    }

    private void setNavigation() {

        viewSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDockActivity.replaceDockableFragment(new NutritionistSummaryFragment());
            }
        });

        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDockActivity.replaceDockableFragment(new AboutUsFragment());

            }
        });
        llWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProfileDialog(false);
            }
        });
        llHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProfileDialog(false);
            }
        });

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addExerciseDialog();
            }
        });

    }

    @Override
    public void ResponseSuccess(String result, String Tag) {
        if (!isAdded()) return;
        switch (Tag) {
            case WebServiceConstants.GET_NUTRITION_CALORIES:

                NutritionCaloriesResponse mContentPojo = GsonFactory.getConfiguredGson().fromJson(result, NutritionCaloriesResponse.class);

                if (mContentPojo.getAllData().get(0).getUser_data() != null) {
                    int weight = Integer.parseInt(mContentPojo.getAllData().get(0).getUser_data().getWeight());
                    float height = Float.parseFloat(mContentPojo.getAllData().get(0).getUser_data().getHeight());

                    if (weight <= 0 || height <= 0) {
                        showProfileDialog(true);
                    }

                    setDashboardData(mContentPojo.getAllData().get(0).getUser_data());
                    setMainProgress(greenPercentageValue, yellowPercentageValue, redPercentageValue);
                }

                clearData();

                formattedDate = dmFormat.format(calendar.getTime());

                if (formattedDate.equals(currentDate)) {
                    dateTv.setText(String.format("Today,%s", currentDate));
                    addExercise.setVisibility(View.VISIBLE);
                } else {
                    addExercise.setVisibility(View.GONE);
                }

                if (mContentPojo.getAllData().get(0).getDay_data().size() != 0) {
                    for (DayData element : mContentPojo.getAllData().get(0).getDay_data()) {
                        if (element.getType().equalsIgnoreCase("breakfast")) {
                            breakfastList.add(element);
                            breakfastTotalCal = breakfastTotalCal + Math.round(Float.parseFloat(element.getClaorie_count()));
                        }
                        if (element.getType().equalsIgnoreCase("lunch")) {
                            lunchList.add(element);
                            lunchTotalCal = lunchTotalCal + Math.round(Float.parseFloat(element.getClaorie_count()));
                        }
                        if (element.getType().equalsIgnoreCase("dinner")) {
                            dinnerList.add(element);
                            dinnerTotalCal = dinnerTotalCal + Math.round(Float.parseFloat(element.getClaorie_count()));
                        }
                        if (element.getType().equalsIgnoreCase("snack") ||
                                element.getType().equalsIgnoreCase("late snack") ||
                                element.getType().equalsIgnoreCase("am snack") ||
                                element.getType().equalsIgnoreCase("pm snack")) {
                            snacksList.add(element);
                            snackTotalCal = snackTotalCal + Math.round(Float.parseFloat(element.getClaorie_count()));
                        }
                    }
                }


                if (breakfastList.isEmpty()) {
                    breakfastList.add(new DayData());
                }
                if (lunchList.isEmpty()) {
                    lunchList.add(new DayData());
                }
                if (dinnerList.isEmpty()) {
                    dinnerList.add(new DayData());
                }
                if (snacksList.isEmpty()) {
                    snacksList.add(new DayData());
                }
                updateAdapterNutrition(ParentItemList());

                if (mContentPojo.getAllData().get(0).getExercise_data() != null) {
                    exerciseList = mContentPojo.getAllData().get(0).getExercise_data();
                    for (ExerciseData element : exerciseList) {
                        exerciseTotalCal = exerciseTotalCal + Math.round(Float.parseFloat(element.getCalorie_burn()));
                    }
                }
               if (!exerciseList.isEmpty()) updateAdapterExercise(exerciseList);

                break;

            case WebServiceConstants.DELETE_NUTRITION:
                GenericMsgResponse responseDeleteNutrition = GsonFactory.getConfiguredGson().fromJson(result, GenericMsgResponse.class);
                if (!responseDeleteNutrition.getAllData().isEmpty()) {
                    breakfastList.clear();
                    lunchList.clear();
                    dinnerList.clear();
                    snacksList.clear();
                    apiCallForNutritionCalories(utcDate);
                }

                break;

            case WebServiceConstants.DELETE_EXERCISE:
                GenericMsgResponse responseDeleteExercise = GsonFactory.getConfiguredGson().fromJson(result, GenericMsgResponse.class);
                if (!responseDeleteExercise.getAllData().isEmpty()) {
                    exerciseList.clear();
                    apiCallForNutritionCalories(utcDate);
                }
                break;

            case WebServiceConstants.SET_HEIGHT_WEIGHT:
                GenericMsgResponse response = GsonFactory.getConfiguredGson().fromJson(result, GenericMsgResponse.class);
                if (!response.getAllData().isEmpty()) {
                    dialog.dismiss();
                }
                break;

            case WebServiceConstants.GET_EXERCISE_DATA:
                GetExerciseDataResponse exerciseResponse = GsonFactory.getConfiguredGson().fromJson(result, GetExerciseDataResponse.class);
                if (exerciseResponse.getExercises() != null) {
                    sessionName = exerciseResponse.getExercises().get(0).getName();
                    calBurn = String.valueOf(exerciseResponse.getExercises().get(0).getNf_calories());
                    time = String.valueOf(exerciseResponse.getExercises().get(0).getDuration_min());
//                    addExerciseDataModel.setUser_id(prefHelper.getUserId());
                    addExerciseDataModel.setLocal_date(utcDate);
                    addExerciseDataModel.setRecording_date(utcDate);

                    CartDataExercise exerciseCartData = new CartDataExercise(sessionName, Float.parseFloat(calBurn), Integer.parseInt(time));
                    cartDataExercises.add(exerciseCartData);
                    addExerciseDataModel.setCart_data(cartDataExercises);
                    apiCallForAddExercise(addExerciseDataModel);
                }
                break;

            case WebServiceConstants.ADD_EXERCISE:
                GenericMsgResponse addExerciseResponse = GsonFactory.getConfiguredGson().fromJson(result, GenericMsgResponse.class);
                if (!addExerciseResponse.getAllData().isEmpty()) {
                    ExerciseDialog.dismiss();
                    apiCallForNutritionCalories(utcDate);
                }
                break;
        }


    }

    public void setMainProgress(int green, int yellow, int red) {
        int totalWidth = llMainProgress.getMeasuredWidth();
        setViewWidth(green / 100.0F * totalWidth, greenView);
        setViewWidth(yellow / 100.0F * totalWidth, yellowView);
        setViewWidth(red / 100.0F * totalWidth, redView);
    }

    private void setDashboardData(UserData mContentPojo) {

        weightResponse = mContentPojo.getWeight();
        heightResponse = mContentPojo.getHeight();
        dobResponse = mContentPojo.getDob();

        tvCalIntake.setText(mContentPojo.getCal_intake());
        tvCalBurned.setText(mContentPojo.getCal_burn());
        tvRemainingCal.setText(mContentPojo.getCal_remain());
        tvBmrCalBurned.setText(mContentPojo.getCal_bmr());
        tvFats.setText(mContentPojo.getTotal_fats() + "g");
        tvCarbs.setText(mContentPojo.getTotal_carbs() + "g");
        tvProtein.setText(mContentPojo.getTotal_proteins() + "g");
        tvCurrentWeight.setText(mContentPojo.getWeight() + " lbs");
        tvBmrCalBurned.setText(mContentPojo.getCal_bmr());


        greenPercentageValue = Integer.parseInt(mContentPojo.getGreen_percentage_value());
        redPercentageValue = Integer.parseInt(mContentPojo.getRed_percentage_value());
        yellowPercentageValue = Integer.parseInt(mContentPojo.getYellow_percentage_value());
        if (!"NAN".equals(mContentPojo.getProteins_percentage_value()))
            proteinPb.setProgress(Integer.parseInt(mContentPojo.getProteins_percentage_value()));
        if (!"NAN".equals(mContentPojo.getFats_percentage_value()))
            fatPb.setProgress(Integer.parseInt(mContentPojo.getFats_percentage_value()));
        if (!"NAN".equals(mContentPojo.getCarbs_percentage_value()))
            carbsPb.setProgress(Integer.parseInt(mContentPojo.getCarbs_percentage_value()));

    }

    private void clearData() {
        breakfastList.clear();
        lunchList.clear();
        dinnerList.clear();
        snacksList.clear();
        exerciseList.clear();
        breakfastTotalCal = 0;
        lunchTotalCal = 0;
        dinnerTotalCal = 0;
        snackTotalCal = 0;
        exerciseTotalCal = 0;
    }

    private List<ParentNutritionistItem> ParentItemList() {
        List<ParentNutritionistItem> itemList
                = new ArrayList<>();

        ParentNutritionistItem item
                = new ParentNutritionistItem(
                "Breakfast",
                String.valueOf(breakfastTotalCal),
                breakfastList,
                Collections.emptyList());
        itemList.add(item);
        ParentNutritionistItem item1
                = new ParentNutritionistItem(
                "Lunch",
                String.valueOf(lunchTotalCal),
                lunchList,
                Collections.emptyList());
        itemList.add(item1);
        ParentNutritionistItem item2
                = new ParentNutritionistItem(
                "Dinner",
                String.valueOf(dinnerTotalCal),
                dinnerList,
                Collections.emptyList());
        itemList.add(item2);
        ParentNutritionistItem item3
                = new ParentNutritionistItem(
                "Snack",
                String.valueOf(snackTotalCal),
                snacksList,
                Collections.emptyList());
        itemList.add(item3);

        return itemList;
    }

    private void setViewWidth(Float width, View view) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = Math.round(width);
        view.setLayoutParams(layoutParams);
    }

    private void apiCallForDeleteNutritionItem(DeleteCalRequestBody body) {
        getServiceHelper().enqueueCall(getWebService().deleteNutritionCalories(ApiHeaderSingleton.apiHeader(requireContext()), body), WebServiceConstants.DELETE_NUTRITION, true);
    }

    private void apiCallForDeleteExerciseItem(DeleteCalRequestBody body) {
        getServiceHelper().enqueueCall(getWebService().deleteExercise(ApiHeaderSingleton.apiHeader(requireContext()), body), WebServiceConstants.DELETE_NUTRITION, true);
    }

    private void apiCallForUpdateHeightAndWeight(String user_id, String weight, String height, String dob) {
        getServiceHelper().enqueueCall(getWebService().setWeightHeight(ApiHeaderSingleton.apiHeader(requireContext()), weight, height, dob), WebServiceConstants.SET_HEIGHT_WEIGHT, true);
    }

    private void apiCallForAddExercise(AddExerciseDataModel addExerciseDataModel) {
        getServiceHelper().enqueueCall(getWebService().addExercise(ApiHeaderSingleton.apiHeader(requireContext()), addExerciseDataModel), WebServiceConstants.ADD_EXERCISE, true);
    }

    private void apiCallForGetExerciseCalData(String text) {
        getCustomServiceHelper().enqueueCall(getWebService().getExerciseCalData(text), WebServiceConstants.GET_EXERCISE_DATA, true);
    }

    private void updateAdapterNutrition(List<ParentNutritionistItem> dayData) {
        if (dayData.size() != 0) {
            nutritionistParentItemAdapter = new NutritionistParentItemAdapter(myDockActivity, getContext(), this, formattedDate);
            nutritionistParentItemAdapter.addAll(dayData);
            breakfastRv.setAdapter(nutritionistParentItemAdapter);
        }
    }

    private void updateAdapterExercise(List<ExerciseData> dayData) {
        exerciseParentAdapter = new NutritionistExerciseAdapter(getContext(), formattedDate, this);
        exerciseCalTv.setText(String.valueOf(exerciseTotalCal));
        llExercise.setVisibility(View.VISIBLE);

        if (dayData.size() != 0) {
            exerciseParentAdapter.addAll(dayData);
        } else {
            exerciseList.add(new ExerciseData());
            exerciseParentAdapter.addAll(exerciseList);
        }

        exerciseRv.setAdapter(exerciseParentAdapter);
    }

    private void showProfileDialog(Boolean check) {
        dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.nutritionist_profile);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        EditText weightEt = dialog.findViewById(R.id.weightEt);
        EditText heightEt = dialog.findViewById(R.id.heightEt);
        TextView tvAddToCalender = dialog.findViewById(R.id.start_date);
        Button cancelBtn = dialog.findViewById(R.id.cancel_btn);
        Button updateBtn = dialog.findViewById(R.id.update_btn);

        weightEt.setText(weightResponse);
        heightEt.setText(heightResponse);
        tvAddToCalender.setText(dobResponse);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                utcDate = monthOfYear + 1 + "-" + dayOfMonth + "-" + "" + year;
                tvAddToCalender.setText(utcDate);
            }

        };

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check && weightEt.getText().equals("") && tvAddToCalender.getText().equals("") && heightEt.getText().equals("")) {
                    Utils.customToast(requireContext(), "Please enter details");
                } else {
                    dialog.dismiss();
                }

            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiCallForUpdateHeightAndWeight(prefHelper.getUserId(), weightEt.getText().toString(), heightEt.getText().toString(), tvAddToCalender.getText().toString());
            }
        });
        tvAddToCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(myDockActivity, date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        dialog.show();
    }

    private void addExerciseDialog() {
        if (ExerciseDialog.isShowing()) ExerciseDialog.dismiss();

        ExerciseDialog.setContentView(R.layout.add_exercise_layout);
        ExerciseDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ExerciseDialog.setCancelable(false);
        ExerciseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ExerciseDialog.setCanceledOnTouchOutside(false);

        Button cancelBtn = ExerciseDialog.findViewById(R.id.cancel_btn);
        Button saveBtn = ExerciseDialog.findViewById(R.id.save_btn);
        EditText exerciseET = ExerciseDialog.findViewById(R.id.exercise_et);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!exerciseET.getText().toString().isEmpty()) {
                    apiCallForGetExerciseCalData(exerciseET.getText().toString());
                    ExerciseDialog.dismiss();
                } else {
                    Utils.customToast(requireContext(), "PLease add exercise");
                }

            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExerciseDialog.dismiss();
            }
        });

        ExerciseDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        breakfastList.clear();
        lunchList.clear();
        dinnerList.clear();
        snacksList.clear();
    }

    @Override
    public void onBackPressed() {
        myDockActivity.emptyBackStack();
        myDockActivity.replaceDockableFragment(new HomeFragment(), Constants.HomeFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        requireView().setFocusable(true);
        requireView().setFocusableInTouchMode(true);
        requireView().requestFocus();
        requireView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    myDockActivity.emptyBackStack();
                    myDockActivity.replaceDockableFragment(new HomeFragment(), Constants.HomeFragment);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onItemClick(int position, String tag) {
        DeleteCalRequestBody deleteCalRequestBody = new DeleteCalRequestBody();
        Log.i("xxTag", tag);
        if (tag.equalsIgnoreCase("addBreakfast")) {
            FreeFormFragment freeFormFragment = new FreeFormFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.BasketName, "Breakfast");
            freeFormFragment.setArguments(bundle);
            myDockActivity.replaceDockableFragment(freeFormFragment);
        }
        if (tag.equalsIgnoreCase("addLunch")) {
            FreeFormFragment freeFormFragment = new FreeFormFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.BasketName, "Lunch");
            freeFormFragment.setArguments(bundle);
            myDockActivity.replaceDockableFragment(freeFormFragment);
        }
        if (tag.equalsIgnoreCase("addDinner")) {
            FreeFormFragment freeFormFragment = new FreeFormFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.BasketName, "Dinner");
            freeFormFragment.setArguments(bundle);
            myDockActivity.replaceDockableFragment(freeFormFragment);
        }
        if (tag.equalsIgnoreCase("addSnack")) {
            FreeFormFragment freeFormFragment = new FreeFormFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.BasketName, "Snack");
            freeFormFragment.setArguments(bundle);
            myDockActivity.replaceDockableFragment(freeFormFragment);
        }
        if (tag.equalsIgnoreCase("breakfast")) {
//            deleteCalRequestBody.setUser_id(prefHelper.getUserId());
            deleteCalRequestBody.setRecord_id(breakfastList.get(position).getRecord_id());
            nutritionistParentItemAdapter.notifyDataSetChanged();
            apiCallForDeleteNutritionItem(deleteCalRequestBody);
        }
        if (tag.equalsIgnoreCase("lunch")) {
//            deleteCalRequestBody.setUser_id(prefHelper.getUserId());
            deleteCalRequestBody.setRecord_id(lunchList.get(position).getRecord_id());
            nutritionistParentItemAdapter.notifyDataSetChanged();
            apiCallForDeleteNutritionItem(deleteCalRequestBody);
        }
        if (tag.equalsIgnoreCase("dinner")) {
//            deleteCalRequestBody.setUser_id(prefHelper.getUserId());
            deleteCalRequestBody.setRecord_id(dinnerList.get(position).getRecord_id());
            nutritionistParentItemAdapter.notifyDataSetChanged();
            apiCallForDeleteNutritionItem(deleteCalRequestBody);
        }
        if (tag.equalsIgnoreCase("exercise")) {
//            deleteCalRequestBody.setUser_id(prefHelper.getUserId());
            deleteCalRequestBody.setRecord_id(exerciseList.get(position).getRecord_id());
            exerciseParentAdapter.notifyDataSetChanged();
            apiCallForDeleteExerciseItem(deleteCalRequestBody);
        }
        if (tag.equalsIgnoreCase("snack") ||
                tag.equalsIgnoreCase("late snack") ||
                tag.equalsIgnoreCase("am snack") ||
                tag.equalsIgnoreCase("pm snack")) {
//            deleteCalRequestBody.setUser_id(prefHelper.getUserId());
            deleteCalRequestBody.setRecord_id(snacksList.get(position).getRecord_id());
            nutritionistParentItemAdapter.notifyDataSetChanged();
            apiCallForDeleteNutritionItem(deleteCalRequestBody);
        }
    }
}

