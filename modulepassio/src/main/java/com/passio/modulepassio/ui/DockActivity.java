package com.passio.modulepassio.ui;

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
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.passio.modulepassio.interfaces.LoadingListener;
import com.passio.modulepassio.ui.base.BaseFragment;

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
//    public BasePreferenceHelper prefHelper;
    //For side menu
    protected DrawerLayout drawerLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        prefHelper = new BasePreferenceHelper(this);

        // Initialize your DockActivity here
        myDockActivity = this;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
        super.onResume();
    }



    public void replaceDockableFragment(BaseFragment frag) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();

        transaction.replace(getDockFrameLayoutId(), frag);
        transaction.addToBackStack(getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST
                                : null).commit();
    }

    public void replaceFragment(Fragment frag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(getDockFrameLayoutId(), frag);
        transaction.addToBackStack(getSupportFragmentManager().getBackStackEntryCount() == 0 ? KEY_FRAG_FIRST : null).commit();
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
