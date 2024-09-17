package com.hotsquad.hotsquadlist.listener;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0007\bf\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\b\u0010\u0004\u001a\u00020\u0003H\u0016J\b\u0010\u0005\u001a\u00020\u0003H\u0016J\b\u0010\u0006\u001a\u00020\u0003H\u0016J\b\u0010\u0007\u001a\u00020\u0003H\u0016J\b\u0010\b\u001a\u00020\u0003H\u0016J\b\u0010\t\u001a\u00020\u0003H\u0016\u00a8\u0006\n"}, d2 = {"Lcom/hotsquad/hotsquadlist/listener/GenericListeners;", "", "onTapBack", "", "onTapForgot", "onTapLogin", "onTapLogout", "onTapNewAccount", "onTapProfile", "onTapRegister", "hotsquadlist_debug"})
public abstract interface GenericListeners {
    
    /**
     * when user tap on sign up button
     */
    public abstract void onTapNewAccount();
    
    public abstract void onTapLogin();
    
    /**
     * When user click on Back Button
     */
    public abstract void onTapBack();
    
    public abstract void onTapRegister();
    
    public abstract void onTapForgot();
    
    /**
     * When User click profile icon
     */
    public abstract void onTapProfile();
    
    /**
     * When User click on Logout
     */
    public abstract void onTapLogout();
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
        
        /**
         * when user tap on sign up button
         */
        public static void onTapNewAccount(@org.jetbrains.annotations.NotNull
        com.hotsquad.hotsquadlist.listener.GenericListeners $this) {
        }
        
        public static void onTapLogin(@org.jetbrains.annotations.NotNull
        com.hotsquad.hotsquadlist.listener.GenericListeners $this) {
        }
        
        /**
         * When user click on Back Button
         */
        public static void onTapBack(@org.jetbrains.annotations.NotNull
        com.hotsquad.hotsquadlist.listener.GenericListeners $this) {
        }
        
        public static void onTapRegister(@org.jetbrains.annotations.NotNull
        com.hotsquad.hotsquadlist.listener.GenericListeners $this) {
        }
        
        public static void onTapForgot(@org.jetbrains.annotations.NotNull
        com.hotsquad.hotsquadlist.listener.GenericListeners $this) {
        }
        
        /**
         * When User click profile icon
         */
        public static void onTapProfile(@org.jetbrains.annotations.NotNull
        com.hotsquad.hotsquadlist.listener.GenericListeners $this) {
        }
        
        /**
         * When User click on Logout
         */
        public static void onTapLogout(@org.jetbrains.annotations.NotNull
        com.hotsquad.hotsquadlist.listener.GenericListeners $this) {
        }
    }
}