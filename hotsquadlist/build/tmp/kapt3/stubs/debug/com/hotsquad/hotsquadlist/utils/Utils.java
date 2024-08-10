package com.hotsquad.hotsquadlist.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u0006\u0010\u0007\u001a\u00020\bJ\u0006\u0010\t\u001a\u00020\bJ\u000e\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\u0006J\u001e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013\u00a8\u0006\u0014"}, d2 = {"Lcom/hotsquad/hotsquadlist/utils/Utils;", "", "()V", "getSimpleTextBody", "Lokhttp3/RequestBody;", "param", "", "isLogin", "", "isOnline", "isValidEmail", "email", "showDatePickerDialog", "", "textView", "Landroid/widget/TextView;", "validator", "Lcom/google/android/material/datepicker/CalendarConstraints$DateValidator;", "manager", "Landroidx/fragment/app/FragmentManager;", "hotsquadlist_debug"})
public final class Utils {
    @org.jetbrains.annotations.NotNull
    public static final com.hotsquad.hotsquadlist.utils.Utils INSTANCE = null;
    
    private Utils() {
        super();
    }
    
    /**
     * Email validation
     * @param email to be check
     * @return false if email is not valid
     */
    public final boolean isValidEmail(@org.jetbrains.annotations.NotNull
    java.lang.String email) {
        return false;
    }
    
    /**
     * Check if internet connection available
     * @return true if internet connection available
     */
    public final boolean isOnline() {
        return false;
    }
    
    /**
     * The method will convert text to text/plain body
     * @param param text to be convert
     * @return Simple text in request body
     */
    @org.jetbrains.annotations.NotNull
    public final okhttp3.RequestBody getSimpleTextBody(@org.jetbrains.annotations.NotNull
    java.lang.String param) {
        return null;
    }
    
    /**
     * Check if user login
     * @return true if user logged in
     */
    public final boolean isLogin() {
        return false;
    }
    
    /**
     * Show Material Date Picker Dialog
     * @param textView Pass the TextView where the selected Date will be show
     * @param validator Pass the Date validation
     * Example DateValidatorPointBackward.now() For Pass Date Selection
     *        DateValidatorPointForward.now() For Future Date Selection
     * @param manager Provide supportFragmentManager
     */
    public final void showDatePickerDialog(@org.jetbrains.annotations.NotNull
    android.widget.TextView textView, @org.jetbrains.annotations.NotNull
    com.google.android.material.datepicker.CalendarConstraints.DateValidator validator, @org.jetbrains.annotations.NotNull
    androidx.fragment.app.FragmentManager manager) {
    }
}