package com.hotsquad.hotsquadlist.network;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J!\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0007J\u001b\u0010\b\u001a\u00020\u00042\b\b\u0001\u0010\t\u001a\u00020\nH\u00a7@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u000b\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006\f"}, d2 = {"Lcom/hotsquad/hotsquadlist/network/SoService;", "", "login", "Lretrofit2/Response;", "Lcom/hotsquad/hotsquadlist/model/response/LoginResponse;", "loginRequest", "Lcom/hotsquad/hotsquadlist/model/request/LoginRequest;", "(Lcom/hotsquad/hotsquadlist/model/request/LoginRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "registration", "registrationRequest", "Lcom/hotsquad/hotsquadlist/model/request/RegistrationRequest;", "(Lcom/hotsquad/hotsquadlist/model/request/RegistrationRequest;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "hotsquadlist_debug"})
public abstract interface SoService {
    
    @retrofit2.http.POST(value = "vendor/login")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object login(@retrofit2.http.Body
    @org.jetbrains.annotations.NotNull
    com.hotsquad.hotsquadlist.model.request.LoginRequest loginRequest, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super retrofit2.Response<com.hotsquad.hotsquadlist.model.response.LoginResponse>> $completion);
    
    @retrofit2.http.POST(value = "vendor/login")
    @org.jetbrains.annotations.Nullable
    public abstract java.lang.Object registration(@retrofit2.http.Body
    @org.jetbrains.annotations.NotNull
    com.hotsquad.hotsquadlist.model.request.RegistrationRequest registrationRequest, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super com.hotsquad.hotsquadlist.model.response.LoginResponse> $completion);
}