package com.hotsquad.hotsquadlist.utils;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0006\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001a\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\t\u001a\u00020\nH\u0002J\u0006\u0010\u000b\u001a\u00020\u0006J\u0010\u0010\f\u001a\u00020\u00062\b\b\u0001\u0010\r\u001a\u00020\nJ\u000e\u0010\f\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\bJ\u0010\u0010\u000f\u001a\u00020\u00062\b\b\u0001\u0010\r\u001a\u00020\nJ\u000e\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u000e\u001a\u00020\bR\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/hotsquad/hotsquadlist/utils/AppToast;", "", "()V", "mToast", "Landroid/widget/Toast;", "createToast", "", "string", "", "toastDuration", "", "showInternetErrorToast", "showLongToast", "resId", "toastMessage", "showToast", "hotsquadlist_debug"})
public final class AppToast {
    @org.jetbrains.annotations.Nullable
    private static android.widget.Toast mToast;
    @org.jetbrains.annotations.NotNull
    public static final com.hotsquad.hotsquadlist.utils.AppToast INSTANCE = null;
    
    private AppToast() {
        super();
    }
    
    public final void showToast(@org.jetbrains.annotations.NotNull
    java.lang.String toastMessage) {
    }
    
    public final void showToast(@androidx.annotation.StringRes
    int resId) {
    }
    
    public final void showLongToast(@org.jetbrains.annotations.NotNull
    java.lang.String toastMessage) {
    }
    
    public final void showLongToast(@androidx.annotation.StringRes
    int resId) {
    }
    
    public final void showInternetErrorToast() {
    }
    
    private final void createToast(java.lang.String string, int toastDuration) {
    }
}