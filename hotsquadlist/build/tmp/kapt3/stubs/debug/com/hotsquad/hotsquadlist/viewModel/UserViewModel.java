package com.hotsquad.hotsquadlist.viewModel;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\r\u001a\u00020\u000fR\u001a\u0010\u0003\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u00060\u00050\b8F\u00a2\u0006\u0006\u001a\u0004\b\t\u0010\nR\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0010"}, d2 = {"Lcom/hotsquad/hotsquadlist/viewModel/UserViewModel;", "Landroidx/lifecycle/ViewModel;", "()V", "_loginResponse", "Landroidx/lifecycle/MutableLiveData;", "Lcom/hotsquad/hotsquadlist/network/ApiResponseCallback;", "Lcom/hotsquad/hotsquadlist/model/response/LoginResponse;", "loginResponse", "Landroidx/lifecycle/LiveData;", "getLoginResponse", "()Landroidx/lifecycle/LiveData;", "repository", "Lcom/hotsquad/hotsquadlist/repository/AppRepository;", "loginRequest", "", "Lcom/hotsquad/hotsquadlist/model/request/LoginRequest;", "hotsquadlist_debug"})
public final class UserViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.hotsquad.hotsquadlist.repository.AppRepository repository = null;
    
    /**
     * Baking Property For Login Request
     * Get Response in [loginResponse] Live Data
     */
    @org.jetbrains.annotations.NotNull
    private final androidx.lifecycle.MutableLiveData<com.hotsquad.hotsquadlist.network.ApiResponseCallback<com.hotsquad.hotsquadlist.model.response.LoginResponse>> _loginResponse = null;
    
    public UserViewModel() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final androidx.lifecycle.LiveData<com.hotsquad.hotsquadlist.network.ApiResponseCallback<com.hotsquad.hotsquadlist.model.response.LoginResponse>> getLoginResponse() {
        return null;
    }
    
    public final void loginRequest(@org.jetbrains.annotations.NotNull
    com.hotsquad.hotsquadlist.model.request.LoginRequest loginRequest) {
    }
}