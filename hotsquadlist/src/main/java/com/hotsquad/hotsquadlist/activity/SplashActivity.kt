package com.hotsquad.hotsquadlist.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.hotsquad.hotsquadlist.extensions.gotoActivityWithNoHistory
import com.hotsquad.hotsquadlist.utils.Constant
import com.hotsquad.hotsquadlist.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        handler = Handler(Looper.getMainLooper())


        handler.postDelayed({
//            AppPreferences.loginData?.let {
//                //gotoActivityWithNoHistory(HomeActivity::class.java)
//            } ?: kotlin.run {
//                gotoActivityWithNoHistory(LoginActivity::class.java)
//            }
            gotoActivityWithNoHistory(LoginActivity::class.java)
        }, Constant.SPLASH_TIME)
    }
}