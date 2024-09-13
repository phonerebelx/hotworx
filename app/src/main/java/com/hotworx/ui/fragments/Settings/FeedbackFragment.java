package com.hotworx.ui.fragments.Settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hotworx.R;
import com.passio.modulepassio.Singletons.ApiHeaderSingleton;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.Utils;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.views.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class FeedbackFragment extends BaseFragment {
    Unbinder unbinder;
    @BindView(R.id.et_name)
    EditText et_name;
    @BindView(R.id.et_email)
    EditText et_email;
    @BindView(R.id.et_desc)
    EditText et_feedback;
    @BindView(R.id.button_submit)
    Button submit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setTitleBar(TitleBar titleBar) {
        super.setTitleBar(titleBar);
        titleBar.showBackButton();
        titleBar.setSubHeading(getString(R.string.feedback_val));
    }

    @OnClick(R.id.button_submit)
    public void onClick() {

        if (et_name.getText().toString().length() == 0) {
            Utils.customToast(myDockActivity, getString(R.string.please_enter_name));
            et_name.requestFocus();
            return;
        }
        if (et_email.getText().toString().length() == 0) {
            Utils.customToast(myDockActivity, getString(R.string.please_enter_email));
            et_email.requestFocus();
            return;
        }
        if (et_feedback.getText().toString().length() == 0) {
            Utils.customToast(myDockActivity, getString(R.string.please_enter_feedback));
            et_feedback.requestFocus();
            return;
        } else {
            apiCallForFeedback(
                    et_name.getText().toString(),
                    et_email.getText().toString(),
                    et_feedback.getText().toString());
        }
    }


    private void apiCallForFeedback(String name, String email, String feedback) {
        getServiceHelper().enqueueCall(getWebService().addFeedBack(ApiHeaderSingleton.apiHeader(requireContext()), name, email, feedback), WebServiceConstants.Feedback
                , true);
    }

    @Override
    public void ResponseSuccess(String result, String Tag) {
        switch (Tag) {
            case WebServiceConstants.Feedback:
                //     AddFeedbackResponse mContentPojo = GsonFactory.getConfiguredGson().fromJson(result, AddFeedbackResponse.class);
                myDockActivity.popFragment();
                break;
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //   unbinder.unbind();
    }
}
