package com.example.heal_go.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.heal_go.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
            splashScreenViewProvider.iconView
                .animate()
                .setDuration(
                    300
                ).translationY(-0f)
                .alpha(0f)
                .withEndAction {
                    // After the fade out, remove the
                    // splash and set content view
                    splashScreenViewProvider.remove()
                }.start()
        }

        setContentView(R.layout.activity_main)
    }
}