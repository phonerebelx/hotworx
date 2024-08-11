package com.hotsquad.hotsquadlist.network.domain;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0003\u0018\u0000 \u00032\u00020\u0001:\u0001\u0003B\u0005\u00a2\u0006\u0002\u0010\u0002\u00a8\u0006\u0004"}, d2 = {"Lcom/hotsquad/hotsquadlist/network/domain/APIError;", "", "()V", "Companion", "hotsquadlist_debug"})
public final class APIError {
    private static final int UNEXPECTED_ERROR = 11001;
    private static final int NO_NETWORK = 11002;
    private static final int UNAUTHENTICATED = 401;
    private static final int NOT_FOUND = 404;
    private static final int BLOCK_BY_ADMIN = 403;
    private static final int BAD_REQUEST = 400;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int INTERNAL_SERVER = 500;
    private static final int SERVER_ERROR = 600;
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String UNAUTHORIZED = "unauthorized";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String NETWORK_CALL_FAILED = "network_failed";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String PAGE_NOT_FOUND = "not_found";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String BLOCK_BY_ADMIN_MSG = "Account_block";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String SERVER_BAD_REQUEST = "bad_request";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String WEB_SERVICE_UNAVAILABLE = "service_unavailable";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String WEB_SERVER_ERROR = "server_error";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String INTERNAL_SERVER_ERROR = "internal_server_error";
    @org.jetbrains.annotations.NotNull
    public static final java.lang.String UNEXPECTED_ERROR_OCCURRED = "unexpected_error";
    @org.jetbrains.annotations.NotNull
    public static final com.hotsquad.hotsquadlist.network.domain.APIError.Companion Companion = null;
    
    public APIError() {
        super();
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0010\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0003\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J0\u0010\u0017\u001a\u00020\u0018\"\u0004\b\u0000\u0010\u00192\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00190\u001b2\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020\u00180\u001dH\u0002J\u001a\u0010\u001f\u001a\b\u0012\u0004\u0012\u0002H\u00190\u001b\"\u0004\b\u0000\u0010\u00192\u0006\u0010\u001f\u001a\u00020 J \u0010\u001f\u001a\b\u0012\u0004\u0012\u0002H\u00190\u001b\"\u0004\b\u0000\u0010\u00192\f\u0010!\u001a\b\u0012\u0004\u0012\u0002H\u00190\"J.\u0010#\u001a\u00020\u0018\"\u0004\b\u0000\u0010\u00192\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00190\u001b2\u0012\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u001e\u0012\u0004\u0012\u00020\u00180\u001dR\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000f\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0012\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0007X\u0086T\u00a2\u0006\u0002\n\u0000\u00a8\u0006$"}, d2 = {"Lcom/hotsquad/hotsquadlist/network/domain/APIError$Companion;", "", "()V", "BAD_REQUEST", "", "BLOCK_BY_ADMIN", "BLOCK_BY_ADMIN_MSG", "", "INTERNAL_SERVER", "INTERNAL_SERVER_ERROR", "NETWORK_CALL_FAILED", "NOT_FOUND", "NO_NETWORK", "PAGE_NOT_FOUND", "SERVER_BAD_REQUEST", "SERVER_ERROR", "SERVICE_UNAVAILABLE", "UNAUTHENTICATED", "UNAUTHORIZED", "UNEXPECTED_ERROR", "UNEXPECTED_ERROR_OCCURRED", "WEB_SERVER_ERROR", "WEB_SERVICE_UNAVAILABLE", "emitError", "", "T", "apiResponseCallback", "Lcom/hotsquad/hotsquadlist/network/ApiResponseCallback;", "errorHandler", "Lkotlin/Function1;", "Lcom/hotsquad/hotsquadlist/network/domain/ErrorHandler;", "error", "", "response", "Lretrofit2/Response;", "networkCallFailed", "hotsquadlist_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        @org.jetbrains.annotations.NotNull
        public final <T extends java.lang.Object>com.hotsquad.hotsquadlist.network.ApiResponseCallback<T> error(@org.jetbrains.annotations.NotNull
        java.lang.Throwable error) {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final <T extends java.lang.Object>com.hotsquad.hotsquadlist.network.ApiResponseCallback<T> error(@org.jetbrains.annotations.NotNull
        retrofit2.Response<T> response) {
            return null;
        }
        
        public final <T extends java.lang.Object>void networkCallFailed(@org.jetbrains.annotations.NotNull
        com.hotsquad.hotsquadlist.network.ApiResponseCallback<T> apiResponseCallback, @org.jetbrains.annotations.NotNull
        kotlin.jvm.functions.Function1<? super com.hotsquad.hotsquadlist.network.domain.ErrorHandler, kotlin.Unit> errorHandler) {
        }
        
        private final <T extends java.lang.Object>void emitError(com.hotsquad.hotsquadlist.network.ApiResponseCallback<T> apiResponseCallback, kotlin.jvm.functions.Function1<? super com.hotsquad.hotsquadlist.network.domain.ErrorHandler, kotlin.Unit> errorHandler) {
        }
    }
}