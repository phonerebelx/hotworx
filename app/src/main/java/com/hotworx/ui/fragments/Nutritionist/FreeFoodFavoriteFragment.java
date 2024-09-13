package com.hotworx.ui.fragments.Nutritionist;

import android.os.Bundle;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hotworx.R;
import com.passio.modulepassio.Singletons.ApiHeaderSingleton;
import com.hotworx.global.Constants;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.UIHelper;
import com.hotworx.interfaces.OnItemClickInterface;
import com.hotworx.requestEntity.Branded;
import com.hotworx.requestEntity.GenericMsgResponse;
import com.hotworx.requestEntity.GetFavoriteFoodsResponse;
import com.hotworx.requestEntity.SetFavoriteFood;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.ui.adapters.FreeFormFavoriteFoodAdapter;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.views.TitleBar;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class FreeFoodFavoriteFragment extends BaseFragment implements OnItemClickInterface {

    Unbinder unbinder;

    @BindView(R.id.rvFavoriteFoods)
    RecyclerView recyclerView;

    List<GetFavoriteFoodsResponse.Food_list> list;
    FreeFormFavoriteFoodAdapter freeFormFavoriteFoodAdapter;
    ArrayList<Branded> dataList = new ArrayList<Branded>();
    private String basketName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_free_food_favorite, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (getArguments() != null) {
            basketName = getArguments().getString(Constants.BasketName);
            Log.i("xxData", basketName);
        }

        apiCallForGetFavoriteFood();

        return view;
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.favorite_foods));
    }

    private void apiCallForGetFavoriteFood() {
        getServiceHelper().enqueueCall(getWebService().getFavoriteFood(ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.GET_FAVORITE_FOOD, true);
    }

    private void apiCallForSetFavoriteFood(SetFavoriteFood data) {
        getServiceHelper().enqueueCall(getWebService().setFavoriteFoods(ApiHeaderSingleton.apiHeader(requireContext()), data), WebServiceConstants.SET_FAVORITE_FOOD, true);
    }

    @Override
    public void ResponseSuccess(String result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.GET_FAVORITE_FOOD:
                GetFavoriteFoodsResponse mContentPojo = GsonFactory.getConfiguredGson().fromJson(result, GetFavoriteFoodsResponse.class);
                if (mContentPojo != null && mContentPojo.getAllData().size() > 0) {
                    list = (List<GetFavoriteFoodsResponse.Food_list>) mContentPojo.getAllData().get(0).getFood_list();
                    updateAdapter(list);
                    UIHelper.hideSoftKeyboard(getDockActivity(), getDockActivity().getWindow().getDecorView());
                }
                break;

            case WebServiceConstants.SET_FAVORITE_FOOD:
                GenericMsgResponse mContent = GsonFactory.getConfiguredGson().fromJson(result, GenericMsgResponse.class);
                if (mContent != null && mContent.getAllData().size() > 0) {
                    UIHelper.showLongToastInCenter(requireContext(), R.string.message_success_fav_food_removed);
                }
                break;
        }
    }

    @Override
    public void ResponseFailure(String message, String tag) {
        super.ResponseFailure(message, tag);
    }

    private void updateAdapter(List<GetFavoriteFoodsResponse.Food_list> data) {

        freeFormFavoriteFoodAdapter = new FreeFormFavoriteFoodAdapter(myDockActivity, data, getContext(), this);
        recyclerView.setLayoutManager((new LinearLayoutManager(myDockActivity, LinearLayoutManager.VERTICAL, false)));
        recyclerView.setAdapter(freeFormFavoriteFoodAdapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT ) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//            Toast.makeText(requireContext(), "on Move", Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();

                SetFavoriteFood setFavoriteFoodData = new SetFavoriteFood();
//                setFavoriteFoodData.setUser_id(prefHelper.getUserId());
                setFavoriteFoodData.setProduct_id(data.get(position).getProduct_id());
                setFavoriteFoodData.setIs_fav(false);
                apiCallForSetFavoriteFood(setFavoriteFoodData);

                list.remove(position);
                freeFormFavoriteFoodAdapter.notifyDataSetChanged();

            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

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