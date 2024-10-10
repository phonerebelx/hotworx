package com.hotworx.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.hotworx.BaseApplication;
import com.hotworx.R;
import com.hotworx.helpers.BasePreferenceHelper;
import com.hotworx.interfaces.DialogBoxInterface;
import com.hotworx.interfaces.LoadingListener;
import com.hotworx.residemenu.ResideMenu;
import com.hotworx.ui.dialog.DialogFactory;
import com.hotworx.ui.fragments.BaseFragment;
import com.hotworx.ui.fragments.Nutritionist.NutritionistFragment;
//import com.hotworx.ui.fragments.Rewards.LevelFiveFragment;
import com.hotworx.ui.fragments.SessionFlow.BeginSessionFragment;
import com.hotworx.ui.fragments.HomeFragment;
import com.hotworx.ui.fragments.SideMenuFragment;
import com.hotworx.ui.fragments.SessionFlow.WorkoutSummaryFragment;
import com.hotworx.ui.fragments.SessionFlow.WorkoutTimeFragment;
import com.tapadoo.alerter.Alerter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public abstract class DockActivity extends AppCompatActivity
        implements LoadingListener {
    public static final String KEY_FRAG_FIRST = "firstFrag";
    protected DockActivity myDockActivity;

    public abstract int getDockFrameLayoutId();

    BaseFragment baseFragment;
    public BasePreferenceHelper prefHelper;
    //For side menu
    protected DrawerLayout drawerLayout;
    public SideMenuFragment sideMenuFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefHelper = new BasePreferenceHelper(this);

        // Initialize your DockActivity here
        myDockActivity = this;
    }

    private ResideMenu.OnMenuListener menuListener = new ResideMenu.OnMenuListener() {
        @Override
        public void openMenu() {

        }

        @Override
        public void closeMenu() {

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }



    public void replaceDockableFragment(BaseFragment frag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(getDockFrameLayoutId(), frag);
        transaction.addToBackStack(getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commit();
    }

    public void replaceDockableFragment(com.example.passiomodulenew.ui.base.BaseFragment<?> frag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(getDockFrameLayoutId(), frag);
        transaction.addToBackStack(null).commit();
    }

//    public void replaceFragment(Fragment frag) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.replace(getDockFrameLayoutId(), frag);
//        transaction.addToBackStack(getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST : null).commit();
//    }

    public void replaceFragment(Fragment frag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(getDockFrameLayoutId(), frag); // Replace the container with the NavHostFragment
        transaction.addToBackStack(null); // Add to back stack
        transaction.commit();
    }

    public void replaceDockableFragment(BaseFragment frag, String Tag) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(getDockFrameLayoutId(), frag);
        transaction.addToBackStack(getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST : null).commit();
    }

    public void removeDockableFragment(BaseFragment frag) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Clear the backstack
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(getDockFrameLayoutId(), frag);
        transaction.addToBackStack(getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST : null);
        transaction.commit();
    }


    public void replaceDockableFragment2(Fragment frag, String Tag) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(getDockFrameLayoutId(), frag);
        transaction
                .addToBackStack(
                        getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commit();
    }


    public void replaceDockableFragment(BaseFragment frag, boolean isAnimate) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(getDockFrameLayoutId(), frag);
        transaction
                .addToBackStack(
                        getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commit();
    }

    public void addDockableFragment(BaseFragment frag, String Tag) {

        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.add(getDockFrameLayoutId(), frag);
        transaction
                .addToBackStack(
                        getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commit();
    }

    public void removeDockableFragment(BaseFragment frag, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(getDockFrameLayoutId(), frag); // Replace the current fragment
        transaction.commit(); // Commit without adding to back stack
    }

    public DrawerLayout getDrawerLayout() {
        return drawerLayout;
    }

    public void closeDrawer() {
        drawerLayout.closeDrawers();
    }

    public void lockDrawer() {
        try {
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected DockActivity getDockActivity() {
        return myDockActivity;
    }

    public void releaseDrawer() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else {
            Fragment f = getSupportFragmentManager().findFragmentById(getDockFrameLayoutId());
            if (!(f instanceof WorkoutTimeFragment || f instanceof WorkoutSummaryFragment || f instanceof BeginSessionFragment)) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
                    super.onBackPressed();
                } else
                    DialogFactory.createQuitDialog(this,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();

                                }
                            }, R.string.message_quit).show();
            } else {
                onBackPressedInFragment();
            }
        }
    }

    public void showCancelDialog(String title, String message,String booking_date, DialogBoxInterface dialogBoxInterface){
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_custom_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(false);
        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        TextView tvDesc = dialog.findViewById(R.id.tvDesc);
        TextView tvAddToCalender = dialog.findViewById(R.id.tvAddToCalender);
        TextView tvCancel = dialog.findViewById(R.id.tvCancel);

        tvTitle.setText(title);
        tvDesc.setText(message);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   dialogBoxInterface.onNegative();
                   dialog.dismiss();
            }
        });

        tvAddToCalender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 dialogBoxInterface.onPositive(booking_date);
                 dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void onBackPressedInFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            if (f != null && f instanceof BaseFragment)
                ((BaseFragment) f).onBackPressed();
        }
    }

    public void emptyBackStack() {
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(i);
            if (entry != null && (!(entry instanceof HomeFragment)) && (!(entry instanceof SideMenuFragment))) {
                getSupportFragmentManager().popBackStack(entry.getId(),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

        }
    }

    public void emptyBackStackNutritionist() {
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(i);
            if (entry != null && (!(entry instanceof NutritionistFragment)) && (!(entry instanceof SideMenuFragment))) {
                getSupportFragmentManager().popBackStack(entry.getId(),
                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }

        }
    }

//    public void emptyBackStackLevel() {
//        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
//            FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(i);
//
//            if (entry != null && (!(entry instanceof LevelFiveFragment))) {
//                getSupportFragmentManager().popBackStack(entry.getId(),
//                        FragmentManager.POP_BACK_STACK_INCLUSIVE);
//            }
//
//        }
//    }
    public void popBackStackTillEntry(int entryIndex) {
        if (getSupportFragmentManager() == null)
            return;
        if (getSupportFragmentManager().getBackStackEntryCount() <= entryIndex)
            return;
        FragmentManager.BackStackEntry entry = getSupportFragmentManager().getBackStackEntryAt(
                entryIndex);
        if (entry != null) {
            getSupportFragmentManager().popBackStack(entry.getId(),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }


    public void popFragment() {
        if (getSupportFragmentManager() == null)
            return;
        getSupportFragmentManager().popBackStack();
    }

    public BaseApplication getMainApplication() {
        return (BaseApplication) getApplication();
    }

    public ResideMenu.OnMenuListener getMenuListener() {
        return menuListener;
    }

    @SuppressLint("WrongConstant")
//    public static void showErrorSnackBar(View currentView, Context context, String message, int snackBarDuration) {
//        Snackbar snackBarView = Snackbar.make(currentView, message, Snackbar.LENGTH_INDEFINITE);
//        snackBarView.setDuration(snackBarDuration);
//
//        View view = snackBarView.getView();
//        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
//        params.gravity = Gravity.TOP;
//        params.setMarginStart(context.getResources().getDimensionPixelSize(R.dimen.snackbar_horizontal_margin));
//        params.setMarginEnd(context.getResources().getDimensionPixelSize(R.dimen.snackbar_horizontal_margin));
//        view.setLayoutParams(params);
//
//        view.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_snackbar_error)); // for custom background
//        snackBarView.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
//
//        TextView messageView = view.findViewById(com.google.android.material.R.id.snackbar_text);
//        messageView.setTextColor(Color.WHITE);
//        messageView.setMaxLines(10);
//
//        snackBarView.setAction("X", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                snackBarView.dismiss();
//            }
//        }).setActionTextColor(Color.WHITE);
//
//        snackBarView.show();
//    }

    public void showErrorMessage(String message) {
        Alerter.create(this)
                .setTitle(getString(R.string.error))
                .setText(message)
                .setDuration(5000)
                .setIcon(R.drawable.ic_close)
                .setBackgroundColorRes(R.color.snack_bar_error_red)
                .enableSwipeToDismiss()
                .show();
    }

    public void showSuccessMessage(String message) {
        Alerter.create(this)
                .setTitle(getString(R.string.success))
                .setText(message)
                .setDuration(5000)
                .setIcon(R.drawable.ic_close)
                .setBackgroundColorRes(R.color.colorDarkGreen)
                .enableSwipeToDismiss()
                .show();
    }

    public File getFileFromUri(Uri uri, String name, Context context) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2, false);
            File file = new File(context.getExternalCacheDir(), name);
            file.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(file);
            scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressLint("Range")
    public File pickPhotoOnly(Context context, Uri uri) {
        if (uri != null) {
            String uriString = uri.toString();
            File myFile = new File(uriString);
            String displayName = null;

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = context.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
            }

            if (displayName != null) {
                return getFileFromUri(uri, displayName, context);
            }
        }
        return null;
    }

}
