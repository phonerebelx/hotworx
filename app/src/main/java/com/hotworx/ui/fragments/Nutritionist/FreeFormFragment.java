package com.hotworx.ui.fragments.Nutritionist;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.ToneGenerator;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.JsonObject;
import com.hotworx.R;
import com.hotworx.Singletons.ApiHeaderSingleton;
import com.hotworx.global.Constants;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.CustomEditText;
import com.hotworx.helpers.DrawableClickListener;
import com.hotworx.helpers.UIHelper;
import com.hotworx.helpers.Utils;
import com.hotworx.interfaces.OnFreeFormItemClick;
import com.hotworx.interfaces.OnItemClickInterface;
import com.hotworx.requestEntity.Branded;
import com.hotworx.requestEntity.GenericMsgResponse;
import com.hotworx.requestEntity.GetFavoriteFoodsResponse;
import com.hotworx.requestEntity.GetFoodResponse;
import com.hotworx.requestEntity.SetFavoriteFood;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.ui.adapters.FreeFormAdapter;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.views.TitleBar;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FreeFormFragment extends BaseFragment implements OnFreeFormItemClick {

    Unbinder unbinder;

    @BindView(R.id.search_box)
    CustomEditText searchEt;
    @BindView(R.id.items_rv)
    RecyclerView recyclerView;
    @BindView(R.id.text_linear)
    LinearLayout textLL;
    @BindView(R.id.surface_view)
    SurfaceView surfaceView;
    @BindView(R.id.favorite_foods_btn)
    Button fav_btn;

    FreeFormAdapter freeFormAdapter;

    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    //This class provides methods to play DTMF tones
    private ToneGenerator toneGen1;
    private String barcodeData;
    private String basketName;
    List<Branded> list;
    List<GetFavoriteFoodsResponse.Food_list> favFoodlist;
    ArrayList<Branded> dataList = new ArrayList<Branded>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.free_form_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (getArguments() != null) {
            basketName = getArguments().getString(Constants.BasketName);
            Log.i("xxData", basketName);
        }

        return view;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.free_form));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        List<BrandedFood> data = new ArrayList<>();
//        updateAdapter(data);

        searchEt.setDrawableClickListener(new DrawableClickListener() {
            @Override
            public void onClick(@Nullable DrawablePosition target) {
//                surfaceView.setVisibility(View.VISIBLE);
//                initialiseDetectorsAndSources();
                barcodeLauncher.launch(new ScanOptions());
            }
        });
        fav_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerView.setVisibility(View.GONE);
                FreeFoodFavoriteFragment freeFoodFavoriteFragment = new FreeFoodFavoriteFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BasketName, getArguments().getString(Constants.BasketName));
                freeFoodFavoriteFragment.setArguments(bundle);
                myDockActivity.replaceDockableFragment(freeFoodFavoriteFragment);
            }
        });
    }

    @OnClick(R.id.search_btn)
    public void onClick() {
        if (!Objects.requireNonNull(searchEt.getText()).toString().isEmpty()) {
            apiCallForSearchFood(searchEt.getText().toString());
        } else {
            Utils.customToast(requireContext(), "Please enter food");
        }

    }

    private void apiCallForSearchFood(String text) {
        getCustomServiceHelper().enqueueCall(getWebService().getFood(text), WebServiceConstants.GET_FOOD, true);
    }

    private void apiCallForBarCode(String text) {
        getCustomServiceHelper().enqueueCall(getWebService().getBarCodeData(text), WebServiceConstants.GET_BARCODE_FOOD, true);
    }

    private void apiCallForSetFavoriteFood(SetFavoriteFood data) {
        getServiceHelper().enqueueCall(getWebService().setFavoriteFoods(ApiHeaderSingleton.apiHeader(requireContext()), data), WebServiceConstants.SET_FAVORITE_FOOD, true);
    }

    private void apiCallForGetFavoriteFood() {
        getServiceHelper().enqueueCall(getWebService().getFavoriteFood(ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.GET_FAVORITE_FOOD, true);
    }

    // Register the launcher and result handler
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Toast.makeText(requireActivity(), "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    searchEt.setText(result.getContents());
                    apiCallForBarCode(result.getContents());
                 //   Toast.makeText(requireContext(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                }
            });


    @Override
    public void ResponseSuccess(String result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_FOOD:

                GetFoodResponse mContentPojo = GsonFactory.getConfiguredGson().fromJson(result, GetFoodResponse.class);

                list = mContentPojo.getBranded();
                apiCallForGetFavoriteFood();
                UIHelper.hideSoftKeyboard(getDockActivity(), getDockActivity()
                        .getWindow().getDecorView());

                break;

            case WebServiceConstants.GET_BARCODE_FOOD:
                GetFoodResponse mContentPojoBarcode = GsonFactory.getConfiguredGson().fromJson(result, GetFoodResponse.class);
                list = mContentPojoBarcode.getFoods();
                textLL.setVisibility(View.GONE);
                apiCallForGetFavoriteFood();
                UIHelper.hideSoftKeyboard(getDockActivity(), getDockActivity()
                        .getWindow().getDecorView());
                break;

            case WebServiceConstants.SET_FAVORITE_FOOD:
                GenericMsgResponse mContent = GsonFactory.getConfiguredGson().fromJson(result, GenericMsgResponse.class);
                UIHelper.showLongToastInCenter(requireContext(), R.string.message_success_fav_food_added);
                break;

            case WebServiceConstants.GET_FAVORITE_FOOD:

                GetFavoriteFoodsResponse mContentPojo2 = GsonFactory.getConfiguredGson().fromJson(result, GetFavoriteFoodsResponse.class);
                if (mContentPojo2 != null && mContentPojo2.getAllData().size() > 0) {
                    favFoodlist = (List<GetFavoriteFoodsResponse.Food_list>) mContentPojo2.getAllData().get(0).getFood_list();
                    textLL.setVisibility(View.GONE);
                    updateAdapter(list);
                    UIHelper.hideSoftKeyboard(getDockActivity(), getDockActivity().getWindow().getDecorView());
                }
                break;
        }
    }

    @Override
    public void ResponseFailure(String message, String tag) {
        super.ResponseFailure(message, tag);
    }

    private void updateAdapter(List<Branded> data) {
        freeFormAdapter = new FreeFormAdapter(myDockActivity, data, favFoodlist, getContext(), this);
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager((new LinearLayoutManager(myDockActivity, LinearLayoutManager.VERTICAL, false)));
        recyclerView.setAdapter(freeFormAdapter);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (list != null){
            textLL.setVisibility(View.GONE);
//            updateAdapter(list);
            recyclerView.setVisibility(View.GONE);
            apiCallForGetFavoriteFood();
        }

    }

    @Override
    public void onItemClick(String user_id, String product_id, Boolean is_fav) {
        SetFavoriteFood data = new SetFavoriteFood();
//        data.setUser_id(user_id);
        data.setProduct_id(product_id);
        data.setIs_fav(is_fav);
        apiCallForSetFavoriteFood(data);
    }

    @Override
    public void onItemClick(String value) {
        Branded branded = GsonFactory.getConfiguredGson().fromJson(value, Branded.class);
        dataList.add(branded);
        BasketFragment basketFragment = new BasketFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.FreeFormData, (Serializable) dataList);
        bundle.putString(Constants.BasketName, basketName);
        bundle.putString(Constants.isFasting, "no");
        basketFragment.setArguments(bundle);
        myDockActivity.replaceDockableFragment(basketFragment);
    }


}