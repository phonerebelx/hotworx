package com.hotworx.activities;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.RawResourceDataSource;
import com.hotworx.R;
import com.hotworx.global.Constants;
import com.hotworx.global.WebServiceConstants;
import com.hotworx.helpers.ApplicationManager;
import com.hotworx.helpers.HashUtils;
import com.hotworx.helpers.ServiceHelper;
import com.hotworx.helpers.UIHelper;
import com.hotworx.helpers.Utils;
import com.hotworx.helpers.UtilsHelpers;
import com.hotworx.interfaces.LoadingListener;
import com.hotworx.models.ErrorResponseEnt;
import com.hotworx.models.UserDataModel;
import com.hotworx.retrofit.GsonFactory;
import com.hotworx.requestEntity.*;
import com.hotworx.retrofit.WebService;
import com.hotworx.retrofit.WebServiceFactory;

import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.Unbinder;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import spencerstudios.com.bungeelib.Bungee;

public class LoginActivity extends BaseActivity implements LoadingListener {
    final int MIN_TIME_INTERVAL_FOR_SPLASH = 1000; // in millis

    @BindView(R.id.login_user_pass_et)
    AppCompatEditText pass_et;
    @BindView(R.id.login_user_name_et)
    AppCompatEditText email_et;
    @BindView(R.id.login_button)
    AppCompatButton login_btn;

    @BindView(R.id.layout_login)
    LinearLayout loginView;

    Unbinder unbinder;
    static String TAG = "LoginActivity";
    protected WebService webService;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    boolean isLoading = false;
    private TextureView textureView;
    private MediaPlayer mediaPlayer;
    private SimpleExoPlayer player;
    private PlayerView playerView;
    Boolean  checkLoginFlow = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        unbinder = ButterKnife.bind(this);

        playerView = findViewById(R.id.textureView);
        player = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
        playerView.setPlayer(player);
        Uri uri = RawResourceDataSource.buildRawResourceUri(R.raw.splash_video);
        DataSource.Factory dataSourceFactory = new DataSource.Factory() {
            @Override
            public DataSource createDataSource() {
                return new RawResourceDataSource(LoginActivity.this);
            }
        };
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
        player.prepare(videoSource);
        player.setPlayWhenReady(true);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);
        playerView.setUseController(false);

        if (webService == null) {
            webService = WebServiceFactory.getWebServiceInstanceWithCustomInterceptor(this, WebServiceConstants.BASE_URL);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        launchTimerAndTask();
    }

    @OnClick({R.id.tv_forgot_pass, R.id.login_button, R.id.sign_up_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_forgot_pass:

                Intent intent = new Intent(LoginActivity.this,ForgotPasswordActivity.class);
                intent.putExtra(Constants.flag, Constants.ForgotPassword);
                startActivity(intent);
                finish();
                break;
            case R.id.login_button:
                if (email_et.getText().toString().length() > 1 && pass_et.getText().toString().length() > 1) {
                    onLoadingStarted();
                    login();
                } else
                    Utils.customToast(this, getString(R.string.login_incomplete_info));
                break;

            case R.id.sign_up_button:
                break;
        }
    }

    @OnEditorAction(R.id.login_user_pass_et)
    public boolean onEditorAction() {
        boolean handled = false;
        if (email_et.getText().toString().length() > 1 && pass_et.getText().toString().length() > 1) {
            onLoadingStarted();
            UIHelper.hideSoftKeyboard(this,email_et);
            login();
        } else
            Utils.customToast(this, getString(R.string.login_incomplete_info));
        return true;
    }

    private void login() {
        String encodedPassword = HashUtils.INSTANCE.sha256(pass_et.getText().toString());
        prefHelper.setDeviceId(UUID.randomUUID().toString());
        webService.loginwithpassword(email_et.getText().toString(), encodedPassword, UtilsHelpers.Companion.getDeviceId(this))
                .enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                onLoadingFinished();
                try {
                    if (response.code() == 200 && response.body() != null) {
                        String responseBody = response.body().string();
                        ExtendedBaseModel extBaseModel = GsonFactory.getConfiguredGson().fromJson(responseBody, ExtendedBaseModel.class);
                        prefHelper.putLoginToken(extBaseModel.getToken());
                        prefHelper.setUserDataModel(new UserDataModel(email_et.getText().toString(),encodedPassword,"","password"));
                        if (extBaseModel.getTwo_factor().equals("yes")){
                            Intent intent = new Intent(LoginActivity.this,OtpActivity.class);
                            intent.putExtra(Constants.flag, Constants.LoginActivity);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra(Constants.flag, Constants.LoginActivity);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        assert response.errorBody() != null;
                        String responseBody = response.errorBody().string();
                        ErrorResponseEnt errorResponseEnt = GsonFactory.getConfiguredGson().fromJson(responseBody, ErrorResponseEnt.class);
                        Utils.customToast(LoginActivity.this, errorResponseEnt.getError());
                    }
                } catch (Exception ex) {
                    Log.e("Exception OTP",ex.toString());
                    Utils.customToast(LoginActivity.this, getResources().getString(R.string.internal_exception_messsage));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onLoadingFinished();
                t.printStackTrace();
                Log.e(ServiceHelper.class.getSimpleName() + " by tag: " + TAG, t.toString());
                Utils.customToast(LoginActivity.this, t.toString());
            }
        });
    }

    private void launchTimerAndTask() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showMainActivity();
            }
        }, MIN_TIME_INTERVAL_FOR_SPLASH);
    }

    private void showMainActivity() {
        if(prefHelper.getLoginToken() != null && !prefHelper.getLoginToken().isEmpty()){
            Intent intent = new Intent(this,MainActivity.class);
            if(getIntent()!=null){
                intent.putExtra("navigateTo",getIntent().getStringExtra("navigateTo"));
                intent.putExtra("notification_type",getIntent().getStringExtra("notification_type"));
                intent.putExtra("custom_message",getIntent().getStringExtra("custom_message"));
                intent.putExtra("booking_date",getIntent().getStringExtra("booking_date"));
                intent.putExtra("title",getIntent().getStringExtra("title"));
                intent.putExtra("objid",getIntent().getStringExtra("objid"));
                intent.putExtra("calendar_title",getIntent().getStringExtra("calendar_title"));
                intent.putExtra("duration",getIntent().getIntExtra("duration",0));
                intent.putExtra("flag","");
            }
            intent.putExtra("flag","");
            startActivity(intent);
            finish();
        } else{
            clearSessions();
            loginView.setVisibility(View.VISIBLE);
        }
    }

    private void clearSessions() {
        ApplicationManager.getInstance(this).setActivityId("0");
        ApplicationManager.getInstance(this).setParentActivityId("0");
        ApplicationManager.getInstance(this).setSessionId(0);
        prefHelper.removeActiveSession();
    }

    @Override
    public void onLoadingStarted() {
        isLoading = true;
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadingFinished() {
        isLoading = false;
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onProgressUpdated(int percentLoaded) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isLoading) Utils.customToast(this, getString(R.string.message_wait));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.release();
            player = null;
        }
    }
}
