package com.example.githubsearch.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewPropertyAnimator
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.githubsearch.R
import com.example.githubsearch.viewmodel.SetNightViewModel
import com.example.githubsearch.viewmodel.ViewModelFactory

class SplashActivity : AppCompatActivity() {
    private var startSplash: ViewPropertyAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val text = findViewById<TextView>(R.id.textsplash)
        val pref = ModePreferences.getInstance(dataStore)
        val setNightViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[SetNightViewModel::class.java]
        setNightViewModel.getThemeSettings().observe(this)
        { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                startSplash?.start()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                startSplash?.start()
            }
        }


        startSplash = text.animate().setDuration(splashDelay).alpha(1f).withEndAction {
            val intent = Intent(this, MainActivity::class.java)
            intent.apply {
                startActivity(this)
                finish()
            }
        }
    }

    override fun onDestroy() {
        startSplash?.cancel()
        super.onDestroy()
    }

    companion object {
        private var splashDelay: Long = 1_000L
    }
}
