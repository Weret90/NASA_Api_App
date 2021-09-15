package com.umbrella.nasaapiapp.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.umbrella.nasaapiapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sp = getPreferences(Context.MODE_PRIVATE)
        val isRedTheme = sp.getBoolean(ARG_IS_RED_THEME, false)
        if (isRedTheme) {
            setTheme(R.style.RedTheme)
        } else {
            setTheme(R.style.Theme_NASAApiApp)
        }
        setContentView(R.layout.activity_main)
    }
}