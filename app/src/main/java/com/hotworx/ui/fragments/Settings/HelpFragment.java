package com.hotworx.ui.fragments.Settings;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hotworx.R;
import com.hotworx.Singletons.ApiHeaderSingleton;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.requestEntity.ViewHelpResponse;
import com.hotworx.requestEntity.ViewSummaryResponse;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HelpFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.help_tv)
    TextView help_tv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_fragment,container,false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        apiCallForHelp();
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.help));
    }

    private void apiCallForHelp(){
        getServiceHelper().enqueueCall(getWebService().getHelp(ApiHeaderSingleton.apiHeader(requireContext())), WebServiceConstants.Help,true);
    }

    @Override
    public void ResponseSuccess(String result, String Tag) {
        switch (Tag){
            case WebServiceConstants.Help:
                ViewHelpResponse mContentPojo = GsonFactory.getConfiguredGson().fromJson(result, ViewHelpResponse.class);
                if (mContentPojo.getAllData().get(0).getContents() != null) {
                    String content = mContentPojo.getAllData().get(0).getContents();
                    if (content != null) {
                        content = content.replaceAll("(\\\\r\\\\n|\\\\n)", "\\\n");
                        content = content.replaceAll("\\\\\\\"", "\\\"");
                        content = content.replaceAll("\\\\\\\'", "\\\'");
                        help_tv.setText(content);
                    }
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
      //  unbinder.unbind();
    }
}
