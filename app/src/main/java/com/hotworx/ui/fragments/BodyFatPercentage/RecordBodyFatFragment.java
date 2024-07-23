package com.hotworx.ui.fragments.BodyFatPercentage;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hotworx.R;
import com.hotworx.Singletons.ApiHeaderSingleton;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.Utils;
import com.hotworx.interfaces.OnItemClickInterface;
import com.hotworx.requestEntity.AnsModel;
import com.hotworx.requestEntity.ImageUploadResponse;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.ui.adapters.MyCustomAdapter;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.views.TitleBar;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;

public class RecordBodyFatFragment extends BaseFragment implements OnItemClickInterface {
    Unbinder unbinder;
    @BindView(R.id.start_date)
    TextView date_start;
    @BindView(R.id.upload_cam)
    ImageView upload_cam;
    @BindView(R.id.btnRecordFat)
    Button btnRecordFat;
    @BindView(R.id.etWeight)
    EditText etWeight;
    @BindView(R.id.etBodyFat)
    EditText etPercent;
    @BindView(R.id.lvAns)
    ListView lvAns;

    //Constants
    String answer = "";
    private static final String TAG_FILE = "file_upload";
    String file_uploaded_url;
    String dateVal;
    File file = null;
    Calendar calendar;
    private ArrayList<AnsModel> ansList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_body_fat, container, false);
        unbinder = ButterKnife.bind(this, view);
        calendar = Calendar.getInstance();
        upload_cam.setImageResource(R.drawable.camera);
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        String formattedDate = df.format(calendar.getTime());
        date_start.setText(formattedDate);
        dateVal = formattedDate;
        ansList = new ArrayList<>();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
    }

    private void initAdapter() {
        ansList.add(new AnsModel(getString(R.string.my_diet_has_remained_the_same), false));
        ansList.add(new AnsModel(getString(R.string.i_ve_consumed_less_calories), false));
        ansList.add(new AnsModel(getString(R.string.more_cal), false));
        ansList.add(new AnsModel(getString(R.string.i_have_a_well_balance_diet_with_restricted_calorie_intake), false));
        lvAns.setAdapter(new MyCustomAdapter(myDockActivity, ansList));
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.record));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    //    unbinder.unbind();
    }


    @Override
    public void onItemClick(String value) {
        answer = value;
    }

    @OnClick({R.id.start_date, R.id.upload_cam, R.id.btnRecordFat})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_date:
                new DatePickerDialog(myDockActivity, date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
                break;

            case R.id.upload_cam:
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON)
                        .setAllowFlipping(false)
                        .setAllowRotation(false)
                        .setCropMenuCropButtonIcon(R.drawable.crop_icon)
                        .setMinCropWindowSize(700, 700)
                        .start(getContext(), RecordBodyFatFragment.this);
                break;

            case R.id.btnRecordFat:
                uploadImage();
                break;
        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            dateVal = monthOfYear + 1 + "/" + dayOfMonth + "/" + "" + year;
            date_start.setText(dateVal);
        }

    };

    private void uploadImage() {
        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"),prefHelper.getUserId());
        if(file!=null) {
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("uploaded_file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
            Log.i("xxPart", String.valueOf(filePart));
            getServiceHelper().enqueueCall(getWebService().uploadImage(ApiHeaderSingleton.apiHeader(requireContext()),filePart), WebServiceConstants.UPLOAD_IMAGE,true);
        } else {
            Utils.customToast(myDockActivity, "Please upload photo of scale");
        }
    }

    @Override
    public void ResponseSuccess(String result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.UPLOAD_IMAGE:
                ImageUploadResponse response = GsonFactory.getConfiguredGson().fromJson(result, ImageUploadResponse.class);
                if (response != null && response.getAllData().size() > 0) {
                    if (!response.getAllData().get(0).getFilepath().isEmpty()) {
                        apiCallToSaveMonthData(response.getAllData().get(0).getFilepath());
                    } else {
                        Utils.customToast(myDockActivity, response.getMessage());
                    }
                } else {
                    Utils.customToast(myDockActivity, "Failed to upload image. Please try again");
                }

                break;

            case WebServiceConstants.RECORD_MONTHLY_BODY_FAT:
                Utils.customToast(myDockActivity, getString(R.string.data_saved));
                myDockActivity.popFragment();
                break;
        }
    }

    private void apiCallToSaveMonthData(String filePath){
       getServiceHelper().enqueueCall(getWebService().recordMonthlyBodyFat(
               ApiHeaderSingleton.apiHeader(requireContext()), etWeight.getText().toString(),etPercent.getText().toString(),
               dateVal,filePath,answer),WebServiceConstants.RECORD_MONTHLY_BODY_FAT,true);
    }

//    @Override
//    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
//        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
//            final CropImage.ActivityResult result = CropImage.getActivityResult(data);
//            if (resultCode == RESULT_OK) {
//                Uri imageUri = result.getUri();
//                upload_cam.setImageURI(imageUri);
//                this.file = new File(imageUri.getPath());
//            }
//            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
//                Utils.customToast(myDockActivity, "Failed to get image");
//            }
//        }
//    }
}
