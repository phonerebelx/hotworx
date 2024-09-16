package com.hotworx.activities;

import static android.Manifest.permission.POST_NOTIFICATIONS;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.hotworx.R;
import com.hotworx.activities.BrivoActivity.SitesActivity;
import com.hotworx.global.Constants;
import com.hotworx.global.SideMenuDirection;
import com.hotworx.global.SideMenuChooser;
import com.hotworx.helpers.CustomEvents;
import com.hotworx.helpers.UIHelper;
import com.hotworx.helpers.Utils;
import com.hotworx.residemenu.ResideMenu;
import com.hotworx.retrofit.WebService;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.fragments.Nutritionist.NutritionistFragment;
import com.hotworx.ui.fragments.ProfileAndGoal.ProfileAndGoalFragment;
import com.hotworx.ui.fragments.SessionFlow.BeginSessionFragment;
import com.hotworx.ui.fragments.HomeFragment;
import com.hotworx.ui.fragments.SideMenuFragment;
import com.hotworx.ui.fragments.SessionFlow.WorkoutSummaryFragment;
import com.hotworx.ui.fragments.SessionFlow.WorkoutTimeFragment;
import com.hotworx.ui.fragments.notifications.NotificationFragment;
import com.hotworx.ui.passioactivity.PassioMainActivity;
import ai.passio.passiosdk.core.config.PassioConfiguration;
import ai.passio.passiosdk.core.config.PassioMode;
import ai.passio.passiosdk.core.config.PassioStatus;
import com.hotworx.ui.views.TitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import ai.passio.passiosdk.passiofood.PassioSDK;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.passio.modulepassio.NutritionUIModule;

public class MainActivity extends DockActivity {



    private static final int NOTIFICATION_PERMISSION_CODE = 123;

    private static final String TAG = MainActivity.class.getSimpleName();
    public TitleBar titleBar;

    @BindView(R.id.header_main)
    TitleBar header_main;

    @BindView(R.id.sideMneuFragmentContainer)
    public FrameLayout sideMneuFragmentContainer;
    @BindView(R.id.mainFrameLayout)
    FrameLayout mainFrameLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private MainActivity mContext;
    private boolean loading;
    private ResideMenu resideMenu;
    private String sideMenuType;
    private String sideMenuDirection;
    private WebService webService;
    private Boolean isDrawerStart = false;

    public interface OnMainActivityClickListener {
        void onMainActivityClick(String arg1, String arg2);
    }

    String flag = "";

    @Override
    public int getDockFrameLayoutId() {
        return R.id.mainFrameLayout;
    }

    @SuppressLint("SuspiciousIndentation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_dock);
        ButterKnife.bind(this);
        if(getIntent().getExtras()!=null)


        flag = getIntent().getStringExtra(Constants.flag);

        titleBar = header_main;
//
//        titleBar.hideTitleBar();
        mContext = this;

        sideMenuType = SideMenuChooser.DRAWER.getValue();
        sideMenuDirection = SideMenuDirection.LEFT.getValue();
        settingSideMenu(sideMenuType, sideMenuDirection);
        drawerCallBack();
        titleBar.setMenuButtonListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                isDrawerStart = true;
                if (sideMenuType.equals(SideMenuChooser.DRAWER.getValue()) && getDrawerLayout() != null) {
                    if (sideMenuDirection.equals(SideMenuDirection.LEFT.getValue())) {
                        drawerLayout.openDrawer(Gravity.LEFT);
                    } else {
                        drawerLayout.openDrawer(Gravity.RIGHT);
                    }
                } else {
                    resideMenu.openMenu(sideMenuDirection);
                }

            }
        });

        titleBar.setBackButtonListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (loading) {
                    UIHelper.showLongToastInCenter(getApplicationContext(),
                            R.string.message_wait);
                } else {
                    Fragment f = getSupportFragmentManager().findFragmentById(getDockFrameLayoutId());
                    if (!(f instanceof WorkoutTimeFragment || f instanceof WorkoutSummaryFragment || f instanceof BeginSessionFragment || f instanceof NutritionistFragment)) {
                        popFragment();
                        UIHelper.hideSoftKeyboard(getApplicationContext(), titleBar);
                    } else {
                        onBackPressedInFragment();
                    }
                }
            }
        });

        titleBar.setNotificationButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loading) {
                    UIHelper.showLongToastInCenter(getApplicationContext(), R.string.message_wait);
                } else {
//                    EventBus.getDefault().post(new CustomEvents.notificationSession());

                    Fragment notificationFragment = new NotificationFragment();

                    getDockActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.mainFrameLayout, notificationFragment) // Replace with your container ID
                            .addToBackStack(null) // Optional: add to back stack if you want to allow going back to previous fragment
                            .commit();
                }
            }
        });

        titleBar.setSyncButtonListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loading) {
                    UIHelper.showLongToastInCenter(getApplicationContext(),
                            R.string.message_wait);
                } else {

                    EventBus.getDefault().post(new CustomEvents.syncSession());
                }
            }

        });

        titleBar.setPassioButtonListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (loading) {
                    UIHelper.showLongToastInCenter(getApplicationContext(),
                            R.string.message_wait);
                } else {
                    startActivity(new Intent(getDockActivity(), PassioMainActivity.class));
                    myDockActivity.finish();
                }
            }
        });

        titleBar.setBrivoButtonListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (loading) {
                    UIHelper.showLongToastInCenter(getApplicationContext(),
                            R.string.message_wait);
                } else {
                    EventBus.getDefault().post(new CustomEvents.brivoSession());
                }
            }

        });
        if (savedInstanceState == null)
            initFragment();


    }

    public void drawerCallBack(){
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Called when a drawer's position changes.


            }

            @Override
            public void onDrawerOpened(View drawerView) {
                    EventBus.getDefault().post(new CustomEvents.openDrawer());
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                EventBus.getDefault().post(new CustomEvents.clossDrawer());
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void settingSideMenu(String type, String direction) {

        if (type.equals(SideMenuChooser.DRAWER.getValue())) {
            DrawerLayout.LayoutParams params = new DrawerLayout.LayoutParams((int) getResources().getDimension(R.dimen._260sdp), (int) DrawerLayout.LayoutParams.MATCH_PARENT);
            if (direction.equals(SideMenuDirection.LEFT.getValue())) {
                params.gravity = Gravity.START;
                sideMneuFragmentContainer.setLayoutParams(params);
            } else {
                params.gravity = Gravity.END;
                sideMneuFragmentContainer.setLayoutParams(params);
            }
            drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

            sideMenuFragment = SideMenuFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager()
                    .beginTransaction();
            transaction.replace(getSideMenuFrameLayoutId(), sideMenuFragment).commit();

//            drawerLayout.closeDrawers();
            if (isDrawerStart){
                drawerLayout.setContentDescription(getString(R.string.closed_menu));
            }
        } else {
            resideMenu = new ResideMenu(this);
            resideMenu.attachToActivity(this);
            resideMenu.setMenuListener(getMenuListener());
            resideMenu.setScaleValue(0.52f);
            setMenuItemDirection(direction);
        }
    }

    private void setMenuItemDirection(String direction) {

        if (direction.equals(SideMenuDirection.LEFT.getValue())) {
            SideMenuFragment leftSideMenuFragment = SideMenuFragment.newInstance();
            resideMenu.addMenuItem(leftSideMenuFragment, "LeftSideMenuFragment", direction);

        } else if (direction.equals(SideMenuDirection.RIGHT.getValue())) {

            SideMenuFragment rightSideMenuFragment = SideMenuFragment.newInstance();
            resideMenu.addMenuItem(rightSideMenuFragment, "RightSideMenuFragment", direction);

        }

    }

    private int getSideMenuFrameLayoutId() {
        return R.id.sideMneuFragmentContainer;

    }

    public void initFragment() {
        Bundle bundle;
        switch (flag) {
            case Constants.OtpActivity:
            case Constants.LoginActivity:
                getSupportFragmentManager().addOnBackStackChangedListener(getListener());
                replaceDockableFragment(new ProfileAndGoalFragment(), Constants.ProfileAndGoalFragment);
                break;

            case Constants.BeginSessionFragment:
                getSupportFragmentManager().addOnBackStackChangedListener(getListener());
                replaceDockableFragment(new BeginSessionFragment(), Constants.BeginSessionFragment);
                break;

            case Constants.WorkoutSummaryFragment:
                getSupportFragmentManager().addOnBackStackChangedListener(getListener());
                WorkoutSummaryFragment workoutSummaryFragment = new WorkoutSummaryFragment();
                bundle = new Bundle();
                bundle.putString("parentActivityId", getIntent().getStringExtra("parentActivityId"));
                workoutSummaryFragment.setArguments(bundle);
                replaceDockableFragment(workoutSummaryFragment, Constants.WorkoutSummaryFragment);
                break;

            case Constants.WorkoutTimeFragment:
                getSupportFragmentManager().addOnBackStackChangedListener(getListener());
                WorkoutTimeFragment workoutTimeFragment = new WorkoutTimeFragment();
                bundle = new Bundle();
                bundle.putSerializable("activeSession", getIntent().getSerializableExtra("activeSession"));
                bundle.putBoolean("isAfterBurnWorkoutSession",getIntent().getBooleanExtra("isAfterBurnWorkoutSession",true));
                bundle.putBoolean("isFitbitWatchSelected", true);
                workoutTimeFragment.setArguments(bundle);
                replaceDockableFragment(workoutTimeFragment, Constants.WorkoutTimeFragment);
                break;

            default:
                String navigateTo = getIntent().getStringExtra("navigateTo");
                String hashId = getIntent().getStringExtra("hashId");
                String image = getIntent().getStringExtra("image");
                String body = getIntent().getStringExtra("body");
                String notification_type = getIntent().getStringExtra("notification_type");
                String custom_message = getIntent().getStringExtra("custom_message");
                String booking_date = getIntent().getStringExtra("booking_date");
                String title = getIntent().getStringExtra("title");
                String objid = getIntent().getStringExtra("objid");
                String calendar_title = getIntent().getStringExtra("calendar_title");
                int duration = getIntent().getIntExtra("duration",0);
                getSupportFragmentManager().addOnBackStackChangedListener(getListener());
                addDockableFragment(HomeFragment.newInstance(
                        navigateTo,
                        hashId,
                        image,
                        body,
                        title,
                        notification_type,
                        custom_message,
                        booking_date,
                        objid,
                        calendar_title,
                        duration), Constants.HomeFragment);
                break;
        }
    }

    private void onSDKError(String error) {
//        textView.setText("ERROR: " + error);
    }

    private void onSDKReady() {
        // Assuming NutritionUIModule has a static method like getInstance() or a similar one
        NutritionUIModule.INSTANCE.launch(this, null);
        Log.d("ytytytuy", "jhkjhkhlkhlkllhlk");
        finish();
    }

    private FragmentManager.OnBackStackChangedListener getListener() {
        FragmentManager.OnBackStackChangedListener result = new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();
                if (manager != null) {
                    BaseFragment currFrag = (BaseFragment) manager.findFragmentById(getDockFrameLayoutId());
                    if (currFrag != null) {
                        currFrag.fragmentResume();
                    }
                }
            }
        };
        return result;
    }

    @Override
    public void onLoadingStarted() {
        if (mainFrameLayout != null) {
            mainFrameLayout.setVisibility(View.VISIBLE);
            if (progressBar != null) {
                progressBar.setVisibility(View.VISIBLE);
            }
            loading = true;
        }
    }

    @Override
    public void onLoadingFinished() {
        mainFrameLayout.setVisibility(View.VISIBLE);

        if (progressBar != null) {
            progressBar.setVisibility(View.INVISIBLE);
        }

        loading = false;
    }

    @Override
    public void onProgressUpdated(int percentLoaded) {

    }


    @Override
    public void onBackPressed() {
        if (loading) Utils.customToast(this, getString(R.string.message_wait)); else  super.onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        titleBar.showTitleBar();

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isBrivoAllowedEvent(CustomEvents.checkBrivoAllowed event) {
        titleBar.hideBrivoBtn();
    }



    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }



}
