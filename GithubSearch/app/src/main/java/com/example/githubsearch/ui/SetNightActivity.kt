package com.example.githubsearch.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.githubsearch.R
import com.example.githubsearch.databinding.ActivitySettingBinding
import com.example.githubsearch.viewmodel.SetNightViewModel
import com.example.githubsearch.viewmodel.ViewModelFactory

class SetNightActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingBinding


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        actionBar?.setDefaultDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val pref = ModePreferences.getInstance(dataStore)
        val setNightViewModel = ViewModelProvider(this, ViewModelFactory(pref))[SetNightViewModel::class.java]

        setNightViewModel.getThemeSettings().observe(this)
        { isDarkModeActive: Boolean ->
            setNightModeSum(isDarkModeActive)
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.doNight.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.doNight.isChecked = false
            }
        }

        binding.doNight.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            setNightViewModel.saveThemeSetting(isChecked)
            setNightModeSum(isChecked)
        }
    }

    private fun setNightModeSum(isNightmode: Boolean) {
        binding.tvNightmodeSum.text =
            if (isNightmode) resources.getString(R.string.ONnight_mode_sum) else resources.getString(
                R.string.OFFnight_mode_sum
            )
    }

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
