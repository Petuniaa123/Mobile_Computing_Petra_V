package com.example.project

import android.app.ActivityOptions
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val splash_icon: ImageView = findViewById(R.id.splash_icon)
        splash_icon.alpha = 0f
        splash_icon.animate().setDuration(1500).alpha(1f).withEndAction {
            val options = ActivityOptions.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out)
            val i = Intent(this, MainActivity::class.java)
            startActivity(i, options.toBundle())
            finish()
        }
    }
}