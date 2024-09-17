package com.hotsquad.hotsquadlist.listener;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0018\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0012\u0010\u0006\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016\u00a8\u0006\u0007"}, d2 = {"Lcom/hotsquad/hotsquadlist/listener/DialogListeners;", "", "onNegativeButtonTap", "", "dialog", "Landroid/content/DialogInterface;", "onPositionButtonTap", "hotsquadlist_debug"})
public abstract interface DialogListeners {
    
    /**
     * When user taps on positive button on dialog
     */
    public abstract void onPositionButtonTap(@org.jetbrains.annotations.Nullable
    android.content.DialogInterface dialog);
    
    /**
     * When user taps on Negative button on dialog
     */
    public abstract void onNegativeButtonTap(@org.jetbrains.annotations.Nullable
    android.content.DialogInterface dialog);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
        
        /**
         * When user taps on positive button on dialog
         */
        public static void onPositionButtonTap(@org.jetbrains.annotations.NotNull
        com.hotsquad.hotsquadlist.listener.DialogListeners $this, @org.jetbrains.annotations.Nullable
        android.content.DialogInterface dialog) {
        }
        
        /**
         * When user taps on Negative button on dialog
         */
        public static void onNegativeButtonTap(@org.jetbrains.annotations.NotNull
        com.hotsquad.hotsquadlist.listener.DialogListeners $this, @org.jetbrains.annotations.Nullable
        android.content.DialogInterface dialog) {
        }
    }
}