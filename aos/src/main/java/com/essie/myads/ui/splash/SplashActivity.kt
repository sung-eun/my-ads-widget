package com.essie.myads.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.essie.myads.ui.home.MainActivity
import kotlinx.coroutines.FlowPreview

@SuppressLint("CustomSplashScreen")
@FlowPreview
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Intent(this, MainActivity::class.java).also {
            startActivity(it)
        }
        finish()
    }
}