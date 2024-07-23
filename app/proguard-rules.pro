# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.


-renamesourcefileattribute SourceFile
# Firebase
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

-keepclassmembers class * extends androidx.work.Worker {
    public <init>(android.content.Context,androidx.work.WorkerParameters);
}

-keep class com.hotworx.models**{*;}
-keep class com.hotworx.requestEntity**{*;}
-keep class com.hotworx.retrofit**{*;}
-keep class com.hotworx.retrofit.OKHttpClientCreator{*;}
-keep class com.hotworx.retrofit.OKHttpClientCreator{*;}
-keep class com.hotworx.helpers**{*;}
-keep class com.hotworx.helpers.ServiceHelper{*;}
-keep class com.hotworx.helpers.BasePreferenceHelper{*;}
-keep class com.hotworx.helpers.FirebaseMessagingService{*;}
-keep class com.hotworx.helpers.InternetHelper{*;}
-keep class com.hotworx.retrofit.WebService{*;}
-keep class com.hotworx.retrofit.WebServiceFactory{*;}
-keep class com.hotworx.activities.DockActivity{*;}
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *; }
-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp.** { *; }

-dontwarn retrofit.appengine.UrlFetchClient
-keep class retrofit.** { *; }

# For Retrofit Stuff
-dontwarn retrofit.**
-keep class retrofit** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepclasseswithmembers class * {
    @retrofit.http.* <methods>;
}
#-keep class com.hotworx.Wrapper.LoggerWrapper { *; }
#
#-assumenosideeffects public interface org.slf4j.Logger {
#    public *** trace(...);
#    public *** debug(...);
#}
# Remove all logging calls
-assumenosideeffects class android.util.Log {
    public static *** v(...);
    public static *** d(...);
    public static *** i(...);
    public static *** w(...);

}

-ignorewarnings
