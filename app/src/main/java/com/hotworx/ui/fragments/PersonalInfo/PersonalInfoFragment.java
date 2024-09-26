package com.hotworx.ui.fragments.PersonalInfo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hotworx.R;
import com.hotworx.Singletons.ApiHeaderSingleton;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.Utils;
import com.hotworx.requestEntity.ViewProfileResponse;
import com.hotworx.requestEntity.tempModel;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.views.TitleBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PersonalInfoFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.per_dob)
    TextView date_start;
    @BindView(R.id.per_spinner_gender)
    Spinner per_spinner_gender;
    @BindView(R.id.per_user_name)
    EditText user_name;
    @BindView(R.id.per_first_name)
    EditText first_name;
    @BindView(R.id.per_last_name)
    EditText last_name;
    @BindView(R.id.per_email)
    EditText email;
    @BindView(R.id.per_address)
    EditText address;
    @BindView(R.id.per_height)
    EditText height;
    @BindView(R.id.per_weight)
    EditText weight;
    @BindView(R.id.per_send)
    Button edit_info;

    //Constants
    Calendar calendar;
    private static String TAG = "personal";
    String login_id = "";
    String val_gender;
    String[] gender = new String[]{"male", "female"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_info, container, false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        setValues();
        apiCallForViewProfile();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.personal_info));
    }

    private void initViews(){
        per_spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                val_gender = gender[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                val_gender = gender[0];
            }
        });

        calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String formattedDate = df.format(calendar.getTime());
        date_start.setText(formattedDate);
    }

    private void setValues(){
        ViewProfileResponse userObject = prefHelper.getLoginData();
        if (userObject != null) {

            if (userObject.getLogin_id() != null)
                login_id = userObject.getLogin_id();

            if (userObject.getFull_name() != null && userObject.getFull_name().length() > 1)
                user_name.setText(userObject.getFull_name());
            else {
                if (login_id != null)
                    user_name.setText(userObject.getLogin_id());
            }

            if (userObject.getFirst_name() != null)
                first_name.setText(userObject.getFirst_name());

            if (userObject.getLast_name() != null)
                last_name.setText(userObject.getLast_name());

            if (userObject.getEmail() != null)
                email.setText(userObject.getEmail());

            if (userObject.getHeight() != null)
                height.setText(userObject.getHeight());

            if (userObject.getWeight() != null)
                weight.setText(userObject.getWeight());

            if (userObject.getAddress() != null)
                address.setText(userObject.getAddress());

            if (userObject.getGender() != null && (userObject.getGender().equals("male") || userObject.getGender().equals("Male")
                    || userObject.getGender().equals("MALE")))
                per_spinner_gender.setSelection(0);
            else per_spinner_gender.setSelection(1);

            if (userObject.getDob() != null) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                Date myDate = null;
                try {
                    myDate = df.parse(userObject.getDob());

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                df = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
                String finalDate = df.format(myDate);
                System.out.println(finalDate);
                date_start.setText(finalDate);
            }
        }
    }

    @OnClick({R.id.per_dob,R.id.per_send})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.per_dob:
                new DatePickerDialog(myDockActivity, date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
                break;

            case R.id.per_send:
                user_name.setEnabled(true);
                first_name.setEnabled(true);
                last_name.setEnabled(true);
                email.setEnabled(false);
                height.setEnabled(true);
                weight.setEnabled(true);
                user_name.setFocusable(true);
                apiCallForSaveInfo();
                break;
        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            String date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + "" + year;
            date_start.setText(date);
        }

    };

    @Override
    public void onDestroy() {
        super.onDestroy();
       // unbinder.unbind();
    }

    private void apiCallForViewProfile(){
     getServiceHelper().enqueueCall(getWebService().viewUserProfile(ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.VIEW_PROFILE,true);
    }

    private void apiCallForSaveInfo(){
     getServiceHelper().enqueueCall(getWebService().updateProfile(ApiHeaderSingleton.apiHeader(requireContext()),first_name.getText().toString(),
             last_name.getText().toString(),email.getText().toString(),date_start.getText().toString(),
             val_gender,height.getText().toString(), weight.getText().toString(),address.getText().toString()),
             WebServiceConstants.SAVE_PROFILE,true);
    }


    @Override
    public void ResponseSuccess(String result, String Tag) {
        switch (Tag){
            case WebServiceConstants.VIEW_PROFILE:
                tempModel mContentPojo = GsonFactory.getConfiguredGson().fromJson(result.toString(), tempModel.class);
                setValues();
//                if(mContentPojo.isStatus()){
////                    prefHelper.putLoginData(mContentPojo.getData());
//                    setValues();
//                }else{
//                    Utils.customToast(myDockActivity, mContentPojo.getMessage());
//                }
                break;

            case  WebServiceConstants.SAVE_PROFILE:
                ViewProfileResponse mPojo = GsonFactory.getConfiguredGson().fromJson(result.toString(), ViewProfileResponse.class);
                if (mPojo != null) {
                    Log.e(TAG, "login resultReceived-------- " + mPojo.toString());
                    Utils.customToast(myDockActivity, mPojo.getMessage());
                    myDockActivity.popFragment();
                } else
                    Utils.customToast(myDockActivity, getString(R.string.error_failure));
                break;
        }
    }
}
