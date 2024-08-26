package com.hotworx.ui.fragments.HotsquadList.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.hotworx.R
import com.hotworx.activities.BaseActivity
import com.hotworx.activities.MainActivity
import com.hotworx.databinding.ActivityCongratulationsBinding

class CongratulationsActivity : BaseActivity() {
    private lateinit var binding: ActivityCongratulationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCongratulationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnfinish.setOnClickListener{
            val i = Intent(this, MainActivity::class.java)
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(i)
        }
    }
}