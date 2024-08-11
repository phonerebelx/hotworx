package com.hotsquad.hotsquadlist.network;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u0000*\u0004\b\u0000\u0010\u00012\u00020\u0002:\u0003\u0013\u0014\u0015B+\b\u0004\u0012\n\b\u0002\u0010\u0003\u001a\u0004\u0018\u00018\u0000\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\u0002\u0010\bR\u001e\u0010\u0004\u001a\u0004\u0018\u00010\u0005X\u0086\u000e\u00a2\u0006\u0010\n\u0002\u0010\r\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\fR\u0015\u0010\u0003\u001a\u0004\u0018\u00018\u0000\u00a2\u0006\n\n\u0002\u0010\u0010\u001a\u0004\b\u000e\u0010\u000fR\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u0082\u0001\u0003\u0016\u0017\u0018\u00a8\u0006\u0019"}, d2 = {"Lcom/hotsquad/hotsquadlist/network/ApiResponseCallback;", "T", "", "data", "code", "", "message", "", "(Ljava/lang/Object;Ljava/lang/Integer;Ljava/lang/String;)V", "getCode", "()Ljava/lang/Integer;", "setCode", "(Ljava/lang/Integer;)V", "Ljava/lang/Integer;", "getData", "()Ljava/lang/Object;", "Ljava/lang/Object;", "getMessage", "()Ljava/lang/String;", "Error", "Loading", "Success", "Lcom/hotsquad/hotsquadlist/network/ApiResponseCallback$Error;", "Lcom/hotsquad/hotsquadlist/network/ApiResponseCallback$Loading;", "Lcom/hotsquad/hotsquadlist/network/ApiResponseCallback$Success;", "hotsquadlist_debug"})
public abstract class ApiResponseCallback<T extends java.lang.Object> {
    @org.jetbrains.annotations.Nullable
    private final T data = null;
    @org.jetbrains.annotations.Nullable
    private java.lang.Integer code;
    @org.jetbrains.annotations.Nullable
    private final java.lang.String message = null;
    
    private ApiResponseCallback(T data, java.lang.Integer code, java.lang.String message) {
        super();
    }
    
    @org.jetbrains.annotations.Nullable
    public final T getData() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Integer getCode() {
        return null;
    }
    
    public final void setCode(@org.jetbrains.annotations.Nullable
    java.lang.Integer p0) {
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.String getMessage() {
        return null;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B%\u0012\b\u0010\u0003\u001a\u0004\u0018\u00010\u0004\u0012\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006\u0012\n\b\u0002\u0010\u0007\u001a\u0004\u0018\u00018\u0001\u00a2\u0006\u0002\u0010\b\u00a8\u0006\t"}, d2 = {"Lcom/hotsquad/hotsquadlist/network/ApiResponseCallback$Error;", "T", "Lcom/hotsquad/hotsquadlist/network/ApiResponseCallback;", "message", "", "code", "", "data", "(Ljava/lang/String;Ljava/lang/Integer;Ljava/lang/Object;)V", "hotsquadlist_debug"})
    public static final class Error<T extends java.lang.Object> extends com.hotsquad.hotsquadlist.network.ApiResponseCallback<T> {
        
        public Error(@org.jetbrains.annotations.Nullable
        java.lang.String message, @org.jetbrains.annotations.Nullable
        java.lang.Integer code, @org.jetbrains.annotations.Nullable
        T data) {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/hotsquad/hotsquadlist/network/ApiResponseCallback$Loading;", "T", "Lcom/hotsquad/hotsquadlist/network/ApiResponseCallback;", "()V", "hotsquadlist_debug"})
    public static final class Loading<T extends java.lang.Object> extends com.hotsquad.hotsquadlist.network.ApiResponseCallback<T> {
        
        public Loading() {
        }
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u0000*\u0004\b\u0001\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\r\u0012\u0006\u0010\u0003\u001a\u00028\u0001\u00a2\u0006\u0002\u0010\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/hotsquad/hotsquadlist/network/ApiResponseCallback$Success;", "T", "Lcom/hotsquad/hotsquadlist/network/ApiResponseCallback;", "data", "(Ljava/lang/Object;)V", "hotsquadlist_debug"})
    public static final class Success<T extends java.lang.Object> extends com.hotsquad.hotsquadlist.network.ApiResponseCallback<T> {
        
        public Success(T data) {
        }
    }
}